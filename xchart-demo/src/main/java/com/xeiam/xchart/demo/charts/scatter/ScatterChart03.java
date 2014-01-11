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
package com.xeiam.xchart.demo.charts.scatter;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Single point
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Single point
 */
public class ScatterChart03 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new ScatterChart03();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new Chart(800, 600);

    // Customize Chart
    chart.setChartTitle("Single Point");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");

    chart.addSeries("single point (1,1)", new double[] { 1 }, new double[] { 1 });

    return chart;
  }

}
