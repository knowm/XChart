package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Demonstrates the fix for issue #914 — anti-aliasing flags were silently ignored.
 *
 * <p>Two independent flags are now available:
 *
 * <ul>
 *   <li>{@code styler.setAntiAlias(boolean)} — controls shape/graphics anti-aliasing (markers,
 *       lines, filled regions, axis lines, legend boxes). Used by {@code Axis} and all {@code
 *       Legend_*} components.
 *   <li>{@code styler.setTextAntiAlias(boolean)} — controls text anti-aliasing (chart title,
 *       annotation text, chart button labels). Used by {@code ChartTitle}, {@code AnnotationText},
 *       {@code AnnotationTextPanel}, and {@code ChartButton}.
 * </ul>
 *
 * <p>Previously, all 11 affected paint methods hardcoded {@code VALUE_ANTIALIAS_ON}, overriding
 * the user's setting on every repaint.
 *
 * <p>Run this demo: three windows show all-on (default), graphics-off/text-on, and all-off.
 */
public class TestForIssue914 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChartAllOn()).displayChart();
    new SwingWrapper<>(getChartGraphicsOffTextOn()).displayChart();
    new SwingWrapper<>(getChartAllOff()).displayChart();
  }

  /** All anti-aliasing ON (default). */
  public static XYChart getChartAllOn() {

    XYChart chart = buildChart("Issue #914 – graphics AA=ON, text AA=ON (default)");
    // defaults — no calls needed
    return chart;
  }

  /** Graphics anti-aliasing OFF, text anti-aliasing ON. */
  public static XYChart getChartGraphicsOffTextOn() {

    XYChart chart = buildChart("Issue #914 – graphics AA=OFF, text AA=ON");
    chart.getStyler().setAntiAlias(false);
    chart.getStyler().setTextAntiAlias(true);
    return chart;
  }

  /** Both anti-aliasing flags OFF. */
  public static XYChart getChartAllOff() {

    XYChart chart = buildChart("Issue #914 – graphics AA=OFF, text AA=OFF");
    chart.getStyler().setAntiAlias(false);
    chart.getStyler().setTextAntiAlias(false);
    return chart;
  }

  private static XYChart buildChart(String title) {

    XYChart chart =
        new XYChartBuilder()
            .width(600)
            .height(400)
            .title(title)
            .xAxisTitle("X Axis")
            .yAxisTitle("Y Axis")
            .build();

    chart.addSeries("Series A", new double[] {1, 2, 3, 4, 5}, new double[] {2, 5, 3, 7, 4});
    chart
        .addSeries("Series B", new double[] {1, 2, 3, 4, 5}, new double[] {5, 3, 6, 2, 8})
        .setMarker(SeriesMarkers.SQUARE);

    return chart;
  }
}
