/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

/**
 * @author timmolter
 */
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
