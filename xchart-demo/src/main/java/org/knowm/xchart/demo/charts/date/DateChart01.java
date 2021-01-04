package org.knowm.xchart.demo.charts.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
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
 *   <li>Zooming by dragging a selection box over area of interest
 */
public class DateChart01 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new DateChart01();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart =
        new XYChartBuilder().width(800).height(600).title(getClass().getSimpleName()).build();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    chart.getStyler().setZoomEnabled(true);
    //    chart.getStyler().setZoomResetButtomPosition(Styler.CardinalPosition.InsideS);
    //    chart.getStyler().setZoomResetByDoubleClick(false);
    //    chart.getStyler().setZoomResetByButton(true);
    //    chart.getStyler().setZoomSelectionColor(new Color(0, 0, 192, 128));

    // Series
    Random random = new Random();

    // generate data
    List<Date> xData1 = new ArrayList<>();
    List<Double> yData1 = new ArrayList<>();
    List<Date> xData2 = new ArrayList<>();
    List<Double> yData2 = new ArrayList<>();

    DateFormat sdf = new SimpleDateFormat("HH:mm:ss.S");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
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

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Millisecond Scale with Two Separate Y Axis Groups";
  }
}
