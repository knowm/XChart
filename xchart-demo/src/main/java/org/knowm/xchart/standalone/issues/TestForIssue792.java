package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * Demonstrates the fix for issue #792 — {@code setxAxisTickLabelsFormattingFunction} dropping
 * tick labels when the formatting function intentionally returns duplicate strings.
 *
 * <p>Before the fix: the internal {@code do-while} loop in {@code AxisTickCalculator_} kept
 * widening the grid step until all tick labels were unique. When the custom formatter returned
 * {@code " "} for odd x-values, this uniqueness check collapsed most ticks and only two remained.
 *
 * <p>After the fix: {@code AxisTickCalculator_Callback} overrides {@code areAllTickLabelsUnique}
 * to always return {@code true}, so the grid step is never widened and every tick position
 * produced by the calculator is preserved.
 *
 * <p>Expected result: x-axis shows labels 0, 2, 4, 6 with tick marks also present at 1, 3, 5, 7
 * (those positions show a blank label rather than being removed).
 */
public class TestForIssue792 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  public static XYChart getChart() {

    double[] yData = new double[] {100, 90, 90, 89, 80, 101, 102, 99};

    XYChart chart = QuickChart.getChart("Issue #792 — custom tick label formatter", "X", "Y", "y(x)", null, yData);

    // Hide every odd x tick label — previously this caused all but two ticks to disappear.
    chart
        .getStyler()
        .setxAxisTickLabelsFormattingFunction(
            x -> x.intValue() % 2 == 0 ? String.valueOf(x.intValue()) : " ");

    return chart;
  }
}
