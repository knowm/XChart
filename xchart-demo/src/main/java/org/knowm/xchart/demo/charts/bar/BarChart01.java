package org.knowm.xchart.demo.charts.bar;

import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.CardinalPosition;

/**
 * Basic Bar Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Integer categories as List
 *   <li>All positive values
 *   <li>Single series
 *   <li>Place legend at Inside-NW position
 *   <li>Bar Chart Annotations
 */
public class BarChart01 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart01();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title(getClass().getSimpleName())
            .xAxisTitle("Score")
            .yAxisTitle("Number")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(CardinalPosition.InsideNW);
    chart.getStyler().setHasAnnotations(false);
    chart.getStyler().setPlotGridLinesVisible(false);

    // Series
    chart.addSeries("test 1", Arrays.asList(0, 1, 2, 3, 4), Arrays.asList(4, 5, 9, 6, 5));

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Basic Bar Chart";
  }
}
