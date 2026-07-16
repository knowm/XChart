package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.theme.XChartTheme;

/**
 * Issue #503 — Axis Title Padding does not apply to the second (right-side) Y-Axis.
 *
 * <p>A custom theme (extending {@link org.knowm.xchart.style.theme.AbstractBaseTheme} via {@link
 * XChartTheme}) returns a large {@code getAxisTitlePadding()}. On the LEFT axis the padding opens up
 * a wide gap between the rotated axis title and the tick numbers. On the RIGHT axis the title stays
 * jammed against its tick numbers no matter how big the padding is — the padding is spent as dead
 * space on the outer edge of the title instead of between the title and its labels.
 *
 * <p>Run {@link #main} and compare the two sides.
 */
public class TestForIssue503 {

  private static final int BIG_PADDING = 60;

  /** Custom theme, as in the bug report: only the axis-title padding is overridden. */
  static class BigTitlePaddingTheme extends XChartTheme {
    @Override
    public int getAxisTitlePadding() {
      return BIG_PADDING;
    }
  }

  public static XYChart getChart() {

    XYChart chart = new XYChart(900, 500);
    chart.setTitle("Issue #503 — axisTitlePadding=" + BIG_PADDING + " (left works, right doesn't)");
    chart.getStyler().setTheme(new BigTitlePaddingTheme());

    chart.getStyler().setYAxisGroupPosition(0, Styler.YAxisPosition.Left);
    chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);

    chart.setXAxisTitle("x");
    chart.setYAxisGroupTitle(0, "LEFT axis title");
    chart.setYAxisGroupTitle(1, "RIGHT axis title");

    chart
        .addSeries("left series", new double[] {1, 2, 3}, new double[] {10, 20, 30})
        .setYAxisGroup(0);
    chart
        .addSeries("right series", new double[] {1, 2, 3}, new double[] {1000, 2000, 3000})
        .setYAxisGroup(1);

    return chart;
  }

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }
}
