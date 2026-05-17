package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/**
 * Demonstrates the fix for issue #886 — UI freeze when the chart window is too narrow.
 *
 * <p>Root cause: {@code AxisTickCalculator_.willLabelsFitInTickSpaceHint()} was called with a
 * {@code tickSpacingHint} of 0 or negative when the window was extremely narrow. This caused
 * {@code TextLayout.getOutline()} to loop indefinitely inside {@code Path2D.append()}, locking the
 * AWT-EventQueue thread permanently.
 *
 * <p>Two additional guards were also added:
 *
 * <ul>
 *   <li>Labels longer than 20 characters are rejected immediately without calling getOutline().
 *   <li>{@code calculateForEquallySpacedAxisValues()} returns early when range &lt; 1e-10 (all
 *       data points equal), preventing its own infinite loop.
 * </ul>
 *
 * <p>To reproduce the freeze with an unpatched build: run this demo and immediately resize the
 * window to be very narrow (a few pixels wide). With the fix the chart degrades gracefully instead
 * of hanging.
 */
public class TestForIssue886 {

  public static void main(String[] args) {

    // Start with a deliberately narrow window — this is what triggered the freeze.
    new SwingWrapper<>(getChartNarrow()).displayChart();

    // Normal width chart for comparison.
    new SwingWrapper<>(getChartNormal()).displayChart();
  }

  /** Narrow chart (50 px wide) — previously caused an EDT freeze on unpatched builds. */
  public static XYChart getChartNarrow() {

    XYChart chart =
        new XYChartBuilder()
            .width(50)
            .height(400)
            .title("Issue #886 – narrow window (was: freeze)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.addSeries("data", new double[]{1, 2, 3, 4, 5}, new double[]{2, 4, 3, 7, 5});
    return chart;
  }

  /** Normal chart for comparison — should always render fine. */
  public static XYChart getChartNormal() {

    XYChart chart =
        new XYChartBuilder()
            .width(700)
            .height(400)
            .title("Issue #886 – normal window (reference)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.addSeries("data", new double[]{1, 2, 3, 4, 5}, new double[]{2, 4, 3, 7, 5});
    // Flat data — all same Y value — triggers the range<1e-10 guard.
    chart.addSeries("flat", Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(3, 3, 3, 3, 3));
    return chart;
  }
}
