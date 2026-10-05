package org.knowm.xchart;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.knowm.xchart.internal.chartpart.IChart;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 * Exercises the unified {@link ChartEncoder} across every export format: the built-in raster
 * formats provided by ImageIO on the current JDK (png, jpg, bmp, gif, tiff, wbmp, plus any
 * registered plugin), and the optional vector formats (svg, eps, pdf). Also serves as a generator
 * that writes one sample chart per supported format, mirroring the {@code Sample_Chart.*} files
 * produced by the demo.
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

  /**
   * The optional vector formats are on XChart's own test classpath, so they must produce output.
   */
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

  @Test
  public void explicitSizeLaysOutDifferentChartTypes() throws IOException {
    PieChart pie = new PieChart(500, 400);
    pie.addSeries("A", 3);
    pie.addSeries("B", 2);
    CategoryChart bars = new CategoryChart(500, 400);
    bars.addSeries("Sample", new double[] {1, 2, 3}, new double[] {2, 4, 3});
    for (IChart chart : new IChart[] {sampleChart(), pie, bars}) {
      for (int[] size : new int[][] {{800, 300}, {300, 600}}) {
        BufferedImage image =
            ImageIO.read(
                new ByteArrayInputStream(ChartEncoder.getBytes(chart, "png", size[0], size[1])));
        assertEquals(size[0], image.getWidth());
        assertEquals(size[1], image.getHeight());
        // Existing chart implementations retain their most recent paint dimensions.
        assertEquals(size[0], chart.getWidth());
        assertEquals(size[1], chart.getHeight());
      }
    }
  }

  @Test
  public void explicitSizeIsPassedToPaintWithoutScaling() throws IOException {
    IChart chart =
        new IChart() {
          @Override
          public int getWidth() {
            return 100;
          }

          @Override
          public int getHeight() {
            return 100;
          }

          @Override
          public String getTitle() {
            return "Size probe";
          }

          @Override
          public void paint(Graphics2D graphics, int width, int height) {
            assertEquals(240, width);
            assertEquals(120, height);
            assertEquals(1.0, graphics.getTransform().getScaleX());
            assertEquals(1.0, graphics.getTransform().getScaleY());
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.RED);
            graphics.fillRect(width - 1, height - 1, 1, 1);
          }
        };
    BufferedImage image =
        ImageIO.read(new ByteArrayInputStream(ChartEncoder.getBytes(chart, "png", 240, 120)));
    assertEquals(Color.RED.getRGB(), image.getRGB(239, 119));
    assertEquals(Color.WHITE.getRGB(), image.getRGB(238, 119));
  }

  @Test
  public void explicitSizeStreamStaysOpen() throws IOException {
    class Target extends ByteArrayOutputStream {
      boolean closed;

      @Override
      public void close() {
        closed = true;
      }
    }
    Target target = new Target();
    ChartEncoder.saveChart(sampleChart(), target, "jpg", 720, 360);
    assertFalse(target.closed);
    BufferedImage image = ImageIO.read(new ByteArrayInputStream(target.toByteArray()));
    assertEquals(720, image.getWidth());
    assertEquals(360, image.getHeight());
  }

  @Test
  public void explicitSizeFileAppendsExtension(@TempDir Path dir) throws IOException {
    String base = dir.resolve("sized").toString();
    ChartEncoder.saveChart(sampleChart(), base, "PNG", 720, 360);
    BufferedImage image = ImageIO.read(new File(base + ".png"));
    assertEquals(720, image.getWidth());
    assertEquals(360, image.getHeight());
  }

  @Test
  public void explicitSizeControlsVectorPageDimensions() throws IOException {
    byte[] svg = ChartEncoder.getBytes(sampleChart(), "svg", 720, 360);
    Matcher viewBox =
        Pattern.compile("viewBox=\"([^\"]+)\"").matcher(new String(svg, StandardCharsets.UTF_8));
    assertTrue(viewBox.find());
    String[] bounds = viewBox.group(1).trim().split("\\s+");
    assertEquals(720.0, Double.parseDouble(bounds[2]));
    assertEquals(360.0, Double.parseDouble(bounds[3]));
    try (PDDocument pdf = Loader.loadPDF(ChartEncoder.getBytes(sampleChart(), "pdf", 720, 360))) {
      assertEquals(720.0f, pdf.getPage(0).getMediaBox().getWidth());
      assertEquals(360.0f, pdf.getPage(0).getMediaBox().getHeight());
    }
  }

  @Test
  public void invalidSizeDoesNotWriteOrTruncateOutput(@TempDir Path dir) throws IOException {
    XYChart chart = sampleChart();
    Path file = dir.resolve("existing.png");
    byte[] original = {1, 2, 3};
    Files.write(file, original);
    for (int[] size : new int[][] {{0, 100}, {100, 0}, {-1, 100}, {100, -1}}) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      assertThrows(
          IllegalArgumentException.class,
          () -> ChartEncoder.saveChart(chart, file.toString(), "png", size[0], size[1]));
      assertThrows(
          IllegalArgumentException.class,
          () -> ChartEncoder.saveChart(chart, out, "png", size[0], size[1]));
      assertThrows(
          IllegalArgumentException.class,
          () -> ChartEncoder.getBytes(chart, "png", size[0], size[1]));
      assertEquals(0, out.size());
      assertArrayEquals(original, Files.readAllBytes(file));
      assertEquals(500, chart.getWidth());
      assertEquals(400, chart.getHeight());
    }
  }

  @Test
  public void explicitSizePreservesUnsupportedFormatErrors() {
    IOException error =
        assertThrows(
            IOException.class,
            () -> ChartEncoder.getBytes(sampleChart(), "not-a-format", 720, 360));
    assertTrue(error.getMessage().contains("not-a-format"));
  }
}
