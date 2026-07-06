package org.knowm.xchart.standalone;

import java.io.File;
import java.io.IOException;
import org.knowm.xchart.ChartEncoder;
import org.knowm.xchart.XYChart;

/**
 * Generates an AVIF sample chart ({@code ./Sample_Chart.avif}) in the project root.
 *
 * <p>Unlike WebP, there is currently no reliable Maven-Central ImageIO AVIF <em>writer</em> plugin,
 * so this example cannot go through {@link ChartEncoder} directly out of the box. There are two ways
 * to produce an AVIF:
 *
 * <ol>
 *   <li><b>ImageIO plugin (preferred, XChart-native):</b> if you add an ImageIO AVIF writer to the
 *       classpath, then {@code ChartEncoder.saveChart(chart, "./Sample_Chart", "avif")} just works,
 *       exactly like {@link ExampleWebP}. No such plugin is bundled here.
 *   <li><b>libavif CLI (used below):</b> export a PNG with XChart, then convert it with the {@code
 *       avifenc} tool from <a href="https://github.com/AOMediaCodec/libavif">libavif</a>. On macOS:
 *       {@code brew install libavif}. This is the most portable way to produce a sample today.
 * </ol>
 */
public class ExampleAvif {

  public static void main(String[] args) throws Exception {

    XYChart chart = ExampleWebP.buildSampleChart();

    // 1) Render to a temporary PNG using XChart's built-in raster export.
    File png = File.createTempFile("xchart-", ".png");
    ChartEncoder.saveChart(chart, png.getAbsolutePath(), "png");

    // 2) Convert PNG -> AVIF with libavif's avifenc CLI.
    File avif = new File("./Sample_Chart.avif");
    try {
      Process process =
          new ProcessBuilder("avifenc", png.getAbsolutePath(), avif.getAbsolutePath())
              .inheritIO()
              .start();
      int exit = process.waitFor();
      if (exit != 0) {
        throw new IllegalStateException("avifenc exited with code " + exit + ".");
      }
      System.out.println("Wrote " + avif.getPath());
    } catch (IOException e) {
      throw new IllegalStateException(
          "Could not run 'avifenc'. Install libavif and ensure avifenc is on your PATH"
              + " (macOS: 'brew install libavif'). Alternatively, add an ImageIO AVIF writer"
              + " plugin and call ChartEncoder.saveChart(chart, fileName, \"avif\") directly.",
          e);
    } finally {
      png.delete();
    }
  }
}
