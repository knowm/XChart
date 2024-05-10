/*
 * Copyright (c) 2024 Energía Plus. All rights reserved.
 */

package org.knowm.xchart.demo.charts.bar;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Stacked Bars with Overlapped Line Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>bar series are stacked
 *   <li>line series is overlapped
 */
public class BarChart12 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart12();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  private static List<String> getMonths() {
    return Arrays.asList(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
  }

  private static List<Double> getRandomValues(int count) {

    List<Double> values = new ArrayList<>(count);
    Random rand = new Random();
    for (int i = 0; i < count; i++) {
      values.add(rand.nextDouble() * 1000);
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
            .title(getClass().getSimpleName())
            .xAxisTitle("Month")
            .yAxisTitle("Consumption")
            .build();

    // Customize Chart
    chart.getStyler().setPlotGridVerticalLinesVisible(false);
    chart.getStyler().setLegendVisible(true);
    chart.getStyler().setStacked(true);
    chart
        .getStyler()
        .setSeriesColors(
            new Color[] {
              Color.decode("#2133D0"),
              Color.decode("#FF3B47"),
              Color.decode("#FFBD00"),
              Color.DARK_GRAY
            });

    List<String> months = getMonths();
    List<Double> period1Values = getRandomValues(12);
    List<Double> period2Values = getRandomValues(12);
    List<Double> period3Values = getRandomValues(12);
    List<Double> averageValues = new ArrayList<>();
    for (int i = 0; i < 12; i++) {
      averageValues.add((period1Values.get(i) + period2Values.get(i) + period3Values.get(i)) / 3);
    }

    // Series
    CategorySeries staked1 = chart.addSeries("Period 1", months, period1Values);
    CategorySeries staked2 = chart.addSeries("Period 2", months, period2Values);
    CategorySeries staked3 = chart.addSeries("Period 3", months, period3Values);
    CategorySeries overlappedLine = chart.addSeries("Average", months, averageValues);
    overlappedLine.setOverlapped(true);
    overlappedLine.setChartCategorySeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Stacked Bars with Overlapped Line Chart";
  }
}
