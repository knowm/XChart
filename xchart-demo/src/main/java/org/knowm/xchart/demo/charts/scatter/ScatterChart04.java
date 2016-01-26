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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.ChartBuilder_XY;
import org.knowm.xchart.Chart_XY;
import org.knowm.xchart.Series_XY;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.style.markers.SeriesMarkers;

/**
 * Error Bars
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Error Bars
 * <li>Using ChartBuilder to Make a Chart
 * <li>List<Number> data sets
 * <li>Setting Series Marker and Marker Color
 * <li>Using a custom decimal pattern
 */
public class ScatterChart04 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new ScatterChart04();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // generates data
    int size = 10;
    List<Double> xData = new ArrayList<Double>();
    List<Double> yData = new ArrayList<Double>();
    List<Double> errorBars = new ArrayList<Double>();
    for (int i = 0; i <= size; i++) {
      xData.add(((double) i) / 1000000);
      yData.add(10 * Math.exp(-i));
      errorBars.add(Math.random() + .3);
    }

    // Create Chart
    Chart_XY chart = new ChartBuilder_XY().width(800).height(600).title("ScatterChart04").xAxisTitle("X").yAxisTitle("Y").build();

    // Customize Chart
    chart.getStyler().setChartTitleVisible(false);
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setAxisTitlesVisible(false);
    chart.getStyler().setXAxisDecimalPattern("0.0000000");

    // Series
    Series_XY series = chart.addSeries("10^(-x)", xData, yData, errorBars);
    series.setMarkerColor(Color.RED);
    series.setMarker(SeriesMarkers.SQUARE);

    return chart;
  }

}
