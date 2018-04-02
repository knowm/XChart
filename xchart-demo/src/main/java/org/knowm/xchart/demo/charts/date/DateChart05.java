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

/** Day Scale */
public class DateChart05 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new DateChart05();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<XYChart>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).title("Day Scale").build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);

    // Series
    List<Date> xData = new ArrayList<Date>();
    List<Double> yData = new ArrayList<Double>();

    Random random = new Random();

    DateFormat sdf = new SimpleDateFormat("MM-dd");
    Date date = null;
    for (int i = 1; i <= 14; i++) {
      try {
        date = sdf.parse("02-" + (6 * i + random.nextInt(2)));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData.add(date);
      yData.add(Math.random() * i / -100000000);
    }

    chart.addSeries("blah", xData, yData);

    return chart;
  }
}
