package org.knowm.xchart.demo.charts.horizontalbar;

import org.knowm.xchart.HorizontalBarChart;
import org.knowm.xchart.HorizontalBarChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * GGPlot2 Theme Horizontal Bar chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>String categories
 *   <li>Positive and negative values
 *   <li>Multiple series
 */
public class HorizontalBarChart03 implements ExampleChart<HorizontalBarChart> {

  public static void main(String[] args) {

    ExampleChart<HorizontalBarChart> exampleChart = new HorizontalBarChart03();
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
            .yAxisTitle("Color")
            .xAxisTitle("Temperature")
            .theme(ChartTheme.GGPlot2)
            .build();

    // Customize Chart
    chart.getStyler().setPlotGridVerticalLinesVisible(false);

    // Series
    chart.addSeries(
        "fish",
        new ArrayList<Number>(Arrays.asList(new Number[]{-40, 30, 20, 60, 60})),
        new ArrayList<>(Arrays.asList(new String[]{"Blue", "Red", "Green", "Yellow", "Orange"})));
    chart.addSeries(
        "worms",
        new ArrayList<Number>(Arrays.asList(new Number[]{50, 10, -20, 40, 60})),
        new ArrayList<>(Arrays.asList(new String[]{"Blue", "Red", "Green", "Yellow", "Orange"})));
    chart.addSeries(
        "birds",
        new ArrayList<Number>(Arrays.asList(new Number[]{13, 22, -23, -34, 37})),
        new ArrayList<>(Arrays.asList(new String[]{"Blue", "Red", "Green", "Yellow", "Orange"})));
    chart.addSeries(
        "ants",
        new ArrayList<Number>(Arrays.asList(new Number[]{50, 57, -14, -20, 31})),
        new ArrayList<>(Arrays.asList(new String[]{"Blue", "Red", "Green", "Yellow", "Orange"})));
    chart.addSeries(
        "slugs",
        new ArrayList<Number>(Arrays.asList(new Number[]{-2, 29, 49, -16, -43})),
        new ArrayList<>(Arrays.asList(new String[]{"Blue", "Red", "Green", "Yellow", "Orange"})));

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - GGPlot2 Theme";
  }
}
