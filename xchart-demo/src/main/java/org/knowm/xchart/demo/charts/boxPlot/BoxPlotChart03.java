package org.knowm.xchart.demo.charts.boxPlot;

import java.util.Arrays;

import org.knowm.xchart.BoxPlotChartBuilder;
import org.knowm.xchart.BoxPlotChart;
import org.knowm.xchart.BoxPlotSeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.ChartTheme;

/*
 * Box Plot with 6 series
 * and show ToolTips
 * and Y-Axis is logarithmic
 */
public class BoxPlotChart03 implements ExampleChart<BoxPlotChart> {

  public static void main(String[] args) {
    ExampleChart<BoxPlotChart> exampleChart = new BoxPlotChart03();
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
        .title("Y Axis Logarithmic-box plot demo")
        .xAxisTitle("Color")
        .yAxisTitle("temperature")
        .theme(ChartTheme.XChart)
        .build();
    // Series
    BoxPlotSeries[] boxSeries = new BoxPlotSeries[6];
    boxSeries[0] = chart.addSeries("aaa", Arrays.asList(40, 30, 20, 60, 50));
    boxSeries[1] = chart.addSeries("bbb", Arrays.asList(20, 10, 25, null, 60));
    boxSeries[2] = chart.addSeries("ccc", Arrays.asList(30, 20, 22, 40, 50));
    boxSeries[3] = chart.addSeries("ddd", Arrays.asList(10, 15, 28, 50, 65));
    boxSeries[4] = chart.addSeries("eee", Arrays.asList(25, 25, 30, 30, 55));
    boxSeries[5] = chart.addSeries("fff", Arrays.asList(15, 18, 29, 20, 50));
    // Customize Chart
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setYAxisLogarithmic(true);
    chart.getStyler().setYAxisMin(1d);
    chart.getStyler().setYAxisMax(160d);
    return chart;
  }
}
