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
package com.xeiam.xchart.demo.charts.theme;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Matlab Theme
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>Building a Chart with ChartBuilder
 * <li>Applying the Matlab Theme to the Chart
 * <li>Vertical and Horizontal Lines
 */
public class ThemeChart03 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new ThemeChart03();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new ChartBuilder().width(800).height(600).theme(ChartTheme.Matlab).title("Matlab Theme").xAxisTitle("X").yAxisTitle("Y").build();
    chart.getStyleManager().setPlotGridLinesVisible(false);

    // generate data
    Collection<Date> xData = new ArrayList<Date>();
    Collection<Number> y1Data = new ArrayList<Number>();
    Collection<Number> y2Data = new ArrayList<Number>();

    DateFormat sdf = new SimpleDateFormat("yyyy-MM");

    Date date;
    try {
      date = sdf.parse("2012-08");
      xData.add(date);
      y1Data.add(120);
      y2Data.add(15);

      date = sdf.parse("2012-11");
      xData.add(date);
      y1Data.add(165);
      y2Data.add(15);

      date = sdf.parse("2013-01");
      xData.add(date);
      y1Data.add(210);
      y2Data.add(20);

      date = sdf.parse("2013-02");
      xData.add(date);
      y1Data.add(400);
      y2Data.add(30);

      date = sdf.parse("2013-03");
      xData.add(date);
      y1Data.add(800);
      y2Data.add(100);

      date = sdf.parse("2013-04");
      xData.add(date);
      y1Data.add(2000);
      y2Data.add(120);

      date = sdf.parse("2013-05");
      xData.add(date);
      y1Data.add(3000);
      y2Data.add(150);

    } catch (ParseException e) {
      e.printStackTrace();
    }

    Series series1 = chart.addDateSeries("downloads", xData, y1Data);
    series1.setLineStyle(SeriesLineStyle.DOT_DOT);
    chart.addDateSeries("price", xData, y2Data);

    return chart;
  }

}
