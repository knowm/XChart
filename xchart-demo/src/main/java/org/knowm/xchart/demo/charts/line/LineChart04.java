/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
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

import org.knowm.xchart.Chart;
import org.knowm.xchart.Series;
import org.knowm.xchart.SeriesLineStyle;
import org.knowm.xchart.SeriesMarker;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.style.XChartTheme;

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
      series.setLineColor(XChartTheme.BLUE);
      series.setLineStyle(SeriesLineStyle.SOLID);
      series.setMarker(SeriesMarker.CIRCLE);
      series.setMarkerColor(XChartTheme.BLUE);

    }

    return chart;
  }

}
