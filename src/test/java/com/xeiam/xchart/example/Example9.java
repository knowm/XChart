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

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartColor;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.series.Series;
import com.xeiam.xchart.series.SeriesColor;
import com.xeiam.xchart.series.SeriesLineStyle;
import com.xeiam.xchart.series.SeriesMarker;

/**
 * Create a chart with a Date x-axis and extensive chart customization
 * 
 * @author timmolter
 */
public class Example9 {

  public static void main(String[] args) throws ParseException {

    // Create Chart
    Chart chart = new Chart(700, 500);

    // generates linear data
    Collection<Date> xData = new ArrayList<Date>();
    Collection<Number> yData = new ArrayList<Number>();

    DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    for (int i = 1; i <= 10; i++) {
      Date date = sdf.parse(i + ".10.2008");
      xData.add(date);
      yData.add(Math.random() * i);
    }

    // Customize Chart
    chart.setChartTitle("Sample Chart with Date X-Axis");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.setXAxisTicksVisible(false);
    chart.setChartForegroundColor(ChartColor.getAWTColor(ChartColor.GREY));
    chart.setChartGridLinesColor(new Color(255, 255, 255));
    chart.setChartBackgroundColor(Color.WHITE);
    chart.setChartLegendBackgroundColor(Color.PINK);
    chart.setChartBordersColor(Color.GREEN);
    chart.setChartFontColor(Color.MAGENTA);
    chart.setChartTitleFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
    chart.setChartLegendFont(new Font(Font.SERIF, Font.PLAIN, 18));
    chart.setAxisLabelsFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
    chart.setChartTickLabelsFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
    chart.setChartTickLabelsFont(new Font(Font.SERIF, Font.PLAIN, 11));

    Series series = chart.addDateSeries("Fake Data", xData, yData);
    series.setLineColor(SeriesColor.BLUE);
    series.setMarkerColor(Color.ORANGE);
    series.setMarker(SeriesMarker.CIRCLE);
    series.setLineStyle(SeriesLineStyle.SOLID);

    new SwingWrapper(chart).displayChart();
  }

}
