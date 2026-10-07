package org.knowm.xchart.standalone.issues;

import java.io.IOException;
import org.knowm.xchart.ChartEncoder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/**
 * Demonstrates resizing an already constructed chart before export, so a chart received from
 * elsewhere can be saved at any dimensions. https://github.com/knowm/XChart/issues/807
 */
public class TestForIssue807 {

  public static void main(String[] args) throws IOException {

    XYChart chart = getChart();
    chart.setSize(1280, 720);
    ChartEncoder.saveChart(chart, "./TestForIssue807", "png");
    new SwingWrapper<>(chart).displayChart();
  }

  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(640)
            .height(480)
            .title("Resized before export")
            .xAxisTitle("Time")
            .yAxisTitle("Value")
            .build();
    chart.addSeries("Sample", new double[] {0, 1, 2, 3, 4}, new double[] {1, 3, 2, 5, 4});
    return chart;
  }
}
