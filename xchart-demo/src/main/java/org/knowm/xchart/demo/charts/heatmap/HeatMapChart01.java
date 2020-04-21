package org.knowm.xchart.demo.charts.heatmap;

import java.util.Random;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

/**
 * Basic HeatMap Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Default Styler
 *   <li>PlotContentSize = 1
 *   <li>Show value
 */
public class HeatMapChart01 implements ExampleChart<HeatMapChart> {

  public static void main(String[] args) {

    ExampleChart<HeatMapChart> exampleChart = new HeatMapChart01();
    HeatMapChart chart = exampleChart.getChart();
    new SwingWrapper<HeatMapChart>(chart).displayChart();
  }

  @Override
  public HeatMapChart getChart() {

    // Create Chart
    HeatMapChart chart =
        new HeatMapChartBuilder().width(1000).height(600).title(getClass().getSimpleName()).build();

    chart.getStyler().setPlotContentSize(1);
    chart.getStyler().setShowValue(true);

    int[] xData = {1, 2, 3, 4};
    int[] yData = {1, 2, 3};
    int[][] heatData = new int[xData.length][yData.length];
    Random random = new Random();
    for (int i = 0; i < xData.length; i++) {
      for (int j = 0; j < yData.length; j++) {
        heatData[i][j] = random.nextInt(1000);
      }
    }
    chart.addSeries("Basic HeatMap", xData, yData, heatData);
    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + "- Basic HeatMap Chart";
  }
}
