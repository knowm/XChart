package org.knowm.xchart.standalone.issues;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/**
 * Demonstrates issue #805 — cursor label displays the wrong Y value.
 *
 * <p>Two distinct bugs are shown:
 *
 * <ol>
 *   <li><b>Stale cursor data:</b> A background timer flips the "live" series between two Y
 *       datasets every 2 seconds. Move the mouse into the plot, then hold it still. After the timer
 *       fires the chart repaints with the new data, but the cursor label continues to display the
 *       <em>old</em> Y value until the mouse moves again. The root cause is that {@code
 *       Cursor.matchingDataPointList} is never refreshed when {@code setData()} is called with
 *       fresh interaction data — it is only updated on the next {@code mouseMoved} event.
 *   <li><b>Wrong point selected when points overlap:</b> The "overlap" series has two data points
 *       at X&nbsp;=&nbsp;3.0 (Y&nbsp;=&nbsp;10) and X&nbsp;=&nbsp;3.1 (Y&nbsp;=&nbsp;90). Both
 *       points' hit-ellipses cover the same screen region. Hover the cursor over that area:
 *       currently only <em>one</em> Y value appears in the tooltip — either the wrong one is
 *       chosen, or the second value is silently dropped. After the fix both Y values should appear,
 *       combined as "10, 90".
 * </ol>
 *
 * <p>How to reproduce:
 *
 * <ul>
 *   <li>Run the demo, move the mouse to the plot and hold it still near X&nbsp;=&nbsp;3.
 *   <li>Observe the cursor label: it should update every 2&nbsp;s as the timer toggles Y values,
 *       but <em>before the fix</em> it stays frozen on the first value seen.
 *   <li>Also observe that hovering near X&nbsp;=&nbsp;3 on the "overlap" series shows only one Y
 *       value instead of both.
 * </ul>
 */
public class TestForIssue805 {

  public static void main(String[] args) {

    SwingWrapper<XYChart> wrapper = new SwingWrapper<>(getChart());
    wrapper.displayChart();
    wrapper.getXChartPanel().setCursorEnabled(true);

    // Bug 1 — stale cursor data: toggle the "live" series Y values every 2 s.
    // With the bug, holding the mouse still means the cursor label never updates.
    XYChart chart = wrapper.getXChartPanel().getChart();
    final boolean[] toggle = {false};
    Timer timer =
        new Timer(
            2000,
            e -> {
              double[] yHigh = {10, 50, 90, 70, 30};
              double[] yLow = {5, 25, 45, 35, 15};
              double[] y = toggle[0] ? yHigh : yLow;
              toggle[0] = !toggle[0];
              chart.updateXYSeries("live", new double[] {1, 2, 3, 4, 5}, y, null);
              SwingUtilities.invokeLater(() -> wrapper.getXChartPanel().repaint());
            });
    timer.setRepeats(true);
    timer.start();
  }

  /**
   * Constructs and returns the chart without launching a window (headless-safe).
   *
   * <p>Contains two series:
   *
   * <ul>
   *   <li><b>live</b> — updated every 2&nbsp;s by the timer in {@code main()}; demonstrates the
   *       stale-cursor bug.
   *   <li><b>overlap</b> — has two data points at nearly the same X (3.0 and 3.1) with very
   *       different Y values (10 and 90); demonstrates the wrong-point-selected bug.
   * </ul>
   */
  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(700)
            .height(500)
            .title("Issue #805 – cursor label shows wrong / stale value")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Series updated by the timer — demonstrates stale cursor data.
    chart.addSeries("live", new double[] {1, 2, 3, 4, 5}, new double[] {10, 50, 90, 70, 30});

    // Two points at nearly the same X with very different Y values —
    // demonstrates wrong point selection when hit-ellipses overlap.
    chart.addSeries(
        "overlap",
        new double[] {1, 2, 3.0, 3.1, 4, 5},
        new double[] {5, 15, 10, 90, 45, 25});

    return chart;
  }
}
