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
package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/**
 * @author timmolter
 */
public class TestForIssue151 {

  public static void main(String[] args) {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(600).height(400).build();

    // Customize Chart

    // Series
    double[] xData1 = new double[] { 0, 1, 2, 3, 4, 5, 6, 10, 11, 12, 13, 14, 15 };
    double[] yData1 = new double[] { 106, 44, 26, 10, 11, 19, 25, Double.NaN, 30, 21, 36, 32, 30 };
    double[] xData2 = new double[] { 6, 7, 8, 9, 10, 11 };
    double[] yData2 = new double[] { 25, 54, 43, 56, 33, 30 };

    chart.addSeries("A", xData1, yData1);
    chart.addSeries("B", xData2, yData2);

    new SwingWrapper<XYChart>(chart).displayChart();
  }

}
