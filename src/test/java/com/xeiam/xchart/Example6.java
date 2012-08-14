/**
 * Copyright 2011 Xeiam LLC.
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
package com.xeiam.xchart;

import com.xeiam.xchart.swing.SwingWrapper;

/**
 * Create chart with single point
 * 
 * @author timmolter
 */
public class Example6 {

  public static void main(String[] args) {

    // Create Chart
    Chart chart = new Chart(700, 500);

    // Customize Chart
    chart.setChartTitle("Sample Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");

    chart.addSeries("single point (1,1)", new double[] { 1 }, new double[] { 1 });

    new SwingWrapper(chart).displayChart();
  }

}
