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
package org.knowm.xchart.demo.charts.line;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Customized Chart
 * <p>
 * Demonstrates the following:
 * <ul>
 * <li>
 * Extensive Chart Customization
 */
public class LineChart03 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new LineChart03();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<XYChart>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).title("LineChart03").xAxisTitle("X").yAxisTitle("Y").build();

    // Customize Chart
    chart.getStyler().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.GREY));
    chart.getStyler().setPlotGridLinesColor(new Color(255, 255, 255));
    chart.getStyler().setChartBackgroundColor(Color.WHITE);
    chart.getStyler().setLegendBackgroundColor(Color.PINK);
    chart.getStyler().setChartFontColor(Color.MAGENTA);
    chart.getStyler().setChartTitleBoxBackgroundColor(new Color(0, 222, 0));
    chart.getStyler().setChartTitleBoxVisible(true);
    chart.getStyler().setChartTitleBoxBorderColor(Color.BLACK);
    chart.getStyler().setPlotGridLinesVisible(false);

    chart.getStyler().setAxisTickPadding(20);

    chart.getStyler().setAxisTickMarkLength(15);

    chart.getStyler().setPlotMargin(20);

    chart.getStyler().setChartTitleFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
    chart.getStyler().setLegendFont(new Font(Font.SERIF, Font.PLAIN, 18));
    chart.getStyler().setLegendPosition(LegendPosition.InsideSE);
    chart.getStyler().setLegendSeriesLineLength(12);
    chart.getStyler().setAxisTitleFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
    chart.getStyler().setAxisTickLabelsFont(new Font(Font.SERIF, Font.PLAIN, 11));
    chart.getStyler().setDatePattern("dd-MMM");
    chart.getStyler().setDecimalPattern("#0.000");
    chart.getStyler().setLocale(Locale.GERMAN);

    // generates linear data
    List<Date> xData = new ArrayList<Date>();
    List<Double> yData = new ArrayList<Double>();

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

    // Series
    XYSeries series = chart.addSeries("Fake Data", xData, yData);
    series.setLineColor(XChartSeriesColors.BLUE);
    series.setMarkerColor(Color.ORANGE);
    series.setMarker(SeriesMarkers.CIRCLE);
    series.setLineStyle(SeriesLines.SOLID);

    return chart;
  }
}
