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
package com.xeiam.xcharts;

import com.xeiam.xcharts.series.Series;
import com.xeiam.xcharts.series.SeriesMarker;

/**
 * A convenience class for making Charts with one line of code.
 * 
 * @author timmolter
 */
public class QuickChart {

    /**
     * @param chartTitle
     * @param xTitle
     * @param yTitle
     * @param seriesName
     * @param xData
     * @param yData
     * @return a Chart Object
     */
    public static Chart getChart(String chartTitle, String xTitle, String yTitle, String seriesName, double[] xData, double[] yData) {

        double[][] yData2d = { yData };
        if (seriesName == null) {
            return getChart(chartTitle, xTitle, yTitle, null, xData, yData2d);
        } else {
            return getChart(chartTitle, xTitle, yTitle, new String[] { seriesName }, xData, yData2d);
        }
    }

    /**
     * @param chartTitle
     * @param xTitle
     * @param yTitle
     * @param seriesNames
     * @param xData
     * @param yData
     * @return a Chart Object
     */
    public static Chart getChart(String chartTitle, String xTitle, String yTitle, String[] seriesNames, double[] xData, double[][] yData) {

        // Create Chart
        Chart chart = new Chart(400, 280);

        // Customize Chart
        chart.setChartTitle(chartTitle);
        chart.setXAxisTitle(xTitle);
        chart.setYAxisTitle(yTitle);

        // Series
        for (int i = 0; i < yData.length; i++) {
            Series series;
            if (seriesNames != null) {
                series = chart.addSeries(seriesNames[i], xData, yData[i]);
            } else {
                chart.setChartLegendVisible(false);
                series = chart.addSeries(" " + i, xData, yData[i]);
            }
            series.setMarker(SeriesMarker.NONE);
        }

        return chart;
    }

}
