/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
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
package org.knowm.xchart.demo.charts.scatter;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.knowm.xchart.Chart;
import org.knowm.xchart.StyleManager.ChartType;
import org.knowm.xchart.StyleManager.LegendPosition;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

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

    List<Double> xData = new LinkedList<Double>();
    List<Double> yData = new LinkedList<Double>();
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
