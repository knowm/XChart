/**
 * Copyright 2011 - 2014 Xeiam LLC.
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
package com.xeiam.xchart.demo.charts.bar;

import java.util.ArrayList;
import java.util.Arrays;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * GGPlot2 Theme
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>String categories
 * <li>Positive and negative values
 * <li>Multiple series
 */
public class BarChart05 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new BarChart05();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new ChartBuilder().chartType(ChartType.Bar).width(800).height(600).title("Temperature vs. Color").xAxisTitle("Color").yAxisTitle("Temperature").theme(ChartTheme.GGPlot2).build();
    chart.addSeries("fish", new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow" })), new ArrayList<Number>(Arrays.asList(new Number[] { -40, 30, 20, 60 })));
    chart.addSeries("worms", new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow" })), new ArrayList<Number>(Arrays.asList(new Number[] { 50, 10, -20, 40 })));

    return chart;
  }
}
