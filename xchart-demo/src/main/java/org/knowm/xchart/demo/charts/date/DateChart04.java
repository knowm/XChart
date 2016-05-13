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
package org.knowm.xchart.demo.charts.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Hour Scale
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Hiding Y-Axis Axis Ticks (labels, tick marks, tick line)
 */
public class DateChart04 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new DateChart04();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<XYChart>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).title("Hour Scale").build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setYAxisTicksVisible(false);

    // Series
    List<Date> xData = new ArrayList<Date>();
    List<Double> yData = new ArrayList<Double>();

    Random random = new Random();

    DateFormat sdf = new SimpleDateFormat("dd-HH");
    Date date = null;
    for (int i = 1; i <= 14; i++) {
      try {
        date = sdf.parse("25-" + (2 * i + random.nextInt(2)));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData.add(date);
      yData.add(Math.random() * i / 10000000000.0);
    }

    chart.addSeries("blah", xData, yData);

    return chart;
  }
}
