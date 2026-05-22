package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/**
 * Demonstrates the fix for issue #593 — cursor dataPointList memory leak in live XYCharts.
 *
 * <p>Before the fix: each repaint appended new DataPoints to the cursor's list without ever
 * clearing it, causing a memory leak and stale/wrong tooltip values on live charts.
 *
 * <p>After the fix: {@code PlotContent_XY.doPaint()} calls {@code cursor.clearDataPoints()} at
 * the start of each paint cycle, so only the current frame's points are present.
 *
 * <p>To observe: enable the cursor, hover over the chart while it repaints, and confirm the
 * tooltip always reflects the current data rather than accumulating old entries.
 */
public class TestForIssue593 {

  public static void main(String[] args) throws InterruptedException {

    XYChart chart = getChart();
    SwingWrapper<XYChart> sw = new SwingWrapper<>(chart);
    sw.displayChart();
    sw.getXChartPanel().setCursorEnabled(true);

    // Simulate a live chart updating every 500 ms
    List<Double> xData = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
    for (int i = 0; i < 20; i++) {
      Thread.sleep(500);
      double offset = i;
      List<Double> yData =
          Arrays.asList(
              offset + 1, offset + 3, offset + 2, offset + 5, offset + 4);
      chart.updateXYSeries("series", xData, yData, null);
      sw.repaintChart();
    }
  }

  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(700)
            .height(500)
            .title("Issue #593 — Cursor memory leak fix")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.addSeries("series", new double[]{1, 2, 3, 4, 5}, new double[]{1, 3, 2, 5, 4});

    return chart;
  }
}
