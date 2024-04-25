package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import java.text.ParseException;

public class TestForIssue707 {

  public static void main(String[] args) throws ParseException {

    CategoryChart chart = getCategoryChart();
    new SwingWrapper(chart).displayChart();
  }

  public static CategoryChart getCategoryChart() {
      double[] xData = new double[] { 0.0, 1.0, 2.0, 3.0, 4.0 };
      double[] yData = new double[] { 2.0, 1.5, 4.0, 3.77, 2.5 };

      // Create Chart
      CategoryChartBuilder builder = new CategoryChartBuilder();
      builder.title("Sample Chart")
              .xAxisTitle("X")
              .yAxisTitle("Y")
              .theme(Styler.ChartTheme.Matlab);
      CategoryChart chart = builder.build();
      // chart.getStyler().setYAxisMin(1.0);
      chart.getStyler().setLabelsVisible(true);
      //.setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line).

      chart.addSeries("y(x)", xData, yData);

      return chart;
  }
}
