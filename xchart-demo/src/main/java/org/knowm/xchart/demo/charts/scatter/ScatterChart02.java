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
package org.knowm.xchart.demo.charts.scatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.knowm.xchart.ChartBuilder_XY;
import org.knowm.xchart.Chart_XY;
import org.knowm.xchart.Series_XY.ChartXYSeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.style.Styler.LegendPosition;

/**
 * Logarithmic Data
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Scatter chart
 * <li>Logarithmic X-Axis
 * <li>Place legend at Inside-NW position
 * <li>Formatting of number with large magnitude but small differences
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

    List<Double> xData = new ArrayList<Double>();
    List<Double> yData = new ArrayList<Double>();
    Random random = new Random();
    int size = 400;
    for (int i = 0; i < size; i++) {
      double nextRandom = random.nextDouble();
      xData.add(Math.pow(10, nextRandom * 10));
      yData.add(1000000000.0 + nextRandom);
    }

    // Create Chart
    Chart_XY chart = new ChartBuilder_XY().width(800).height(600).title("Logarithmic Data").build();

    // Customize Chart
    chart.getStyler().setDefaultSeriesRenderStyle(ChartXYSeriesRenderStyle.Scatter);
    chart.getStyler().setXAxisLogarithmic(true);
    chart.getStyler().setLegendPosition(LegendPosition.InsideN);

    // Series
    chart.addSeries("logarithmic data", xData, yData);

    return chart;
  }

}
