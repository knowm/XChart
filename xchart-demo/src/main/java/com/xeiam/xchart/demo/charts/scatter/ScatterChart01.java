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
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Gaussian Blob
 * 
 * @author timmolter
 */
public class ScatterChart01 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new ScatterChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    List<Number> xData = new ArrayList<Number>();
    List<Number> yData = new ArrayList<Number>();
    Random random = new Random();
    int size = 1000;
    for (int i = 0; i < size; i++) {
      xData.add(random.nextGaussian());
      yData.add(random.nextGaussian());
    }

    // Create Chart
    Chart chart = new Chart(800, 600);
    chart.getStyleManager().setChartType(ChartType.Scatter);

    // Customize Chart
    chart.getStyleManager().setChartTitleVisible(false);
    chart.getStyleManager().setLegendPosition(LegendPosition.InsideSW);

    // Series
    chart.addSeries("Gaussian Blob", xData, yData);

    return chart;
  }

}
