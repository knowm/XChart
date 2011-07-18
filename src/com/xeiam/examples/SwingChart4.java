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
package com.xeiam.examples;

import com.xeiam.swing.SwingHelper;
import com.xeiam.xcharts.Chart;
import com.xeiam.xcharts.series.Series;
import com.xeiam.xcharts.series.SeriesMarker;

/**
 * Demonstrated/Tests plotting horizontal and vertical lines
 * 
 * @author timmolter
 */
public class SwingChart4 {

    private static void createAndShowGUI() {

        // generates linear data
        double[] yData1 = new double[] { 0.0, 0.0, 0.0, -10.0, 15.0, 15.0 };

        // Create Chart
        Chart chart = new Chart(600, 300);

        // Customize Chart
        chart.setChartTitle("Sample Chart");
        chart.setXAxisTitle("X");
        chart.setYAxisTitle("Y");
        chart.setChartTitleVisible(true);
        chart.setChartLegendVisible(true);
        chart.setAxisTitlesVisible(true);

        // Series
        Series series1 = chart.addSeries("y=0", null, yData1);
        series1.setMarker(SeriesMarker.NONE);

        SwingHelper swingHelper = new SwingHelper(chart);
        swingHelper.displayChart();

    }

    public static void main(String[] args) {

        createAndShowGUI();

    }
}
