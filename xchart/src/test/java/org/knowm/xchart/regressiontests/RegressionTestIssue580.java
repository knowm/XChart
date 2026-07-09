package org.knowm.xchart.regressiontests;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.RadarChart;
import org.knowm.xchart.RadarChartBuilder;
import org.knowm.xchart.RadarSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Regression test for <a href="https://github.com/knowm/XChart/issues/580">issue 580</a>: radar
 * chart radii were laid out counter-clockwise with no way to reverse the direction. The default is
 * now clockwise (the common radar/spider chart convention), toggleable via {@code
 * setCounterClockwise}.
 */
public class RegressionTestIssue580 {

  @Test
  public void defaultIsClockwiseAndSetterRoundTrips() {

    RadarChart chart = new RadarChartBuilder().build();
    assertThat(chart.getStyler().isCounterClockwise()).isFalse();

    chart.getStyler().setCounterClockwise(true);
    assertThat(chart.getStyler().isCounterClockwise()).isTrue();
  }

  @Test
  public void directionFlipsHorizontalBulge() {

    // Index 0 is at the top; index 1 is one step around. Make index 1 the only large radius so the
    // filled polygon clearly bulges toward that vertex. Clockwise puts index 1 on the right half;
    // counter-clockwise puts it on the left half.
    String[] labels = {"1", "2", "3", "4", "5", "6"};
    double[] values = {0.05, 1.0, 0.05, 0.05, 0.05, 0.05};

    int clockwiseRight = rightMinusLeftBlue(render(labels, values, false));
    int counterRight = rightMinusLeftBlue(render(labels, values, true));

    assertThat(clockwiseRight).isGreaterThan(0); // bulges right
    assertThat(counterRight).isLessThan(0); // bulges left
  }

  private static BufferedImage render(String[] labels, double[] values, boolean counterClockwise) {

    RadarChart chart = new RadarChartBuilder().width(500).height(500).build();
    chart.getStyler().setCounterClockwise(counterClockwise);
    chart.getStyler().setLegendVisible(false);
    chart.setRadiiLabels(labels);

    // Pin the series to a strong blue and drop markers so the pixel assertion isolates the
    // clockwise/counter-clockwise winding from theme/color-cycler defaults.
    RadarSeries series = chart.addSeries("s", values);
    series.setLineColor(Color.BLUE);
    series.setFillColor(Color.BLUE);
    series.setMarker(SeriesMarkers.NONE);
    return BitmapEncoder.getBufferedImage(chart);
  }

  /** Positive when the blue series fill sits more to the right of center than the left. */
  private static int rightMinusLeftBlue(BufferedImage img) {

    int cx = img.getWidth() / 2;
    int left = 0;
    int right = 0;
    for (int y = 0; y < img.getHeight(); y++) {
      for (int x = 0; x < img.getWidth(); x++) {
        int rgb = img.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int gg = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        if (b > r + 20 && b > gg + 20) { // blue-dominant series pixel
          if (x < cx) {
            left++;
          } else if (x > cx) {
            right++;
          }
        }
      }
    }
    return right - left;
  }
}
