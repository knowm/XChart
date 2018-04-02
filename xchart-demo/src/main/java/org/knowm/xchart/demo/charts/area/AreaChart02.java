package org.knowm.xchart.demo.charts.area;

import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Null Y-Axis Data Points
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Area Chart
 *   <li>null Y-Axis values
 *   <li>ChartBuilder
 */
public class AreaChart02 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new AreaChart02();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<XYChart>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title(getClass().getSimpleName())
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);

    // Series
    List<Integer> xData = new ArrayList<Integer>();
    List<Integer> yData = new ArrayList<Integer>();
    for (int i = 0; i < 5; i++) {
      xData.add(i);
      yData.add(i * i);
    }
    xData.add(5);
    yData.add(null);

    for (int i = 6; i < 10; i++) {
      xData.add(i);
      yData.add(i * i);
    }
    xData.add(10);
    yData.add(null);
    xData.add(11);
    yData.add(100);
    xData.add(12);
    yData.add(90);

    chart.addSeries("a", xData, yData);

    return chart;
  }
}
