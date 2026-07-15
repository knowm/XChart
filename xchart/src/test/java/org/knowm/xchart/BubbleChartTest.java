package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.awt.image.BufferedImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BubbleChartTest {

  private BubbleChart chart;

  @BeforeEach
  void setUp() {

    chart = new BubbleChartBuilder().title("test").xAxisTitle("X").yAxisTitle("Y").build();
  }

  // https://github.com/knowm/XChart/issues/545
  @Test
  void customToolTipsDefaultOff() {

    BubbleSeries series =
        chart.addSeries("s", new double[] {1, 2}, new double[] {3, 4}, new double[] {5, 6});

    assertThat(series.isCustomToolTips()).isFalse();
    assertThat(series.getToolTips()).isNull();
  }

  // https://github.com/knowm/XChart/issues/545
  @Test
  void customToolTipsRoundTrip() {

    BubbleSeries series =
        chart.addSeries("s", new double[] {1, 2}, new double[] {3, 4}, new double[] {5, 6});

    String[] toolTips = {"2% (279/1298)", "4% (346/843)"};
    series.setCustomToolTips(true);
    series.setToolTips(toolTips);

    assertThat(series.isCustomToolTips()).isTrue();
    assertThat(series.getToolTips()).containsExactly("2% (279/1298)", "4% (346/843)");
  }

  // https://github.com/knowm/XChart/issues/545
  @Test
  void renderWithCustomToolTipsDoesNotThrow() {

    BubbleSeries series =
        chart.addSeries("s", new double[] {1, 2}, new double[] {3, 4}, new double[] {5, 6});
    series.setCustomToolTips(true);
    series.setToolTips(new String[] {"2% (279/1298)", "4% (346/843)"});
    chart.getStyler().setToolTipsAlwaysVisible(true);

    BufferedImage image = assertDoesNotThrow(() -> BitmapEncoder.getBufferedImage(chart));
    assertThat(image).isNotNull();
  }

  // https://github.com/knowm/XChart/issues/545
  @Test
  void renderWithPartialCustomToolTipsFallsBackWithoutThrowing() {

    // fewer tooltip entries than data points, plus a null entry -> falls back to axis values
    BubbleSeries series =
        chart.addSeries("s", new double[] {1, 2, 3}, new double[] {3, 4, 5}, new double[] {5, 6, 7});
    series.setCustomToolTips(true);
    series.setToolTips(new String[] {null});
    chart.getStyler().setToolTipsAlwaysVisible(true);

    assertDoesNotThrow(() -> BitmapEncoder.getBufferedImage(chart));
  }
}
