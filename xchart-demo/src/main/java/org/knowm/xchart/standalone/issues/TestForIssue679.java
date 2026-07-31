package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapChartBuilder;
import org.knowm.xchart.SwingWrapper;

/**
 * Demonstrates issue #679 — custom tooltips via {@code setToolTipGenerator(...)}, available on
 * every series type.
 *
 * <p>The generator receives a {@code ChartDataPoint} carrying the series name, the data point's
 * index within the series data and the default label, and returns the tooltip text to display
 * (multi-line via {@code System.lineSeparator()}). Returning {@code null} falls back to the default
 * label. Here each heat map cell's tooltip shows extra information looked up from an application
 * data structure by the data point index — exactly what the issue asked for.
 */
public class TestForIssue679 {

  public static void main(String[] args) {

    SwingWrapper<HeatMapChart> wrapper = new SwingWrapper<>(getChart());
    wrapper.displayChart();
    // tooltips are an XChartPanel feature and are off by default
    wrapper.getXChartPanel().setToolTipsEnabled(true);
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static HeatMapChart getChart() {

    HeatMapChart chart =
        new HeatMapChartBuilder()
            .width(700)
            .height(400)
            .title("Issue #679 – Custom HeatMap tooltips (hover over a cell)")
            .build();
    chart.getStyler().setShowValue(true);

    int[] xData = {0, 1, 2, 3};
    int[] yData = {0, 1, 2};
    // heatData[x][y] = cell value; the data point index runs x-major: index = x * yLength + y
    int[][] heatData = new int[xData.length][yData.length];
    // application-side extra info, indexed the same as the heat data
    String[] extraInfo = new String[xData.length * yData.length];
    for (int x : xData) {
      for (int y : yData) {
        heatData[x][y] = (x + 1) * (y + 1);
        extraInfo[x * yData.length + y] = "sample count: " + (100 + 10 * x + y);
      }
    }

    chart
        .addSeries("heat", xData, yData, heatData)
        .setToolTipGenerator(
            dataPoint ->
                dataPoint.getLabel()
                    + System.lineSeparator()
                    + extraInfo[dataPoint.getDataPointIndex()]);
    return chart;
  }
}
