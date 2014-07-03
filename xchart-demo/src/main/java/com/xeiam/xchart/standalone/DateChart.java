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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * @author timmolter
 */
public class DateChart implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new DateChart();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new ChartBuilder().width(800).height(600).title("Day Scale").build();
    chart.getStyleManager().setLegendVisible(false);

    // generate data
    List<Date> xData = new ArrayList<Date>();
    List<Double> yData = new ArrayList<Double>();

    Random random = new Random();

    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    for (int i = 1; i <= 5; i++) {
      try {
        date = sdf.parse("2014-02-" + i);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      System.out.println(date);
      xData.add(date);
      System.out.println(date.getTime());
      yData.add(Math.random() * i);
    }

    chart.addSeries("blah", xData, yData);

    return chart;

  }
}
