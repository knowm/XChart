package org.knowm.xchart.standalone.readme;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

/** @author timmolter */
public class IntermediateExample {

  static final Random random = new Random();

  public static void main(String[] args) {

    // Create Chart
    XYChart chart =
        new XYChartBuilder()
            .width(600)
            .height(500)
            .title("Gaussian Blobs")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
    chart.getStyler().setChartTitleVisible(false);
    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
    chart.getStyler().setMarkerSize(16);

    // Series
    chart.addSeries("Gaussian Blob 1", getGaussian(1000, 1, 10), getGaussian(1000, 1, 10));
    XYSeries series =
        chart.addSeries("Gaussian Blob 2", getGaussian(1000, 1, 10), getGaussian(1000, 0, 5));
    series.setMarker(SeriesMarkers.DIAMOND);

    new SwingWrapper(chart).displayChart();
  }

  private static List<Double> getGaussian(int number, double mean, double std) {

    List<Double> seriesData = new LinkedList<Double>();
    for (int i = 0; i < number; i++) {
      seriesData.add(mean + std * random.nextGaussian());
    }

    return seriesData;
  }
}
