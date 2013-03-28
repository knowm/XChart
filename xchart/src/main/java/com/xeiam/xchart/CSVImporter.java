/**
 * Copyright (C) 2013 Xeiam LLC http://xeiam.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to create a Chart object from a folder containing one or more CSV files. The parent folder's name becomes the title of the chart. Each CSV file in the folder becomes a series on
 * the chart. the CSV file's name becomes the series' name.
 * 
 * @author timmolter
 */
public class CSVImporter {

  /**
   * @param path2Directory
   * @param width
   * @param height
   * @return
   */
  public static Chart getChartFromCSVDir(String path2Directory, int width, int height) {

    // 1. get the directory, name chart the dir name

    Chart chart = new Chart(width, height);

    // 2. get all the csv files in the dir
    File[] csvFiles = getAllFiles(path2Directory, ".*.csv");

    // 3. create a series for each file, naming the series the file name
    for (int i = 0; i < csvFiles.length; i++) {
      File csvFile = csvFiles[i];
      String[] xAndYData = getSeriesData(csvFile);
      chart.addSeries(csvFile.getName().substring(0, csvFile.getName().indexOf(".csv")), getAxisData(xAndYData[0]), getAxisData(xAndYData[1]));
    }

    return chart;
  }

  private static String[] getSeriesData(File csvFile) {

    String[] xAndYData = new String[2];

    BufferedReader bufferedReader = null;
    try {
      int counter = 0;
      String line = null;
      bufferedReader = new BufferedReader(new FileReader(csvFile));
      while ((line = bufferedReader.readLine()) != null) {
        xAndYData[counter++] = line;
      }

    } catch (Exception e) {
      System.out.println("Exception while reading csv file: " + e);
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return xAndYData;
  }

  private static List<Number> getAxisData(String stringData) {

    List<Number> axisData = new ArrayList<Number>();
    String[] stringDataArray = stringData.split(",");
    for (int i = 0; i < stringDataArray.length; i++) {
      String dataPoint = stringDataArray[i];
      BigDecimal value = new BigDecimal(dataPoint);
      axisData.add(value);
    }
    return axisData;
  }

  /**
   * This method returns the files found in the given directory matching the given regular expression.
   * 
   * @param dirName - ex. "./path/to/directory/" *make sure you have the '/' on the end
   * @param regex - ex. ".*.csv"
   * @return File[] - an array of files
   */
  public static File[] getAllFiles(String dirName, String regex) {

    File[] allFiles = getAllFiles(dirName);

    List<File> matchingFiles = new ArrayList<File>();

    for (int i = 0; i < allFiles.length; i++) {

      if (allFiles[i].getName().matches(regex)) {
        matchingFiles.add(allFiles[i]);
      }
    }

    return matchingFiles.toArray(new File[matchingFiles.size()]);

  }

  /**
   * This method returns the Files found in the given directory
   * 
   * @param dirName - ex. "./path/to/directory/" *make sure you have the '/' on the end
   * @return File[] - an array of files
   */
  public static File[] getAllFiles(String dirName) {

    File dir = new File(dirName);

    File[] files = dir.listFiles(); // returns files and folders

    if (files != null) {
      List<File> filteredFiles = new ArrayList<File>();
      for (int i = 0; i < files.length; i++) {

        if (files[i].isFile()) {
          filteredFiles.add(files[i]);
        }
      }
      return filteredFiles.toArray(new File[filteredFiles.size()]);
    } else {
      System.out.println(dirName + " does not denote a valid directory!");
      return new File[0];
    }
  }

}
