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
package org.knowm.xchart.demo.charts.line;

import java.awt.Color;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

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
public class LineChart06 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new LineChart06();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<XYChart>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).build();

    // Customize Chart
    chart.getStyler().setYAxisLogarithmic(true);
    chart.getStyler().setYAxisMin(.08);
    chart.getStyler().setYAxisMax(1000.0);
    chart.getStyler().setErrorBarsColor(Color.black);

    // Series
    int[] xData = new int[] { 0, 1, 2, 3, 4, 5, 6 };
    int[] yData1 = new int[] { 100, 100, 100, 60, 10, 10, 10 };
    int[] errdata = new int[] { 50, 20, 10, 52, 9, 2, 1 };
    XYSeries series1 = chart.addSeries("Error bar\ntest data", xData, yData1, errdata);
    series1.setLineStyle(SeriesLines.SOLID);
    series1.setMarker(SeriesMarkers.DIAMOND);
    series1.setMarkerColor(Color.GREEN);

    return chart;
  }

}
