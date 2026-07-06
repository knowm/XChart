package org.knowm.xchart;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Exercises the unified {@link ChartEncoder} across every export format: the built-in raster formats
 * provided by ImageIO on the current JDK (png, jpg, bmp, gif, tiff, wbmp, plus any registered
 * plugin), and the optional vector formats (svg, eps, pdf). Also serves as a generator that writes
 * one sample chart per supported format, mirroring the {@code Sample_Chart.*} files produced by the
 * demo.
 */
public class ChartEncoderTest {

  private static XYChart sampleChart() {
    XYChart chart = new XYChart(500, 400);
    chart.setTitle("Sample Chart");
    chart.setXAxisTitle("X");
    chart.setYAxisTitle("Y");
    XYSeries series = chart.addSeries("y(x)", null, new double[] {2.0, 1.0, 0.0});
    series.setMarker(SeriesMarkers.CIRCLE);
    return chart;
  }

  /** Every raster format the current classpath can write must produce a non-empty file. */
  @Test
  public void writesEverySupportedRasterFormat(@TempDir Path dir) throws IOException {

    XYChart chart = sampleChart();
    assertTrue(
        ChartEncoder.getSupportedRasterFormats().contains("png"),
        "png must always be available from the JDK");

    for (String format : ChartEncoder.getSupportedRasterFormats()) {
      // WBMP has a registered writer but only accepts 1-bit black-and-white images, so a color
      // chart cannot be encoded to it. It is intentionally not a usable chart export format.
      if (format.equals("wbmp")) {
        continue;
      }
      File file = dir.resolve("Sample_Chart." + format).toFile();
      ChartEncoder.saveChart(chart, file.getAbsolutePath(), format);
      assertTrue(file.exists() && file.length() > 0, format + " output should be non-empty");
    }
  }

  /** TIFF ships with the JDK (Java 9+), so it must be writable without any plugin. */
  @Test
  public void jdkProvidesTiffWriter() {
    assertTrue(ChartEncoder.getSupportedRasterFormats().contains("tiff"));
  }

  /** The optional vector formats are on XChart's own test classpath, so they must produce output. */
  @Test
  public void writesVectorFormats(@TempDir Path dir) throws IOException {

    XYChart chart = sampleChart();
    for (String format : new String[] {"svg", "eps", "pdf"}) {
      File file = dir.resolve("Sample_Chart." + format).toFile();
      ChartEncoder.saveChart(chart, file.getAbsolutePath(), format);
      assertTrue(file.exists() && file.length() > 0, format + " output should be non-empty");
    }
  }

  @Test
  public void getBytesReturnsEncodedImage() throws IOException {
    byte[] bytes = ChartEncoder.getBytes(sampleChart(), "png");
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
  }

  /** The file-name overload appends the format as extension when missing. */
  @Test
  public void appendsExtension(@TempDir Path dir) throws IOException {
    String base = dir.resolve("no_extension").toString();
    ChartEncoder.saveChart(sampleChart(), base, "png");
    assertTrue(new File(base + ".png").exists());
  }

  /** An unregistered raster format fails with an actionable message rather than silently. */
  @Test
  public void unsupportedFormatThrowsHelpfulError(@TempDir Path dir) {
    // "avif" has no ImageIO writer on the default classpath.
    assertFalse(ChartEncoder.getSupportedRasterFormats().contains("avif"));
    IOException e =
        assertThrows(
            IOException.class,
            () ->
                ChartEncoder.saveChart(
                    sampleChart(), dir.resolve("chart.avif").toString(), "avif"));
    assertTrue(e.getMessage().toLowerCase().contains("avif"));
    assertTrue(e.getMessage().contains("plugin"));
  }
}
