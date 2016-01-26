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
package org.knowm.xchart.demo.charts.scatter;

import org.knowm.xchart.Chart_XY;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;

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
    Chart_XY chart = new Chart_XY(800, 600);

    // Customize Chart
    chart.setTitle("Single Point");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");

    chart.addSeries("single point (1,1)", new double[] { 1 }, new double[] { 1 });

    return chart;
  }

}
