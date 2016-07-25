/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
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
package org.knowm.xchart.standalone;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * Creates a simple real-time Chart using QuickChart
 */
public class SimpleRealTime {

  public static void main(String[] args) throws Exception {

    double phase = 0;
    double[][] initdata = getSineData(phase);

    // Create Chart
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", initdata[0], initdata[1]);

    // Show it
    final SwingWrapper<XYChart> sw = new SwingWrapper(chart);

    sw.displayChart();

    for (int i = 0; i < 10000; i++) {

      phase += Math.PI * 2 + 1 / 50.0;

      Thread.sleep(10);

      final double[][] data = getSineData(phase);

      javax.swing.SwingUtilities.invokeLater(new Runnable() {

        @Override
        public void run() {

          sw.getXChartPanel().updateXYSeries("y(x)", data[0], data[1], null);
        }
      });
    }

  }

  private static double[][] getSineData(double phase) {

    double[] xData = new double[100];
    double[] yData = new double[100];
    for (int i = 0; i < xData.length; i++) {
      double radians = phase + (Math.PI / (xData.length / 2) * i);
      xData[i] = phase + i - xData.length / 2;
      yData[i] = Math.sin(radians);
    }
    return new double[][] { xData, yData };
  }
}
