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
package org.knowm.xchart.demo.charts.bar;

import java.util.Arrays;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Missing Point in Series
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Number categories
 * <li>Positive values
 * <li>Multiple series
 * <li>Missing point in series
 * <li>Manually setting y-axis min and max values
 * <li>Bar Chart Annotations
 */
public class BarChart04 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart04();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("XFactor vs. Age").xAxisTitle("Age").yAxisTitle("XFactor").build();

    // Customize Chart
    chart.getStyler().setYAxisMin(5.0);
    chart.getStyler().setYAxisMax(70.0);
    chart.getStyler().setHasAnnotations(true);
    chart.getStyler().setPlotGridVerticalLinesVisible(false);

    // Series
    chart.addSeries("female", Arrays.asList(new Integer[] { 10, 20, 30, 40, 50 }), Arrays.asList(new Integer[] { 50, 10, 20, 40, 35 }));
    chart.addSeries("male", Arrays.asList(new Integer[] { 10, 20, 30, 40, 50 }), Arrays.asList(new Integer[] { 40, 30, 20, null, 60 }));

    return chart;
  }
}
