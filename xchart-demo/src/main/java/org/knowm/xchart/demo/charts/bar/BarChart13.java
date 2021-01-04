package org.knowm.xchart.demo.charts.bar;

import java.awt.*;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Stacked Bar Chart with annotation color auto detection
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>int categories as array
 *   <li>Positive and negative values
 *   <li>Single series
 */
public class BarChart13 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart13();
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
    chart.getStyler().setLabelsVisible(true);
    chart.getStyler().setAnnotationsAutodetectColors(Color.BLACK, Color.WHITE);

    // Series
    chart.addSeries("males", new int[] {10, 20, 30, 40}, new int[] {40, -30, -20, -60});
    chart.addSeries("females", new int[] {10, 20, 30, 40}, new int[] {45, -35, -25, 65});

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Stacked Bar Chart With Annotation Color Auto Detection";
  }
}
