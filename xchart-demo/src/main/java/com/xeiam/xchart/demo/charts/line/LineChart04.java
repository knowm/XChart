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

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Hundreds of Series on One Plot
 */
public class LineChart04 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new LineChart04();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new Chart(800, 600);

    // Customize Chart
    chart.setChartTitle("LineChart04");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.getStyleManager().setLegendVisible(false);

    for (int i = 0; i < 200; i++) {
      Series series = chart.addSeries("A" + i, new double[] { Math.random() / 1000, Math.random() / 1000 }, new double[] { Math.random() / -1000, Math.random() / -1000 });
      series.setLineColor(SeriesColor.BLUE);
      series.setLineStyle(SeriesLineStyle.SOLID);
      series.setMarker(SeriesMarker.CIRCLE);
      series.setMarkerColor(SeriesColor.BLUE);
    }

    return chart;
  }

}
