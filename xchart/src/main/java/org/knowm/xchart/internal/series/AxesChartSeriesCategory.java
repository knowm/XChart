package org.knowm.xchart.internal.series;

import java.util.Collection;
import java.util.List;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes. xData can be Number
 * or Date or String, hence a List<?>. yData and extraValues are always numeric and stored as
 * primitive double[] to avoid boxing overhead.
 */
public abstract class AxesChartSeriesCategory extends MarkerSeries {

  List<?> xData; // can be Number or Date or String

  double[] yData;

  double[] extraValues;

  /**
   * Constructor — accepts lists (List API path; yData and extraValues are converted to double[]
   * internally, mapping null elements to Double.NaN).
   *
   * @param name
   * @param xData
   * @param yData
   * @param extraValues
   * @param xAxisDataType
   */
  public AxesChartSeriesCategory(
      String name,
      List<?> xData,
      List<? extends Number> yData,
      List<? extends Number> extraValues,
      DataType xAxisDataType) {

    super(name, xAxisDataType);

    this.xData = xData;
    this.yData = numberListToDoubleArray(yData);
    this.extraValues = numberListToDoubleArray(extraValues);

    calculateMinMax();
  }

  /**
   * Constructor — direct primitive-array path; avoids boxing entirely.
   *
   * @param name
   * @param xData
   * @param yData
   * @param extraValues
   * @param xAxisDataType
   */
  public AxesChartSeriesCategory(
      String name,
      List<?> xData,
      double[] yData,
      double[] extraValues,
      DataType xAxisDataType) {

    super(name, xAxisDataType);

    this.xData = xData;
    this.yData = yData;
    this.extraValues = extraValues;

    calculateMinMax();
  }

  /**
   * This is an internal method which shouldn't be called from client code. Use
   * XYChart.updateXYSeries or CategoryChart.updateXYSeries instead!
   *
   * @param newXData
   * @param newYData
   * @param newExtraValues
   */
  public void replaceData(
      List<?> newXData, List<? extends Number> newYData, List<? extends Number> newExtraValues) {

    // Sanity check
    if (newExtraValues != null && newExtraValues.size() != newYData.size()) {
      throw new IllegalArgumentException("error bars and Y-Axis sizes are not the same!!!");
    }
    if (newXData.size() != newYData.size()) {
      throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
    }

    xData = newXData;
    yData = numberListToDoubleArray(newYData);
    extraValues = numberListToDoubleArray(newExtraValues);
    calculateMinMax();
  }

  /**
   * Direct primitive-array update path — avoids boxing entirely.
   *
   * @param newXData
   * @param newYData
   * @param newExtraValues
   */
  public void replaceData(List<?> newXData, double[] newYData, double[] newExtraValues) {

    // Sanity check
    if (newExtraValues != null && newExtraValues.length != newYData.length) {
      throw new IllegalArgumentException("error bars and Y-Axis sizes are not the same!!!");
    }
    if (newXData.size() != newYData.length) {
      throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
    }

    xData = newXData;
    yData = newYData;
    extraValues = newExtraValues;
    calculateMinMax();
  }

  /**
   * For box plot, replace yData via list (maps null → NaN).
   *
   * @param newYData Updated yData
   */
  public void replaceData(List<? extends Number> newYData) {

    yData = numberListToDoubleArray(newYData);
    calculateMinMax();
  }

  /**
   * For box plot, replace yData via primitive array — avoids boxing entirely.
   *
   * @param newYData Updated yData
   */
  public void replaceData(double[] newYData) {

    yData = newYData;
    calculateMinMax();
  }

  @Override
  protected void calculateMinMax() {

    // xData
    double[] xMinMax = SeriesMinMaxCalculator.findMinMax(xData, xAxisDataType);
    xMin = xMinMax[0];
    xMax = xMinMax[1];

    // yData
    double[] yMinMax;
    if (extraValues == null) {
      yMinMax = SeriesMinMaxCalculator.findMinMax(yData);
    } else {
      yMinMax = SeriesMinMaxCalculator.findMinMaxWithErrorBars(yData, extraValues);
    }
    yMin = yMinMax[0];
    yMax = yMinMax[1];
  }

  public Collection<?> getXData() {

    return xData;
  }

  public double[] getYData() {

    return yData;
  }

  public double[] getExtraValues() {

    return extraValues;
  }

  /** Converts a {@code List<? extends Number>} to a {@code double[]}, mapping null → NaN. */
  static double[] numberListToDoubleArray(List<? extends Number> list) {

    if (list == null) {
      return null;
    }
    double[] arr = new double[list.size()];
    int i = 0;
    for (Number n : list) {
      arr[i++] = (n == null) ? Double.NaN : n.doubleValue();
    }
    return arr;
  }
}
