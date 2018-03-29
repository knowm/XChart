package org.knowm.xchart.demo.charts.bar;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
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
 *   <li>Render style - Non-overlayed stepped bars
 */
public class BarChart12 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart12();
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

    CategorySeries[] categorySeries = new CategorySeries[5];

    // Series
    categorySeries[0] =
        chart.addSeries(
            "fish",
            new ArrayList<String>(
                Arrays.asList(new String[] {"Blue", "Red", "Green", "Yellow", "Orange"})),
            new ArrayList<Number>(Arrays.asList(new Number[] {-40, 30, 20, 60, 60})));
    categorySeries[1] =
        chart.addSeries(
            "worms",
            new ArrayList<String>(
                Arrays.asList(new String[] {"Blue", "Red", "Green", "Yellow", "Orange"})),
            new ArrayList<Number>(Arrays.asList(new Number[] {50, 10, -20, 40, 60})));
    categorySeries[2] =
        chart.addSeries(
            "birds",
            new ArrayList<String>(
                Arrays.asList(new String[] {"Blue", "Red", "Green", "Yellow", "Orange"})),
            new ArrayList<Number>(Arrays.asList(new Number[] {13, 22, -23, -34, 37})));
    categorySeries[3] =
        chart.addSeries(
            "ants",
            new ArrayList<String>(
                Arrays.asList(new String[] {"Blue", "Red", "Green", "Yellow", "Orange"})),
            new ArrayList<Number>(Arrays.asList(new Number[] {50, 57, -14, -20, 31})));
    categorySeries[4] =
        chart.addSeries(
            "slugs",
            new ArrayList<String>(
                Arrays.asList(new String[] {"Blue", "Red", "Green", "Yellow", "Orange"})),
            new ArrayList<Number>(Arrays.asList(new Number[] {-2, 29, 49, -16, -43})));

    // Set the render style to SteppedBar, and make the fill transparent
    for (CategorySeries series : categorySeries) {
      series.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.SteppedBar);
      series.setFillColor(new Color(0, 0, 0, 0));
    }

    return chart;
  }
}
