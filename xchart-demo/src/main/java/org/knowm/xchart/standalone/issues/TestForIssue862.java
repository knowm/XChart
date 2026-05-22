package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.ToolTipType;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Demonstrates the fix for issue #862 — {@link BitmapEncoder#saveBitmap} throws NPE when tooltips
 * are enabled.
 *
 * <p>Root cause: {@code ToolTips} and {@code Cursor} previously self-injected into the chart's
 * {@code PlotContent_} inside their constructors (called from {@code XChartPanel}). When rendering
 * headlessly via {@link BitmapEncoder}, no panel is ever created, so those fields remained {@code
 * null}. Every {@code doPaint()} override that referenced {@code toolTips} therefore threw an NPE.
 *
 * <p>Fix: a full two-phase architecture via {@code PlotInteractionData}. Interaction objects ({@code
 * ToolTips}, {@code ChartZoom}, {@code Cursor}) are owned exclusively by {@code XChartPanel} and
 * are never wired into the core rendering pipeline. Headless rendering via {@link BitmapEncoder}
 * is completely unaffected.
 *
 * <p>Chart code is taken directly from the original bug report (logarithmic Y-axis, "Powers of
 * Ten" dataset, red tooltip border). Previously it threw:
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

    // Headless render — must not throw NPE (was the bug).
    BitmapEncoder.saveBitmap(chart, "/tmp/issue862", BitmapFormat.PNG);
    System.out.println("Saved /tmp/issue862.png — no NPE.");

    // Interactive display — tooltips configured on the panel, not the styler.
    SwingWrapper<XYChart> sw = new SwingWrapper<>(chart);
    sw.displayChart();
    sw.getXChartPanel()
        .setToolTipsEnabled(true)
        .setToolTipsAlwaysVisible(true)
        .setToolTipType(ToolTipType.yLabels);
  }

  /**
   * Reproduces the exact chart from the bug report. Safe to call headlessly — no tooltip
   * interaction is wired until an {@code XChartPanel} is created.
   */
  public static XYChart getChart() {

    List<Integer> xData = new ArrayList<>();
    List<Double> yData = new ArrayList<>();
    for (int i = -3; i <= 3; i++) {
      xData.add(i);
      yData.add(Math.pow(10, i));
    }

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Powers of Ten")
            .xAxisTitle("Power")
            .yAxisTitle("Value")
            .build();

    chart.getStyler().setChartTitleVisible(true);
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setYAxisLogarithmic(true);
    chart.getStyler().setXAxisLabelRotation(45);
    chart.getStyler().setToolTipBorderColor(Color.RED);

    chart.addSeries("10^x", xData, yData);

    return chart;
  }
}

