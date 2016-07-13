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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;

import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * Real-time Bubble Chart
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>real-time chart updates
 * <li>multiple series
 * <li>Bubble chart
 * <li>GGPlot2 chart
 */
public class RealtimeChart04 implements ExampleChart<BubbleChart> {

  private List<Double> yData;
  private List<Double> bubbleData;
  public static final String SERIES_NAME = "series1";

  public static void main(String[] args) {

    // Setup the panel
    final RealtimeChart04 realtimeChart04 = new RealtimeChart04();
    final XChartPanel<BubbleChart> chartPanel = realtimeChart04.buildPanel();

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

        realtimeChart04.updateData();
        chartPanel.updateXYSeries(SERIES_NAME, null, realtimeChart04.getyData(), realtimeChart04.getbubbleData());

      }
    };

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);

  }

  public XChartPanel<BubbleChart> buildPanel() {

    return new XChartPanel<BubbleChart>(getChart());
  }

  @Override
  public BubbleChart getChart() {

    yData = getRandomData(5);
    bubbleData = getRandomData(5);

    // Create Chart
    BubbleChart chart = new BubbleChart(500, 400, ChartTheme.GGPlot2);
    chart.setTitle("Real-time Bubble Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");

    chart.addSeries(SERIES_NAME, null, yData, bubbleData);

    return chart;
  }

  private List<Double> getRandomData(int numPoints) {

    List<Double> data = new CopyOnWriteArrayList<Double>();
    for (int i = 0; i < numPoints; i++) {
      data.add(Math.random() * 100);
    }
    return data;
  }

  public void updateData() {

    // Get some new data
    List<Double> newData = getRandomData(1);
    yData.addAll(newData);
    // Limit the total number of points
    while (yData.size() > 20) {
      yData.remove(0);
    }

    // Get some new data
    newData = getRandomData(1);
    bubbleData.addAll(newData);
    // Limit the total number of points
    while (bubbleData.size() > 20) {
      bubbleData.remove(0);
    }

  }

  public List<Double> getyData() {

    return yData;
  }

  public List<Double> getbubbleData() {

    return bubbleData;
  }
}
