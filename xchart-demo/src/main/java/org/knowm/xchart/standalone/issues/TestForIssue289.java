package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class TestForIssue289 {

  public static void main(String[] args) throws IOException {

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

    // Scatter Series w/ Error Bars
    int size = 10;
    List<Double> xData = new ArrayList<Double>();
    List<Double> yData = new ArrayList<Double>();
    List<Double> errorBars = new ArrayList<Double>();
    for (int i = 0; i <= size; i++) {
      xData.add(((double) i) / 1000000);
      yData.add(10 * Math.exp(-i) + Math.random());
      errorBars.add(Math.random() + .3);
    }
    XYSeries series = chart.addSeries("data", xData, yData, errorBars);
    series.setMarkerColor(Color.RED);
    series.setMarker(SeriesMarkers.SQUARE);

    // Line Series
    size = 100;
    xData = new ArrayList<Double>();
    yData = new ArrayList<Double>();
    for (int i = 0; i <= size; i++) {
      xData.add(((double) i) / 10000000);
      yData.add(10 * Math.exp(-i / (double) 10));
      errorBars.add(Math.random() + .3);
    }
    series = chart.addSeries("fit", xData, yData);
    series.setMarker(SeriesMarkers.NONE);
    series.setXYSeriesRenderStyle(XYSeriesRenderStyle.Line);

    new SwingWrapper<XYChart>(chart).displayChart();
  }
}
