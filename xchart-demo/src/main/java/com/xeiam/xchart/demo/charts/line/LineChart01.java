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
package com.xeiam.xchart.demo.charts.line;

import java.util.ArrayList;
import java.util.List;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;
import com.xeiam.xchart.style.StyleManager.LegendPosition;

/**
 * Logarithmic Y-Axis
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Logarithmic Y-Axis
 * <li>Building a Chart with ChartBuilder
 * <li>Place legend at Inside-NW position
 */
public class LineChart01 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new LineChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // generates Log data
    List<Number> xData1 = new ArrayList<Number>();
    List<Number> yData1 = new ArrayList<Number>();
    for (int i = 0; i <= 10; i++) {
      xData1.add(i);
      yData1.add(Math.pow(10, i));
    }

    // Create Chart
    Chart chart = new ChartBuilder().width(800).height(600).build();

    // Customize Chart
    chart.getStyleManager().setChartTitleVisible(false);
    chart.getStyleManager().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyleManager().setYAxisLogarithmic(true);

    // Series
    Series series = chart.addSeries("10^x", xData1, yData1);

    return chart;
  }
}
