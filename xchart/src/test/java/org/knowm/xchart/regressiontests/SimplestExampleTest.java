package org.knowm.xchart.regressiontests;

import org.junit.Assert;
import org.junit.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestOutputStream;
import java.security.MessageDigest;

public class SimplestExampleTest {

  static String digestType = "md5";

  @Test
  public void testSimplestExampleStaysTheSame() throws Exception {
    // given
    double[] xData = new double[] {0.0, 1.0, 2.0};
    double[] yData = new double[] {2.0, 1.0, 0.0};

    // when
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
    DigestOutputStream output =
        new DigestOutputStream(new ByteArrayOutputStream(), MessageDigest.getInstance(digestType));
    BitmapEncoder.saveBitmap(chart, output, BitmapEncoder.BitmapFormat.PNG);
    output.close();

    // test
    assertImagesEquals("/simplestExample.png", output);
  }

  public void assertImagesEquals(String expectedFileName, DigestOutputStream actual)
      throws Exception {
    byte[] expectedBytes =
        Files.readAllBytes(Paths.get(getClass().getResource(expectedFileName).toURI()));
    byte[] expectedDigest = MessageDigest.getInstance(digestType).digest(expectedBytes);

    Assert.assertArrayEquals(expectedDigest, actual.getMessageDigest().digest());
  }
}
