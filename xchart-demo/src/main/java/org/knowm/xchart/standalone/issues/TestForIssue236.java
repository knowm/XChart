package org.knowm.xchart.standalone.issues;

import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.internal.chartpart.Chart;

/**
 * Issue #236: "Collate y axis values for same x axis values in Category chart"
 *
 * <p>Reproduces the reported behavior: when a CategoryChart is given non-unique x values (here x = 2
 * appears twice), each data point occupies its own ordinal slot, so the duplicate x value renders as
 * two separate, adjacent tick marks both labeled "2" rather than collating both y values onto a
 * single "2" tick.
 *
 * <p>This is by design: CategoryChart positions every data point by its index, not by its x value
 * (see PlotContent_Category / AxisTickCalculator_Category). The requested "collate same-x points on
 * one tick" behavior is exactly what XYChart provides, since it plots at the actual numeric x
 * coordinate. The two charts below are shown side by side so the difference is visible:
 *
 * <ul>
 *   <li>LEFT (CategoryChart): x = {1, 2, 2, 3} -&gt; four slots, "2" appears twice (the issue).
 *   <li>RIGHT (XYChart, Scatter): same data, but the two x = 2 points land on the same vertical
 *       (the "collated" look the reporter wants).
 * </ul>
 */
public class TestForIssue236 {

  private static final int WIDTH = 600;
  private static final int HEIGHT = 500;

  // Non-unique x values: x = 2 occurs twice, with two different y values.
  private static final List<Number> X_DATA = List.of(1, 2, 2, 3);
  private static final List<Number> Y_DATA = List.of(4, 6, 9, 5);

  public static void main(String[] args) {

    List<Chart<?, ?>> charts = new ArrayList<>();
    charts.add(getCategoryChart());
    charts.add(getXYChart());
    new SwingWrapper<>(charts).displayChartMatrix();
  }

  /** The reported problem: duplicate x = 2 renders as two separate "2" ticks. */
  private static CategoryChart getCategoryChart() {

    CategoryChart chart =
        new CategoryChartBuilder()
            .width(WIDTH)
            .height(HEIGHT)
            .title("CategoryChart: x=2 NOT collated (issue #236)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Scatter);
    chart.getStyler().setMarkerSize(12);

    chart.addSeries("points", X_DATA, Y_DATA);
    return chart;
  }

  /** The workaround / intended tool: XYChart plots by numeric x, so both x = 2 points collate. */
  private static XYChart getXYChart() {

    XYChart chart =
        new XYChartBuilder()
            .width(WIDTH)
            .height(HEIGHT)
            .title("XYChart: x=2 collated (use this instead)")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
    chart.getStyler().setMarkerSize(12);

    chart.addSeries("points", X_DATA, Y_DATA);
    return chart;
  }

  public static CategoryChart getChart() {

    return getCategoryChart();
  }
}
