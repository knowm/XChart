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
package org.knowm.xchart.demo.charts.pie;

import java.awt.Color;

import org.knowm.xchart.ChartBuilder_Pie;
import org.knowm.xchart.Chart_Pie;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Pie Chart Custom Color Palette
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Pie Chart
 * <li>ChartBuilderPie
 * <li>Custom series palette
 */
public class PieChart02 implements ExampleChart<Chart_Pie> {

  public static void main(String[] args) {

    ExampleChart<Chart_Pie> exampleChart = new PieChart02();
    Chart_Pie chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart_Pie getChart() {

    // Create Chart
    Chart_Pie chart = new ChartBuilder_Pie().width(800).height(600).title(getClass().getSimpleName()).build();
    chart.addSeries("Gold", 24);
    chart.addSeries("Silver", 21);
    chart.addSeries("Platinum", 39);
    chart.addSeries("Copper", 17);
    chart.addSeries("Zinc", 40);

    Color[] sliceColors = new Color[] { new Color(224, 68, 14), new Color(230, 105, 62), new Color(236, 143, 110), new Color(243, 180, 159), new Color(246, 199, 182) };
    chart.getStyler().setSeriesColors(sliceColors);

    return chart;
  }

}
