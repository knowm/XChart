/**
 * Copyright 2013 Xeiam LLC.
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
package com.xeiam.xchart.standalone;

import java.io.IOException;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.VectorGraphicsEncoder;
import com.xeiam.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;

/**
 * @author timmolter
 */
public class VectorGraphicsTest {

  public static void main(String[] args) throws IOException {

    double[] xData = new double[] { 0.0, 1.0, 2.0 };
    double[] yData = new double[] { 2.0, 1.0, 0.0 };

    // Create Chart
    Chart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);

    VectorGraphicsEncoder.saveVectorGraphic(chart, "", VectorGraphicsFormat.PDF);
  }

}
