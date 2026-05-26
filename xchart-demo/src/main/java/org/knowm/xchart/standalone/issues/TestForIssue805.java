package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * Demonstrates the fix for issue #805 — wrong value displayed in cursor label.
 *
 * <p>Before the fix:
 *
 * <ol>
 *   <li>Moving the cursor between data points left stale Y-values in the tooltip because {@code
 *       matchingDataPointList} was only cleared inside the {@code dataPoints.size() > 0} branch.
 *   <li>When multiple data points of the same series shared the same X position, only one Y value
 *       was kept (closest X wins), so the others were silently dropped.
 * </ol>
 *
 * <p>After the fix:
 *
 * <ol>
 *   <li>{@code calculateMatchingDataPoints()} always clears {@code matchingDataPointList} at the
 *       start, so no stale values persist when the cursor is between data points.
 *   <li>Multiple Y values for the same series at the same X are combined into a comma-separated
 *       string (e.g. {@code "1.0, 2.0"}).
 * </ol>
 *
 * <p>To observe: enable the cursor, hover the mouse across the chart, and confirm the tooltip
 * always reflects only the current data under the cursor.
 */
public class TestForIssue805 {

  public static void main(String[] args) {

    SwingWrapper<XYChart> sw = new SwingWrapper<>(getChart());
    sw.displayChart();
    sw.getXChartPanel().setCursorEnabled(true);
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Issue #805 — Cursor label wrong value fix")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .theme(ChartTheme.Matlab)
            .build();

    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);

    // Series 1: standard ascending data
    chart.addSeries(
        "Series A",
        new double[] {1.0, 2.0, 3.0, 4.0, 5.0},
        new double[] {2.0, 4.5, 3.0, 6.0, 5.5});

    // Series 2: overlaps Series A at some X values to exercise multi-series cursor
    chart.addSeries(
        "Series B",
        new double[] {1.0, 2.0, 3.0, 4.0, 5.0},
        new double[] {1.0, 2.0, 4.0, 3.5, 7.0});

    return chart;
  }
}
