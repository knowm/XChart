package org.knowm.xchart.internal.chartpart;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.OHLCChart;
import org.knowm.xchart.OHLCChartBuilder;
import org.knowm.xchart.OHLCSeries;

// Issue #992: the x-axis range spanned exactly [dataMin, dataMax], so the first and last candles
// were centered on the plot's left/right boundaries and roughly half of each edge candle body was
// clipped. The axis range is now padded by half the candle spacing per side so edge candles render
// fully inside the plot. A chart with few, wide candles makes the clipping obvious.
//
// Renders to an off-screen image (no XChartPanel / Swing display) so it runs headless on CI.
class OhlcEdgeCandlePaddingTest {

  @Test
  void edgeCandlesAreNotClippedAtPlotBoundaries() throws Exception {

    OHLCChart chart = new OHLCChartBuilder().width(500).height(400).build();
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setPlotGridLinesVisible(false);
    chart.getStyler().setPlotBackgroundColor(Color.YELLOW);
    chart.getStyler().setPlotBorderVisible(false);

    double[] x = {1, 2, 3, 4};
    double[] open = {10, 12, 11, 13};
    double[] high = {13, 14, 13.5, 15};
    double[] low = {9, 10.5, 10, 12};
    double[] close = {12, 11, 13, 14};
    OHLCSeries series = chart.addSeries("s", x, open, high, low, close);
    // one distinctive candle color regardless of up/down so the scan below is simple
    series.setUpColor(Color.MAGENTA);
    series.setDownColor(Color.MAGENTA);

    BufferedImage img = BitmapEncoder.getBufferedImage(chart);

    // locate the yellow plot area
    int yellow = Color.YELLOW.getRGB();
    int minX = Integer.MAX_VALUE, maxX = -1, minY = Integer.MAX_VALUE, maxY = -1;
    for (int py = 0; py < img.getHeight(); py++) {
      for (int px = 0; px < img.getWidth(); px++) {
        if (img.getRGB(px, py) == yellow) {
          minX = Math.min(minX, px);
          maxX = Math.max(maxX, px);
          minY = Math.min(minY, py);
          maxY = Math.max(maxY, py);
        }
      }
    }
    assertThat(maxX).as("plot area must be found").isGreaterThan(minX);

    // count candle-colored pixels per column band
    int magenta = Color.MAGENTA.getRGB();
    int inEdgeColumns = 0;
    int anywhere = 0;
    for (int py = minY; py <= maxY; py++) {
      for (int px = minX; px <= maxX; px++) {
        if (img.getRGB(px, py) == magenta) {
          anywhere++;
          if (px <= minX + 1 || px >= maxX - 1) {
            inEdgeColumns++;
          }
        }
      }
    }

    assertThat(anywhere).as("candles must be painted").isPositive();
    // Without the axis padding the first/last candle bodies are cut off by the plot boundary, so
    // candle pixels sit directly in the outermost plot columns.
    assertThat(inEdgeColumns)
        .as("no candle pixels may touch the plot's left/right boundary columns")
        .isZero();
  }
}
