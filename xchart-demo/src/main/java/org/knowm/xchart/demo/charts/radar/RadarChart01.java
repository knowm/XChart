/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.demo.charts.radar;

import org.knowm.xchart.RadarChart;
import org.knowm.xchart.RadarChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Radar Chart GGPlot2 Theme
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Radar Chart
 * <li>RadarChartBuilder
 * <li>Setting start angle
 */
public class RadarChart01 implements ExampleChart<RadarChart> {

  public static void main(String[] args) {

    ExampleChart<RadarChart> exampleChart = new RadarChart01();
    RadarChart chart = exampleChart.getChart();
    new SwingWrapper<RadarChart>(chart).displayChart();
  }

  @Override
  public RadarChart getChart() {

    // Create Chart
    RadarChart chart = new RadarChartBuilder().width(800).height(600).title("Radar Chart").build();

    // Series
    chart.setVariableLabels(new String[] {"Sales", "Marketting", "Development", "Customer Support", "Information Technology", "Administration" });
    chart.addSeries("Old System", new double[] { 0.78, 0.85, 0.80, 0.82, 0.93, 0.92 }, new String[] { "Lowest varible 78%", "85%", null, null, null, null });
    chart.addSeries("New System", new double[] { 0.67, 0.73, 0.97, 0.95, 0.93, 0.73});
    chart.addSeries("Experimental System", new double[] { 0.37, 0.93, 0.57, 0.55, 0.33, 0.73});
    
    return chart;
  }
}
