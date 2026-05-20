package org.knowm.xchart.demo.charts.line;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.YAxisPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Merged Y-Axes — Weather Station
 *
 * <p>Demonstrates the following:
 *
 * <ul>
 *   <li>Merged Y-axis groups (two axes sharing one column per side)
 *   <li>Left: Temperature (°C) and Relative Humidity (%) — scales differ by ~3×
 *   <li>Right: Barometric Pressure (hPa) and Wind Speed (km/h) — scales differ by ~50×
 *   <li>Independent tick labels per axis on the same visual column
 * </ul>
 */
public class LineChart11 implements ExampleChart<XYChart> {

  public static void main(String[] args) {

    ExampleChart<XYChart> exampleChart = new LineChart11();
    XYChart chart = exampleChart.getChart();
    new SwingWrapper<>(chart).displayChart();
  }

  @Override
  public XYChart getChart() {

    // 30 days of synthetic weather station readings
    int days = 30;
    List<Double> x = new ArrayList<>();
    List<Double> temperature = new ArrayList<>();
    List<Double> humidity = new ArrayList<>();
    List<Double> pressure = new ArrayList<>();
    List<Double> windSpeed = new ArrayList<>();

    for (int i = 0; i < days; i++) {
      double t = i / (double) (days - 1); // 0.0 → 1.0
      x.add((double) (i + 1));

      // Temperature: sinusoidal warming trend, 10 °C – 28 °C
      temperature.add(19.0 + 9.0 * Math.sin(2 * Math.PI * t - Math.PI / 2) + 1.5 * Math.sin(7 * Math.PI * t));

      // Humidity: inversely correlated with temperature, 42 % – 88 %
      humidity.add(65.0 - 23.0 * Math.sin(2 * Math.PI * t - Math.PI / 2) + 4.0 * Math.cos(5 * Math.PI * t));

      // Pressure: slow high/low system passage, 998 hPa – 1022 hPa
      pressure.add(1010.0 + 12.0 * Math.cos(2 * Math.PI * t) - 4.0 * Math.sin(4 * Math.PI * t));

      // Wind speed: peaks during pressure troughs, 1 km/h – 38 km/h
      windSpeed.add(Math.max(1.0, 18.0 - 17.0 * Math.cos(2 * Math.PI * t) + 5.0 * Math.abs(Math.sin(3 * Math.PI * t))));
    }

    XYChart chart =
        new XYChartBuilder()
            .width(900)
            .height(550)
            .title("30-Day Weather Station — Merged Y-Axes")
            .xAxisTitle("Day")
            .build();

    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
    chart.getStyler().setLegendPosition(LegendPosition.InsideNW);

    // ── Left side: Temperature (group 0) + Humidity (group 1) ──────────────
    XYSeries tempSeries = chart.addSeries("Temperature (°C)", x, temperature);
    tempSeries.setYAxisGroup(0);
    tempSeries.setMarker(SeriesMarkers.NONE);
    tempSeries.setLineColor(new Color(214, 39, 40)); // red

    XYSeries humSeries = chart.addSeries("Humidity (%)", x, humidity);
    humSeries.setYAxisGroup(1);
    humSeries.setMarker(SeriesMarkers.NONE);
    humSeries.setLineColor(new Color(31, 119, 180)); // blue

    // ── Right side: Pressure (group 2) + Wind Speed (group 3) ──────────────
    XYSeries presSeries = chart.addSeries("Pressure (hPa)", x, pressure);
    presSeries.setYAxisGroup(2);
    presSeries.setMarker(SeriesMarkers.NONE);
    presSeries.setLineColor(new Color(44, 160, 44)); // green

    XYSeries windSeries = chart.addSeries("Wind Speed (km/h)", x, windSpeed);
    windSeries.setYAxisGroup(3);
    windSeries.setMarker(SeriesMarkers.NONE);
    windSeries.setLineColor(new Color(148, 103, 189)); // purple

    // Position groups 2 and 3 on the right side
    chart.getStyler().setYAxisGroupPosition(2, YAxisPosition.Right);
    chart.getStyler().setYAxisGroupPosition(3, YAxisPosition.Right);

    // Axis titles
    chart.setYAxisGroupTitle(0, "°C");
    chart.setYAxisGroupTitle(1, "%");
    chart.setYAxisGroupTitle(2, "hPa");
    chart.setYAxisGroupTitle(3, "km/h");

    // Merge: groups 0+1 share one left column, groups 2+3 share one right column
    chart.getStyler().mergeYAxisGroups(0, 1);
    chart.getStyler().mergeYAxisGroups(2, 3);

    // Colocate slave labels on the master axis column
    chart.getStyler().setMergedAxisColocateSlaveLabels(true);
    chart.getStyler().setMergedAxisColocatedSlaveLabelsGap(8.0);

    // Color the slave axis labels to match their series
    chart.getStyler().setYAxisGroupTickLabelsColorMap(1, new Color(31, 119, 180));
    chart.getStyler().setYAxisGroupTickLabelsColorMap(3, new Color(148, 103, 189));

    return chart;
  }

  @Override
  public String getExampleChartName() {

    return getClass().getSimpleName() + " - Merged Y-Axes (Weather Station)";
  }
}
