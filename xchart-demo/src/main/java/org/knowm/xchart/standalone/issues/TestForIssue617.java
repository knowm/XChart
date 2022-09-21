package org.knowm.xchart.standalone.issues;

import java.text.ParseException;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

public class TestForIssue617 {

  public static void main(String[] args) throws ParseException {

    CategoryChart chart = getCategoryChart();
    new SwingWrapper(chart).displayChart();
  }

  public static CategoryChart getCategoryChart() {
    double[] xData = new double[] {0.0, 1.0, 2.0, 3.0, 4.0};
    double[] yData = new double[] {1.0, 2, 3.0, 4, 2.5};
    double[] yData2 = new double[] {2.2, 2, 1.2, 3.1, 2.7};

    // Create Chart
    CategoryChartBuilder builder = new CategoryChartBuilder();
    builder.title("Sample Chart").xAxisTitle("X").yAxisTitle("Y").theme(Styler.ChartTheme.Matlab);
    CategoryChart chart = builder.build();
    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Area);

    chart.addSeries("y(x)", xData, yData);
    chart.addSeries("y2(x)", xData, yData2);

    return chart;
  }
}
