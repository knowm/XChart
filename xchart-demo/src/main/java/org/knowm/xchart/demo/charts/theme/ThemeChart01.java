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

import org.knowm.xchart.ChartBuilder_XY;
import org.knowm.xchart.Chart_XY;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.style.Styler.ChartTheme;

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
    Chart_XY chart = new ChartBuilder_XY().width(800).height(600).theme(ChartTheme.XChart).title("XChart Theme").xAxisTitle("X").yAxisTitle("Y").build();

    // Customize Chart
    chart.getStyler().setMarkerSize(11);

    // Series
    for (int i = 1; i <= 14; i++) {

      // generates linear data
      int b = 20;
      List<Number> xData = new ArrayList<Number>();
      List<Number> yData = new ArrayList<Number>();
      for (int x = 0; x <= b; x++) {
        xData.add(2 * x - b);
        yData.add(2 * i * x - i * b);
      }

      String seriesName = "y=" + 2 * i + "x-" + i * b + "b";
      chart.addSeries(seriesName, xData, yData);

    }
    return chart;
  }
}
