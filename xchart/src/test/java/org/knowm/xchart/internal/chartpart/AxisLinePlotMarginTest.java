package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

// The axis tick lines that hug the plot area must sit at the same distance (plotMargin) from the
// plot on both the y-axis and x-axis sides. Historically the y-axis line was positioned from the
// painted tick label width while the plot edge was positioned from the width *hint*; when the two
// measurements diverged (e.g. advance width vs ink width after issue #618), the y-axis line
// drifted toward the plot and the gaps went visibly asymmetric.
//
// Renders to an off-screen image (no XChartPanel / Swing display) so it runs headless on CI.
class AxisLinePlotMarginTest {

  @Test
  void axisLinesSitAtPlotMarginOnBothSides() throws Exception {

    XYChart chart = new XYChartBuilder().width(800).height(600).title("t").build();
    chart.addSeries("s", new double[] {1, 2, 3}, new double[] {10, 20, 30});
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setPlotGridLinesVisible(false);
    chart.getStyler().setPlotBackgroundColor(Color.YELLOW);
    chart.getStyler().setPlotBorderVisible(true);
    chart.getStyler().setPlotBorderColor(Color.RED);

    BufferedImage img = BitmapEncoder.getBufferedImage(chart);

    // locate the yellow plot area
    int yellow = Color.YELLOW.getRGB();
    int minX = Integer.MAX_VALUE, maxX = -1, minY = Integer.MAX_VALUE, maxY = -1;
    for (int y = 0; y < img.getHeight(); y++) {
      for (int x = 0; x < img.getWidth(); x++) {
        if (img.getRGB(x, y) == yellow) {
          minX = Math.min(minX, x);
          maxX = Math.max(maxX, x);
          minY = Math.min(minY, y);
          maxY = Math.max(maxY, y);
        }
      }
    }
    assertThat(maxX).as("plot area must be found").isGreaterThan(minX);

    // walk left from the plot border to the y-axis line (first non-background pixel)
    int borderLeft = minX - 1;
    int background = img.getRGB(0, 0);
    int midY = (minY + maxY) / 2;
    int yAxisLineX = -1;
    for (int x = borderLeft - 1; x > borderLeft - 20; x--) {
      if (img.getRGB(x, midY) != background) {
        yAxisLineX = x;
        break;
      }
    }
    assertThat(yAxisLineX).as("y-axis line must be found left of the plot").isPositive();

    // walk down from the plot border to the x-axis line; use a column 1/3 into the plot to make a
    // tick mark collision unlikely (tick marks are only a few px wide, and if one were hit it
    // would not change the first dark pixel: marks start at the line and extend away from it)
    int borderBottom = maxY + 1;
    int midX = minX + (maxX - minX) / 3;
    int xAxisLineY = -1;
    for (int y = borderBottom + 1; y < borderBottom + 20; y++) {
      if (img.getRGB(midX, y) != background) {
        xAxisLineY = y;
        break;
      }
    }
    assertThat(xAxisLineY).as("x-axis line must be found below the plot").isPositive();

    int leftGap = borderLeft - yAxisLineX;
    int bottomGap = xAxisLineY - borderBottom;
    assertThat(leftGap)
        .as("y-axis line and x-axis line must sit at the same distance from the plot area")
        .isEqualTo(bottomGap);
    assertThat(leftGap)
        .as("axis line distance must equal the styler's plotMargin")
        .isEqualTo(chart.getStyler().getPlotMargin());
  }
}
