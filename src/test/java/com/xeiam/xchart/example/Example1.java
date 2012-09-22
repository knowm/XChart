/**
 * Copyright 2011-2012 Xeiam LLC.
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
package com.xeiam.xchart.example;

import java.util.Arrays;
import java.util.Collection;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;

/**
 * Creates a simple Chart and saves it as a PNG and JPEG image file.
 * 
 * @author timmolter
 */
public class Example1 {

  public static void main(String[] args) throws Exception {

    Collection<Number> xData = Arrays.asList(new Number[] { 0.0, 1.0, 2.0 });
    Collection<Number> yData = Arrays.asList(new Number[] { 0.0, 1.0, 2.0 });

    // Create Chart
    Chart chart = new Chart(500, 400);
    chart.setTitle("Sample Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.addSeries("y(x)", xData, yData);

    BitmapEncoder.savePNG(chart, "./Sample_Chart.png");
    BitmapEncoder.saveJPG(chart, "./Sample_Chart.jpg", 0.95f);

  }

}
