package org.knowm.xchart;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.IChart;

/**
 * Unified, format-agnostic entry point for exporting a {@link org.knowm.xchart.internal.chartpart.IChart}.
 *
 * <p>A single {@code format} string selects the encoder:
 *
 * <ul>
 *   <li>{@code "svg"} / {@code "eps"} &rarr; {@link VectorGraphicsEncoder} (optional {@code
 *       de.erichseifert.vectorgraphics2d:VectorGraphics2D})
 *   <li>{@code "pdf"} &rarr; {@link PdfboxGraphicsEncoder} (optional {@code
 *       de.rototor.pdfbox:graphics2d})
 *   <li>any other name &rarr; the raster path, delegating to {@link javax.imageio.ImageIO}. This
 *       covers the JDK's built-in writers ({@code png}, {@code jpg}/{@code jpeg}, {@code bmp}, {@code
 *       gif}, {@code tiff}/{@code tif}, {@code wbmp}) plus <em>any</em> additional format for which
 *       an ImageIO plugin is registered on the classpath (e.g. {@code webp}, {@code avif} via a
 *       TwelveMonkeys / native plugin).
 * </ul>
 *
 * <p>Because raster export is delegated to ImageIO's pluggable Service Provider Interface, new
 * formats can be supported without any change to XChart: drop a suitable {@code ImageWriter} plugin
 * on the classpath and pass its format name here.
 *
 * <p>This class supersedes the per-format {@link BitmapEncoder}, {@link VectorGraphicsEncoder} and
 * {@link PdfboxGraphicsEncoder} save methods, whose {@code save*} entry points are now deprecated.
 */
public final class ChartEncoder {

  /** Constructor - Private constructor to prevent instantiation */
  private ChartEncoder() {}

  /**
   * Save a chart to a file. The format string is also used as the file extension (appended if the
   * file name does not already end with it).
   *
   * @param chart the chart to export
   * @param fileName target file name, with or without extension
   * @param format the format name, e.g. {@code "png"}, {@code "svg"}, {@code "pdf"}, {@code "tiff"},
   *     {@code "webp"}
   * @throws IOException if writing fails or no encoder is available for the format
   */
  public static void saveChart(IChart chart, String fileName, String format) throws IOException {

    String fileNameWithExtension =
        Utils.addFileExtension(fileName, "." + format.toLowerCase(Locale.ROOT));
    try (OutputStream out = new FileOutputStream(fileNameWithExtension)) {
      saveChart(chart, out, format);
    }
  }

  /**
   * Write a chart to a stream. Does not close the target stream.
   *
   * @param chart the chart to export
   * @param out the target stream
   * @param format the format name, e.g. {@code "png"}, {@code "svg"}, {@code "pdf"}, {@code "tiff"},
   *     {@code "webp"}
   * @throws IOException if writing fails or no encoder is available for the format
   */
  @SuppressWarnings("deprecation")
  public static void saveChart(IChart chart, OutputStream out, String format) throws IOException {

    switch (format.toLowerCase(Locale.ROOT)) {
      case "svg":
        VectorGraphicsEncoder.saveVectorGraphic(chart, out, VectorGraphicsFormat.SVG);
        return;
      case "eps":
        VectorGraphicsEncoder.saveVectorGraphic(chart, out, VectorGraphicsFormat.EPS);
        return;
      case "pdf":
        PdfboxGraphicsEncoder.savePdfboxGraphics(chart, out);
        return;
      default:
        saveRaster(chart, out, format);
    }
  }

  /**
   * Generate the encoded bytes for a chart in the given format.
   *
   * @param chart the chart to export
   * @param format the format name
   * @return the encoded image bytes
   * @throws IOException if writing fails or no encoder is available for the format
   */
  public static byte[] getBytes(IChart chart, String format) throws IOException {

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      saveChart(chart, baos, format);
      return baos.toByteArray();
    }
  }

  /** Raster export via ImageIO's pluggable writers. */
  private static void saveRaster(IChart chart, OutputStream out, String format) throws IOException {

    String normalized = format.toLowerCase(Locale.ROOT);
    if (!ImageIO.getImageWritersByFormatName(normalized).hasNext()) {
      TreeSet<String> available = new TreeSet<>();
      for (String name : ImageIO.getWriterFormatNames()) {
        available.add(name.toLowerCase(Locale.ROOT));
      }
      throw new IOException(
          "No image writer is registered for format '"
              + format
              + "'. Available raster formats: "
              + available
              + ". To export formats such as webp or avif, add a matching ImageIO plugin to your"
              + " classpath.");
    }

    if (!ImageIO.write(BitmapEncoder.getBufferedImage(chart), normalized, out)) {
      // A writer exists but declined the image, almost always a color-model mismatch: e.g. the
      // JDK's WBMP writer only accepts a 1-bit black-and-white image, not an RGB chart.
      throw new IOException(
          "The registered '"
              + format
              + "' writer could not encode the chart. Its writer likely does not support the"
              + " chart's color model (for example, WBMP only supports 1-bit black-and-white"
              + " images).");
    }
  }

  /**
   * Returns the sorted set of raster format names that can be written on the current classpath. This
   * reflects the JDK's built-in writers plus any registered ImageIO plugins, but not the vector
   * formats ({@code svg}, {@code eps}, {@code pdf}).
   *
   * @return the available raster format names, lower-cased
   */
  public static TreeSet<String> getSupportedRasterFormats() {

    TreeSet<String> formats = new TreeSet<>();
    formats.addAll(Arrays.asList(ImageIO.getWriterFormatNames()));
    TreeSet<String> lowerCased = new TreeSet<>();
    for (String name : formats) {
      lowerCased.add(name.toLowerCase(Locale.ROOT));
    }
    return lowerCased;
  }
}
