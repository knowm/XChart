/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.demo.charts.dial;

import org.knowm.xchart.DialChart;
import org.knowm.xchart.DialChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Dial Chart
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Dial Chart
 * <li>DialChartBuilder
 */
public class DialChart01 implements ExampleChart<DialChart> {

  public static void main(String[] args) {

    ExampleChart<DialChart> exampleChart = new DialChart01();
    DialChart chart = exampleChart.getChart();
    new SwingWrapper<DialChart>(chart).displayChart();
  }

  @Override
  public DialChart getChart() {

    // Create Chart
    DialChart chart = new DialChartBuilder().width(800).height(600).title("Radar Chart").build();

    // Series
    chart.addSeries("Rate", 0.9381, "93.81 %");
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setHasAnnotations(true);
    chart.getStyler().setLegendVisible(false);

    return chart;
  }
}
