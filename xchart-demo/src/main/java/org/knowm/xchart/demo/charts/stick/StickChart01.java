/**
 * Copyright 2013 Xeiam LLC.
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
package org.knowm.xchart.demo.charts.stick;

import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.ChartBuilder_Category;
import org.knowm.xchart.Chart_Category;
import org.knowm.xchart.Series_Category.ChartCategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.style.Styler.LegendPosition;

/**
 * Stick Chart
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Stick category series render type
 */
public class StickChart01 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new StickChart01();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart_Category chart = new ChartBuilder_Category().width(800).height(600).title("Stick").build();

    // Customize Chart
    chart.getStyler().setChartTitleVisible(true);
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setChartCategorySeriesRenderStyle(ChartCategorySeriesRenderStyle.Stick);

    // Series
    List<Integer> xData = new ArrayList<Integer>();
    List<Integer> yData = new ArrayList<Integer>();
    for (int i = -3; i <= 24; i++) {
      xData.add(i);
      yData.add(i);
    }
    chart.addSeries("data", xData, yData);

    return chart;
  }
}