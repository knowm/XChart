/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;

/**
 * Creates a list of Charts and saves it as a PNG file.
 */
public class TestForIssue1 {

  public static void main(String[] args) throws Exception {

    List<Chart> charts = Arrays.asList(new Chart[] {
      createChart("chart1", new double[]{2.0, 1.0, 0.0}),
      createChart("chart2", new double[]{3.0, 4.0, 0.0}),
      createChart("chart3", new double[]{4.0, 1.5, 0.0}),
      createChart("chart4", new double[]{2.0, 3.0, 0.0}),
      createChart("chart5", new double[]{4.0, 1.0, 0.0}),
      createChart("chart6", new double[]{5.0, 2.0, 0.0})
    });

    BitmapEncoder.saveBitmap(charts, 2, 3, "./Sample_Charts", BitmapFormat.PNG);
  }

  private static XYChart createChart(String title, double[] yData) {
    XYChart chart = new XYChart(300, 200);
    chart.setTitle(title);
    chart.setXAxisTitle("X");
    chart.setXAxisTitle("Y");
    chart.addSeries("y(x)", null, yData);
    return chart;
  }

}
