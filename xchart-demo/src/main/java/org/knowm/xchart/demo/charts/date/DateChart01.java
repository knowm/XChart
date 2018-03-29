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
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Millisecond Scale
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Millisecond Scale
 *   <li>LegendPosition.OutsideS
 *   <li>Two YAxis Groups - both on left
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
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    // Series
    Random random = new Random();

    // generate data
    List<Date> xData1 = new ArrayList<Date>();
    List<Double> yData1 = new ArrayList<Double>();
    List<Date> xData2 = new ArrayList<Date>();
    List<Double> yData2 = new ArrayList<Double>();

    DateFormat sdf = new SimpleDateFormat("HH:mm:ss.S");
    Date date = null;
    for (int i = 1; i <= 14; i++) {

      try {
        date = sdf.parse("23:45:31." + (100 * i + random.nextInt(20)));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData1.add(date);
      xData2.add(date);
      yData1.add(Math.random() * i);
      yData2.add(Math.random() * i * 100);
    }

    XYSeries series = chart.addSeries("series 1", xData1, yData1);
    series.setMarker(SeriesMarkers.NONE);
    chart.addSeries("series 2", xData2, yData2).setMarker(SeriesMarkers.NONE).setYAxisGroup(1);

    return chart;
  }
}
