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
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Millisecond Scale
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Millisecond Scale
 * <li>Series with no Markers
 */
public class DateChart01 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new DateChart01();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<XYChart>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).title("Millisecond Scale").build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);

    // Series
    Random random = new Random();

    // generate data
    List<Date> xData = new ArrayList<Date>();
    List<Double> yData = new ArrayList<Double>();

    DateFormat sdf = new SimpleDateFormat("HH:mm:ss.S");
    Date date = null;
    for (int i = 1; i <= 14; i++) {

      try {
        date = sdf.parse("23:45:31." + (100 * i + random.nextInt(20)));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      // System.out.println(date.getTime());
      // System.out.println(date.toString());
      xData.add(date);
      yData.add(Math.random() * i);
    }

    XYSeries series = chart.addSeries("blah", xData, yData);
    series.setMarker(SeriesMarkers.NONE);

    return chart;

  }
}
