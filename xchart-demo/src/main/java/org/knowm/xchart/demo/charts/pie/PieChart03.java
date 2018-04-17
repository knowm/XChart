package org.knowm.xchart.demo.charts.pie;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * Pie Chart GGPlot2 Theme
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Pie Chart
 *   <li>PieChartBuilder
 *   <li>GGPlot2 Theme
 *   <li>Setting start angle
 */
public class PieChart03 implements ExampleChart<PieChart> {

  public static void main(String[] args) {

    ExampleChart<PieChart> exampleChart = new PieChart03();
    PieChart chart = exampleChart.getChart();
    new SwingWrapper<PieChart>(chart).displayChart();
  }

  @Override
  public PieChart getChart() {

    // Create Chart
    PieChart chart =
        new PieChartBuilder()
            .width(800)
            .height(600)
            .title("Pie Chart GGPlot2 Theme")
            .theme(ChartTheme.GGPlot2)
            .build();

    // Customize Chart
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setAnnotationDistance(1.15);
    chart.getStyler().setPlotContentSize(.7);
    chart.getStyler().setStartAngleInDegrees(90);

    // Series
    chart.addSeries("Prague", 2);
    chart.addSeries("Dresden", 4);
    chart.addSeries("Munich", 34);
    chart.addSeries("Hamburg", 22);
    chart.addSeries("Berlin", 29);

    return chart;
  }
}
