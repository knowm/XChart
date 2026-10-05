package org.knowm.xchart.standalone.issues;

import java.io.IOException;
import org.knowm.xchart.ChartEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

public class TestForIssue807 {

  public static void main(String[] args) throws IOException {
    XYChart chart = getChart();
    ChartEncoder.saveChart(chart, "chart-1280x720", "png", 1280, 720);
    new SwingWrapper<>(chart).displayChart();
  }

  /** Constructs the chart without launching a window. */
  public static XYChart getChart() {
    XYChart chart =
        new XYChartBuilder()
            .width(640)
            .height(480)
            .title("Export at a chosen size")
            .xAxisTitle("Time")
            .yAxisTitle("Value")
            .build();
    chart.addSeries("Sample", new double[] {0, 1, 2, 3, 4}, new double[] {1, 3, 2, 5, 4});
    return chart;
  }
}
