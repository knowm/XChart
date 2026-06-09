package org.knowm.xchart.standalone.issues;

import java.util.HashMap;
import java.util.Map;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

/**
 * Reproducer for GitHub issue #799: BitmapEncoder hang / crash when a custom Y-axis tick label
 * formatter returns empty strings for unmapped values.
 *
 * <p>The formatter maps only a subset of Y values to human-readable labels and returns {@code ""}
 * for everything else. Before the fix this caused either an infinite loop in the tick calculator
 * (older code) or an {@code IllegalArgumentException} from {@code TextLayout} (newer code). Both
 * are now resolved: empty labels are silently skipped during rendering, just like {@code null}.
 */
public class TestForIssue799 {

  public static void main(String[] args) {
    new SwingWrapper<>(getChart()).displayChart();
  }

  /** Constructs and returns the chart without launching a window (headless-safe). */
  public static XYChart getChart() {
    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Issue 799 – Custom Y-axis formatter with empty labels")
            .xAxisTitle("X")
            .yAxisTitle("Y (scaled)")
            .build();

    double[] xData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    double[] yData = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
    XYSeries series = chart.addSeries("data", xData, yData);
    series.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

    // Only a few Y values have a mapped label; the rest intentionally return ""
    Map<Double, Double> yLabelMap = new HashMap<>();
    yLabelMap.put(10.0, 0.01);
    yLabelMap.put(30.0, 0.03);
    yLabelMap.put(50.0, 0.05);
    yLabelMap.put(70.0, 0.07);
    yLabelMap.put(100.0, 0.10);

    chart.setCustomYAxisTickLabelsFormatter(
        yValue -> {
          Double label = yLabelMap.get(yValue);
          return label == null ? "" : label.toString();
        });

    return chart;
  }
}
