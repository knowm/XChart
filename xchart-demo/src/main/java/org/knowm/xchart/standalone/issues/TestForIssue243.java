package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.markers.Circle;

public class TestForIssue243 {

  public static void main(String[] args) throws Exception {

    double[] xData = new double[] {1.0, 2.0};
    double[] yData = new double[] {Double.NaN, 1.0};

    // Create Chart
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "1", xData, yData);

    chart.getSeriesMap().get("1").setMarker(new Circle());

    // Show it
    new SwingWrapper(chart).displayChart();
  }
}
