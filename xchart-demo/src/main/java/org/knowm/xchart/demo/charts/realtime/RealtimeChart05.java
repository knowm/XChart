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
package org.knowm.xchart.demo.charts.realtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * Real-time Category Chart
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>real-time chart updates with SwingWrapper
 */
public class RealtimeChart05 implements ExampleChart<CategoryChart> {

  private CategoryChart categoryChart;

  private List<String> xData;
  private List<Double> yData;
  public static final String SERIES_NAME = "series1";

  public static void main(String[] args) {

    // Setup the panel
    final RealtimeChart05 realtimeChart01 = new RealtimeChart05();
    realtimeChart01.go();
  }

  private void go() {

    final SwingWrapper<CategoryChart> swingWrapper = new SwingWrapper<CategoryChart>(getChart());
    swingWrapper.displayChart();

    // Simulate a data feed
    TimerTask chartUpdaterTask = new TimerTask() {

      @Override
      public void run() {

        updateData();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {

            swingWrapper.repaintChart();
          }
        });
      }
    };

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);
  }

  @Override
  public CategoryChart getChart() {

    xData = new ArrayList<String>(Arrays.asList(new String[] { "Blue", "Red", "Green", "Yellow", "Orange" }));
    Histogram histogram = new Histogram(getGaussianData(1000), 5, -10, 10);
    yData = histogram.getyAxisData();

    // Create Chart
    categoryChart = new CategoryChartBuilder().width(500).height(400).theme(ChartTheme.Matlab).title("Real-time Category Chart").build();

    categoryChart.addSeries(SERIES_NAME, xData, yData);

    return categoryChart;
  }

  public void updateData() {

    // Get some new data

    Histogram histogram = new Histogram(getGaussianData(1000), 5, -10, 10);
    yData = histogram.getyAxisData();

    categoryChart.updateCategorySeries(SERIES_NAME, xData, yData, null);
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
}
