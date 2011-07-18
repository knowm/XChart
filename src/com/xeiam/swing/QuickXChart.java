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

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.xeiam.xcharts.Chart;
import com.xeiam.xcharts.JChartPanel;
import com.xeiam.xcharts.series.Series;
import com.xeiam.xcharts.series.SeriesMarker;

public class QuickXChart {

    Chart[] charts;
    int numRows;
    int numCols;

    int chartIdx = 0;

    int height = 300;
    int width = 600;

    SeriesMarker seriesMarker = SeriesMarker.NONE;

    boolean chartTitleVisible = true;
    boolean chartLegendVisible = true;
    boolean axisTitlesVisible = true;

    public QuickXChart(int numRows, int numCols) {
        charts = new Chart[numRows * numCols];
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public void setChartPosition(int row, int col) {
        chartIdx = row * numCols + col;
        System.out.println("chartIdx set=" + chartIdx);
    }

    public void setChartSize(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public void setSeriesMarker(SeriesMarker seriesMarker) {
        this.seriesMarker = seriesMarker;
    }

    public void setChart(String chartTitle, String xTitle, String yTitle, double[] x, double[] y, String legend) {
        // Create Chart
        Chart chart = new Chart(width, height);

        // Customize Chart
        chart.setChartTitle(chartTitle);
        chart.setXAxisTitle(xTitle);
        chart.setYAxisTitle(yTitle);
        chart.setChartTitleVisible(chartTitleVisible);
        chart.setChartLegendVisible(chartLegendVisible);
        chart.setAxisTitlesVisible(axisTitlesVisible);
        // Series
        Series series;
        if (legend != null) {
            series = chart.addSeries(legend, x, y);
        } else {
            chart.setChartLegendVisible(false);
            series = chart.addSeries(" ", x, y);
        }

        series.setMarker(seriesMarker);

        charts[chartIdx] = chart;
    }

    public void setChart(String chartTitle, String xTitle, String yTitle, double[] x, double[][] y, String[] legend) {
        // Create Chart
        Chart chart = new Chart(width, height);

        // Customize Chart
        chart.setChartTitle(chartTitle);
        chart.setXAxisTitle(xTitle);
        chart.setYAxisTitle(yTitle);
        chart.setChartTitleVisible(chartTitleVisible);
        chart.setChartLegendVisible(chartLegendVisible);
        chart.setAxisTitlesVisible(axisTitlesVisible);
        // Series
        for (int i = 0; i < y.length; i++) {
            Series series;
            if (legend != null) {
                series = chart.addSeries(legend[i], x, y[i]);
            } else {
                chart.setChartLegendVisible(false);
                series = chart.addSeries(" " + i, x, y[i]);
            }
            series.setMarker(seriesMarker);
        }

        charts[chartIdx] = chart;
    }

    public void display() {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and set up the window.
                JFrame frame = new JFrame("XChart");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().setLayout(new GridLayout(numRows, numCols));

                for (int i = 0; i < charts.length; i++) {

                    if (charts[i] != null) {
                        JPanel chartPanel = new JChartPanel(charts[i]);
                        frame.getContentPane().add(chartPanel);
                    } else {
                        JPanel chartPanel = new JPanel();
                        frame.getContentPane().add(chartPanel);
                    }

                }

                // frame.setContentPane(newContentPane);

                // Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    /**
     * @param chartTitleVisible the chartTitleVisible to set
     */
    public void setChartTitleVisible(boolean chartTitleVisible) {
        this.chartTitleVisible = chartTitleVisible;
    }

    /**
     * @param chartLegendVisible the chartLegendVisible to set
     */
    public void setChartLegendVisible(boolean chartLegendVisible) {
        this.chartLegendVisible = chartLegendVisible;
    }

    /**
     * @param axisTitlesVisible the axisTitlesVisible to set
     */
    public void setAxisTitlesVisible(boolean axisTitlesVisible) {
        this.axisTitlesVisible = axisTitlesVisible;
    }

}
