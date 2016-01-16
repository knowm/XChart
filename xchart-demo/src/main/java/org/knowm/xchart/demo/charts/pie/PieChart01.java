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
package org.knowm.xchart.demo.charts.pie;

import org.knowm.xchart.Chart;
import org.knowm.xchart.ChartBuilder;
import org.knowm.xchart.StyleManager.ChartType;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Pie Chart with 4 Slices
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Pie Chart
 * <li>ChartBuilder
 */
public class PieChart01 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new PieChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    // TODO remove chartType(ChartType.Pie)
    Chart chart = new ChartBuilder().chartType(ChartType.Pie).width(800).height(600).title(getClass().getSimpleName()).xAxisTitle("X").yAxisTitle("Y").build();
    chart.addPieSeries("Pennies", 387);
    chart.addPieSeries("Nickels", 234);
    chart.addPieSeries("Dimes", 190);
    chart.addPieSeries("Quarters", 270);

    return chart;
  }

}
