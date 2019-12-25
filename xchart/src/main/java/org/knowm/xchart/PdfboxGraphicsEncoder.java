package org.knowm.xchart;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.knowm.xchart.internal.chartpart.Chart;

import de.rototor.pdfbox.graphics2d.PdfBoxGraphics2D;

/**
 * A helper class with static methods for saving Charts as a PDF file
 *
 * @author Mr14huashao
 */
public class PdfboxGraphicsEncoder {

  private static final String PDF_FILE_EXTENSION = ".pdf";

  /** Constructor - Private constructor to prevent instantiation */
  private PdfboxGraphicsEncoder() {}

  /**
   * Write a chart to a file
   *
   * @param chart Chart
   * @param fileName file name path
   * @throws IOException
   */
  public static void savePdfboxGraphics(Chart chart, String fileName) throws IOException {

    savePdfboxGraphics(chart, new File(addFileExtension(fileName)));
  }

  /**
   * Write a chart to a file
   *
   * @param chart Chart
   * @param file File
   * @throws IOException
   */
  public static void savePdfboxGraphics(Chart chart, File file) throws IOException {

    savePdfboxGraphics(chart, new BufferedOutputStream(new FileOutputStream(file)));
  }

  /**
   * Write a chart to an OutputStream
   *
   * @param chart Chart
   * @param os OutputStream
   * @throws IOException
   */
  public static void savePdfboxGraphics(Chart chart, OutputStream os) throws IOException {

    List<Chart> charts = new ArrayList<>();
    charts.add(chart);
    savePdfboxGraphics(charts, os);
  }

  /**
   * Write multiple charts to a file
   *
   * @param charts List<Chart>
   * @param fileName file name path
   * @throws IOException
   */
  public static void savePdfboxGraphics(List<Chart> charts, String fileName) throws IOException {

    savePdfboxGraphics(charts, new File(addFileExtension(fileName)));
  }

  /**
   * Write multiple charts to a file
   *
   * @param charts List<Chart>
   * @param file File
   * @throws IOException
   */
  public static void savePdfboxGraphics(List<Chart> charts, File file) throws IOException {

    savePdfboxGraphics(charts, new BufferedOutputStream(new FileOutputStream(file)));
  }

  /**
   * Write multiple charts to an OutputStream
   *
   * @param charts List<Chart>
   * @param os OutputStream
   * @throws IOException
   */
  public static void savePdfboxGraphics(List<Chart> charts, OutputStream os) throws IOException {

    PDDocument document = new PDDocument();
    PDRectangle mediaBox = null;
    PDPage page = null;
    PDPageContentStream contentStream = null;
    PdfBoxGraphics2D pdfBoxGraphics2D = null;
    PDFormXObject xform = null;
    for (Chart chart : charts) {
      mediaBox = new PDRectangle(chart.getWidth(), chart.getHeight());
      page = new PDPage(mediaBox);
      // add page
      document.addPage(page);
      pdfBoxGraphics2D = new PdfBoxGraphics2D(document, chart.getWidth(), chart.getHeight());
      chart.paint(pdfBoxGraphics2D, chart.getWidth(), chart.getHeight());
      pdfBoxGraphics2D.dispose();
      xform = pdfBoxGraphics2D.getXFormObject();

      contentStream = new PDPageContentStream(document, page);
      contentStream.drawForm(xform);
      contentStream.close();
    }

    document.save(os);
    document.close();
  }

  /**
   * Only adds the extension of the ".pdf" to the filename if the filename doesn't already have it.
   *
   * @param fileName
   * @return filename (if extension already exists), otherwise;: filename + ".pdf"
   */
  private static String addFileExtension(String fileName) {

    String fileNameWithFileExtension = fileName;
    if (fileName.length() <= PDF_FILE_EXTENSION.length()
        || !fileName
            .substring(fileName.length() - PDF_FILE_EXTENSION.length(), fileName.length())
            .equalsIgnoreCase(PDF_FILE_EXTENSION)) {
      fileNameWithFileExtension = fileName + PDF_FILE_EXTENSION;
    }
    return fileNameWithFileExtension;
  }
}
