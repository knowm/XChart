package org.knowm.xchart;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class BitmapEncoderTest {

  @Test
  public void testAddFileExtension() {
    String fileName1 = "image";
    String fileName2 = "image.png";
    String fileName3 = "image.PNG";

    for (String s : Arrays.asList(fileName1, fileName2, fileName3)) {
      assertEquals("image.png", BitmapEncoder.addFileExtension(s, BitmapEncoder.BitmapFormat.PNG));
    }
    assertEquals("z.bmp", BitmapEncoder.addFileExtension("z", BitmapEncoder.BitmapFormat.BMP));
    assertEquals(
        "asdf.bmp", BitmapEncoder.addFileExtension("asdf", BitmapEncoder.BitmapFormat.BMP));
    assertEquals(".bmp", BitmapEncoder.addFileExtension(".bmp", BitmapEncoder.BitmapFormat.BMP));
    assertEquals(".bmp", BitmapEncoder.addFileExtension(".BmP", BitmapEncoder.BitmapFormat.BMP));
  }

  /**
   * Regression test for issue #862: BitmapEncoder must not NPE when always-visible tooltips are
   * enabled. Tooltips should be rendered into the image.
   */
  @Test
  public void getBufferedImageRendersAlwaysVisibleToolTips() {
    XYChart chart = new XYChartBuilder().width(400).height(300).build();
    chart.addSeries("series", new double[] {1, 2, 3}, new double[] {4, 5, 6});
    chart.getStyler().setToolTipsAlwaysVisible(true);

    assertDoesNotThrow(() -> BitmapEncoder.getBufferedImage(chart));
  }

  /**
   * Regression test for issue #862: BitmapEncoder must not NPE when headless-rendering a chart
   * where hover tooltips would be enabled in a panel at runtime. Since zoom/hover are panel-owned,
   * they must not affect headless rendering.
   */
  @Test
  public void getBufferedImageDoesNotNPEHeadless() {
    XYChart chart = new XYChartBuilder().width(400).height(300).build();
    chart.addSeries("series", new double[] {1, 2, 3}, new double[] {4, 5, 6});

    assertDoesNotThrow(() -> BitmapEncoder.getBufferedImage(chart));
  }
}
