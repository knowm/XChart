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
package com.xeiam.xchart.demo.charts.theme;

import java.util.ArrayList;
import java.util.Collection;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Default XChart Theme
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Setting marker size
 */
public class ThemeChart01 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new ThemeChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new Chart(800, 600);

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
      chart.setChartTitle("XChart Theme");
      chart.setXAxisTitle("X");
      chart.setYAxisTitle("Y");

      String seriesName = "y=" + 2 * i + "x-" + i * b + "b";
      chart.addSeries(seriesName, xData, yData);
      chart.getStyleManager().setMarkerSize(11);

    }
    return chart;
  }
}
