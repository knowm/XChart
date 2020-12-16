package org.knowm.xchart.demo.charts.scatter;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Error Bars
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Error Bars
 *   <li>Using ChartBuilder to Make a Chart
 *   <li>List<Number> data sets
 *   <li>Setting Series Marker and Marker Color
 *   <li>Using a custom decimal pattern
 *   <li>InfoPanel
 */
public class ScatterChart04 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new ScatterChart04();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("ScatterChart04")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
    chart.getStyler().setChartTitleVisible(false);
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setAxisTitlesVisible(false);
    chart.getStyler().setXAxisDecimalPattern("0.0000000");

    // InfoPanel
    chart.addInfoPanelContent("Here are some words in an InfoPanel!", 40, 40);
    chart.addInfoPanelContent("Here are some additional words", 0.5, 0.5);
    chart.addInfoPanelContent("Here are some additional words \n in the upper right-hand corner \n with multiple lines", 1.0, 1.0);
    chart.getStyler().setInfoPanelPadding(20);
    chart.getStyler().setInfoPanelFont( new Font("Verdana", Font.BOLD, 12));
//    chart.getStyler().setInfoPanelBackgroundColor(Color.RED);
//    chart.getStyler().setInfoPanelBorderColor(Color.BLUE);
//    chart.getStyler().setInfoPanelVisible(false);

    // Series
    int size = 10;
    List<Double> xData = new ArrayList<>();
    List<Double> yData = new ArrayList<>();
    List<Double> errorBars = new ArrayList<>();
    for (int i = 0; i <= size; i++) {
      xData.add(((double) i) / 1000000);
      yData.add(10 * Math.exp(-i));
      errorBars.add(Math.random() + .3);
    }
    XYSeries series = chart.addSeries("10^(-x)", xData, yData, errorBars);
    series.setMarkerColor(Color.RED);
    series.setMarker(SeriesMarkers.SQUARE);

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Error Bars";
  }
}
