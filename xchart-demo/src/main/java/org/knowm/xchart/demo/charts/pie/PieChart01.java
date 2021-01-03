package org.knowm.xchart.demo.charts.pie;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;

/**
 * Pie Chart with 4 Slices
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Pie Chart
 *   <li>ChartBuilderPie
 *   <li>Setting Non-circular to match container aspect ratio
 *   <li>Legend outside south, with Horizontal Legend Layout
 */
public class PieChart01 implements ExampleChart<PieChart> {

  public static void main(String[] args) {

    ExampleChart<PieChart> exampleChart = new PieChart01();
    PieChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public PieChart getChart() {

    // Create Chart
    PieChart chart =
        new PieChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();

    // Customize Chart
    chart.getStyler().setCircular(false);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    // Series
    chart.addSeries("Pennies", 100);
    chart.addSeries("Nickels", 100);
    chart.addSeries("Dimes", 100);
    chart.addSeries("Quarters", 100);

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Pie Chart with 4 Slices";
  }
}
