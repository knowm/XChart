package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * Demonstrates issue #816 — controlling the width of the boxes in a box plot.
 *
 * <p>By default the box width is a small, fixed amount derived from the plot margin and independent
 * of the number of series. The new {@code BoxStyler#setBoxWidthFraction(double)} sizes each box
 * relative to the horizontal slot available to it (a value in {@code (0, 1]}); {@code <= 0} keeps
 * the legacy width.
 *
 * <p>This shows the same data rendered with the default width and with {@code 0.6} of the slot,
 * side by side.
 */
public class TestForIssue816 {

  public static void main(String[] args) {

    new SwingWrapper<>(Arrays.asList(getChart(-1), getChart(0.6))).displayChartMatrix();
  }

  /**
   * Constructs a box chart (headless-safe).
   *
   * @param boxWidthFraction fraction of the per-series slot in {@code (0, 1]}, or {@code <= 0} for
   *     the legacy default width
   */
  public static BoxChart getChart(double boxWidthFraction) {

    BoxChart chart =
        new BoxChartBuilder()
            .width(600)
            .height(450)
            .title(boxWidthFraction > 0 ? "boxWidthFraction = " + boxWidthFraction : "default width")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .theme(ChartTheme.GGPlot2)
            .build();

    chart.getStyler().setBoxWidthFraction(boxWidthFraction);

    List<Integer> aaa = Arrays.asList(40, 30, 20, 60, 50);
    List<Integer> bbb = Arrays.asList(-20, -10, -30, -15, -25);
    List<Integer> ccc = Arrays.asList(50, -20);
    chart.addSeries("aaa", aaa);
    chart.addSeries("bbb", bbb);
    chart.addSeries("ccc", ccc);
    return chart;
  }
}
