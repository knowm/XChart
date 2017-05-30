/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.YAxisPosition;

/**
 * Create a Chart matrix
 *
 * @author timmolter
 */
public class TestForIssue54_2 {

  static final int WIDTH = 465;
  static final int HEIGHT = 320;

  public static void main(String[] args) {

    Chart chart = getLineChart();
    chart.setTitle("sin(x) on second axis with title");
    Series series = (Series) chart.getSeriesMap().get("y=sin(x)");
    series.setYAxisGroup(1);
    chart.getStyler().setYAxisGroupPosition(1, YAxisPosition.Left);
    chart.getStyler().setYAxisGroupPosition(0, YAxisPosition.Right);
    chart.setYAxisGroupTitle(1, "sin(x)");

    new SwingWrapper<Chart>(chart).displayChart();

  }

  static Chart getLineChart() {

    XYChart chart = new XYChartBuilder().width(WIDTH).height(HEIGHT).xAxisTitle("X").yAxisTitle("Y").build();

    // Customize Chart
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    // generates sine data
    int size = 30;
    List<Integer> xData = new ArrayList<Integer>();
    List<Double> yData = new ArrayList<Double>();
    List<Integer> xData2 = new ArrayList<Integer>();
    List<Double> yData2 = new ArrayList<Double>();
    for (int i = 0; i <= size; i++) {
      double radians = (Math.PI / (size / 2) * i);
      int x = i - size / 2;
      xData.add(x);
      yData.add(-1 * Math.sin(radians));
      xData2.add(x);
      yData2.add(-10 * Math.cos(radians));
    }

    // Series
    chart.addSeries("y=sin(x)", xData, yData);
    chart.addSeries("y=cos(x)", xData2, yData2);
    return chart;
  }

}
