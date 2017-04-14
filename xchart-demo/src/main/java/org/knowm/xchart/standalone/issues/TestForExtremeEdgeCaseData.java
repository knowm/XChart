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
package org.knowm.xchart.standalone.issues;

import java.io.IOException;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/**
 * @author timmolter
 */
public class TestForExtremeEdgeCaseData {

  public static void main(String[] args) throws IOException {

    final XYChart chart = new XYChartBuilder().build();

    final double[] x = {1, 2, 3};
    // final double[] y = { 40.16064257028113, 40.16064257028115, Double.NaN };
    // final double[] y = { 40.16064257028113, 40.16064257028115, Double.NEGATIVE_INFINITY };
    // final double[] y = { 40.16064257028113, 40.16064257028115, Double.POSITIVE_INFINITY };
    // final double[] y = { 40.16064257028113, 40.16064257028115, -Double.MAX_VALUE + 1e308 };
    final double[] y = {40.16064257028113, 40.16064257028115, -1 * Double.MAX_VALUE};

    chart.addSeries("Values", x, y);
    new SwingWrapper(chart).displayChart();
  }
}
