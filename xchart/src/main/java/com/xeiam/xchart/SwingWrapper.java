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
package com.xeiam.xchart;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A convenience class used to display a Chart in a barebones Swing application
 * 
 * @author timmolter
 */
public class SwingWrapper {

  private String windowTitle = "XChart";

  private List<Chart> charts = new ArrayList<Chart>();
  private int numRows;
  private int numColumns;

  /**
   * Constructor
   * 
   * @param chart
   */
  public SwingWrapper(Chart chart) {

    this.charts.add(chart);
  }

  /**
   * Constructor - The number of rows and columns will be calculated automatically Constructor
   * 
   * @param charts
   */
  public SwingWrapper(List<Chart> charts) {

    this.charts = charts;

    this.numRows = (int) (Math.sqrt(charts.size()) + .5);
    this.numColumns = (int) ((double) charts.size() / this.numRows + 1);
  }

  /**
   * Constructor
   * 
   * @param charts
   * @param numRows - the number of rows
   * @param numColumns - the number of columns
   */
  public SwingWrapper(List<Chart> charts, int numRows, int numColumns) {

    this.charts = charts;
    this.numRows = numRows;
    this.numColumns = numColumns;
  }

  /**
   * Display the chart in a Swing JFrame
   * 
   * @param windowTitle the title of the window
   */
  public JFrame displayChart(String windowTitle) {

    this.windowTitle = windowTitle;

    return displayChart();
  }

  /**
   * Display the chart in a Swing JFrame
   */
  public JFrame displayChart() {

    // Create and set up the window.
    final JFrame frame = new JFrame(windowTitle);

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel chartPanel = new XChartPanel(charts.get(0));
        frame.add(chartPanel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }
    });

    return frame;
  }

  /**
   * Display the charts in a Swing JFrame
   * 
   * @param windowTitle the title of the window
   * @return the JFrame
   */
  public JFrame displayChartMatrix(String windowTitle) {

    this.windowTitle = windowTitle;

    return displayChartMatrix();
  }

  /**
   * Display the chart in a Swing JFrame
   */
  public JFrame displayChartMatrix() {

    // Create and set up the window.
    final JFrame frame = new JFrame(windowTitle);

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(numRows, numColumns));

        for (Chart chart : charts) {
          if (chart != null) {
            JPanel chartPanel = new XChartPanel(chart);
            frame.add(chartPanel);
          }
          else {
            JPanel chartPanel = new JPanel();
            frame.getContentPane().add(chartPanel);
          }

        }

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }
    });

    return frame;
  }

}
