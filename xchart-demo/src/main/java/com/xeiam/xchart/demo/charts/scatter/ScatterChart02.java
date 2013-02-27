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
package com.xeiam.xchart.demo.charts.scatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;
import com.xeiam.xchart.style.StyleManager.ChartType;
import com.xeiam.xchart.style.StyleManager.LegendPosition;

/**
 * Logarithmic Data
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Scatter chart
 * <li>Logarithmic X-Axis
 * <li>Place legend at Inside-NW position
 * 
 * @author timmolter
 */
public class ScatterChart02 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new ScatterChart02();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    List<Number> xData = new ArrayList<Number>();
    List<Number> yData = new ArrayList<Number>();
    Random random = new Random();
    int size = 400;
    for (int i = 0; i < size; i++) {
      double nextRandom = random.nextDouble();
      xData.add(Math.pow(10, nextRandom * 10));
      yData.add(nextRandom + random.nextDouble());
    }

    // Create Chart
    Chart chart = new Chart(800, 600);
    chart.setChartTitle("Logarithmic Data");
    chart.getStyleManager().setChartType(ChartType.Scatter);
    chart.getStyleManager().setXAxisLogarithmic(true);

    // Customize Chart
    chart.getStyleManager().setLegendPosition(LegendPosition.InsideNW);

    // Series
    chart.addSeries("logarithmic data", xData, yData);

    return chart;
  }

}
