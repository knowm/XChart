package org.knowm.xchart.demo.charts.pie;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Pie Chart - oval with border
 *
 * <p>
 * Demonstrates the following:
 *
 * <ul>
 * <li>Pie Chart
 * <li>ChartBuilderPie
 * <li>Setting Non-circular to match container aspect ratio
 */
public class PieChart07 implements ExampleChart<PieChart> {

  public static void main(String[] args) {

    ExampleChart<PieChart> exampleChart = new PieChart07();
    PieChart chart = exampleChart.getChart();
    new SwingWrapper<PieChart>(chart).displayChart();
  }

  @Override
  public PieChart getChart() {

    // Create Chart
    PieChart chart = new PieChartBuilder().width(800).height(600).title("Pie Chart - oval with border").build();

    // Customize Chart
    chart.getStyler().setCircular(false);
    chart.getStyler().setBorderWidth(10);

    // Series
    chart.addSeries("Pennies", 100);
    chart.addSeries("Nickels", 100);
    chart.addSeries("Dimes", 100);
    chart.addSeries("Quarters", 100);

    return chart;
  }
}
