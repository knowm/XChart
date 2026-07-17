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
 * and got truncated. Entries now wrap onto additional rows, keeping the legend box within the image
 * width.
 */
public class RegressionTestIssue577 {

  private static final int CHART_WIDTH = 800;

  @Test
  public void manySeriesHorizontalLegendWrapsAndStaysWithinImageWidth() {

    XYChart wrapped = horizontalLegendChart(12);
    // Rendering forces the layout pass that computes the legend bounds.
    BitmapEncoder.getBufferedImage(wrapped);

    // getLegend() is package-private in Chart and not inherited by XYChart (different package), so
    // reach it through the Chart type.
    double wrappedWidth = ((Chart<?, ?>) wrapped).getLegend().getBounds().getWidth();
    double wrappedHeight = ((Chart<?, ?>) wrapped).getLegend().getBounds().getHeight();

    // (1) The legend box must fit within the image. Pre-fix, its width was the sum of every entry's
    // width and far exceeded the chart width.
    assertThat(wrappedWidth)
        .as("wrapped horizontal legend must not be wider than the image")
        .isLessThanOrEqualTo((double) CHART_WIDTH);

    // (2) The entries must have wrapped onto more than one row, so the box is taller than a single
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
        new XYChartBuilder().width(CHART_WIDTH).height(600).title("issue 577").build();
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    for (int i = 0; i < seriesCount; i++) {
      chart.addSeries(
          "A Fairly Long Series Name Number " + i,
          new double[] {0.0, 1.0},
          new double[] {i, i + 1.0});
    }
    return chart;
  }
}
