package org.knowm.xchart.demo.charts.bar;

import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Bar Chart with Gradient Fills
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Opt-in bar gradient from the series fill color at the base to a brighter color at the value
 *   <li>Positive and negative values, with the brighter end away from zero
 *   <li>Legend swatches use the positive-bar gradient
 * </ul>
 */
public class BarChart14 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart14();
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
            .xAxisTitle("Category")
            .yAxisTitle("Value")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setBarFillGradientEnabled(true);

    // Series
    chart.addSeries(
        "high", Arrays.asList("A", "B", "C", "D", "E"), Arrays.asList(50, -20, 70, -40, 30));
    chart.addSeries(
        "low", Arrays.asList("A", "B", "C", "D", "E"), Arrays.asList(20, -35, 25, -15, 45));

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Bar Chart with Gradient Fills";
  }
}
