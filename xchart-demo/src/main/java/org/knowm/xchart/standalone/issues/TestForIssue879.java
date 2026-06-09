package org.knowm.xchart.standalone.issues;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

/**
 * Demonstrates memory-efficient primitive-array usage for CategoryChart (issue #879).
 *
 * <p>The root cause of the perceived high memory usage in issue #879 was two-fold:
 *
 * <ol>
 *   <li>JDK 8's Parallel GC is less aggressive at reclaiming heap than JDK 21's G1GC. Adding
 *       {@code -XX:+UseG1GC} on JDK 8 restores comparable behaviour. This is not an XChart bug.
 *   <li>Passing {@code double[]} / {@code int[]} to {@link CategoryChart#addSeries} previously
 *       boxed every value into a {@code List<Double>}, consuming ~3× more memory per element (24
 *       bytes vs 8 bytes for a primitive double). This has been fixed: the data is now stored
 *       internally as {@code double[]}, eliminating the boxing overhead on both the add and update
 *       paths.
 * </ol>
 */
public class TestForIssue879 {

  public static void main(String[] args) {

    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static CategoryChart getChart() {

    int numPoints = 18_000;
    double[] xData = new double[numPoints];
    double[] yData = new double[numPoints];
    for (int i = 0; i < numPoints; i++) {
      xData[i] = i;
      yData[i] = Math.sin(i * 0.001) * 100;
    }

    CategoryChart chart =
        new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title("Issue #879 — CategoryChart with 18 000 points (double[] path, no boxing)")
            .xAxisTitle("Index")
            .yAxisTitle("Value")
            .theme(Styler.ChartTheme.GGPlot2)
            .build();

    // double[] passed directly — stored as primitive double[] internally, no boxing
    chart.addSeries("series1", xData, yData);

    return chart;
  }
}
