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
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

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
    Chart chart = new Chart(800, 600);

    // Customize Chart
    chart.setChartTitle("LineChart05");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.getStyleManager().setLegendPosition(LegendPosition.InsideSW);

    double[] xData = new double[] { 0.0, 1.0, 2.0, 3.0, 4.0, 5, 6 };
    double[] yData = new double[] { 106, 44, 26, 10, 7.5, 3.4, .88 };
    double[] yData2 = new double[] { 102, 49, 23.6, 11.3, 5.4, 2.6, 1.25 };

    Series series = chart.addSeries("A", xData, yData);
    series.setLineStyle(SeriesLineStyle.NONE);
    series.setMarker(SeriesMarker.DIAMOND);
    series.setMarkerColor(Color.BLACK);

    Series series2 = chart.addSeries("B", xData, yData2);
    series2.setMarker(SeriesMarker.NONE);
    series2.setLineStyle(SeriesLineStyle.DASH_DASH);
    series2.setLineColor(Color.BLACK);

    chart.getStyleManager().setYAxisLogarithmic(true);

    chart.getStyleManager().setYAxisMin(0.01);
    chart.getStyleManager().setYAxisMax(1000);

    chart.getStyleManager().setXAxisMin(2);
    chart.getStyleManager().setXAxisMax(7);

    return chart;
  }

}
