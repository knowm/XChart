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

import com.xeiam.swing.SwingWrapper;
import com.xeiam.xcharts.Chart;
import com.xeiam.xcharts.QuickChart;

/**
 * @author timmolter
 */
public class Example4 {

    public static void main(String[] args) {

        int numRows = 2;
        int numCols = 2;
        Chart[] charts = new Chart[numRows * numCols];

        int chartCount = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {

                // double[][] yData2d = new double[10][1000];
                // for (int k = 0; k < yData2d.length; k++) {
                // yData2d[k] = getRandomWalk(1000);
                // }
                // charts[chartCount++] = QuickChart.getChart(i + "," + j, "X", "Y", null, null, yData2d);
                charts[chartCount++] = QuickChart.getChart(i + "," + j, "X", "Y", "random walk", null, getRandomWalk(1000));

            }
        }

        SwingWrapper swingWrapper = new SwingWrapper(charts);
        swingWrapper.displayChartMatrix(numRows, numCols);

    }

    /**
     * Generates a set of random walk data
     * 
     * @param numPoints
     * @return
     */
    private static double[] getRandomWalk(int numPoints) {

        double[] y = new double[numPoints];
        for (int i = 1; i < y.length; i++) {
            y[i] = y[i - 1] + Math.random() - .5;
        }

        return y;

    }
}
