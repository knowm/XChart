/**
 * Copyright 2011-2013 Xeiam LLC.
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
package com.xeiam.xchart.demo.charts;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.SwingWrapper;

/**
 * Using ChartBuilder to Make a Chart
 * 
 * @author timmolter
 */
public class Example11 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new Example11();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new ChartBuilder().width(800).height(600).title("My Title").xAxisTitle("X").yAxisTitle("Y").showLegend(false).build();
    chart.addSeries("x", new double[] { 1, 2, 5, 7 }, new double[] { -3, 6, 9, 0 });

    return chart;
  }
}
