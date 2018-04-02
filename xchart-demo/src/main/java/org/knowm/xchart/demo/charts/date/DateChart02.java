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
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;

/** Second Scale */
public class DateChart02 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new DateChart02();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<XYChart>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).title("Second Scale").build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);

    // Series
    List<Date> xData = new ArrayList<Date>();
    List<Double> yData = new ArrayList<Double>();

    Random random = new Random();

    DateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    Date date = null;
    for (int i = 1; i <= 14; i++) {
      try {
        date = sdf.parse("23:45:" + (5 * i + random.nextInt(2)) + "." + random.nextInt(1000));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData.add(date);
      yData.add(Math.random() * i);
    }

    chart.addSeries("blah", xData, yData);

    return chart;
  }
}
