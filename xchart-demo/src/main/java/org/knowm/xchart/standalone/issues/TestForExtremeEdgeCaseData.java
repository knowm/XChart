package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

public class TestForExtremeEdgeCaseData {

  public static XYChart getChart() {


    final XYChart chart = new XYChartBuilder().build();

    final double[] x = {1, 2, 3};
    // final double[] y = { 40.16064257028113, 40.16064257028115, Double.NaN };
    // final double[] y = { 40.16064257028113, 40.16064257028115, Double.NEGATIVE_INFINITY };
    // final double[] y = { 40.16064257028113, 40.16064257028115, Double.POSITIVE_INFINITY };
    // final double[] y = { 40.16064257028113, 40.16064257028115, -Double.MAX_VALUE + 1e308 };
    final double[] y = {40.16064257028113, 40.16064257028115, -1 * Double.MAX_VALUE};

    chart.addSeries("Values", x, y);
    return chart;
  }

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }
}
