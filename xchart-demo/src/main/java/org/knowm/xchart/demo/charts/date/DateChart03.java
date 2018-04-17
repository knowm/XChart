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
import org.knowm.xchart.style.Styler;

/**
 * Minute Scale *
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Minute Scale
 *   <li>10^9 formatting
 *   <li>LegendPosition.InsideS
 *   <li>Two YAxis Groups - one on left, one on right
 */
public class DateChart03 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new DateChart03();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<XYChart>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).title("Minute Scale").build();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideS);
    chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);

    // Series
    List<Date> xData1 = new ArrayList<Date>();
    List<Double> yData1 = new ArrayList<Double>();
    List<Date> xData2 = new ArrayList<Date>();
    List<Double> yData2 = new ArrayList<Double>();

    Random random = new Random();

    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
    Date date = null;
    for (int i = 1; i <= 14; i++) {
      try {
        date =
            sdf.parse(
                "2013-07-22-08:"
                    + (5 * i + random.nextInt(2))
                    + ":"
                    + (random.nextInt(2))
                    + "."
                    + random.nextInt(1000));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      // System.out.println(date.getTime());
      // System.out.println(date.toString());
      xData1.add(date);
      xData2.add(date);
      yData1.add(Math.random() * i * 1000000000);
      yData2.add(Math.random() * i * 10);
    }

    chart.addSeries("series1", xData1, yData1).setYAxisGroup(1);
    chart.addSeries("series2", xData2, yData2);

    return chart;
  }
}
