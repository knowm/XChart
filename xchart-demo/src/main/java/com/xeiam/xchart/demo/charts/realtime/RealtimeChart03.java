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
package com.xeiam.xchart.demo.charts.realtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.XChartPanel;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Realtime
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>real-time chart updates
 * <li>fixed window
 * <li>error bars
 */
public class RealtimeChart03 implements ExampleChart {

  private List<Integer> xData = new ArrayList<Integer>();
  private List<Double> yData = new ArrayList<Double>();
  private List<Double> errorBars = new ArrayList<Double>();

  public static final String SERIES_NAME = "series1";

  public static void main(String[] args) {

    // Setup the panel
    final RealtimeChart03 realtimeChart03 = new RealtimeChart03();
    final XChartPanel chartPanel = realtimeChart03.buildPanel();

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {

        // Create and set up the window.
        JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }
    });

    // Simulate a data feed
    TimerTask chartUpdaterTask = new TimerTask() {

      @Override
      public void run() {

        realtimeChart03.updateData();
        chartPanel.updateSeries(SERIES_NAME, realtimeChart03.xData, realtimeChart03.getyData(), realtimeChart03.errorBars);

      }
    };

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);

  }

  public XChartPanel buildPanel() {

    return new XChartPanel(getChart());
  }

  @Override
  public Chart getChart() {

    yData.add(0.0);
    for (int i = 0; i < 50; i++) {
      double lastPoint = yData.get(yData.size() - 1);
      yData.add(getRandomWalk(lastPoint));
    }
    // generate X-Data
    xData = new ArrayList<Integer>();
    for (int i = 1; i < yData.size() + 1; i++) {
      xData.add(i);
    }
    // generate error bars
    errorBars = new ArrayList<Double>();
    for (int i = 0; i < yData.size(); i++) {
      errorBars.add(20 * Math.random());
    }

    // Create Chart
    Chart chart = new Chart(500, 400);
    chart.setChartTitle("Sample Real-time Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.addSeries(SERIES_NAME, xData, yData, errorBars);

    return chart;
  }

  private Double getRandomWalk(double lastPoint) {

    return lastPoint + (Math.random() * 100 - 50);
  }

  public void updateData() {

    // Get some new data
    double lastPoint = yData.get(yData.size() - 1);
    yData.add(getRandomWalk(lastPoint));
    yData.remove(0);

    // update error bars
    errorBars.add(20 * Math.random());
    errorBars.remove(0);

  }

  public List<Double> getyData() {

    return yData;
  }
}
