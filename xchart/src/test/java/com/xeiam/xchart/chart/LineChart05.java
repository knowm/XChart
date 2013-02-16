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
package com.xeiam.xchart.chart;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.SwingWrapper;

/**
 * Vertical and horizontal lines
 * 
 * @author timmolter
 */
public class LineChart05 {

  public static void main(String[] args) {

    // Create Chart
    Chart chart = new Chart(800, 600);

    // Customize Chart
    chart.setChartTitle("LineChart05");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");

    chart.addSeries("vertical", new double[] { 1, 1 }, new double[] { -10, 10 });
    chart.addSeries("horizontal", new double[] { -10, 10 }, new double[] { 0, 0 });
    new SwingWrapper(chart).displayChart();
  }

}
