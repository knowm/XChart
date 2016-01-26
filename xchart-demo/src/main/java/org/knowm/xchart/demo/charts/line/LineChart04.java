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

import org.knowm.xchart.ChartBuilder_XY;
import org.knowm.xchart.Chart_XY;
import org.knowm.xchart.Series_XY;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.style.colors.XChartSeriesColors;
import org.knowm.xchart.internal.style.lines.SeriesLines;
import org.knowm.xchart.internal.style.markers.SeriesMarkers;

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
    Chart_XY chart = new ChartBuilder_XY().width(800).height(600).title("LineChart04").xAxisTitle("X").yAxisTitle("Y").build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);

    // Series
    for (int i = 0; i < 200; i++) {
      Series_XY series = chart.addSeries("A" + i, new double[] { Math.random() / 1000, Math.random() / 1000 }, new double[] { Math.random() / -1000, Math.random() / -1000 });
      series.setLineColor(XChartSeriesColors.BLUE);
      series.setLineStyle(SeriesLines.SOLID);
      series.setMarker(SeriesMarkers.CIRCLE);
      series.setMarkerColor(XChartSeriesColors.BLUE);
    }

    return chart;
  }

}
