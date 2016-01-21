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

import org.knowm.xchart.Chart_XY;
import org.knowm.xchart.Series_XY;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.style.Styler.LegendPosition;
import org.knowm.xchart.internal.style.lines.SeriesLines;
import org.knowm.xchart.internal.style.markers.SeriesMarkers;

/**
 * Scatter and Line
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Customizing the series style properties
 * <li>Scatter and Line overlay
 * <li>Logarithmic Y-Axis
 * <li>An X-Axis min value clipping off the series
 */
public class LineChart05 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new LineChart05();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart_XY chart = new Chart_XY(800, 600);

    // Customize Chart
    chart.setTitle("LineChart05");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);

    double[] xData = new double[] { 0.0, 1.0, 2.0, 3.0, 4.0, 5, 6 };
    double[] yData = new double[] { 106, 44, 26, 10, 7.5, 3.4, .88 };
    double[] yData2 = new double[] { 102, 49, 23.6, 11.3, 5.4, 2.6, 1.25 };

    Series_XY series = chart.addSeries("A", xData, yData);
    series.setLineStyle(SeriesLines.NONE);
    series.setMarker(SeriesMarkers.DIAMOND);
    series.setMarkerColor(Color.BLACK);

    Series_XY series2 = chart.addSeries("B", xData, yData2);
    series2.setMarker(SeriesMarkers.NONE);
    series2.setLineStyle(SeriesLines.DASH_DASH);
    series2.setLineColor(Color.BLACK);

    chart.getStyler().setYAxisLogarithmic(true);

    chart.getStyler().setYAxisMin(0.01);
    chart.getStyler().setYAxisMax(1000);

    chart.getStyler().setXAxisMin(2);
    chart.getStyler().setXAxisMax(7);

    return chart;
  }

}
