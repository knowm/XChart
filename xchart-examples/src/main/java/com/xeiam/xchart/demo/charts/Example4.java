/**
 * Copyright 2011-2013 Xeiam LLC.
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
package com.xeiam.xchart.demo.charts;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SwingWrapper;

/**
 * Date Axis
 * 
 * @author timmolter
 */
public class Example4 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new Example4();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new Chart(800, 600);

    // generates linear data
    Collection<Date> xData = new ArrayList<Date>();
    Collection<Number> yData = new ArrayList<Number>();

    DateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH");
    Date date = null;
    for (int i = 1; i <= 10; i++) {

      try {
        date = sdf.parse("2012.12.22." + (10 + i));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData.add(date);
      yData.add(Math.random() * i);
    }

    // Customize Chart
    chart.setTitle("Example4");
    chart.setXAxisTitle("time of day");
    chart.setYAxisTitle("gigawatts");

    Series series = chart.addDateSeries("value", xData, yData);

    return chart;
  }

}
