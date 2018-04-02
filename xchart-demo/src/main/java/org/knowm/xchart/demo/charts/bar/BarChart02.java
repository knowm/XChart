package org.knowm.xchart.demo.charts.bar;

import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

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
public class BarChart02 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new BarChart02();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart =
        new CategoryChartBuilder()
            .theme(ChartTheme.Matlab)
            .width(800)
            .height(600)
            .title("Units Sold Per Year")
            .xAxisTitle("Year")
            .yAxisTitle("Units Sold")
            .build();

    // Customize Chart
    chart.getStyler().setPlotGridLinesVisible(false);
    chart.getStyler().setDatePattern("yyyy");

    // Series
    List<Date> xData = new ArrayList<Date>();
    List<Number> yData = new ArrayList<Number>();

    Random random = new Random();
    DateFormat sdf = new SimpleDateFormat("yyyy");
    Date date = null;
    for (int i = 1; i <= 8; i++) {
      try {
        date = sdf.parse("" + (2000 + i));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      xData.add(date);
      yData.add(-1 * 0.00000001 * ((random.nextInt(i) + 1)));
    }
    CategorySeries series = chart.addSeries("Model 77", xData, yData);
    series.setFillColor(new Color(230, 150, 150));

    return chart;
  }
}
