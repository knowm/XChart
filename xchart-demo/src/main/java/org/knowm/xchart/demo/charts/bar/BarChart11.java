package org.knowm.xchart.demo.charts.bar;

import java.awt.Color;
import java.util.Random;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
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
 *   <li>Render style - Stacked stepped bars
 */
public class BarChart11 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart11();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  private static int[] getRandomValues(int startRange, int endRange, int count) {

    int[] values = new int[count];
    Random rand = new Random();
    for (int i = 0; i < count; i++) {
      values[i] = rand.nextInt(endRange - startRange) + startRange;
    }
    return values;
  }

  private static int[] getLinearValues(int startRange, int endRange, int count) {

    int[] values = new int[count];
    int step = (endRange - startRange) / count;
    for (int i = 0; i < count; i++) {
      values[i] = step * i;
    }
    return values;
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("Tumbler Speed  vs. Average Spin")
            .xAxisTitle("Speed")
            .yAxisTitle("Spin")
            .build();

    // Customize Chart
    chart.getStyler().setPlotGridVerticalLinesVisible(false);
    chart.getStyler().setStacked(true);

    // Series
    CategorySeries series1 =
        chart.addSeries("Cats", getLinearValues(0, 200, 21), getRandomValues(-50, 50, 21));
    CategorySeries series2 =
        chart.addSeries("Hamsters", getLinearValues(0, 200, 21), getRandomValues(-50, 50, 21));

    // Set render style to SteppedBar
    series1.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.SteppedBar);
    series2.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.SteppedBar);

    // Make the line of series1 transparent
    series1.setLineColor(new Color(0, 0, 0, 0));

    // Make the fill of series 2 transparent
    series2.setFillColor(new Color(0, 0, 0, 0));

    return chart;
  }
}
