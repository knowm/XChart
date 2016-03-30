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

import org.knowm.xchart.ChartBuilder_Category;
import org.knowm.xchart.Chart_Category;
import org.knowm.xchart.Series_Category.ChartCategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * GGPlot2 Theme
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>String categories
 * <li>Positive and negative values
 * <li>Multiple series
 */
public class LinkedBar implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new LinkedBar();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart_Category chart = new ChartBuilder_Category().width(800).height(600).title("Temperature vs. Color").xAxisTitle("Color").yAxisTitle("Temperature").theme(ChartTheme.GGPlot2).build();
    // Customize Chart
	chart.getStyler().setDefaultSeriesRenderStyle(ChartCategorySeriesRenderStyle.LinkedBar);
	chart.getStyler().setAvailableSpaceFill(1.0);
	
    // Series
    chart.addSeries("fish", new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow", "Orange" })), new ArrayList<Number>(Arrays.asList(new Number[] { -40, 30, 20, 60,
        60 })));
    chart.addSeries("worms", new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow", "Orange" })), new ArrayList<Number>(Arrays.asList(new Number[] { 50, 10, -20, 40,
        60 })));
    chart.addSeries("birds", new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow", "Orange" })), new ArrayList<Number>(Arrays.asList(new Number[] { 13, 22, -23, -34,
        37 })));
    chart.addSeries("ants", new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow", "Orange" })), new ArrayList<Number>(Arrays.asList(new Number[] { 50, 57, -14, -20,
        31 })));
    chart.addSeries("slugs", new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow", "Orange" })), new ArrayList<Number>(Arrays.asList(new Number[] { -2, 29, 49, -16,
        -43 })));

    return chart;
  }
}
