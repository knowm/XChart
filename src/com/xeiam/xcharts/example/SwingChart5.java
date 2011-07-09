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
import com.xeiam.xcharts.series.SeriesMarker;

/**
 * Demonstrated/Tests plotting horizontal and vertical lines
 * 
 * @author timmolter
 */
public class SwingChart5 {

    private static void createAndShowGUI() {

        Chart[] charts = new Chart[3];
        for (int i = 0; i < charts.length; i++) {
            charts[i] = getRandomWalkChart(1000);
        }

        SwingHelper swingHelper = new SwingHelper(charts);
        swingHelper.displayChart();

    }

    private static Chart getRandomWalkChart(int N) {

        double[] y = new double[N];
        for (int i = 1; i < y.length; i++) {
            y[i] = y[i - 1] + Math.random() - .5;
        }

        // Create Chart
        Chart chart = new Chart(600, 300);

        // Customize Chart
        chart.setChartTitle("Random Walk");
        chart.setXAxisTitle("X");
        chart.setYAxisTitle("Y");
        chart.setChartTitleVisible(true);
        chart.setChartLegendVisible(true);
        chart.setAxisTitlesVisible(true);

        // Series
        Series series1 = chart.addSeries("y=0", null, y);
        series1.setMarker(SeriesMarker.NONE);

        return chart;

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
