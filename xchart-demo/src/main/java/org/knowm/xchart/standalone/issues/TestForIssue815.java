package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * Issue #815 - Extra space below min Y-axis value when calling setYAxisMin(0.0).
 *
 * <p>Root cause: plotContentSize (default ~0.95) adds a symmetric margin on both sides of the axis
 * via Utils.getTickStartOffset(). Even with setYAxisMin(0.0) the bottom margin is still rendered.
 *
 * <p>Workaround: call setPlotContentSize(1.0) to remove the symmetric margin, then use
 * setYAxisMax() to preserve a small breathing room at the top.
 */
public class TestForIssue815 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChartWithExtraSpace())
        .setTitle("Before fix – extra space below 0")
        .displayChart();

    new SwingWrapper<>(getChartFixed())
        .setTitle("After fix – 0 anchored at bottom")
        .displayChart();
  }

  /** Demonstrates the problem: setYAxisMin(0.0) leaves extra space below zero. */
  public static XYChart getChartWithExtraSpace() {

    XYChart chart =
        new XYChartBuilder()
            .width(600)
            .height(400)
            .title("Extra space below 0 (before fix)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .theme(ChartTheme.Matlab)
            .build();

    chart.addSeries("series1", xData(), yData()).setXYSeriesRenderStyle(XYSeriesRenderStyle.Area);

    chart.getStyler().setYAxisMin(0.0);

    return chart;
  }

  /**
   * Demonstrates the fix: setPlotContentSize(1.0) removes the symmetric margin so 0 sits exactly
   * at the bottom edge. setYAxisMax(105.0) preserves a small breathing room at the top.
   */
  public static XYChart getChartFixed() {

    XYChart chart =
        new XYChartBuilder()
            .width(600)
            .height(400)
            .title("0 anchored at bottom (after fix)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .theme(ChartTheme.Matlab)
            .build();

    chart.addSeries("series1", xData(), yData()).setXYSeriesRenderStyle(XYSeriesRenderStyle.Area);

    chart.getStyler().setYAxisMin(0.0);
    chart.getStyler().setYAxisMax(105.0);
    chart.getStyler().setPlotContentSize(1.0);

    return chart;
  }

  private static List<Double> xData() {

    return Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);
  }

  private static List<Double> yData() {

    return Arrays.asList(20.0, 45.0, 30.0, 60.0, 85.0, 70.0, 55.0, 90.0, 40.0, 75.0);
  }
}
