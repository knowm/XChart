package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.RadarChart;
import org.knowm.xchart.RadarChartBuilder;
import org.knowm.xchart.RadarSeries;
import org.knowm.xchart.SwingWrapper;

public class TestForIssue335 {

  public static void main(String[] args) {

    RadarChart chart = new TestForIssue335().getChart();
    new SwingWrapper<RadarChart>(chart).displayChart();
  }

  public RadarChart getChart() {

    // Create Chart
    RadarChart chart =
        new RadarChartBuilder().width(800).height(600).title("TestForIssue335").build();
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setHasAnnotations(true);
    chart.getStyler().setSeriesFilled(false);

    // Series
    chart.setRadiiLabels(
        new String[] {
          "Sales",
          "Marketing",
          "Development",
          "Customer Support",
          "Information Technology",
          "Administration"
        });
    RadarSeries oldSystemSeries =
        chart.addSeries("Old System", new double[] {0.78, 0.85, 0.80, 0.82, 0.93, 0.92});
    oldSystemSeries.setLineWidth(6);
    RadarSeries newSystemSeries =
        chart.addSeries("New System", new double[] {0.67, 0.73, 0.97, 0.95, 0.93, 0.73});
    newSystemSeries.setLineWidth(4);
    return chart;
  }
}
