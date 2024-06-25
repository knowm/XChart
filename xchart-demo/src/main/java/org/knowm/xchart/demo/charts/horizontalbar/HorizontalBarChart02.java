package org.knowm.xchart.demo.charts.horizontalbar;

import org.knowm.xchart.HorizontalBarChart;
import org.knowm.xchart.HorizontalBarChartBuilder;
import org.knowm.xchart.HorizontalBarSeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Date Categories
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Date categories as List
 *   <li>All negative values
 *   <li>Single series
 *   <li>No horizontal plot gridlines
 *   <li>Change series color
 *   <li>MATLAB Theme
 */
public class HorizontalBarChart02 implements ExampleChart<HorizontalBarChart> {

  public static void main(String[] args) {

    ExampleChart<HorizontalBarChart> exampleChart = new HorizontalBarChart02();
    HorizontalBarChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public HorizontalBarChart getChart() {

    // Create Chart
    HorizontalBarChart chart =
        new HorizontalBarChartBuilder()
            .theme(ChartTheme.Matlab)
            .width(800)
            .height(600)
            .title(getClass().getSimpleName())
            .yAxisTitle("Year")
            .xAxisTitle("Units Sold")
            .build();

    // Customize Chart
    chart.getStyler().setPlotGridLinesVisible(false);
    chart.getStyler().setDatePattern("yyyy");

    // Series
    List<Date> yData = new ArrayList<Date>();
    List<Number> xData = new ArrayList<Number>();

    Random random = new Random();
    DateFormat sdf = new SimpleDateFormat("yyyy");
    Date date = null;
    for (int i = 1; i <= 8; i++) {
      try {
        date = sdf.parse("" + (2000 + i));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      yData.add(date);
      xData.add(-1 * 0.00000001 * ((random.nextInt(i) + 1)));
    }
    HorizontalBarSeries series = chart.addSeries("Model 77", xData, yData);
    series.setFillColor(new Color(230, 150, 150));

    return chart;
  }

  @Override
  public String getExampleChartName() {
    return getClass().getSimpleName() + " - Date Categories";
  }
}
