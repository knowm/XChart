package org.knowm.xchart.demo.charts.radar;

import org.knowm.xchart.RadarChart;
import org.knowm.xchart.RadarChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Radar Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Radar Chart
 *   <li>RadarChartBuilder
 *   <li>Tool tips
 */
public class RadarChart01 implements ExampleChart<RadarChart> {

  public static void main(String[] args) {

    ExampleChart<RadarChart> exampleChart = new RadarChart01();
    RadarChart chart = exampleChart.getChart();
    new SwingWrapper<RadarChart>(chart).displayChart();
  }

  @Override
  public RadarChart getChart() {

    // Create Chart
    RadarChart chart = new RadarChartBuilder().width(800).height(600).title("Radar Chart").build();
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setHasAnnotations(true);

    // Series
    chart.setVariableLabels(
        new String[] {
          "Sales",
          "Marketing",
          "Development",
          "Customer Support",
          "Information Technology",
          "Administration"
        });
    chart.addSeries(
        "Old System",
        new double[] {0.78, 0.85, 0.80, 0.82, 0.93, 0.92},
        new String[] {"Lowest varible 78%", "85%", null, null, null, null});
    chart.addSeries("New System", new double[] {0.67, 0.73, 0.97, 0.95, 0.93, 0.73});
    chart.addSeries("Experimental System", new double[] {0.37, 0.93, 0.57, 0.55, 0.33, 0.73});

    return chart;
  }
}
