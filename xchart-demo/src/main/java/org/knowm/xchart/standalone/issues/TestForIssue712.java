package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

/**
 * Demonstrates the fix for issue #712 — NullPointerException when the chart panel is resized to be
 * very small.
 *
 * <p>When the chart window was made narrow (especially with multiple Y-axis groups), the axis
 * columns consumed more space than the total chart width, leaving a zero or negative plot area.
 * Painting with degenerate geometry caused a {@link NullPointerException} in AWT path operations.
 *
 * <p>The fix adds an early-exit guard in {@code Plot_AxesChart.paint()} that skips painting
 * entirely when the computed plot width or height is &lt;= 0.
 *
 * <p>Run this demo, then shrink the window as narrow as possible — no exception should be thrown.
 */
public class TestForIssue712 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Builds an XYChart with multiple Y-axis groups to reproduce the original failure scenario. */
  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(600)
            .height(400)
            .title("Issue #712 – resize me as narrow as possible")
            .xAxisTitle("X")
            .build();

    chart.addSeries("Series A", new double[] {1, 2, 3, 4, 5}, new double[] {10, 20, 15, 25, 18});

    XYSeries seriesB =
        chart.addSeries("Series B", new double[] {1, 2, 3, 4, 5}, new double[] {100, 200, 150, 250, 180});
    seriesB.setYAxisGroup(1);

    XYSeries seriesC =
        chart.addSeries("Series C", new double[] {1, 2, 3, 4, 5}, new double[] {1000, 2000, 1500, 2500, 1800});
    seriesC.setYAxisGroup(2);

    return chart;
  }
}
