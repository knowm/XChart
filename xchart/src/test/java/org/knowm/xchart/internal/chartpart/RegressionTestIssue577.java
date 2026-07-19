package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

/**
 * Regression test for <a href="https://github.com/knowm/XChart/issues/577">issue 577</a>.
 *
 * <p>A horizontal legend used to lay every entry out on a single row. With many (or long-named)
 * series that row grew wider than the image, so the centered OutsideS legend spilled past both edges
 * and got truncated. Entries now wrap onto additional rows, and the whole box is kept within the
 * image, so nothing is truncated.
 */
public class RegressionTestIssue577 {

  private static final int CHART_WIDTH = 800;

  @Test
  public void manySeriesHorizontalLegendWrapsAndStaysWithinImageWidth() {

    XYChart wrapped = horizontalLegendChart(12);
    // Rendering forces the layout pass that computes the legend bounds and position.
    BitmapEncoder.getBufferedImage(wrapped);

    // getLegend() is package-private in Chart and not inherited by XYChart (different package), so
    // reach it through the Chart type.
    Legend_<?, ?> legend = ((Chart<?, ?>) wrapped).getLegend();
    double wrappedWidth = legend.getBounds().getWidth();
    double wrappedHeight = legend.getBounds().getHeight();

    // (1) The legend box must be no wider than the image. Pre-fix, its width was the sum of every
    // entry's width and far exceeded the chart width.
    assertThat(wrappedWidth)
        .as("wrapped horizontal legend must not be wider than the image")
        .isLessThanOrEqualTo((double) CHART_WIDTH);

    // (2) The box must sit fully within the image. The OutsideS legend is centered on the plot,
    // whose center is right of the image center (the left y-axis takes horizontal space), so a wide
    // wrapped legend used to still overflow the right edge until it was clamped into the image.
    assertThat(legend.xOffset)
        .as("legend box must not extend past the left image edge")
        .isGreaterThanOrEqualTo(0.0);
    assertThat(legend.xOffset + wrappedWidth)
        .as("legend box must not extend past the right image edge")
        .isLessThanOrEqualTo((double) CHART_WIDTH);

    // (3) The entries must have wrapped onto more than one row, so the box is taller than a single
    // row. Compare against an otherwise-identical single-series (single-row) legend.
    XYChart singleRow = horizontalLegendChart(1);
    BitmapEncoder.getBufferedImage(singleRow);
    double singleRowHeight = ((Chart<?, ?>) singleRow).getLegend().getBounds().getHeight();

    assertThat(wrappedHeight)
        .as("wrapped horizontal legend must be taller than a single row")
        .isGreaterThan(singleRowHeight);
  }

  private static XYChart horizontalLegendChart(int seriesCount) {

    XYChart chart =
        new XYChartBuilder().width(CHART_WIDTH).height(600).title("issue 577").yAxisTitle("Y").build();
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
    // Wide y-axis tick labels enlarge the left margin, pushing the plot center well right of the
    // image center. That is what let the centered legend overflow the right edge pre-fix (here by
    // ~40px), so assertion (2) meaningfully exercises the clamp.
    chart.getStyler().setYAxisDecimalPattern("###,###,###,##0.0000");

    for (int i = 0; i < seriesCount; i++) {
      chart.addSeries(
          "A Fairly Long Series Name Number " + i,
          new double[] {0.0, 1.0},
          new double[] {i * 123456789.0, (i + 1) * 123456789.0});
    }
    return chart;
  }
}
