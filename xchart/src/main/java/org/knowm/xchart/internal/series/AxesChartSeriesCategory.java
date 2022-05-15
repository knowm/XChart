package org.knowm.xchart.internal.series;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes. xData can be Number
 * or Date or String, hence a List<?>
 *
 * @author timmolter
 */
public abstract class AxesChartSeriesCategory extends MarkerSeries {

  List<?> xData; // can be Number or Date or String

  List<? extends Number> yData;

  List<? extends Number> extraValues;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   */
  public AxesChartSeriesCategory(
      String name,
      List<?> xData,
      List<? extends Number> yData,
      List<? extends Number> extraValues,
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
    dataSanityCheck(newXData, newYData, newExtraValues);

    updateData(newXData, newYData, newExtraValues);
    calculateMinMax();
  }

  private void updateData(List<?> newXData, List<? extends Number> newYData, List<? extends Number> newExtraValues) {
	xData = newXData;
    yData = newYData;
    extraValues = newExtraValues;
  }

  private void dataSanityCheck(List<?> newXData, List<? extends Number> newYData, List<? extends Number> newExtraValues) {
	if (newExtraValues != null && newExtraValues.size() != newYData.size()) {
      throw new IllegalArgumentException("error bars and Y-Axis sizes are not the same!!!");
    }
    if (newXData.size() != newYData.size()) {
      throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
    }
  }

  /**
   * For box plot, replace yData
   *
   * @param newYData Updated yData
   */
  public void replaceData(List<? extends Number> newYData) {

    yData = newYData;
    calculateMinMax();
  }

  @Override
  protected void calculateMinMax() {

    // xData
    calculateXMinMax();
    // System.out.println(xMin);
    // System.out.println(xMax);

    // yData
    calculateYMinMax();
    // System.out.println(yMin);
    // System.out.println(yMax);
  }

  private void calculateYMinMax() {
	double[] yMinMax;
    if (extraValues == null) {
      yMinMax = findMinMax(yData, yAxisType);
    } else {
      yMinMax = findMinMaxWithErrorBars(yData, extraValues);
    }
    yMin = yMinMax[0];
    yMax = yMinMax[1];
  }

  private void calculateXMinMax() {
	double[] xMinMax = findMinMax(xData, xAxisDataType);
    xMin = xMinMax[0];
    xMax = xMinMax[1];
  }

  /**
   * Finds the min and max of a dataset accounting for error bars
   *
   * @param data
   * @param errorBars
   * @return
   */
  private double[] findMinMaxWithErrorBars(
      Collection<? extends Number> data, Collection<? extends Number> errorBars) {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;

    Iterator<? extends Number> itr = data.iterator();
    Iterator<? extends Number> ebItr = errorBars.iterator();
    while (itr.hasNext()) {
      double bigDecimal = itr.next().doubleValue();
      double eb = ebItr.next().doubleValue();
      if (bigDecimal - eb < min) {
        min = bigDecimal - eb;
      }
      if (bigDecimal + eb > max) {
        max = bigDecimal + eb;
      }
    }
    return new double[] {min, max};
  }

  /**
   * Finds the min and max of a dataset
   *
   * @param data
   * @return
   */
  double[] findMinMax(Collection<?> data, DataType dataType) {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;

    for (Object dataPoint : data) {

      if (dataPoint == null) {
        continue;
      }
      if (dataType == DataType.String) {
          return new double[] {Double.NaN, Double.NaN};
      }
      
      if (dataValue(dataType, dataPoint) < min) {
        min = dataValue(dataType, dataPoint);
      }
      if (dataValue(dataType, dataPoint) > max) {
        max = dataValue(dataType, dataPoint);
      }
    }

    return new double[] {min, max};
  }

  private double dataValue(DataType dataType, Object dataPoint) {
	if (dataType == DataType.Number) {
        return ((Number) dataPoint).doubleValue();
    }
    return ((Date)dataPoint).getTime();
  }

  public Collection<?> getXData() {

    return xData;
  }

  public Collection<? extends Number> getYData() {

    return yData;
  }

  public Collection<? extends Number> getExtraValues() {

    return extraValues;
  }
}
