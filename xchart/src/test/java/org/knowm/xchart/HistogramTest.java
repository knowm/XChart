package org.knowm.xchart;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/** @author timmolter */
public class HistogramTest {

  @Test
  public void test1() {

    Histogram histogram = new Histogram(Arrays.asList(1, 2, 3, 4, 5, 6), 2, 0, 4);

    assertThat(histogram.getMax()).isEqualTo(4.0);
    assertThat(histogram.getMin()).isEqualTo(0.0);
    assertThat(histogram.getNumBins()).isEqualTo(2);
    assertThat(histogram.getyAxisData().get(0) + histogram.getyAxisData().get(1)).isEqualTo(4);

    // Chart chart = new ChartBuilder().chartType(ChartType.Bar).width(800).height(600).build();
    // chart.addSeries("histogram 1", histogram.getxAxisData(), histogram.getyAxisData());
    // new SwingWrapper(chart).displayChart();
  }

  @Test
  public void testNegetiveValues() {

    Histogram histogram = new Histogram(Arrays.asList(-1, -2, -3, -4, -5, -6), 3);

    assertThat(histogram.getMax()).isEqualTo(-1);
    assertThat(histogram.getMin()).isEqualTo(-6);
    assertThat(histogram.getNumBins()).isEqualTo(3);
  }
}
