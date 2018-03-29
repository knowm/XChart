package org.knowm.xchart.demo.charts.bar;

import java.util.Arrays;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;

/**
 * Missing Point in Series
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Number categories
 *   <li>Positive values
 *   <li>Multiple series
 *   <li>Missing point in series
 *   <li>Manually setting y-axis min and max values
 *   <li>Bar Chart Annotations
 *   <li>Horizontal Legend OutsideS
 */
public class BarChart04 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart04();
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
            .title("XFactor vs. Age")
            .xAxisTitle("Age")
            .yAxisTitle("XFactor")
            .build();

    // Customize Chart
    chart.getStyler().setYAxisMin(5.0);
    chart.getStyler().setYAxisMax(70.0);
    chart.getStyler().setHasAnnotations(true);
    chart.getStyler().setPlotGridVerticalLinesVisible(false);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    // Series
    chart.addSeries("female", Arrays.asList(10, 20, 30, 40, 50), Arrays.asList(50, 10, 20, 40, 35));
    chart.addSeries("male", Arrays.asList(10, 20, 30, 40, 50), Arrays.asList(40, 30, 20, null, 60));

    return chart;
  }
}
