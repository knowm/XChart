package org.knowm.xchart.standalone.issues;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.HeatMapChartBuilder;
import org.knowm.xchart.HeatMapSeries;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler.LegendLayout;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Demonstrates issue #585 — a symmetric colormap definition produces an asymmetric legend.
 *
 * <p>The gradient legend bar for the Vertical layout has its LinearGradientPaint "start" point
 * placed {@code fontSize} pixels above the actual bottom of the rectangle. This leaves a strip at
 * the bottom that is always clamped to {@code rangeColors[0]} (the min colour), making the
 * min-colour area visually larger than the max-colour area even when the colormap and data range
 * are perfectly symmetric.
 *
 * <p>Two windows open side by side — one for each LegendPosition — so the asymmetry is visible in
 * both orientations. The colourmap intentionally goes blue → dark → blue to make the off-centre
 * midpoint obvious.
 */
public class TestForIssue585 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChartVertical()).displayChart();
    new SwingWrapper<>(getChartHorizontal()).displayChart();
  }

  /** Vertical legend — bug is visible here: blue band at the bottom is wider than at the top. */
  public static HeatMapChart getChartVertical() {

    HeatMapChart chart = buildChart("Issue #585 – Vertical legend (bug: asymmetric gradient)");
    chart.getStyler().setLegendPosition(LegendPosition.OutsideE);
    chart.getStyler().setLegendLayout(LegendLayout.Vertical);
    chart.getStyler().setGradientColorColumnHeight(200);
    return chart;
  }

  /** Horizontal legend — shown for comparison. */
  public static HeatMapChart getChartHorizontal() {

    HeatMapChart chart = buildChart("Issue #585 – Horizontal legend (for comparison)");
    chart.getStyler().setLegendPosition(LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(LegendLayout.Horizontal);
    chart.getStyler().setGradientColorColumnHeight(200);
    return chart;
  }

  public static HeatMapChart getChart() {

    return getChartVertical();
  }

  private static HeatMapChart buildChart(String title) {

    // 21-colour symmetric palette: full-blue → no-blue → full-blue
    Color[] colors =
        new Color[] {
          new Color(0.0f, 0.5f, 1.0f),
          new Color(0.0f, 0.5f, 0.9f),
          new Color(0.0f, 0.5f, 0.8f),
          new Color(0.0f, 0.5f, 0.7f),
          new Color(0.0f, 0.5f, 0.6f),
          new Color(0.0f, 0.5f, 0.5f),
          new Color(0.0f, 0.5f, 0.4f),
          new Color(0.0f, 0.5f, 0.3f),
          new Color(0.0f, 0.5f, 0.2f),
          new Color(0.0f, 0.5f, 0.1f),
          new Color(0.0f, 0.5f, 0.0f), // middle — darkest
          new Color(0.0f, 0.5f, 0.1f),
          new Color(0.0f, 0.5f, 0.2f),
          new Color(0.0f, 0.5f, 0.3f),
          new Color(0.0f, 0.5f, 0.4f),
          new Color(0.0f, 0.5f, 0.5f),
          new Color(0.0f, 0.5f, 0.6f),
          new Color(0.0f, 0.5f, 0.7f),
          new Color(0.0f, 0.5f, 0.8f),
          new Color(0.0f, 0.5f, 0.9f),
          new Color(0.0f, 0.5f, 1.0f),
        };

    List<String> xData = new ArrayList<>();
    List<String> yData = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      xData.add("X" + i);
      yData.add("Y" + i);
    }

    // Data ranges symmetrically from -5 to +5
    List<Number[]> heatData = new ArrayList<>();
    double step = 10.0 / (xData.size() * yData.size() - 1);
    int idx = 0;
    for (int x = 0; x < xData.size(); x++) {
      for (int y = 0; y < yData.size(); y++) {
        heatData.add(new Number[] {x, y, -5.0 + step * idx});
        idx++;
      }
    }

    HeatMapChart chart =
        new HeatMapChartBuilder().width(700).height(500).title(title).build();

    HeatMapSeries series = chart.addSeries("heat", xData, yData, heatData);
    series.setMin(-5.0);
    series.setMax(5.0);

    chart
        .getStyler()
        .setRangeColors(colors)
        .setMin(-5.0)
        .setMax(5.0)
        .setLegendVisible(true);

    return chart;
  }
}
