package org.knowm.xchart.demo.charts.bar;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Stacked Bar Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>int categories as array
 *   <li>Positive and negative values
 *   <li>Single series
 */
public class BarChart03 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart03();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("Score vs. Age")
            .xAxisTitle("Age")
            .yAxisTitle("Score")
            .build();

    // Customize Chart
    chart.getStyler().setPlotGridVerticalLinesVisible(false);
    chart.getStyler().setStacked(true);

    // Series
    chart.addSeries("males", new int[] {10, 20, 30, 40}, new int[] {40, -30, -20, -60});
    chart.addSeries("females", new int[] {10, 20, 30, 40}, new int[] {45, -35, -25, 65});

    return chart;
  }
}
