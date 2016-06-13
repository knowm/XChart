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
package org.knowm.xchart.demo.charts.bubble;

import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.BubbleChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Basic Bubble Chart
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Bubble Chart
 */
public class BubbleChart01 implements ExampleChart<BubbleChart> {

  public static void main(String[] args) {

    ExampleChart<BubbleChart> exampleChart = new BubbleChart01();
    BubbleChart chart = exampleChart.getChart();
    new SwingWrapper<BubbleChart>(chart).displayChart();
  }

  @Override
  public BubbleChart getChart() {

    // Create Chart
    BubbleChart chart = new BubbleChartBuilder().width(800).height(600).title("BubbleChart01").xAxisTitle("X").yAxisTitle("Y").build();

    // Customize Chart

    // Series
    double[] xData = new double[] { 1.5, 2.6, 3.3, 4.9, 5.5, 6.3, 1, 2.0, 3.0, 4.0, 5, 6 };
    double[] yData = new double[] { 10, 4, 7, 7.7, 7, 5.5, 10, 4, 7, 1, 7, 9 };
    double[] bubbleData = new double[] { 17, 40, 50, 51, 26, 20, 66, 35, 80, 27, 29, 44 };

    double[] xData2 = new double[] { 1, 2.0, 3.0, 4.0, 5, 6, 1.5, 2.6, 3.3, 4.9, 5.5, 6.3 };
    double[] yData2 = new double[] { 1, 2, 3, 4, 5, 6, 10, 8.5, 4, 1, 4.7, 9 };
    double[] bubbleData2 = new double[] { 37, 35, 80, 27, 29, 44, 57, 40, 50, 33, 26, 20 };

    chart.addSeries("A", xData, yData, bubbleData);
    chart.addSeries("B", xData2, yData2, bubbleData2);

    return chart;
  }

}
