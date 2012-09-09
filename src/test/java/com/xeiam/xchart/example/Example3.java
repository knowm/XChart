/**
 * Copyright 2011-2012 Xeiam LLC.
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
package com.xeiam.xchart.example;

import java.util.ArrayList;
import java.util.Collection;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.swing.SwingWrapper;

/**
 * Create multiple curves on one chart
 * 
 * @author timmolter
 */
public class Example3 {

  public static void main(String[] args) {

    // Create Chart
    Chart chart = new Chart(700, 500);

    for (int i = 1; i <= 14; i++) {

      // generates linear data
      int b = 20;
      Collection<Number> xData = new ArrayList<Number>();
      Collection<Number> yData = new ArrayList<Number>();
      for (int x = 0; x <= b; x++) {
        xData.add(2 * x - b);
        yData.add(2 * i * x - i * b);
      }

      // Customize Chart
      chart.setChartTitle("Sample Chart");
      chart.setXAxisTitle("X");
      chart.setYAxisTitle("Y");

      String seriesName = "y=" + 2 * i + "x-" + i * b + "b";
      chart.addSeries(seriesName, xData, yData);

    }
    // JFrame testFrame = new TestFrame(chart);
    new SwingWrapper(chart).displayChart();
  }
}
