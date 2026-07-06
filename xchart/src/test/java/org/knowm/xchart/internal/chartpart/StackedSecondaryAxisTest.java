package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.style.Styler;

/**
 * Regression tests for issue #906: stacked bars on one Y-axis group plus a non-stacked Line series
 * on a secondary Y-axis group. Each axis group must range from only its own series — the line must
 * neither participate in the bar stack nor inflate either axis.
 */
public class StackedSecondaryAxisTest {

  private static final List<String> X = List.of("A", "B", "C", "D");

  /** Stacked bars on group 0, a Line on group 1. Mirrors the demo BarChart13 / the issue report. */
  private static CategoryChart buildStackedBarsPlusSecondaryLine() {
    CategoryChart chart =
        new CategoryChartBuilder().width(800).height(600).title("issue906").build();
    chart.getStyler().setStacked(true);
    chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);

    // Stacked bars on the primary (left) group. Per-category sums: A=23, B=47, C=43, D=55.
    chart.addSeries("Series A", X, List.of(10, 20, 15, 25));
    chart.addSeries("Series B", X, List.of(5, 15, 10, 20));
    chart.addSeries("Series C", X, List.of(8, 12, 18, 10));

    // Line on the secondary (right) group. Raw values 100..180, must not be stacked.
    CategorySeries line = chart.addSeries("Trend Line", X, List.of(100, 150, 130, 180));
    line.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    line.setYAxisGroup(1);
    return chart;
  }

  @Test
  public void primaryAxisRangesToStackedBarSumsOnly() throws Exception {
    CategoryChart chart = buildStackedBarsPlusSecondaryLine();
    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    Axis_Y<?, ?> left = chart.axisPair.getYAxis(0);
    // Bars are positive, so the axis anchors at 0 and tops out at the largest stack sum (D = 55).
    assertThat(left.getMin()).isEqualTo(0.0);
    assertThat(left.getMax()).isEqualTo(55.0);
  }

  @Test
  public void secondaryAxisRangesToLineDataOnly() throws Exception {
    CategoryChart chart = buildStackedBarsPlusSecondaryLine();
    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    Axis_Y<?, ?> right = chart.axisPair.getYAxis(1);
    // The line is not stackable, so its group tops out at the raw line max (180) — it is NOT
    // inflated by the bar stack (the pre-fix bug summed every series into every group, pushing this
    // to 55 + 180 = 235). The min is 0 because the chart's default render style is Bar, which
    // zero-anchors all groups.
    assertThat(right.getMin()).isEqualTo(0.0);
    assertThat(right.getMax()).isEqualTo(180.0);
  }

  /** A group containing only a non-stackable Line keeps its raw range even with stacking enabled. */
  @Test
  public void lineOnlyGroupIsNotStacked() throws Exception {
    CategoryChart chart =
        new CategoryChartBuilder().width(800).height(600).title("issue906-lineonly").build();
    chart.getStyler().setStacked(true);

    CategorySeries l1 = chart.addSeries("L1", X, List.of(10, 20, 30, 40));
    l1.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);
    CategorySeries l2 = chart.addSeries("L2", X, List.of(5, 5, 5, 5));
    l2.setChartCategorySeriesRenderStyle(CategorySeriesRenderStyle.Line);

    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    Axis_Y<?, ?> axis = chart.axisPair.getYAxis(0);
    // If the lines were stacked the max would be 40 + 5 = 45; they are not, so it stays at 40.
    assertThat(axis.getMax()).isEqualTo(40.0);
  }

  /** Sanity: with only stacked bars, the single-group range still equals the stack sum. */
  @Test
  public void singleGroupStackedBarsUnchanged() throws Exception {
    CategoryChart chart =
        new CategoryChartBuilder().width(800).height(600).title("issue906-barsonly").build();
    chart.getStyler().setStacked(true);
    chart.addSeries("Series A", X, List.of(10, 20, 15, 25));
    chart.addSeries("Series B", X, List.of(5, 15, 10, 20));
    chart.addSeries("Series C", X, List.of(8, 12, 18, 10));

    BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

    Axis_Y<?, ?> axis = chart.axisPair.getYAxis(0);
    assertThat(axis.getMin()).isEqualTo(0.0);
    assertThat(axis.getMax()).isEqualTo(55.0);
  }
}
