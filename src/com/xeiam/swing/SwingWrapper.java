/**
 * Copyright 2011 Xeiam LLC.
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
package com.xeiam.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.xeiam.xcharts.Chart;

/**
 * @author timmolter
 */
public class SwingWrapper {

    private Chart[] charts;
    private int numRows;
    private int numColumns;

    /**
     * Constructor
     * 
     * @param chart
     */
    public SwingWrapper(Chart chart) {
        this.charts = new Chart[1];
        charts[0] = chart;
    }

    /**
     * Constructor
     * 
     * @param charts
     */
    public SwingWrapper(Chart[] charts, int numRows, int numColumns) {
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
                frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

                JPanel chartPanel = new ChartJPanel(charts[0]);
                frame.getContentPane().add(chartPanel);

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

                for (int i = 0; i < charts.length; i++) {

                    if (charts[i] != null) {
                        JPanel chartPanel = new ChartJPanel(charts[i]);
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

    private class ChartJPanel extends JPanel {

        private Chart chart;

        public ChartJPanel(Chart chart) {
            this.chart = chart;
        }

        @Override
        public void paint(Graphics g) {
            chart.paint((Graphics2D) g);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(chart.getWidth(), chart.getHeight());
        }
    }
}
