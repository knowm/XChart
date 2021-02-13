package org.knowm.xchart.regressiontests;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.Assert;
import org.junit.Test;
import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;

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

    // test
    assertImagesEquals("simplestExample.png", chart);
  }

  private Font fontToBeUsed() {
    return new Font("Barlow", Font.PLAIN, 12);
  }

  public void assertImagesEquals(String expectedFileName, Chart chart) throws Exception {
    String path = "/expectedChartRenderings/" + expectedFileName;
    File actualFile = new File(getClass().getResource(path).getFile());
    BufferedImage expectedImage = ImageIO.read(actualFile);

    BufferedImage actualImage = BitmapEncoder.getBufferedImage(chart);
    ImageComparison compare = new ImageComparison(expectedImage, actualImage);
    compare.setPixelToleranceLevel(0);
    compare.setThreshold(0);
    if (compare.compareImages().getImageComparisonState() != ImageComparisonState.MATCH) {
      Assert.fail("Rendered chart is different than expected");
    }
  }
}
