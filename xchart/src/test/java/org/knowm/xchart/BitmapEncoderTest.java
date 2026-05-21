package org.knowm.xchart;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.style.Styler;

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

  /** Regression test for issue #862: BitmapEncoder must not NPE when tooltips are enabled. */
  @Test
  public void getBufferedImageDoesNotNPEWithToolTipsEnabled() {
    XYChart chart = new XYChartBuilder().width(400).height(300).build();
    chart.getStyler().setToolTipsEnabled(true);
    chart.getStyler().setToolTipsAlwaysVisible(true);
    chart.getStyler().setToolTipType(Styler.ToolTipType.yLabels);
    chart.addSeries("series", new double[] {1, 2, 3}, new double[] {4, 5, 6});

    assertDoesNotThrow(() -> BitmapEncoder.getBufferedImage(chart));
  }

  /** Regression test for issue #862: zoom-enabled charts must not NPE with BitmapEncoder. */
  @Test
  public void getBufferedImageDoesNotNPEWithZoomEnabled() {
    XYChart chart = new XYChartBuilder().width(400).height(300).build();
    chart.getStyler().setZoomEnabled(true);
    chart.addSeries("series", new double[] {1, 2, 3}, new double[] {4, 5, 6});

    assertDoesNotThrow(() -> BitmapEncoder.getBufferedImage(chart));
  }
}
