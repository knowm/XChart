package org.knowm.xchart;

import java.io.ByteArrayOutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import org.junit.jupiter.api.Disabled;

public class XYChartTest {
  private static final String digestType = "md5";

  // https://github.com/knowm/XChart/issues/799
  @Disabled // because the issue is not fixed yet
  public void issue799() throws Exception {
    // given
    double[] xData = new double[] {0.0, 1.0, 2.0};
    double[] yData = new double[] {2.0, 1.0, 0.0};
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
    chart.getStyler().setyAxisTickLabelsFormattingFunction(yValue -> "1");

    // when
    DigestOutputStream output =
        new DigestOutputStream(new ByteArrayOutputStream(), MessageDigest.getInstance(digestType));
    BitmapEncoder.saveBitmap(chart, output, BitmapEncoder.BitmapFormat.PNG);
    output.close();

    // test
    // finishes
  }
}
