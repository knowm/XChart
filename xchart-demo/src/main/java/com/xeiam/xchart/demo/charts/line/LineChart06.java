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
package com.xeiam.xchart.demo.charts.line;

import java.awt.Color;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Logarithmic Y-Axis with Error Bars
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Error Bars
 * <li>Logarithmic Y-Axis
 * <li>Setting min and max values for Y-Axis
 * <li>Multi-line series labels in legend
 */
public class LineChart06 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new LineChart06();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    int[] xData = new int[] { 0, 1, 2, 3, 4, 5, 6 };

    int[] yData1 = new int[] { 100, 100, 100, 60, 10, 10, 10 };

    int[] errdata = new int[] { 50, 20, 10, 52, 9, 2, 1 };

    Chart chart = new Chart(800, 600);

    chart.getStyleManager().setYAxisLogarithmic(true);

    chart.getStyleManager().setYAxisMin(.08);

    chart.getStyleManager().setYAxisMax(1000);

    chart.getStyleManager().setErrorBarsColor(Color.black);

    Series series1 = chart.addSeries("Error bar\ntest data", xData, yData1, errdata);

    series1.setLineStyle(SeriesLineStyle.SOLID);

    series1.setMarker(SeriesMarker.DIAMOND);

    series1.setMarkerColor(Color.MAGENTA);

    return chart;
  }

}
