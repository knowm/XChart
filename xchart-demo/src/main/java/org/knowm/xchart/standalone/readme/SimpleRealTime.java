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
package org.knowm.xchart.standalone.readme;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * Creates a simple real-time chart
 */
public class SimpleRealTime {

  public static void main(String[] args) throws Exception {

    double phase = 0;
    double[][] initdata = getSineData(phase);

    // Create Chart
    final XYChart chart = QuickChart.getChart("Simple XChart Real-time Demo", "Radians", "Sine", "sine", initdata[0], initdata[1]);

    // Show it
    final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
    sw.displayChart();

    while (true) {

      phase += 2 * Math.PI * 2 / 20.0;

      Thread.sleep(100);

      final double[][] data = getSineData(phase);

      chart.updateXYSeries("sine", data[0], data[1], null);
      sw.repaintChart();
    }

  }

  private static double[][] getSineData(double phase) {

    double[] xData = new double[100];
    double[] yData = new double[100];
    for (int i = 0; i < xData.length; i++) {
      double radians = phase + (2 * Math.PI / xData.length * i);
      xData[i] = radians;
      yData[i] = Math.sin(radians);
    }
    return new double[][] { xData, yData };
  }
}
