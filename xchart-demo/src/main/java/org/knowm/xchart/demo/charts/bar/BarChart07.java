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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Histogram Not Overlapped
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Histogram
 * <li>Bar Chart styles - not overlapped, bar width
 * <li>Integer data values
 */
public class BarChart07 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart07();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Score Histogram").xAxisTitle("Mean").yAxisTitle("Count").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setAvailableSpaceFill(.96);
    chart.getStyler().setPlotGridVerticalLinesVisible(false);

    // Series
    Histogram histogram1 = new Histogram(getGaussianData(1000), 10, -30, 30);
    chart.addSeries("histogram 1", histogram1.getxAxisData(), histogram1.getyAxisData());
    Histogram histogram2 = new Histogram(getGaussianData(1000), 10, -30, 30);
    chart.addSeries("histogram 2", histogram2.getxAxisData(), histogram2.getyAxisData());

    return chart;
  }

  private List<Integer> getGaussianData(int count) {

    List<Integer> data = new ArrayList<Integer>(count);
    Random r = new Random();
    for (int i = 0; i < count; i++) {
      data.add((int) (r.nextGaussian() * 10));
    }
    return data;
  }

}
