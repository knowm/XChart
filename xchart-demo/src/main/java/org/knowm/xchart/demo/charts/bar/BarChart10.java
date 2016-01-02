/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
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

import org.knowm.xchart.Chart;
import org.knowm.xchart.ChartBuilder;
import org.knowm.xchart.Series;
import org.knowm.xchart.Series.SeriesType;
import org.knowm.xchart.StyleManager.ChartTheme;
import org.knowm.xchart.StyleManager.ChartType;
import org.knowm.xchart.StyleManager.LegendPosition;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Category chart with Bar and Line Series
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Mixed series types - Bar, Line and Scatter
 * <li>Bar Chart styles - overlapped, bar width
 */
public class BarChart10 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new BarChart10();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new ChartBuilder().chartType(ChartType.Bar).width(800).height(600).title("Value vs. Letter").xAxisTitle("Letter").yAxisTitle("Value").theme(ChartTheme.GGPlot2).build();
    chart.addCategorySeries("China", new ArrayList<String>(Arrays.asList(new String[] { "A", "B", "C", "D", "E" })), new ArrayList<Number>(Arrays.asList(new Number[] { 11, 23, 20, 36, 5 })));
    Series series2 = chart.addCategorySeries("Korea", new ArrayList<String>(Arrays.asList(new String[] { "A", "B", "C", "D", "E" })), new ArrayList<Number>(Arrays.asList(new Number[] { 13, 25, 22, 38,
        7 })), new ArrayList<Number>(Arrays.asList(new Number[] { 1, 3, 2, 1, 2 })));
    series2.setSeriesType(SeriesType.Line);
    Series series3 = chart.addCategorySeries("World Ave.", new ArrayList<String>(Arrays.asList(new String[] { "A", "B", "C", "D", "E" })), new ArrayList<Number>(Arrays.asList(new Number[] { 20, 22,
        18, 36, 32 })));
    series3.setSeriesType(SeriesType.Scatter);

    // Customize Chart
    chart.getStyleManager().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyleManager().setBarWidthPercentage(.7);
    chart.getStyleManager().setBarsOverlapped(true);

    return chart;
  }

}
