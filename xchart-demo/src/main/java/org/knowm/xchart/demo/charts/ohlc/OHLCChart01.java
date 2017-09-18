/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.demo.charts.ohlc;

import org.knowm.xchart.*;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Demonstrates the following:
 * <ul>
 * <li>HiLo render style
 * <li>LegendPosition.OutsideS
 * <li>Two YAxis Groups - both on left
 */
public class OHLCChart01 implements ExampleChart<OHLCChart> {

  public static void main(String[] args) {

    ExampleChart<OHLCChart> exampleChart = new OHLCChart01();
    OHLCChart chart = exampleChart.getChart();
    new SwingWrapper<OHLCChart>(chart).displayChart();
  }

  public static void populateData(List<Date> xData,
                                  List<Double> openData, List<Double> highData, List<Double> lowData, List<Double> closeData) {
    // generate data
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = sdf.parse("2017-01-01");
      populateData(date, 5000.0, 20, xData, openData, highData, lowData, closeData);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public static void populateData(Date startDate, double startPrice, int count, List<Date> xData,
                                  List<Double> openData, List<Double> highData, List<Double> lowData, List<Double> closeData) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(startDate);
    double data = startPrice;
    for (int i = 1; i <= count; i++) {

      // add 1 day
      //startDate = new Date(startDate.getTime() + (1 * 1000 * 60 * 60 * 24));
      //xData.add(startDate);
      cal.add(Calendar.DATE, 1);
      xData.add(cal.getTime());

      double previous = data;

      data = getNewClose(data, startPrice);

      openData.add(previous);

      highData.add(getHigh(Math.max(previous, data), startPrice));
      lowData.add(getLow(Math.min(previous, data), startPrice));

      closeData.add(data);
    }
  }

  private static double getHigh(double close, double orig) {
    return close + (orig * Math.random() * 0.02);
  }

  private static double getLow(double close, double orig) {
    return close - (orig * Math.random() * 0.02);
  }

  private static double getNewClose(double close, double orig) {
    return close + (orig * (Math.random() - 0.5) * 0.1);
  }


  @Override
  public OHLCChart getChart() {

    // Create Chart
    OHLCChart chart = new OHLCChartBuilder().width(800).height(600).title("Prices").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    List<Date> xData = new ArrayList<Date>();
    List<Double> openData = new ArrayList<Double>();
    List<Double> highData = new ArrayList<Double>();
    List<Double> lowData = new ArrayList<Double>();
    List<Double> closeData = new ArrayList<Double>();

    populateData(xData, openData, highData, lowData, closeData);

    xData = null;
    chart.addSeries("Series", xData, openData, highData, lowData, closeData).setRenderStyle(OHLCSeries.OHLCSeriesRenderStyle.HiLo);
    chart.getStyler().setToolTipsEnabled(true);
    return chart;
  }

}
