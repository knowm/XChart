package org.knowm.xchart;

import java.io.*;

/**
 * This class is used to export Chart data to a folder containing one or more CSV files. The parent
 * folder's name is the title of the chart. Each series becomes a CSV file in the folder. The
 * series' name becomes the CSV files' name.
 *
 * @author timmolter
 */
public class CSVExporter {

  /**
   * Export all XYChart series as rows in separate CSV files.
   *
   * @param chart
   * @param path2Dir
   */
  public static void writeCSVRows(XYChart chart, String path2Dir) {

    for (XYSeries xySeries : chart.getSeriesMap().values()) {
      writeCSVRows(xySeries, path2Dir);
    }
  }

  /**
   * Export a XYChart series into rows in a CSV file.
   *
   * @param series
   * @param path2Dir - ex. "./path/to/directory/" *make sure you have the '/' on the end
   */
  public static void writeCSVRows(XYSeries series, String path2Dir) {

    File newFile = new File(path2Dir + series.getName() + ".csv");
    Writer out = null;
    try {

      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "UTF8"));
      String csv = join(series.getXData(), ",") + System.getProperty("line.separator");
      out.write(csv);
      csv = join(series.getYData(), ",") + System.getProperty("line.separator");
      out.write(csv);
      if (series.getExtraValues() != null) {
        csv = join(series.getExtraValues(), ",") + System.getProperty("line.separator");
        out.write(csv);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        try {
          out.flush();
          out.close();
        } catch (IOException e) {
          // NOP
        }
      }
    }
  }

  /**
   * Joins a series into an entire row of comma separated values.
   *
   * @param seriesData
   * @param separator
   * @return
   */
  private static String join(double[] seriesData, String separator) {

    // two or more elements
    StringBuilder sb = new StringBuilder(256); // Java default is 16, probably too small
    sb.append(seriesData[0]);
    for (int i = 1; i < seriesData.length; i++) {

      if (separator != null) {
        sb.append(separator);
      }

      sb.append(seriesData[i]);
    }
    return sb.toString();
  }

  /**
   * Export all XYChart series as columns in separate CSV files.
   *
   * @param chart
   * @param path2Dir
   */
  public static void writeCSVColumns(XYChart chart, String path2Dir) {

    for (XYSeries xySeries : chart.getSeriesMap().values()) {
      writeCSVColumns(xySeries, path2Dir);
    }
  }

  /**
   * Export a Chart series in columns in a CSV file.
   *
   * @param series
   * @param path2Dir - ex. "./path/to/directory/" *make sure you have the '/' on the end
   */
  public static void writeCSVColumns(XYSeries series, String path2Dir) {

    File newFile = new File(path2Dir + series.getName() + ".csv");
    Writer out = null;
    try {

      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "UTF8"));
      double[] xData = series.getXData();
      double[] yData = series.getYData();
      double[] errorBarData = series.getExtraValues();
      for (int i = 0; i < xData.length; i++) {

        StringBuilder sb = new StringBuilder();
        sb.append(xData[i]).append(",");
        sb.append(yData[i]).append(",");
        if (errorBarData != null) {
          sb.append(errorBarData[i]).append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append(System.getProperty("line.separator"));

        // String csv = xDataPoint + "," + yDataPoint + errorBarValue == null ? "" : ("," +
        // errorBarValue) + System.getProperty("line.separator");
        // String csv = + yDataPoint + System.getProperty("line.separator");
        out.write(sb.toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        try {
          out.flush();
          out.close();
        } catch (IOException e) {
          // NOP
        }
      }
    }
  }
}
