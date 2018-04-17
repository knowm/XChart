package org.knowm.xchart.standalone;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/** Creates a simple Chart using QuickChart */
public class Example0 {

  public static void main(String[] args) throws Exception {

    double[] xData = new double[] {0.0, 1.0, 2.0};
    double[] yData = new double[] {2.0, 1.0, 0.0};

    // Create Chart
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);

    // Show it
    new SwingWrapper(chart).displayChart();
  }
}
