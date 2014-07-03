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

import java.util.ArrayList;
import java.util.List;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

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
    Chart chart = new Chart(800, 600);

    // Customize Chart
    chart.getStyleManager().setChartTitleVisible(false);
    chart.getStyleManager().setLegendVisible(false);

    // Series 1
    Series series1 = chart.addSeries("y=sin(x)", xData, yData);
    series1.setLineColor(SeriesColor.PURPLE);
    series1.setLineStyle(SeriesLineStyle.DASH_DASH);
    series1.setMarkerColor(SeriesColor.GREEN);
    series1.setMarker(SeriesMarker.SQUARE);

    return chart;
  }

}
