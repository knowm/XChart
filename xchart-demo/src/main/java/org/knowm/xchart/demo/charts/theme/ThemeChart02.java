package org.knowm.xchart.demo.charts.theme;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * GGPlot2 Theme
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Building a Chart with ChartBuilder
 *   <li>Applying the GGPlot2 Theme to the Chart
 *   <li>Vertical and Horizontal Lines
 */
public class ThemeChart02 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new ThemeChart02();
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
            .theme(ChartTheme.GGPlot2)
            .title("GGPlot2 Theme")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart

    // Series
    chart.addSeries("vertical", new double[] {1, 1}, new double[] {-10, 10});
    chart.addSeries("horizontal", new double[] {-10, 10}, new double[] {0, 0});

    return chart;
  }
}
