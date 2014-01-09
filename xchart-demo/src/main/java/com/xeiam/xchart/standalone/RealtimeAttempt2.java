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
package com.xeiam.xchart.standalone;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.XChartPanel;

/**
 * @author timmolter
 */
public class RealtimeAttempt2 {

  private Chart chart;
  private XChartPanel chartPanel;
  private static final String SERIES_NAME = "series1";
  private List<Number> yData;

  public static void main(String[] args) throws Exception {

    // Setup the panel
    final RealtimeAttempt2 realtimeAttempt = new RealtimeAttempt2();
    realtimeAttempt.buildPanel();

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {

        // Create and set up the window.
        JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(realtimeAttempt.getChartPanel());

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }
    });

    // Simulate a data feed
    TimerTask chartUpdaterTask = new TimerTask() {

      @Override
      public void run() {

        realtimeAttempt.updateData();
      }
    };

    Timer timer = new Timer();
    timer.scheduleAtFixedRate(chartUpdaterTask, 0, 500);

  }

  public void buildPanel() {

    yData = getRandomData(5);

    // Create Chart
    chart = new Chart(500, 400);
    chart.setChartTitle("Sample Real-time Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.addSeries(SERIES_NAME, null, yData);

    chartPanel = new XChartPanel(chart);
  }

  public void updateData() {

    // Get some new data
    List<Number> newData = getRandomData(1);

    yData.addAll(newData);

    // Limit the total number of points
    while (yData.size() > 20) {
      yData.remove(0);
    }

    chartPanel.updateSeries(SERIES_NAME, yData);
  }

  public Chart getChart() {

    return chart;
  }

  public JPanel getChartPanel() {

    return chartPanel;
  }

  private static List<Number> getRandomData(int numPoints) {

    List<Number> data = new ArrayList<Number>();
    for (int i = 0; i < numPoints; i++) {
      data.add(Math.random() * 100);
    }
    return data;
  }
}
