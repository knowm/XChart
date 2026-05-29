package org.knowm.xchart.internal.series;

import java.util.Collection;
import java.util.List;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes. xData can be Number
 * or Date or String, hence a List<?>
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
    if (newExtraValues != null && newExtraValues.size() != newYData.size()) {
      throw new IllegalArgumentException("error bars and Y-Axis sizes are not the same!!!");
    }
    if (newXData.size() != newYData.size()) {
      throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
    }

    xData = newXData;
    yData = newYData;
    extraValues = newExtraValues;
    calculateMinMax();
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
    double[] xMinMax = SeriesMinMaxCalculator.findMinMax(xData, xAxisDataType);
    xMin = xMinMax[0];
    xMax = xMinMax[1];

    // yData
    double[] yMinMax;
    if (extraValues == null) {
      yMinMax = SeriesMinMaxCalculator.findMinMax(yData, yAxisType);
    } else {
      yMinMax = SeriesMinMaxCalculator.findMinMaxWithErrorBars(yData, extraValues);
    }
    yMin = yMinMax[0];
    yMax = yMinMax[1];
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
