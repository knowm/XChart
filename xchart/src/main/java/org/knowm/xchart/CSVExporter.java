/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class is used to export Chart data to a folder containing one or more CSV files. The parent folder's name is the title of the chart. Each
 * series becomes a CSV file in the folder. The series' name becomes the CSV files' name.
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
   * Joins a collection into an entire row of comma separated values.
   *
   * @param collection
   * @param separator
   * @return
   */
  private static String join(Collection<? extends Object> collection, String separator) {

    if (collection == null) {
      return null;
    }
    Iterator<? extends Object> iterator = collection.iterator();
    // handle null, zero and one elements before building a buffer
    if (iterator == null) {
      return null;
    }
    if (!iterator.hasNext()) {
      return "";
    }
    Object first = iterator.next();
    if (!iterator.hasNext()) {
      return first == null ? "" : first.toString();
    }

    // two or more elements
    StringBuffer buf = new StringBuffer(256); // Java default is 16, probably too small
    if (first != null) {
      buf.append(first);
    }

    while (iterator.hasNext()) {
      if (separator != null) {
        buf.append(separator);
      }
      Object obj = iterator.next();
      if (obj != null) {
        buf.append(obj);
      }
    }
    return buf.toString();

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
      Collection<?> xData = series.getXData();
      Collection<? extends Number> yData = series.getYData();
      Collection<? extends Number> errorBarData = series.getExtraValues();
      Iterator<?> itrx = xData.iterator();
      Iterator<? extends Number> itry = yData.iterator();
      Iterator<? extends Number> itrErrorBar = null;
      if (errorBarData != null) {
        itrErrorBar = errorBarData.iterator();
      }
      while (itrx.hasNext()) {
        Number xDataPoint = (Number) itrx.next();
        Number yDataPoint = itry.next();
        Number errorBarValue = null;
        if (itrErrorBar != null) {
          errorBarValue = itrErrorBar.next();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(xDataPoint + ",");
        sb.append(yDataPoint + ",");
        if (errorBarValue != null) {
          sb.append(errorBarValue + ",");
        }
        sb.setLength(sb.length() - 1);
        sb.append(System.getProperty("line.separator"));

        // String csv = xDataPoint + "," + yDataPoint + errorBarValue == null ? "" : ("," + errorBarValue) + System.getProperty("line.separator");
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
