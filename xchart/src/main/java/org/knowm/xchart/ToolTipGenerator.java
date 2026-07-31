package org.knowm.xchart;

import org.knowm.xchart.internal.series.Series;

/**
 * Generates custom tooltip text for a series' data points, replacing the default label built from
 * the formatted axis values. Set one on any series via {@link
 * Series#setToolTipGenerator(ToolTipGenerator)}:
 *
 * <pre>
 * chart.getHeatMapSeries()
 *     .setToolTipGenerator(
 *         dataPoint -&gt; "extra info for cell #" + dataPoint.getDataPointIndex());
 * </pre>
 *
 * <p>The generator is invoked once per rendered data point each time the chart is painted. The
 * {@link ChartDataPoint} argument carries the series name, the data point's index within the
 * series' data (so raw values can be looked up in the data structures the series was built from),
 * the default formatted label or x/y values, and the data point's screen geometry.
 *
 * <p>Multi-line tooltips are supported: separate lines with {@link System#lineSeparator()}.
 */
@FunctionalInterface
public interface ToolTipGenerator {

  /**
   * Generate the tooltip text for one data point.
   *
   * @param dataPoint the rendered data point the tooltip belongs to
   * @return the tooltip text, or {@code null} to fall back to the default label
   */
  String generateToolTip(ChartDataPoint dataPoint);
}
