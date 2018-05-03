package org.knowm.xchart.standalone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Create a Chart matrix
 *
 * @author timmolter
 */
public class Example2 {

  public static void main(String[] args) throws IOException {

    int numCharts = 4;

    List<Chart> charts = new ArrayList<Chart>();

    for (int i = 0; i < numCharts; i++) {
      XYChart chart =
          new XYChartBuilder().xAxisTitle("X").yAxisTitle("Y").width(600).height(400).build();
      chart.getStyler().setYAxisMin(-10.0);
      chart.getStyler().setYAxisMax(10.0);
      XYSeries series = chart.addSeries("" + i, null, getRandomWalk(200));
      series.setMarker(SeriesMarkers.NONE);
      charts.add(chart);
    }
    new SwingWrapper<Chart>(charts).displayChartMatrix();

    BitmapEncoder.saveBitmap(charts, 2, 2, "./Sample_Chart_Matrix", BitmapEncoder.BitmapFormat.PNG);
  }

  /**
   * Generates a set of random walk data
   *
   * @param numPoints
   * @return
   */
  private static double[] getRandomWalk(int numPoints) {

    double[] y = new double[numPoints];
    y[0] = 0;
    for (int i = 1; i < y.length; i++) {
      y[i] = y[i - 1] + Math.random() - .5;
    }
    return y;
  }
}
