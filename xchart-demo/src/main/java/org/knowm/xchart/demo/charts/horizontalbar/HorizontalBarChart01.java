package org.knowm.xchart.demo.charts.horizontalbar;

import org.knowm.xchart.HorizontalBarChart;
import org.knowm.xchart.HorizontalBarChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

import java.util.Arrays;

/**
 * Basic Horizontal Bar Chart
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
public class HorizontalBarChart01 implements ExampleChart<HorizontalBarChart> {

  public static void main(String[] args) {

    ExampleChart<HorizontalBarChart> exampleChart = new HorizontalBarChart01();
    HorizontalBarChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public HorizontalBarChart getChart() {

    // Create Chart
    HorizontalBarChart chart =
        new HorizontalBarChartBuilder()
            .width(800)
            .height(600)
            .title(getClass().getSimpleName())
            .yAxisTitle("Score")
            .xAxisTitle("Number")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setLabelsVisible(false);
    chart.getStyler().setPlotGridLinesVisible(false);

    // Series
    chart.addSeries("test 1", Arrays.asList(4, 5, 9, 6, 5), Arrays.asList(0, 1, 2, 3, 4));

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Basic Horizontal Bar Chart";
  }
}
