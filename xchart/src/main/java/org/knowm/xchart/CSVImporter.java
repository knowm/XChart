package org.knowm.xchart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.style.Styler.ChartTheme;

/**
 * This class is used to create a Chart object from a folder containing one or more CSV files. The
 * parent folder's name becomes the title of the chart. Each CSV file in the folder becomes a series
 * on the chart. the CSV file's name becomes the series' name.
 *
 * @author timmolter
 */
public class CSVImporter {

  /**
   * @param path2Directory
   * @param dataOrientation
   * @param width
   * @param height
   * @param chartTheme
   * @return
   */
  public static XYChart getChartFromCSVDir(
      String path2Directory,
      DataOrientation dataOrientation,
      int width,
      int height,
      ChartTheme chartTheme) {

    // 1. get the directory, name chart the dir name
    XYChart chart;
    if (chartTheme != null) {
      chart = new XYChart(width, height, chartTheme);
    } else {
      chart = new XYChart(width, height);
    }

    // 2. get all the csv files in the dir
    File[] csvFiles = getAllFiles(path2Directory, ".*.csv");

    // 3. create a series for each file, naming the series the file name
    for (File csvFile : csvFiles) {
      String[] xAndYData;
      if (dataOrientation == DataOrientation.Rows) {
        xAndYData = getSeriesDataFromCSVRows(csvFile);
      } else {
        xAndYData = getSeriesDataFromCSVColumns(csvFile);
      }

      if (xAndYData[2] == null || xAndYData[2].trim().equalsIgnoreCase("")) {
        chart.addSeries(
            csvFile.getName().substring(0, csvFile.getName().indexOf(".csv")),
            getAxisData(xAndYData[0]),
            getAxisData(xAndYData[1]));
      } else {
        chart.addSeries(
            csvFile.getName().substring(0, csvFile.getName().indexOf(".csv")),
            getAxisData(xAndYData[0]),
            getAxisData(xAndYData[1]),
            getAxisData(xAndYData[2]));
      }
    }

    return chart;
  }

  /**
   * Get a particular SerieData from a CSV File that contains a headline
   *
   * @param csvFile
   * @param dataOrientation
   * @param columnWanted
   * @param rowWanted
   * @return
   * @throws IOException
   * @author Maxime Colomb
   */
  public static SeriesData getSeriesDataFromCSVFile(
      File csvFile, DataOrientation dataOrientation, String columnWanted, String rowWanted)
      throws IOException {
    HashMap<String, List<String>> series = getSeriesFromCSVFile(csvFile, columnWanted, rowWanted);
    List<String> row = series.get("row");
    List<String> column = series.get("column");
    if (dataOrientation == DataOrientation.Columns) {
      row = series.get("column");
      column = series.get("row");
    }
    List<Number> finalRow = new ArrayList<Number>();
    List<Number> finalColumn = new ArrayList<Number>();

    for (String r : row) {
      finalRow.add((Double.valueOf(r)));
    }
    for (String c : column) {
      finalColumn.add((Double.valueOf(c)));
    }
    return new SeriesData(
        finalRow, finalColumn, csvFile.getName().substring(0, csvFile.getName().indexOf(".csv")));
  }

  public static CategorySeries getCategorySeriesFromCSVFile(
      File csv, String columnWanted, String rowWanted, String name, DataType axisType)
      throws IOException {
    HashMap<String, List<String>> series = getSeriesFromCSVFile(csv, columnWanted, rowWanted);
    List<String> rows = series.get("row");

    List<Number> finalRows = new ArrayList<Number>();
    for (String r : rows) {
      finalRows.add(Double.valueOf(r));
    }
    return new CategorySeries(name, series.get("column"), finalRows, null, axisType);
  }

  public static HashMap<String, List<String>> getSeriesFromCSVFile(
      File csvFile, String columnWanted, String rowWanted) throws IOException {

    // 1. get csv file in the dir
    if (!csvFile.exists() || !csvFile.getName().endsWith(".csv")) {
      throw new FileNotFoundException("no csv on that path");
    }

    // 2. get the first line
    String[] fLine;
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader(csvFile));
      fLine = bufferedReader.readLine().split(",");
    } catch (Exception e) {
      bufferedReader.close();
      throw new FileNotFoundException("Exception while reading csv file: " + e);
    }

    // 3. identify the indice
    int iColumnWanted = 0;
    int iRowWanted = 0;
    for (int i = 0; i < fLine.length; i++) {
      if (fLine[i].equals(columnWanted)) {
        iColumnWanted = i;
      }
      if (fLine[i].equals(rowWanted)) {
        iRowWanted = i;
      }
    }

    // 4. get the series
    List<String> column = new LinkedList<String>();
    List<String> row = new LinkedList<String>();
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      String[] l = line.split(",");
      column.add((l[iColumnWanted]).replace("\"", ""));
      row.add((l[iRowWanted]).replace("\"", ""));
    }
    bufferedReader.close();

    // 5. Create returned object
    HashMap<String, List<String>> result = new HashMap<String, List<String>>();
    result.put("column", column);
    result.put("row", row);

    return result;
  }

  public static SeriesData getSeriesDataFromCSVFile(
      String path2CSVFile, DataOrientation dataOrientation) {

    // 1. get csv file in the dir
    File csvFile = new File(path2CSVFile);

    // 2. Create Series
    String[] xAndYData;
    if (dataOrientation == DataOrientation.Rows) {
      xAndYData = getSeriesDataFromCSVRows(csvFile);
    } else {
      xAndYData = getSeriesDataFromCSVColumns(csvFile);
    }
    return new SeriesData(
        getAxisData(xAndYData[0]),
        getAxisData(xAndYData[1]),
        csvFile.getName().substring(0, csvFile.getName().indexOf(".csv")));
  }

  /**
   * @param path2Directory
   * @param dataOrientation
   * @param width
   * @param height
   * @return
   */
  public static XYChart getChartFromCSVDir(
      String path2Directory, DataOrientation dataOrientation, int width, int height) {

    return getChartFromCSVDir(path2Directory, dataOrientation, width, height, null);
  }

  /**
   * Get the series's data from a file
   *
   * @param csvFile
   * @return
   */
  private static String[] getSeriesDataFromCSVRows(File csvFile) {

    String[] xAndYData = new String[3];

    BufferedReader bufferedReader = null;
    try {
      int counter = 0;
      String line;
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

  /**
   * @param csvFile
   * @return
   */
  private static String[] getSeriesDataFromCSVColumns(File csvFile) {

    String[] xAndYData = new String[3];
    xAndYData[0] = "";
    xAndYData[1] = "";
    xAndYData[2] = "";

    BufferedReader bufferedReader = null;
    try {
      String line;
      bufferedReader = new BufferedReader(new FileReader(csvFile));
      while ((line = bufferedReader.readLine()) != null) {
        String[] dataArray = line.split(",");
        xAndYData[0] += dataArray[0] + ",";
        xAndYData[1] += dataArray[1] + ",";
        if (dataArray.length > 2) {
          xAndYData[2] += dataArray[2] + ",";
        }
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

  /**
   * @param stringData
   * @return
   */
  private static List<Number> getAxisData(String stringData) {

    List<Number> axisData = new ArrayList<Number>();
    String[] stringDataArray = stringData.split(",");
    for (String dataPoint : stringDataArray) {
      try {
        Double value = Double.parseDouble(dataPoint);
        axisData.add(value);
      } catch (NumberFormatException e) {
        System.out.println("Error parsing >" + dataPoint + "< !");
        throw (e);
      }
    }
    return axisData;
  }

  /**
   * This method returns the files found in the given directory matching the given regular
   * expression.
   *
   * @param dirName - ex. "./path/to/directory/" *make sure you have the '/' on the end
   * @param regex - ex. ".*.csv"
   * @return File[] - an array of files
   */
  private static File[] getAllFiles(String dirName, String regex) {

    File[] allFiles = getAllFiles(dirName);

    List<File> matchingFiles = new ArrayList<File>();

    for (File allFile : allFiles) {

      if (allFile.getName().matches(regex)) {
        matchingFiles.add(allFile);
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
  private static File[] getAllFiles(String dirName) {

    File dir = new File(dirName);

    File[] files = dir.listFiles(); // returns files and folders

    if (files != null) {
      List<File> filteredFiles = new ArrayList<File>();
      for (File file : files) {

        if (file.isFile()) {
          filteredFiles.add(file);
        }
      }
      return filteredFiles.toArray(new File[filteredFiles.size()]);
    } else {
      System.out.println(dirName + " does not denote a valid directory!");
      return new File[0];
    }
  }

  public enum DataOrientation {
    Rows,
    Columns
  }

  public static class SeriesData {

    private final List<Number> xAxisData;
    private final List<Number> yAxisData;
    private final String seriesName;

    public SeriesData(List<Number> xAxisData, List<Number> yAxisData, String seriesName) {

      this.xAxisData = xAxisData;
      this.yAxisData = yAxisData;
      this.seriesName = seriesName;
    }

    public List<Number> getxAxisData() {

      return xAxisData;
    }

    public List<Number> getyAxisData() {

      return yAxisData;
    }

    public String getSeriesName() {

      return seriesName;
    }
  }
}
