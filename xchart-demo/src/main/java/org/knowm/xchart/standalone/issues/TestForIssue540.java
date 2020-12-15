package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class TestForIssue540 {

  public static void main(String[] args) {

    TestForIssue540 exampleChart = new TestForIssue540();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  public XYChart getChart() {

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).build();

    // Customize Chart
    chart.getStyler().setErrorBarsColor(Color.black);

    // Series
    int[] xData = new int[] {0, 1, 2, 3, 4, 5, 6};
    int[] yData1 = new int[] {0, 1, 2, 3, 4, 5, 6};
    //    int[] errdata = new int[] {0, 1, 2, 3, 4, 5, 6};
    int[] errdata = new int[] {0, 2, 4, 6, 8, 10, 12};
    XYSeries series1 = chart.addSeries("Error bar\ntest data", xData, yData1, errdata);
    series1.setLineStyle(SeriesLines.SOLID);
    series1.setMarker(SeriesMarkers.DIAMOND);
    series1.setMarkerColor(Color.GREEN);

    return chart;
  }
}
