package org.knowm.xchart;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.geom.Rectangle2D;
import org.junit.jupiter.api.Test;

// https://github.com/knowm/XChart/issues/492
class DataPointListenerTest {

  @Test
  void chartDataPointEqualityIgnoresShape() {

    ChartDataPoint a =
        new ChartDataPoint("s", 2, null, "x", "y", 10, 20, new Rectangle2D.Double(0, 0, 5, 5));
    ChartDataPoint b =
        new ChartDataPoint("s", 2, null, "x", "y", 10, 20, new Rectangle2D.Double(9, 9, 1, 1));
    ChartDataPoint different =
        new ChartDataPoint("s", 3, null, "x", "y", 10, 20, new Rectangle2D.Double(0, 0, 5, 5));

    assertThat(a).isEqualTo(b);
    assertThat(a.hashCode()).isEqualTo(b.hashCode());
    assertThat(a).isNotEqualTo(different);
  }
}
