package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;

public class TestForIssue158 {

  public static void main(String[] args) throws Exception {

    double[] xData = new double[] {0.0, 1.0, 2.0};
    double[] yData = new double[] {2.0, 1.0, 0.0};

    XYChart chart = new XYChart(500, 200);
    XYSeries xySeries = chart.addSeries("Sample Chart", xData, yData);
    xySeries.setEnabled(false);

    new SwingWrapper(chart).displayChart();
  }
}
