/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
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
package org.knowm.xchart.demo.charts.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.knowm.xchart.Chart;
import org.knowm.xchart.ChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Minute Scale
 */
public class DateChart03 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new DateChart03();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new ChartBuilder().width(800).height(600).title("Minute Scale").build();
    chart.getStyleManager().setLegendVisible(false);

    // generate data
    List<Date> xData = new ArrayList<Date>();
    List<Double> yData = new ArrayList<Double>();

    Random random = new Random();

    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
    Date date = null;
    for (int i = 1; i <= 14; i++) {
      try {
        date = sdf.parse("2013-07-22-08:" + (5 * i + random.nextInt(2)) + ":" + (random.nextInt(2)) + "." + random.nextInt(1000));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      // System.out.println(date.getTime());
      // System.out.println(date.toString());
      xData.add(date);
      yData.add(Math.random() * i * 1000000000);
    }

    chart.addSeries("blah", xData, yData);

    return chart;

  }
}
