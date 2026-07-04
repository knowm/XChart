package org.knowm.xchart;

import de.erichseifert.vectorgraphics2d.Document;
import de.erichseifert.vectorgraphics2d.Processor;
import de.erichseifert.vectorgraphics2d.VectorGraphics2D;
import de.erichseifert.vectorgraphics2d.eps.EPSProcessor;
import de.erichseifert.vectorgraphics2d.intermediate.CommandSequence;
import de.erichseifert.vectorgraphics2d.svg.SVGProcessor;
import de.erichseifert.vectorgraphics2d.util.PageSize;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.IChart;

/** A helper class with static methods for saving Charts as vectors */
public final class VectorGraphicsEncoder {

  /** Constructor - Private constructor to prevent instantiation */
  private VectorGraphicsEncoder() {}

  /**
   * Write a chart to a file.
   *
   * @deprecated use {@link ChartEncoder#saveChart(IChart, String, String)} with format {@code
   *     "svg"}, {@code "eps"} or {@code "pdf"} instead
   */
  @Deprecated
  public static void saveVectorGraphic(
      IChart chart, String fileName, VectorGraphicsFormat vectorGraphicsFormat) throws IOException {
    FileOutputStream file = new FileOutputStream(addFileExtension(fileName, vectorGraphicsFormat));

    try {
      saveVectorGraphic(chart, file, vectorGraphicsFormat);
    } finally {
      file.close();
    }
  }

  /**
   * Write a chart to an OutputStream.
   *
   * @deprecated use {@link ChartEncoder#saveChart(IChart, OutputStream, String)} with format {@code
   *     "svg"}, {@code "eps"} or {@code "pdf"} instead
   */
  @Deprecated
  public static void saveVectorGraphic(
      IChart chart, OutputStream os, VectorGraphicsFormat vectorGraphicsFormat) throws IOException {
    final Processor p;

    if (VectorGraphicsFormat.PDF != vectorGraphicsFormat) {
      // SVG and EPS are produced by VectorGraphics2D; PDF delegates to the (separately guarded)
      // PdfboxGraphicsEncoder below.
      Utils.requireOnClasspath(
          "de.erichseifert.vectorgraphics2d.VectorGraphics2D",
          vectorGraphicsFormat + " export",
          "de.erichseifert.vectorgraphics2d:VectorGraphics2D");
    }

    switch (vectorGraphicsFormat) {
      case EPS:
        p = new EPSProcessor();
        break;
      case PDF:
        p = new PDFBoxProcessor();
        break;
      case SVG:
        p = new SVGProcessor();
        break;

      default:
        throw new UnsupportedOperationException(
            "Unsupported vector graphics format: " + vectorGraphicsFormat);
    }

    if (VectorGraphicsFormat.PDF != vectorGraphicsFormat) {
      VectorGraphics2D vg2d = new VectorGraphics2D();
      //    vg2d.draw(new Rectangle2D.Double(0.0, 0.0, chart.getWidth(), chart.getHeight()));
      CommandSequence commands = vg2d.getCommands();

      chart.paint(vg2d, chart.getWidth(), chart.getHeight());

      PageSize pageSize = new PageSize(0.0, 0.0, chart.getWidth(), chart.getHeight());
      Document doc = p.getDocument(commands, pageSize);
      doc.writeTo(os);
    } else {
      ((PDFBoxProcessor) p).savePdf(chart, os);
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

  private static class PDFBoxProcessor implements Processor {

    @Override
    public Document getDocument(CommandSequence arg0, PageSize arg1) {

      return null;
    }

    @SuppressWarnings("deprecation")
    public void savePdf(IChart chart, OutputStream os) throws IOException {

      PdfboxGraphicsEncoder.savePdfboxGraphics(chart, os);
    }
  }
}
