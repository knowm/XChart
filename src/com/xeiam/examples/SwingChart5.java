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

import com.xeiam.swing.QuickXChart;

/**
 * Demonstrated/Tests plotting horizontal and vertical lines
 * 
 * @author timmolter
 */
public class SwingChart5 {

    private static double[] getRandomWalk(int N) {

        double[] y = new double[N];
        for (int i = 1; i < y.length; i++) {
            y[i] = y[i - 1] + Math.random() - .5;
        }

        return y;

    }

    public static void main(String[] args) {

        int numRows = 2;
        int numCols = 2;

        QuickXChart quickChart = new QuickXChart(2, 2);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                quickChart.setChartPosition(i, j);
                if (i == 1 && j == 1) {

                    double[][] y = new double[10][1000];
                    String[] legend = new String[10];
                    for (int k = 0; k < y.length; k++) {
                        y[k] = getRandomWalk(1000);
                        legend[k] = "" + k;
                    }
                    quickChart.setChart(i + "," + j, "X", "Y", null, y, legend);
                } else if (i == 0 && j == 1) {
                    // nothing
                } else {
                    quickChart.setChart(i + "," + j, "X", "Y", null, getRandomWalk(1000), null);
                }

            }
        }

        quickChart.display();

    }
}
