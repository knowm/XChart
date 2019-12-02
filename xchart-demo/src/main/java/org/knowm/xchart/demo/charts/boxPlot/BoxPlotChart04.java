package org.knowm.xchart.demo.charts.boxPlot;

import java.util.Arrays;

import org.knowm.xchart.BoxPlotChartBuilder;
import org.knowm.xchart.BoxPlotChart;
import org.knowm.xchart.BoxPlotSeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/*
 * Box Plot with 2 series
 * it cannot draw box plot
 */
public class BoxPlotChart04 implements ExampleChart<BoxPlotChart> {

  public static void main(String[] args) {
    ExampleChart<BoxPlotChart> exampleChart = new BoxPlotChart04();
    BoxPlotChart chart = exampleChart.getChart();
    new SwingWrapper<BoxPlotChart>(chart).displayChart();
  }

  @Override
  public BoxPlotChart getChart() {

    // Create Chart
    BoxPlotChart chart =
        new BoxPlotChartBuilder()
        .width(600)
        .height(450)
        .title("Series < 3 -- box plot demo")
        .xAxisTitle("Color")
        .yAxisTitle("temperature")
        .theme(ChartTheme.GGPlot2)
        .build();
    // Series
    BoxPlotSeries[] boxSeries = new BoxPlotSeries[2];
    boxSeries[0] = chart.addSeries("aaa", Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
        Arrays.asList(40, -30, 20, 60, 50));
    boxSeries[1] = chart.addSeries("bbb", Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
        Arrays.asList(10, -10, 20, 50, 60));
    return chart;
  }
}
