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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Real-time XY Chart with Error Bars
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>real-time chart updates with JFrame
 * <li>fixed window
 * <li>error bars
 */
public class RealtimeChart03 implements ExampleChart<XYChart> {

  private XYChart xyChart;

  private List<Integer> xData = new CopyOnWriteArrayList<Integer>();
  private List<Double> yData = new CopyOnWriteArrayList<Double>();
  private List<Double> errorBars = new CopyOnWriteArrayList<Double>();

  public static final String SERIES_NAME = "series1";

  public static void main(String[] args) {

    // Setup the panel
    final RealtimeChart03 realtimeChart03 = new RealtimeChart03();
    final XChartPanel<XYChart> chartPanel = realtimeChart03.buildPanel();

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
        chartPanel.revalidate();
        chartPanel.repaint();
      }
    };

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);
  }

  public XChartPanel<XYChart> buildPanel() {

    return new XChartPanel<XYChart>(getChart());
  }

  @Override
  public XYChart getChart() {

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
    xyChart = new XYChartBuilder().width(500).height(400).xAxisTitle("X").yAxisTitle("Y").title("Real-time XY Chart with Error Bars").build();

    xyChart.addSeries(SERIES_NAME, xData, yData, errorBars);

    return xyChart;
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

    xyChart.updateXYSeries(SERIES_NAME, xData, yData, errorBars);
  }
}
