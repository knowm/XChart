package org.knowm.xchart;

import de.erichseifert.vectorgraphics2d.EPSGraphics2D;
import de.erichseifert.vectorgraphics2d.PDFGraphics2D;
import de.erichseifert.vectorgraphics2d.ProcessingPipeline;
import de.erichseifert.vectorgraphics2d.SVGGraphics2D;
import java.io.FileOutputStream;
import java.io.IOException;
import org.knowm.xchart.internal.chartpart.Chart;

/**
 * A helper class with static methods for saving Charts as vectors
 *
 * @author timmolter
 */
public final class VectorGraphicsEncoder {

  /** Constructor - Private constructor to prevent instantiation */
  private VectorGraphicsEncoder() {}

  public static void saveVectorGraphic(
      Chart chart, String fileName, VectorGraphicsFormat vectorGraphicsFormat) throws IOException {

    ProcessingPipeline g = null;

    switch (vectorGraphicsFormat) {
      case EPS:
        g = new EPSGraphics2D(0.0, 0.0, chart.getWidth(), chart.getHeight());
        break;
      case PDF:
        g = new PDFGraphics2D(0.0, 0.0, chart.getWidth(), chart.getHeight());
        break;
      case SVG:
        g = new SVGGraphics2D(0.0, 0.0, chart.getWidth(), chart.getHeight());
        break;

      default:
        break;
    }

    chart.paint(g, chart.getWidth(), chart.getHeight());

    // Write the vector graphic output to a file
    FileOutputStream file = new FileOutputStream(addFileExtension(fileName, vectorGraphicsFormat));

    try {
      file.write(g.getBytes());
    } finally {
      file.close();
    }
  }

  /**
   * Only adds the extension of the VectorGraphicsFormat to the filename if the filename doesn't
   * already have it.
   *
   * @param fileName
   * @param vectorGraphicsFormat
   * @return filename (if extension already exists), otherwise;: filename + "." + extension
   */
  public static String addFileExtension(
      String fileName, VectorGraphicsFormat vectorGraphicsFormat) {

    String fileNameWithFileExtension = fileName;
    final String newFileExtension = "." + vectorGraphicsFormat.toString().toLowerCase();
    if (fileName.length() <= newFileExtension.length()
        || !fileName
            .substring(fileName.length() - newFileExtension.length(), fileName.length())
            .equalsIgnoreCase(newFileExtension)) {
      fileNameWithFileExtension = fileName + newFileExtension;
    }
    return fileNameWithFileExtension;
  }

  public enum VectorGraphicsFormat {
    EPS,
    PDF,
    SVG
  }
}
