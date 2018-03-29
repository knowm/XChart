package org.knowm.xchart.demo.charts.theme;

import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Matlab Theme
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Building a Chart with ChartBuilder
 *   <li>Applying the Matlab Theme to the Chart
 *   <li>Generating Gaussian Bell Curve Data
 */
public class ThemeChart03 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new ThemeChart03();
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
            .theme(ChartTheme.Matlab)
            .title("Matlab Theme")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart
    chart.getStyler().setPlotGridLinesVisible(false);
    chart.getStyler().setXAxisTickMarkSpacingHint(100);
    chart.getStyler().setToolTipsEnabled(true);

    // Series
    List<Integer> xData = new ArrayList<Integer>();
    for (int i = 0; i < 640; i++) {
      xData.add(i);
    }
    List<Double> y1Data = getYAxis(xData, 320, 160);
    List<Double> y2Data = getYAxis(xData, 320, 320);
    List<Double> y3Data = new ArrayList<Double>(xData.size());
    for (int i = 0; i < 640; i++) {
      y3Data.add(y1Data.get(i) - y2Data.get(i));
    }

    XYSeries series = chart.addSeries("Gaussian 1", xData, y1Data);
    series.setMarker(SeriesMarkers.NONE);
    series = chart.addSeries("Gaussian 2", xData, y2Data);
    series.setMarker(SeriesMarkers.NONE);
    series.setYAxisGroup(1); // default is group 0
    series = chart.addSeries("Difference", xData, y3Data);
    series.setMarker(SeriesMarkers.NONE);

    chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideS);

    return chart;
  }

  private List<Double> getYAxis(List<Integer> xData, double mean, double std) {

    List<Double> yData = new ArrayList<Double>(xData.size());

    for (Integer integer : xData) {
      yData.add(
          (1 / (std * Math.sqrt(2 * Math.PI)))
              * Math.exp(-(((integer - mean) * (integer - mean)) / ((2 * std * std)))));
    }
    return yData;
  }
}
