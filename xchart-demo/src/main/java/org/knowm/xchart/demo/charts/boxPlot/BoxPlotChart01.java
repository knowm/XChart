package org.knowm.xchart.demo.charts.boxPlot;

import java.util.Arrays;

import org.knowm.xchart.BoxPlotChartBuilder;
import org.knowm.xchart.BoxPlotChart;
import org.knowm.xchart.BoxPlotSeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/*
 * Box Plot with 3 series
 */
public class BoxPlotChart01 implements ExampleChart<BoxPlotChart> {

  public static void main(String[] args) {
    ExampleChart<BoxPlotChart> exampleChart = new BoxPlotChart01();
    BoxPlotChart chart = exampleChart.getChart();
    new SwingWrapper<BoxPlotChart>(chart).displayChart();
  }

  @Override
  public BoxPlotChart getChart() {

    // Create Chart
    BoxPlotChart chart =
        new BoxPlotChartBuilder()
        .title("box plot demo")
        .xAxisTitle("Color")
        .yAxisTitle("temperature")
        .theme(ChartTheme.GGPlot2)
        .build();
    // Series
    BoxPlotSeries[] boxSeries = new BoxPlotSeries[3];
    boxSeries[0] = chart.addSeries("aaa", Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
        Arrays.asList(40, -30, 20, 60, 50));
    boxSeries[1] = chart.addSeries("bbb", Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
        Arrays.asList(10, -10, 30, 50, 60));
    boxSeries[2] = chart.addSeries("ccc", Arrays.asList("Blue", "Red", "Green", "Yellow", "Orange"),
        Arrays.asList(50, -20, 10, 40, 50));
    return chart;
  }
}
