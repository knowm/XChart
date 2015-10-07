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
package org.knowm.xchart.demo.charts.bar;

import org.knowm.xchart.Chart;
import org.knowm.xchart.ChartBuilder;
import org.knowm.xchart.StyleManager.ChartType;
import org.knowm.xchart.StyleManager.LegendPosition;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Basic Bar Chart
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Number categories
 * <li>All positive values
 * <li>Single series
 * <li>Place legend at Inside-NW position
 */
public class BarChart01 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new BarChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new ChartBuilder().chartType(ChartType.Bar).width(800).height(600).title("Score Histogram").xAxisTitle("Score").yAxisTitle("Number").build();
    chart.addSeries("test 1", new double[] { 0, 1, 2, 3, 4 }, new double[] { 4, 5, 9, 6, 5 });

    // Customize Chart
    chart.getStyleManager().setLegendPosition(LegendPosition.InsideNW);

    return chart;
  }
}
