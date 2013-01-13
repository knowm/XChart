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
package com.xeiam.xchart.demo.charts;

import java.util.ArrayList;
import java.util.Collection;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.SwingWrapper;

/**
 * Sine wave with customized series style
 * 
 * @author timmolter
 */
public class Example2 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new Example10();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // generates sine data
    int size = 30;
    Collection<Number> xData1 = new ArrayList<Number>();
    Collection<Number> yData1 = new ArrayList<Number>();
    for (int i = 0; i <= size; i++) {
      double radians = (Math.PI / (size / 2) * i);
      xData1.add(i - size / 2);
      yData1.add(size * Math.sin(radians));
    }

    // Create Chart
    Chart chart = new Chart(800, 600);

    // Customize Chart
    chart.setTitleVisible(false);
    chart.setLegendVisible(false);

    // Series 1
    Series series1 = chart.addSeries("y=sin(x)", xData1, yData1);
    series1.setLineColor(SeriesColor.PURPLE);
    series1.setLineStyle(SeriesLineStyle.DASH_DASH);
    series1.setMarkerColor(SeriesColor.GREEN);
    series1.setMarker(SeriesMarker.SQUARE);

    return chart;
  }

}
