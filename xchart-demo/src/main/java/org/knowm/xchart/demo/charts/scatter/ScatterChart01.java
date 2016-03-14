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

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.knowm.xchart.ChartBuilder_XY;
import org.knowm.xchart.Chart_XY;
import org.knowm.xchart.Series_XY.ChartXYSeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

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
public class ScatterChart01 implements ExampleChart<Chart_XY> {

  public static void main(String[] args) {

    ExampleChart<Chart_XY> exampleChart = new ScatterChart01();
    Chart_XY chart = exampleChart.getChart();
    new SwingWrapper<Chart_XY>(chart).displayChart();
  }

  @Override
  public Chart_XY getChart() {

    // Create Chart
    Chart_XY chart = new ChartBuilder_XY().width(800).height(600).build();

    // Customize Chart
    chart.getStyler().setDefaultSeriesRenderStyle(ChartXYSeriesRenderStyle.Scatter);
    chart.getStyler().setChartTitleVisible(false);
    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
    chart.getStyler().setMarkerSize(16);

    // Series
    List<Double> xData = new LinkedList<Double>();
    List<Double> yData = new LinkedList<Double>();
    Random random = new Random();
    int size = 1000;
    for (int i = 0; i < size; i++) {
      xData.add(random.nextGaussian() / 1000);
      yData.add(-1000000 + random.nextGaussian());
    }
    chart.addSeries("Gaussian Blob", xData, yData);

    return chart;
  }

}
