package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/** Numeric axis labels on a formatter's restricted domain (issue 997). */
public class TestForIssue997 {

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }

  public static XYChart getChart() {
    XYChart chart = new XYChartBuilder().width(640).height(400).title("Probability").build();
    chart.addSeries("Probability", new double[] {1, 2, 3}, new double[] {0.12, 0.8, 0.54});
    chart.getStyler().setYAxisMin(0.0);
    chart.getStyler().setYAxisMax(1.0);
    chart
        .getStyler()
        .setYAxisTickLabelsFormattingFunction(
            value -> {
              if (value < 0 || value > 1) {
                throw new IllegalArgumentException("Probability outside [0, 1]");
              }
              return Math.round(value * 100) + "%";
            });
    return chart;
  }
}
