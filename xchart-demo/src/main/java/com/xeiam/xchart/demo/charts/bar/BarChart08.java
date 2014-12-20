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
package com.xeiam.xchart.demo.charts.bar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Histogram;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Histogram with Error Bars
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Histogram
 * <li>Bar Chart with error bars
 */
public class BarChart08 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new BarChart08();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new ChartBuilder().chartType(ChartType.Bar).width(800).height(600).title("Histogram").xAxisTitle("Mean").yAxisTitle("Count").build();

    Histogram histogram1 = new Histogram(getGaussianData(10000), 10, -10, 10);
    chart.addSeries("histogram", histogram1.getxAxisData(), histogram1.getyAxisData(), getFakeErrorData(histogram1.getxAxisData().size()));

    // Customize Chart
    chart.getStyleManager().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyleManager().setBarWidthPercentage(.96);

    return chart;
  }

  private List<Double> getGaussianData(int count) {

    List<Double> data = new ArrayList<Double>(count);
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      data.add(r.nextGaussian() * 5);
      // data.add(r.nextDouble() * 60 - 30);
    }
    return data;
  }

  private List<Double> getFakeErrorData(int count) {

    List<Double> data = new ArrayList<Double>(count);
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      data.add(r.nextDouble() * 200);
    }
    return data;
  }
}
