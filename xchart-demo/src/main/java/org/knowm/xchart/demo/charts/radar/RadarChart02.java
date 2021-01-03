package org.knowm.xchart.demo.charts.radar;

import org.knowm.xchart.RadarChart;
import org.knowm.xchart.RadarChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.RadarStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Radar Chart
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Radar Chart
 *   <li>Circular style
 *   <li>Tool tips
 *   <li>GGPlot2 Theme
 */
public class RadarChart02 implements ExampleChart<RadarChart> {

  public static void main(String[] args) {

    ExampleChart<RadarChart> exampleChart = new RadarChart02();
    RadarChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public RadarChart getChart() {

    // Create Chart
    RadarChart chart =
        new RadarChartBuilder()
            .width(800)
            .height(600)
            .title(getClass().getSimpleName())
            .theme(Styler.ChartTheme.GGPlot2)
            .build();
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setRadarRenderStyle(RadarStyler.RadarRenderStyle.Circle);
    chart.getStyler().setSeriesFilled(false);
    chart.getStyler().setRadiiTickMarksCount(4);
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    // Series
    chart.setRadiiLabels(
        new String[] {
          "Math",
          "Biology",
          "German",
          "English",
          "Chemistry",
          "PhyEd",
          "Band",
          "Physics",
          "Programming"
        });
    chart
        .addSeries(
            "Mark",
            new double[] {
              3.9 / 4.0, 2.9 / 4.0, 3.4 / 4.0, 2.8 / 4.0, 4 / 4.0, 2.4 / 4.0, 3.1 / 4.0, 2.9 / 4.0,
              3.8 / 4.0
            })
        .setMarker(SeriesMarkers.NONE);
    chart
        .addSeries(
            "Mary",
            new double[] {
              2.6 / 4.0, 3.3 / 4.0, 3.7 / 4.0, 3.1 / 4.0, 3.6 / 4.0, 3.2 / 4.0, 3.8 / 4.0,
              3.7 / 4.0, 3.1 / 4.0
            })
        .setMarker(SeriesMarkers.NONE);

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Circular Radar Chart with GGplot2 Theme";
  }
}
