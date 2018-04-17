package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

/**
 * Logarithmic Y-Axis Demonstrates the following:
 *
 * <ul>
 *   <li>Logarithmic Y-Axis
 *   <li>Building a Chart with ChartBuilder
 *   <li>Place legend at Inside-NW position
 */
public class TestForIssue249 {

  public static void main(String[] args) {

    TestForIssue249 exampleChart = new TestForIssue249();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  public CategoryChart getChart() {

    // generates Log data
    List<Integer> xData = new ArrayList<Integer>();
    List<Double> yData = new ArrayList<Double>();
    for (int i = -3; i <= 3; i++) {
      xData.add(i);
      yData.add(Math.pow(10, i));
    }

    // Create Chart
    //    XYChart chart = new XYChartBuilder().width(800).height(600).title("Powers of
    // Ten").xAxisTitle("Power").yAxisTitle("Value").build();
    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("Powers of Ten")
            .xAxisTitle("Power")
            .yAxisTitle("Value")
            .build();

    // Customize Chart
    chart.getStyler().setChartTitleVisible(true);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
    chart.getStyler().setYAxisLogarithmic(true);

    chart.getStyler().setYAxisMin(0.001);
    chart.getStyler().setYAxisMax(1000.0);

    // Series
    CategorySeries series = chart.addSeries("10^x", xData, yData);
    series.setChartCategorySeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Scatter);

    return chart;
  }
}
