package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

/**
 * Verifies that a custom Y-axis tick label formatter works correctly with a logarithmic Y-axis.
 *
 * <p>Before the fix, setting a custom formatter disabled logarithmic scaling entirely and the
 * formatter received linearly-spaced values (0, 1e8, 2e8 …) instead of the expected powers of ten
 * (1, 10, 100, … 1e8).
 */
public class TestForIssue834 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(720)
            .height(480)
            .title("Issue 834 – custom formatter + logarithmic Y-axis")
            .xAxisTitle("Count")
            .yAxisTitle("Energy")
            .build();

    chart.getStyler().setYAxisLogarithmic(true);
    chart.setCustomYAxisTickLabelsFormatter(TestForIssue834::formatEnergy);

    double[] xValues = new double[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
    double[] yValues = new double[] {1, 10, 100, 1e3, 1e4, 1e5, 1e6, 1e7, 1e8};
    chart.addSeries("main", xValues, yValues);

    return chart;
  }

  private static String formatEnergy(Double value) {

    if (value < 1e3) {
      return String.format("%.0f nJ", value);
    } else if (value < 1e6) {
      return String.format("%.2f µJ", value / 1e3);
    } else if (value < 1e9) {
      return String.format("%.2f mJ", value / 1e6);
    } else {
      return String.format("%.2f J", value / 1e9);
    }
  }
}
