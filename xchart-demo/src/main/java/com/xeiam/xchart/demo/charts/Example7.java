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

import java.util.Arrays;
import java.util.Collection;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.SwingWrapper;

/**
 * Longs as X-Axis data
 * 
 * @author timmolter
 */
public class Example7 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new Example7();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    Collection<Number> xData = Arrays.asList(new Number[] { 12228120L, 12228984L, 12229848L, 12230712L, 12231576L, 12232440L, 12233304L, 12234168L, 12235032L, 12235896L });
    Collection<Number> yData = Arrays.asList(new Number[] { 0.0, 1.0, 2.0, 0.0, 1.0, 2.0, 0.0, 1.0, 2.0, 0.0 });

    // Create Chart
    Chart chart = new Chart(800, 600);
    chart.setChartTitle("Example7");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.addSeries("y(x)", xData, yData);

    return chart;
  }

}
