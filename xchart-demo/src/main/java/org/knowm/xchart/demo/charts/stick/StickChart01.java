package org.knowm.xchart.demo.charts.stick;

import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Basic Stick Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Stick category series render type
 */
public class StickChart01 implements ExampleChart<CategoryChart> {

  public static void main(String[] args) {

    ExampleChart<CategoryChart> exampleChart = new StickChart01();
    CategoryChart chart = exampleChart.getChart();
    new SwingWrapper<CategoryChart>(chart).displayChart();
  }

  @Override
  public CategoryChart getChart() {

    // Create Chart
    CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Stick").build();

    // Customize Chart
    chart.getStyler().setChartTitleVisible(true);
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Stick);

    // Series
    List<Integer> xData = new ArrayList<Integer>();
    List<Integer> yData = new ArrayList<Integer>();
    for (int i = -3; i <= 24; i++) {
      xData.add(i);
      yData.add(i);
    }
    chart.addSeries("data", xData, yData);

    return chart;
  }
}
