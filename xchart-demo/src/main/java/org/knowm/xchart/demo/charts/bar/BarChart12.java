/*
 * Copyright (c) 2024 Energía Plus. All rights reserved.
 */

package org.knowm.xchart.demo.charts.bar;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
            .xAxisTitle("Quarter")
            .yAxisTitle("Sales")
            .build();

    // Customize Chart
    chart.getStyler().setPlotGridVerticalLinesVisible(false);
    chart.getStyler().setLegendVisible(true);
    chart.getStyler().setStacked(true);
    chart
        .getStyler()
        .setSeriesColors(new Color[] {Color.RED, Color.ORANGE, Color.YELLOW, Color.DARK_GRAY});

    List<Double> applesValues = getRandomValues(4);
    List<Double> orangesValues = getRandomValues(4);
    List<Double> lemonsValues = getRandomValues(4);
    List<Double> averageValues = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      averageValues.add((applesValues.get(i) + orangesValues.get(i) + lemonsValues.get(i)) / 3);
    }

    // Series
    CategorySeries staked1 =
        chart.addSeries(
            "Apples", Arrays.asList("1Q", "2Q", "3Q", "4Q"), applesValues);
    CategorySeries staked2 =
        chart.addSeries(
            "Oranges", Arrays.asList("1Q", "2Q", "3Q", "4Q"), orangesValues);
    CategorySeries staked3 =
        chart.addSeries(
            "Lemons", Arrays.asList("1Q", "2Q", "3Q", "4Q"), lemonsValues);
    CategorySeries overlappedLine =
        chart.addSeries(
            "Average", Arrays.asList("1Q", "2Q", "3Q", "4Q"), averageValues);
    overlappedLine.setOverlapped(true);
    overlappedLine.setChartCategorySeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Stacked Bars with Overlapped Line Chart";
  }
}
