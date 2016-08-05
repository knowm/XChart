/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.demo.charts.bar;

import java.util.ArrayList;
import java.util.Arrays;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Category chart with Bar, Line and Scatter Series
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Mixed series types - Bar, Line and Scatter
 * <li>Bar Chart styles - overlapped, bar width
 */
public class BarChart09 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart09();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Value vs. Letter").xAxisTitle("Letter").yAxisTitle("Value").theme(ChartTheme.GGPlot2).build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setAvailableSpaceFill(.55);
     chart.getStyler().setOverlapped(true);

    // Series
    chart.addSeries("China", new ArrayList<String>(Arrays.asList(new String[] { "A", "B", "C", "D", "E" })), new ArrayList<Number>(Arrays.asList(new Number[] { 11, 23, 20, 36, 5 })));
    CategorySeries series2 = chart.addSeries("Korea", new ArrayList<String>(Arrays.asList(new String[] { "A", "B", "C", "D", "E" })), new ArrayList<Number>(Arrays.asList(new Number[] { 13, 25, 22, 38,
        7 })), new ArrayList<Number>(Arrays.asList(new Number[] { 1, 3, 2, 1, 2 })));
    series2.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    CategorySeries series3 = chart.addSeries("World Ave.", new ArrayList<String>(Arrays.asList(new String[] { "A", "B", "C", "D", "E" })), new ArrayList<Number>(Arrays.asList(new Number[] { 20, 22,
        18, 36, 32 })));
    series3.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Scatter);

    return chart;
  }

}
