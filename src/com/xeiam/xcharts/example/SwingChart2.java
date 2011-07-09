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

import com.xeiam.swing.SwingHelper;
import com.xeiam.xcharts.Chart;
import com.xeiam.xcharts.series.Series;

/**
 * @author timmolter
 */
public class SwingChart2 {

    private static void createAndShowGUI() {

        // Create Chart
        Chart chart = new Chart(800, 600);

        for (int i = 1; i <= 14; i++) {

            // generates linear data
            int b = 50;
            double[] xData = new double[b + 1];
            double[] yData = new double[b + 1];
            for (int x = 0; x <= b; x++) {
                xData[x] = 2 * x - b;
                yData[x] = 2 * i * x - i * b;
            }

            // Customize Chart
            chart.setChartTitle("Sample Chart");
            chart.setXAxisTitle("X");
            chart.setYAxisTitle("Y");
            // chart.setChartTitleVisible(false);
            // chart.setChartLegendVisible(false);
            // chart.setAxisTitlesVisible(false);

            Series series = chart.addSeries("y=" + 2 * i + "x-" + i * b + "b", xData, yData);
            // series.setLineColor(SeriesColor.PURPLE);
            // series.setLineStyle(SeriesLineStyle.NONE);
            // series.setMarkerColor(SeriesColor.GREEN);
            // series.setMarker(SeriesMarker.NONE);
        }

        SwingHelper swingHelper = new SwingHelper(chart);
        swingHelper.displayChart();
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
