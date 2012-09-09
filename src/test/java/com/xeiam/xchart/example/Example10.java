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

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.swing.SwingWrapper;

/**
 * Creates a simple charts using Longs as inputs
 * 
 * @author timmolter
 */
public class Example10 {

  public static void main(String[] args) throws Exception {

    Collection<Number> xData = Arrays
        .asList(new Number[] { 1222812000000L, 1222898400000L, 1222984800000L, 1223071200000L, 1223157600000L, 1223244000000L, 1223330400000L, 1223416800000L, 1223503200000L, 1223589600000L });
    // .asList(new Number[] { 12228120L, 12228984L, 12229848L, 12230712L, 12231576L, 12232440L, 12233304L, 12234168L, 12235032L, 12235896L });
    // .asList(new Number[] { 12228120, 12228984, 12229848, 12230712, 12231576, 12232440, 12233304, 12234168, 12235032, 12235896 });
    // .asList(new Number[] { 12228120.0, 12228984.0, 12229848.0, 12230712.0, 12231576.0, 12232440.0, 12233304.0, 12234168.0, 12235032.0, 12235896.0 });
    // .asList(new Number[] { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 });
    // .asList(new Number[] { 0.0, 100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0 });
    // .asList(new Number[] { 0.0, 100000000.0, 200000000.0, 300000000.0, 400000000.0, 500000000.0, 600000000.0, 700000000.0, 800000000.0, 900000000.0 });
    Collection<Number> yData = Arrays.asList(new Number[] { 0.0, 1.0, 2.0, 0.0, 1.0, 2.0, 0.0, 1.0, 2.0, 0.0 });

    // Create Chart
    Chart chart = new Chart(700, 500);
    chart.setChartTitle("Sample Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.addSeries("y(x)", xData, yData);

    new SwingWrapper(chart).displayChart();

  }

}
