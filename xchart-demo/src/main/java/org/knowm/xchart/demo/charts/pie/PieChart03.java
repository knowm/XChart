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

import org.knowm.xchart.ChartBuilder_Pie;
import org.knowm.xchart.Chart_Pie;
import org.knowm.xchart.Styler_Pie.AnnotationType;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.style.Styler.ChartTheme;

/**
 * Pie Chart with 5 Slices and customization
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Pie Chart
 * <li>ChartBuilderPie
 * <li>GGPlot2 Theme
 */
public class PieChart03 implements ExampleChart<Chart_Pie> {

  public static void main(String[] args) {

    ExampleChart<Chart_Pie> exampleChart = new PieChart03();
    Chart_Pie chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart_Pie getChart() {

    // Create Chart
    Chart_Pie chart = new ChartBuilder_Pie().width(400).height(500).title(getClass().getSimpleName()).theme(ChartTheme.GGPlot2).build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setAnnotationType(AnnotationType.LabelAndPercentage);
    chart.getStyler().setAnnotationDistance(1.15);
    chart.getStyler().setPlotContentSize(.7);

    // Series
    chart.addSeries("Prague", 2);
    chart.addSeries("Dresden", 4);
    chart.addSeries("Munich", 34);
    chart.addSeries("Hamburg", 22);
    chart.addSeries("Berlin", 29);

    return chart;
  }

}
