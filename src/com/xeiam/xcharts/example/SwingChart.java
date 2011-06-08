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
package com.xeiam.xcharts.example;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.xeiam.xcharts.Chart;
import com.xeiam.xcharts.JChartPanel;
import com.xeiam.xcharts.series.Series;
import com.xeiam.xcharts.series.SeriesMarker;

/**
 * @author timmolter
 */
public class SwingChart {

    private static void createAndShowGUI() {

        // generates sine data
        int size = 100;
        double[] xData1 = new double[size + 1];
        double[] yData1 = new double[size + 1];
        for (int i = 0; i <= size; i++) {
            double radians = (Math.PI / (size / 2) * i);
            xData1[i] = i - size / 2;
            yData1[i] = size * Math.sin(radians);
        }

        // generates linear data
        int size2 = 40;
        double[] xData2 = new double[size2 + 1];
        double[] yData2 = new double[size2 + 1];
        for (int i = 0; i <= size2; i++) {
            xData2[i] = -size2 + 2 * i;
            yData2[i] = -size2 + 2 * i;
        }

        // Create and set up the window.
        JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);

        // Create Chart
        Chart chart = new Chart(800, 600);

        // Customize Chart
        chart.setChartTitle("Sample Chart");
        chart.setXAxisTitle("X");
        chart.setYAxisTitle("Y");
        // chart.setChartTitleVisible(false);
        // chart.setChartLegendVisible(false);
        // chart.setAxisTitlesVisible(false);

        // Series 1
        Series series1 = chart.addSeries("y=sin(x)", xData1, yData1);
        // series1.setLineColor(SeriesColor.PURPLE);
        // series1.setLineStyle(SeriesLineStyle.NONE);
        // series1.setMarkerColor(SeriesColor.GREEN);
        // series1.setMarker(SeriesMarker.NONE);

        // Series 2
        Series series2 = chart.addSeries("y=x", xData2, yData2);
        // series2.setLineColor(SeriesColor.PURPLE);
        // series2.setLineStyle(SeriesLineStyle.NONE);
        // series2.setMarkerColor(SeriesColor.GREEN);
        series2.setMarker(SeriesMarker.NONE);

        // Swing
        JPanel chartPanel = new JChartPanel(chart);

        // add the panel to the content pane
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);

        // Display the window
        frame.pack();
        frame.setLocationRelativeTo(null); // centers on screen
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
