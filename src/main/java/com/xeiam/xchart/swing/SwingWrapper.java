/**
 * Copyright 2011-2012 Xeiam LLC.
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
package com.xeiam.xchart.swing;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.xeiam.xchart.Chart;

/**
 * @author timmolter
 */
public class SwingWrapper {

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
   * Constructor - The number of rows and columns will be calculated automatically
   * 
   * @param charts
   * @param numRows
   * @param numColumns
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
   */
  public void displayChart() {

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {

        // Create and set up the window.
        JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(new Dimension(charts.get(0).getWidth(), charts.get(0).getHeight()));
        JPanel chartPanel = new XChartJPanel(charts.get(0));
        // frame.getContentPane().add(chartPanel);
        frame.add(chartPanel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }
    });
  }

  /**
   * Display the chart in a Swing JFrame
   */
  public void displayChartMatrix() {

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {

        // Create and set up the window.
        JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(numRows, numColumns));

        for (Chart chart : charts) {
          if (chart != null) {
            JPanel chartPanel = new XChartJPanel(chart);
            frame.getContentPane().add(chartPanel);
          } else {
            JPanel chartPanel = new JPanel();
            frame.getContentPane().add(chartPanel);
          }

        }

        // Display the window.
        frame.pack();
        frame.setVisible(true);
      }
    });
  }

}
