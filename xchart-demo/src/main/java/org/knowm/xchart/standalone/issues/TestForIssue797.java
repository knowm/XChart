package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

/** Demonstrates independent tick spacing for two Y-axis groups. */
public class TestForIssue797 {

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs the chart without opening a window. */
  public static XYChart getChart() {
    XYChart chart =
        new XYChartBuilder()
            .width(900)
            .height(600)
            .title("Y-axis group tick spacing")
            .xAxisTitle("Sample")
            .yAxisTitle("Measurement")
            .build();
    double[] x = {0, 1, 2, 3, 4, 5};
    chart.addSeries("Measurement", x, new double[] {0, 20, 40, 60, 80, 100});
    chart.addSeries("Enabled", x, new double[] {0, 0, 1, 1, 0, 1}).setYAxisGroup(1);
    chart.setYAxisGroupTitle(1, "Enabled");
    chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
    chart.getStyler().setYAxisMin(1, 0.0);
    chart.getStyler().setYAxisMax(1, 1.0);
    chart.getStyler().setYAxisTickMarkSpacingHint(40);
    chart.getStyler().setYAxisGroupTickMarkSpacingHint(1, 200);
    return chart;
  }
}
