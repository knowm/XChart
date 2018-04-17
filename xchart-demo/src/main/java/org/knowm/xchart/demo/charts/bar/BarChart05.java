package org.knowm.xchart.demo.charts.bar;

import java.util.ArrayList;
import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * GGPlot2 Theme Bar chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>String categories
 *   <li>Positive and negative values
 *   <li>Multiple series
 */
public class BarChart05 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart05();
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
            .title("Temperature vs. Color")
            .xAxisTitle("Color")
            .yAxisTitle("Temperature")
            .theme(ChartTheme.GGPlot2)
            .build();

    // Customize Chart
    chart.getStyler().setPlotGridVerticalLinesVisible(false);

    // Series
    chart.addSeries(
        "fish",
        new ArrayList<String>(
            Arrays.asList(new String[] {"Blue", "Red", "Green", "Yellow", "Orange"})),
        new ArrayList<Number>(Arrays.asList(new Number[] {-40, 30, 20, 60, 60})));
    chart.addSeries(
        "worms",
        new ArrayList<String>(
            Arrays.asList(new String[] {"Blue", "Red", "Green", "Yellow", "Orange"})),
        new ArrayList<Number>(Arrays.asList(new Number[] {50, 10, -20, 40, 60})));
    chart.addSeries(
        "birds",
        new ArrayList<String>(
            Arrays.asList(new String[] {"Blue", "Red", "Green", "Yellow", "Orange"})),
        new ArrayList<Number>(Arrays.asList(new Number[] {13, 22, -23, -34, 37})));
    chart.addSeries(
        "ants",
        new ArrayList<String>(
            Arrays.asList(new String[] {"Blue", "Red", "Green", "Yellow", "Orange"})),
        new ArrayList<Number>(Arrays.asList(new Number[] {50, 57, -14, -20, 31})));
    chart.addSeries(
        "slugs",
        new ArrayList<String>(
            Arrays.asList(new String[] {"Blue", "Red", "Green", "Yellow", "Orange"})),
        new ArrayList<Number>(Arrays.asList(new Number[] {-2, 29, 49, -16, -43})));

    return chart;
  }
}
