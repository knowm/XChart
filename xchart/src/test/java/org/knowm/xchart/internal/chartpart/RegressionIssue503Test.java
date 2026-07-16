package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.YAxisPosition;

/**
 * Regression test for issue #503: {@code axisTitlePadding} must open up a gap between the axis title
 * and its tick labels on BOTH the left and the right Y axis. Before the fix the padding widened the
 * left-axis gap but was dumped as dead space on the outer edge of the right-axis title, leaving the
 * right title jammed against its tick numbers no matter how big the padding was.
 *
 * <p>The title text and tick labels are painted in distinct colors so their painted positions can
 * be located directly from the rendered pixels — the axis title bounds origin is pinned to the
 * column's inner edge and does not reflect where the rotated text actually lands.
 */
public class RegressionIssue503Test {

  private static final Color TITLE_COLOR = new Color(255, 0, 0); // red
  private static final Color TICKS_COLOR = new Color(0, 0, 255); // blue

  private static BufferedImage render(int padding) {
    XYChart chart = new XYChartBuilder().width(900).height(500).build();
    chart.getStyler().setAxisTitlePadding(padding);
    chart.getStyler().setYAxisGroupPosition(0, YAxisPosition.Left);
    chart.getStyler().setYAxisGroupPosition(1, YAxisPosition.Right);

    XYSeries left = chart.addSeries("left", List.of(1, 2, 3), List.of(10, 20, 30));
    left.setYAxisGroup(0);
    XYSeries right = chart.addSeries("right", List.of(1, 2, 3), List.of(1000, 2000, 3000));
    right.setYAxisGroup(1);

    // Neutralize series colors so the data lines/markers don't pollute the red/blue pixel search.
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setPlotGridLinesVisible(false);
    for (XYSeries s : new XYSeries[] {left, right}) {
      s.setLineColor(Color.BLACK);
      s.setMarker(org.knowm.xchart.style.markers.SeriesMarkers.NONE);
    }

    chart.setYAxisGroupTitle(0, "LEFT TITLE");
    chart.setYAxisGroupTitle(1, "RIGHT TITLE");

    // Color title text and tick labels distinctly so they can be located in the pixels.
    chart.getStyler().setYAxisGroupTitleColor(0, TITLE_COLOR);
    chart.getStyler().setYAxisGroupTitleColor(1, TITLE_COLOR);
    chart.getStyler().setYAxisGroupTickLabelsColorMap(0, TICKS_COLOR);
    chart.getStyler().setYAxisGroupTickLabelsColorMap(1, TICKS_COLOR);

    return BitmapEncoder.getBufferedImage(chart);
  }

  /** Smallest x containing a pixel of the given color, or -1 if none. */
  private static int minX(BufferedImage img, Color c) {
    for (int x = 0; x < img.getWidth(); x++) {
      if (columnHasColor(img, x, c)) {
        return x;
      }
    }
    return -1;
  }

  /** Largest x containing a pixel of the given color, or -1 if none. */
  private static int maxX(BufferedImage img, Color c) {
    for (int x = img.getWidth() - 1; x >= 0; x--) {
      if (columnHasColor(img, x, c)) {
        return x;
      }
    }
    return -1;
  }

  private static boolean columnHasColor(BufferedImage img, int x, Color c) {
    for (int y = 0; y < img.getHeight(); y++) {
      if (isColor(img.getRGB(x, y), c)) {
        return true;
      }
    }
    return false;
  }

  private static boolean isColor(int rgb, Color c) {
    int r = (rgb >> 16) & 0xFF;
    int g = (rgb >> 8) & 0xFF;
    int b = rgb & 0xFF;
    // Antialiasing blends toward the background, so match on the dominant channel.
    if (c.equals(TITLE_COLOR)) {
      return r > 120 && g < 100 && b < 100;
    }
    return b > 120 && r < 100 && g < 100;
  }

  /**
   * On the right side (title is outboard of the tick labels) the gap is the distance from the tick
   * labels' right edge to the title text's left edge.
   */
  private static int rightGap(BufferedImage img) {
    int ticksRight = maxX(img, TICKS_COLOR);
    // Title text left edge = smallest title x that is to the right of the tick labels.
    for (int x = ticksRight + 1; x < img.getWidth(); x++) {
      if (columnHasColor(img, x, TITLE_COLOR)) {
        return x - ticksRight;
      }
    }
    throw new AssertionError("right axis title text not found");
  }

  /**
   * On the left side (title is outboard of the tick labels, i.e. to their left) the gap is the
   * distance from the title text's right edge to the tick labels' left edge.
   */
  private static int leftGap(BufferedImage img) {
    int ticksLeft = minX(img, TICKS_COLOR);
    for (int x = ticksLeft - 1; x >= 0; x--) {
      if (columnHasColor(img, x, TITLE_COLOR)) {
        return ticksLeft - x;
      }
    }
    throw new AssertionError("left axis title text not found");
  }

  @Test
  public void rightAxisTitleGapScalesWithPadding() {
    int smallGap = rightGap(render(5));
    int largeGap = rightGap(render(105));
    // The gap must grow by ~100 (the padding delta); before the fix it stayed fixed.
    assertThat((double) (largeGap - smallGap)).isCloseTo(100.0, Offset.offset(3.0));
  }

  @Test
  public void leftAxisTitleGapStillScalesWithPadding() {
    int smallGap = leftGap(render(5));
    int largeGap = leftGap(render(105));
    assertThat((double) (largeGap - smallGap)).isCloseTo(100.0, Offset.offset(3.0));
  }
}
