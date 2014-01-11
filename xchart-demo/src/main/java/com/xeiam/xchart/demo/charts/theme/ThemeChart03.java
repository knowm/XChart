/**
 * Copyright 2011 - 2014 Xeiam LLC.
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
import java.util.Date;
import java.util.List;

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
    chart.getStyleManager().setXAxisTickMarkSpacingHint(100);
    // generate data
    List<Date> xData = new ArrayList<Date>();
    List<Double> y1Data = new ArrayList<Double>();
    List<Double> y2Data = new ArrayList<Double>();

    DateFormat sdf = new SimpleDateFormat("yyyy-MM");

    Date date;
    try {
      date = sdf.parse("2012-08");
      xData.add(date);
      y1Data.add(120d);
      y2Data.add(15d);

      date = sdf.parse("2012-11");
      xData.add(date);
      y1Data.add(165d);
      y2Data.add(15d);

      date = sdf.parse("2013-01");
      xData.add(date);
      y1Data.add(210d);
      y2Data.add(20d);

      date = sdf.parse("2013-02");
      xData.add(date);
      y1Data.add(400d);
      y2Data.add(30d);

      date = sdf.parse("2013-03");
      xData.add(date);
      y1Data.add(800d);
      y2Data.add(100d);

      date = sdf.parse("2013-04");
      xData.add(date);
      y1Data.add(2000d);
      y2Data.add(120d);

      date = sdf.parse("2013-05");
      xData.add(date);
      y1Data.add(3000d);
      y2Data.add(150d);

    } catch (ParseException e) {
      e.printStackTrace();
    }

    Series series1 = chart.addSeries("downloads", xData, y1Data);
    series1.setLineStyle(SeriesLineStyle.DOT_DOT);
    chart.addSeries("price", xData, y2Data);

    return chart;
  }

}
