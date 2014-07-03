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
package com.xeiam.xchart.demo.charts.scatter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Gaussian Blob
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>ChartType.Scatter
 * <li>Series data as a Set
 * <li>Setting marker size
 * <li>Formatting of negative numbers with large magnitude but small differences
 */
public class ScatterChart01 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new ScatterChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    Set<Double> xData = new HashSet<Double>();
    Set<Double> yData = new HashSet<Double>();
    Random random = new Random();
    int size = 1000;
    for (int i = 0; i < size; i++) {
      xData.add(random.nextGaussian() / 1000);
      yData.add(-1000000 + random.nextGaussian());
    }

    // Create Chart
    Chart chart = new Chart(800, 600);
    chart.getStyleManager().setChartType(ChartType.Scatter);

    // Customize Chart
    chart.getStyleManager().setChartTitleVisible(false);
    chart.getStyleManager().setLegendPosition(LegendPosition.InsideSW);
    chart.getStyleManager().setMarkerSize(16);

    // Series
    chart.addSeries("Gaussian Blob", xData, yData);

    return chart;
  }

}
