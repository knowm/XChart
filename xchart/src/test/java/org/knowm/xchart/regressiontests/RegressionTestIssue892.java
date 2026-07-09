package org.knowm.xchart.regressiontests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Regression test for <a href="https://github.com/knowm/XChart/issues/892">issue 892</a>.
 *
 * <p>In a horizontal legend mixing render styles (Bar + Line), each entry used to be vertically
 * centered against its own graphic height, so the Bar's 20px box and the Line's smaller
 * marker/line ended up on different baselines. This asserts that the Bar box and the Line marker
 * share (approximately) the same vertical center in the rendered legend.
 */
public class RegressionTestIssue892 {

  private static final Color BAR_FILL = new Color(35, 127, 211);
  private static final Color LINE_COLOR = new Color(237, 133, 53);

  @Test
  public void mixedStyleHorizontalLegendGraphicsAreVerticallyAligned() {

    CategoryChart chart =
        new CategoryChartBuilder().width(800).height(600).title("issue 892").build();
    chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
    chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);

    CategorySeries bar =
        chart.addSeries("Bar Series", Arrays.asList("A", "B", "C"), Arrays.asList(20, 40, 55));
    bar.setChartCategorySeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Bar);
    bar.setFillColor(BAR_FILL);

    CategorySeries line =
        chart.addSeries("Line Series", Arrays.asList("A", "B", "C"), Arrays.asList(15, 30, 45));
    line.setChartCategorySeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);
    line.setLineColor(LINE_COLOR);
    line.setLineWidth(2.5f);
    line.setMarker(SeriesMarkers.CIRCLE);

    BufferedImage image = BitmapEncoder.getBufferedImage(chart);

    // The OutsideS legend sits at the very bottom. Its background matches the (white) plot
    // background higher up, so locate the legend container as the bottom-most contiguous block of
    // background-colored rows (below it is only the chart-background margin). Then measure the
    // entries strictly within its rows, so plot content can never leak into the measurement.
    int[] containerRows = bottomMostBand(image, chart.getStyler().getLegendBackgroundColor());
    int[] barRows = verticalExtent(image, BAR_FILL, containerRows[0]);
    int[] lineRows = verticalExtent(image, LINE_COLOR, containerRows[0]);
    double barCenter = (barRows[0] + barRows[1]) / 2.0;
    double lineCenter = (lineRows[0] + lineRows[1]) / 2.0;

    // (1) the entries must align to each other. Pre-fix the two centers were ~6px apart.
    double diff = Math.abs(barCenter - lineCenter);
    assertTrue(
        diff <= 2.0,
        "Bar box center ("
            + barCenter
            + ") and line marker center ("
            + lineCenter
            + ") should be vertically aligned in a horizontal legend, but differ by "
            + diff
            + "px");

    // (2) the entries as a group must be vertically centered inside the legend container. Pre-fix
    // the container was sized to the last series' height, so the taller box sat too low.
    double containerCenter = (containerRows[0] + containerRows[1]) / 2.0;
    double contentCenter =
        (Math.min(barRows[0], lineRows[0]) + Math.max(barRows[1], lineRows[1])) / 2.0;
    double offCenter = Math.abs(contentCenter - containerCenter);
    assertTrue(
        offCenter <= 2.0,
        "Legend content center ("
            + contentCenter
            + ") should sit at the legend container center ("
            + containerCenter
            + "), but is off by "
            + offCenter
            + "px");
  }

  /** {top row, bottom row} of the bottom-most contiguous block of rows containing {@code bg}. */
  private static int[] bottomMostBand(BufferedImage image, Color bg) {

    int bottom = -1;
    int top = -1;
    for (int y = image.getHeight() - 1; y >= 0; y--) {
      boolean rowHasBg = false;
      for (int x = 0; x < image.getWidth(); x++) {
        if (isClose(new Color(image.getRGB(x, y)), bg)) {
          rowHasBg = true;
          break;
        }
      }
      if (rowHasBg) {
        if (bottom == -1) {
          bottom = y;
        }
        top = y;
      } else if (bottom != -1) {
        break; // reached the gap above the bottom-most block
      }
    }
    if (bottom == -1) {
      throw new AssertionError("legend background color " + bg + " not found");
    }
    return new int[] {top, bottom};
  }

  /** {min row, max row} of pixels matching {@code target} at or below {@code y0}. */
  private static int[] verticalExtent(BufferedImage image, Color target, int y0) {

    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (int y = Math.max(0, y0); y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        if (isClose(new Color(image.getRGB(x, y)), target)) {
          minY = Math.min(minY, y);
          maxY = Math.max(maxY, y);
          break;
        }
      }
    }
    if (minY == Integer.MAX_VALUE) {
      throw new AssertionError("color " + target + " not found in legend band");
    }
    return new int[] {minY, maxY};
  }

  private static boolean isClose(Color a, Color b) {
    int tol = 25;
    return Math.abs(a.getRed() - b.getRed()) < tol
        && Math.abs(a.getGreen() - b.getGreen()) < tol
        && Math.abs(a.getBlue() - b.getBlue()) < tol;
  }
}
