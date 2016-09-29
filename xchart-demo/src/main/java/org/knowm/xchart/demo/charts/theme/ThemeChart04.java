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
package org.knowm.xchart.demo.charts.theme;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * My Custom Theme
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Using a custom class that implements Theme
 */
public class ThemeChart04 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new ThemeChart04();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<XYChart>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).title("My Custom Theme").xAxisTitle("X").yAxisTitle("Y").build();
    chart.getStyler().setTheme(new MyCustomTheme());

    // Customize Chart
    chart.getStyler().setMarkerSize(11);

    // Series
    for (int i = 1; i <= 3; i++) {

      // generates circle data
      double radius = i;
      double x, y;
      List<Number> xData = new ArrayList<Number>();
      List<Number> yData = new ArrayList<Number>();

      for (int j = 0; j < 360; j = j + 5) {

        x = radius * Math.cos(Math.toRadians(j));
        y = radius * Math.sin(Math.toRadians(j));
        xData.add(x);
        yData.add(y);

      }

      String seriesName = "r=" + i;
      chart.addSeries(seriesName, xData, yData);

    }
    return chart;
  }
}
