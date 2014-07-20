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
package com.xeiam.xchart.demo.charts.area;

import java.util.ArrayList;
import java.util.List;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Null Y-Axis Data Points
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Area Chart
 * <li>null Y-Axis values
 * <li>ChartBuilder
 */
public class AreaChart02 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new AreaChart02();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new ChartBuilder().chartType(ChartType.Area).width(800).height(600).title(getClass().getSimpleName()).xAxisTitle("X").yAxisTitle("Y").build();

    List<Integer> xData = new ArrayList<Integer>();
    List<Integer> yData = new ArrayList<Integer>();
    for (int i = 0; i < 5; i++) {
      xData.add(i);
      yData.add(i * i);
    }
    xData.add(5);
    yData.add(null);

    for (int i = 6; i < 10; i++) {
      xData.add(i);
      yData.add(i * i);
    }
    xData.add(10);
    yData.add(null);
    xData.add(11);
    yData.add(100);
    xData.add(12);
    yData.add(90);

    chart.addSeries("a", xData, yData);

    // Customize Chart
    chart.getStyleManager().setLegendPosition(LegendPosition.InsideNW);

    return chart;
  }

}
