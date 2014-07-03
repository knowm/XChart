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
package com.xeiam.xchart.demo.charts.line;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartColor;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.StyleManager.LegendPosition;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.demo.charts.ExampleChart;

/**
 * Extensive Chart Customization
 */
public class LineChart03 implements ExampleChart {

  public static void main(String[] args) {

    ExampleChart exampleChart = new LineChart03();
    Chart chart = exampleChart.getChart();
    new SwingWrapper(chart).displayChart();
  }

  @Override
  public Chart getChart() {

    // Create Chart
    Chart chart = new Chart(800, 600);

    // generates linear data
    Collection<Date> xData = new ArrayList<Date>();
    Collection<Double> yData = new ArrayList<Double>();

    DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    Date date = null;
    for (int i = 1; i <= 10; i++) {

      try {
        date = sdf.parse(i + ".10.2008");
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData.add(date);
      yData.add(Math.random() * i);
    }

    // Customize Chart
    chart.setChartTitle("LineChart03");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    chart.getStyleManager().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.GREY));
    chart.getStyleManager().setPlotGridLinesColor(new Color(255, 255, 255));
    chart.getStyleManager().setChartBackgroundColor(Color.WHITE);
    chart.getStyleManager().setLegendBackgroundColor(Color.PINK);
    chart.getStyleManager().setChartFontColor(Color.MAGENTA);
    chart.getStyleManager().setChartTitleBoxBackgroundColor(new Color(0, 222, 0));
    chart.getStyleManager().setChartTitleBoxVisible(true);
    chart.getStyleManager().setChartTitleBoxBorderColor(Color.BLACK);
    chart.getStyleManager().setPlotGridLinesVisible(false);

    chart.getStyleManager().setAxisTickPadding(20);

    chart.getStyleManager().setAxisTickMarkLength(15);

    chart.getStyleManager().setPlotPadding(20);

    chart.getStyleManager().setChartTitleFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
    chart.getStyleManager().setLegendFont(new Font(Font.SERIF, Font.PLAIN, 18));
    chart.getStyleManager().setLegendPosition(LegendPosition.InsideSE);
    chart.getStyleManager().setLegendSeriesLineLength(12);
    chart.getStyleManager().setAxisTitleFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
    chart.getStyleManager().setAxisTickLabelsFont(new Font(Font.SERIF, Font.PLAIN, 11));
    chart.getStyleManager().setDatePattern("dd-MMM");
    chart.getStyleManager().setDecimalPattern("#0.000");
    chart.getStyleManager().setLocale(Locale.GERMAN);

    Series series = chart.addSeries("Fake Data", xData, yData);
    series.setLineColor(SeriesColor.BLUE);
    series.setMarkerColor(Color.ORANGE);
    series.setMarker(SeriesMarker.CIRCLE);
    series.setLineStyle(SeriesLineStyle.SOLID);

    return chart;
  }
}
