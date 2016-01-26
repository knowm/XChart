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

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.Chart_XY;
import org.knowm.xchart.Series_XY;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.style.colors.XChartSeriesColors;
import org.knowm.xchart.internal.style.lines.SeriesLines;
import org.knowm.xchart.internal.style.markers.SeriesMarkers;

/**
 * Sine wave with customized series style
 * <p>
 * * Demonstrates the following:
 * <ul>
 * <li>Customizing the series style properties
 */
public class LineChart02 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new LineChart02();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // generates sine data
    int size = 30;
    List<Integer> xData = new ArrayList<Integer>();
    List<Double> yData = new ArrayList<Double>();
    for (int i = 0; i <= size; i++) {
      double radians = (Math.PI / (size / 2) * i);
      xData.add(i - size / 2);
      yData.add(-.000001 * Math.sin(radians));
    }

    // Create Chart
    Chart_XY chart = new Chart_XY(800, 600);

    // Customize Chart
    chart.getStyler().setChartTitleVisible(false);
    chart.getStyler().setLegendVisible(false);

    // Series 1
    Series_XY series1 = chart.addSeries("y=sin(x)", xData, yData);
    series1.setLineColor(XChartSeriesColors.PURPLE);
    series1.setLineStyle(SeriesLines.DASH_DASH);
    series1.setMarkerColor(XChartSeriesColors.GREEN);
    series1.setMarker(SeriesMarkers.SQUARE);

    return chart;
  }

}
