package org.knowm.xchart.demo.charts.ohlc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.knowm.xchart.*;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;

/**
 * Demonstrates the following:
 *
 * <ul>
 *   <li>HiLo render style
 *   <li>LegendPosition.OutsideS
 *   <li>Two YAxis Groups - both on left
 */
public class OHLCChart01 implements ExampleChart<OHLCChart> {

  public static void main(String[] args) {

    ExampleChart<OHLCChart> exampleChart = new OHLCChart01();
    OHLCChart chart = exampleChart.getChart();
    new SwingWrapper<OHLCChart>(chart).displayChart();
  }

  public static void populateData(
      List<Date> xData,
      List<Double> openData,
      List<Double> highData,
      List<Double> lowData,
      List<Double> closeData) {
    // generate data
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = sdf.parse("2017-01-01");
      populateData(date, 5000.0, 20, xData, openData, highData, lowData, closeData);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public static void populateData(
      Date startDate,
      double startPrice,
      int count,
      List<Date> xData,
      List<Double> openData,
      List<Double> highData,
      List<Double> lowData,
      List<Double> closeData) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(startDate);
    double data = startPrice;
    for (int i = 1; i <= count; i++) {

      // add 1 day
      // startDate = new Date(startDate.getTime() + (1 * 1000 * 60 * 60 * 24));
      // xData.add(startDate);
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
    chart
        .addSeries("Series", xData, openData, highData, lowData, closeData)
        .setOhlcSeriesRenderStyle(OHLCSeries.OHLCSeriesRenderStyle.HiLo);
    chart.getStyler().setToolTipsEnabled(true);
    return chart;
  }
}
