package org.knowm.xchart.standalone.issues;

import java.util.Arrays;
import java.util.List;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;

/**
 * Reproducer / how-to for https://github.com/knowm/XChart/issues/884
 *
 * <p>"For a chart I need on the Y-Axis Strings instead of Numbers."
 *
 * <p>XChart does not transpose arbitrary charts, but any axes-based chart can show Strings on the
 * Y-axis by plotting numeric category indices (0, 1, 2, ...) and supplying a custom Y-axis tick
 * label formatter that maps each numeric tick position back to a String. This works for XYChart,
 * line/scatter, etc. — not just bar charts.
 */
public class TestForIssue884 {

  /** The String labels we want to see on the Y-axis instead of numbers. */
  private static final List<String> CATEGORIES =
      Arrays.asList("Apples", "Bananas", "Cherries", "Dates", "Elderberries");

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Strings on the Y-Axis (issue 884)")
            .xAxisTitle("Value")
            .yAxisTitle("Fruit")
            .build();

    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);

    // Y values are simply the category indices: 0..CATEGORIES.size()-1
    double[] xData = {12, 7, 19, 4, 15};
    double[] yData = {0, 1, 2, 3, 4};
    chart.addSeries("counts", xData, yData);

    // The key: map each numeric Y tick position to its String label. Non-integer tick positions
    // (the formatter may be asked about fractional grid lines) are blanked out so only the
    // category rows are labelled.
    chart
        .getStyler()
        .setYAxisTickLabelsFormattingFunction(
            value -> {
              int index = (int) Math.round(value);
              if (Math.abs(value - index) < 1e-9 && index >= 0 && index < CATEGORIES.size()) {
                return CATEGORIES.get(index);
              }
              return "";
            });

    return chart;
  }
}
