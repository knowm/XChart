package org.knowm.xchart.demo.charts.dial;

import org.knowm.xchart.DialChart;
import org.knowm.xchart.DialChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Dial Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Dial Chart
 *   <li>DialChartBuilder
 */
public class DialChart01 implements ExampleChart<DialChart> {

  public static void main(String[] args) {

    ExampleChart<DialChart> exampleChart = new DialChart01();
    DialChart chart = exampleChart.getChart();
    new SwingWrapper<DialChart>(chart).displayChart();
  }

  @Override
  public DialChart getChart() {

    // Create Chart
    DialChart chart = new DialChartBuilder().width(800).height(600).title("Radar Chart").build();

    // Series
    chart.addSeries("Rate", 0.9381, "93.81 %");
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setHasAnnotations(true);
    chart.getStyler().setLegendVisible(false);

    return chart;
  }
}
