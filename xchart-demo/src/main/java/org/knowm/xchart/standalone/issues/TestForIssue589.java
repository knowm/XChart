package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.AnnotationLine;
import org.knowm.xchart.AnnotationText;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.YAxisPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Issue #589 — annotations could not be resolved against a secondary Y-Axis group.
 *
 * <p>Two series on wildly different scales: Temperature on group 0 (left, ~10–28 °C) and Barometric
 * Pressure on group 1 (right, ~998–1022 hPa). Each gets a horizontal threshold line drawn at a value
 * that is meaningful on ITS OWN axis.
 *
 * <p><b>Before the fix:</b> every annotation resolved its Y value through the group-0 axis, so the
 * 1002 hPa line was interpreted on the 10–28 °C scale and flew far off the top of the plot — only
 * the 22 °C line was visible, and there was no way to place the other one.
 *
 * <p><b>After the fix:</b> {@code setYAxisGroup(1)} puts the pressure threshold at 1002 hPa on the
 * right-hand scale, cutting through the green pressure curve, while the temperature threshold stays
 * on the left scale cutting through the red temperature curve.
 *
 * <p>Each line should intersect the curve of the same color-coded units it belongs to. If both lines
 * land at the same height, or one is missing, the fix is not in effect.
 */
public class TestForIssue589 {

  private static final double TEMP_THRESHOLD = 22.0; // °C, group 0 — "warm day"
  private static final double PRESSURE_THRESHOLD = 1002.0; // hPa, group 1 — "storm watch"

  public static XYChart getChart() {

    int days = 30;
    List<Double> x = new ArrayList<>();
    List<Double> temperature = new ArrayList<>();
    List<Double> pressure = new ArrayList<>();

    for (int i = 0; i < days; i++) {
      double t = i / (double) (days - 1); // 0.0 → 1.0
      x.add((double) (i + 1));
      // 10 °C – 28 °C
      temperature.add(19.0 + 9.0 * Math.sin(2 * Math.PI * t - Math.PI / 2));
      // 998 hPa – 1022 hPa
      pressure.add(1010.0 + 12.0 * Math.cos(2 * Math.PI * t));
    }

    XYChart chart =
        new XYChartBuilder()
            .width(900)
            .height(600)
            .title("Issue #589 — each threshold line on its own Y-Axis group")
            .xAxisTitle("Day")
            .build();

    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
    chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
    chart.getStyler().setAnnotationLineColor(Color.DARK_GRAY);

    XYSeries tempSeries = chart.addSeries("Temperature (°C)", x, temperature);
    tempSeries.setYAxisGroup(0);
    tempSeries.setMarker(SeriesMarkers.NONE);
    tempSeries.setLineColor(new Color(214, 39, 40)); // red

    XYSeries pressureSeries = chart.addSeries("Pressure (hPa)", x, pressure);
    pressureSeries.setYAxisGroup(1);
    pressureSeries.setMarker(SeriesMarkers.NONE);
    pressureSeries.setLineColor(new Color(44, 160, 44)); // green

    chart.setYAxisGroupTitle(0, "°C");
    chart.setYAxisGroupTitle(1, "hPa");
    chart.getStyler().setYAxisGroupPosition(1, YAxisPosition.Right);

    // ── The point of this demo ────────────────────────────────────────────
    // Group 0 is the default, so this behaves exactly as it always has.
    chart.addAnnotation(new AnnotationLine(TEMP_THRESHOLD, false, false));
    chart.addAnnotation(
        new AnnotationText(TEMP_THRESHOLD + " °C", 4.0, TEMP_THRESHOLD + 0.7, false));

    // Group 1 is the new capability — without setYAxisGroup(1) this line would be
    // interpreted on the temperature scale and land off the top of the plot.
    chart.addAnnotation(new AnnotationLine(PRESSURE_THRESHOLD, false, false).setYAxisGroup(1));
    chart.addAnnotation(
        new AnnotationText(PRESSURE_THRESHOLD + " hPa", 26.0, PRESSURE_THRESHOLD + 1.0, false)
            .setYAxisGroup(1));

    return chart;
  }

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }
}
