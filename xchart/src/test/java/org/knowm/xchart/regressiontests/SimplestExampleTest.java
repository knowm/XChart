package org.knowm.xchart.regressiontests;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import org.junit.Assert;
import org.junit.Test;
import org.knowm.xchart.*;

public class SimplestExampleTest {

  static String digestType = "md5";

  Font font;

  {
    try {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      font =
          Font.createFont(
              Font.TRUETYPE_FONT,
              new File(getClass().getResource("/font/Barlow-Regular.ttf").getFile()));
      ge.registerFont(font);
    } catch (IOException | FontFormatException e) {
      throw new RuntimeException("Failed to load the font file for tests", e);
    }
  }

  @Test
  public void testSimplestExampleStaysTheSame() throws Exception {
    // given
    double[] xData = new double[] {0.0, 1.0, 2.0};
    double[] yData = new double[] {2.0, 1.0, 0.0};

    // when
    XYChart chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xData, yData);
    chart.getStyler().setChartTitleFont(fontToBeUsed());
    chart.getStyler().setAxisTickLabelsFont(fontToBeUsed());
    chart.getStyler().setLegendFont(fontToBeUsed());
    chart.getStyler().setAxisTitleFont(fontToBeUsed());
    DigestOutputStream output =
        new DigestOutputStream(new ByteArrayOutputStream(), MessageDigest.getInstance(digestType));
    BitmapEncoder.saveBitmap(chart, output, BitmapEncoder.BitmapFormat.PNG);
    BitmapEncoder.saveBitmap(chart, "/tmp/simplestExample.png", BitmapEncoder.BitmapFormat.PNG);
    output.close();

    // test
    assertImagesEquals("simplestExample.png", output);
  }

  private Font fontToBeUsed() {
    return new Font("Barlow", Font.PLAIN, 12);
  }

  public void assertImagesEquals(String expectedFileName, DigestOutputStream actual)
      throws Exception {
    String path = "/expectedChartRenderings/" + expectedFileName;
    byte[] expectedBytes = Files.readAllBytes(Paths.get(getClass().getResource(path).toURI()));
    byte[] expectedDigest = MessageDigest.getInstance(digestType).digest(expectedBytes);

    Assert.assertArrayEquals(expectedDigest, actual.getMessageDigest().digest());
  }
}
