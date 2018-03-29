package org.knowm.xchart.standalone.issues;

import java.io.IOException;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/** @author timmolter */
public class TestForExtremeEdgeCaseData {

  public static void main(String[] args) throws IOException {

    final XYChart chart = new XYChartBuilder().build();

    final double[] x = {1, 2, 3};
    // final double[] y = { 40.16064257028113, 40.16064257028115, Double.NaN };
    // final double[] y = { 40.16064257028113, 40.16064257028115, Double.NEGATIVE_INFINITY };
    // final double[] y = { 40.16064257028113, 40.16064257028115, Double.POSITIVE_INFINITY };
    // final double[] y = { 40.16064257028113, 40.16064257028115, -Double.MAX_VALUE + 1e308 };
    final double[] y = {40.16064257028113, 40.16064257028115, -1 * Double.MAX_VALUE};

    chart.addSeries("Values", x, y);
    new SwingWrapper(chart).displayChart();
  }
}
