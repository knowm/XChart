package org.knowm.xchart.standalone.issues;

import java.io.IOException;
import java.util.Arrays;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

/**
 * Demonstrates the fix for issue #862 — {@link BitmapEncoder#saveBitmap} throws NPE when tooltips
 * are enabled.
 *
 * <p>Root cause: {@code ToolTips} and {@code Cursor} are only injected into the chart's
 * {@code PlotContent_} from {@code XChartPanel}. When rendering headlessly via {@link
 * BitmapEncoder}, no panel is created, so the fields remain {@code null}. Every {@code doPaint()}
 * override that called {@code toolTips.addData()} guarded only on {@code isToolTipsEnabled()},
 * causing an NPE at runtime.
 *
 * <p>Fix: all {@code isToolTipsEnabled()} / {@code isCursorEnabled()} guards in every {@code
 * PlotContent_*} subclass now also check {@code toolTips != null} / {@code cursor != null}.
 *
 * <p>To verify the fix: calling {@link #getChart()} must not throw. Previously it threw:
 *
 * <pre>
 *   java.lang.NullPointerException
 *     at PlotContent_XY.doPaint(PlotContent_XY.java:304)
 *     at BitmapEncoder.getBufferedImage(BitmapEncoder.java:277)
 * </pre>
 */
public class TestForIssue862 {

  public static void main(String[] args) throws IOException {

    XYChart chart = getChart();

    // Write to /tmp — confirms no NPE during headless rendering with tooltips enabled.
    BitmapEncoder.saveBitmap(chart, "/tmp/issue862", BitmapFormat.PNG);
    System.out.println("Saved /tmp/issue862.png — no NPE.");

    // Also show the interactive chart to confirm tooltips still work with a panel.
    new SwingWrapper<>(chart).displayChart();
  }

  /**
   * Returns an XYChart with tooltips enabled. Calling this method previously triggered an NPE
   * inside {@code BitmapEncoder.saveBitmap()} before the fix.
   */
  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Issue #862 – BitmapEncoder + tooltips (was: NPE)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setToolTipsAlwaysVisible(true);
    chart.getStyler().setToolTipType(Styler.ToolTipType.yLabels);

    chart.addSeries("series", Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(2.0, 4.0, 3.0, 7.0, 5.0));

    return chart;
  }
}
