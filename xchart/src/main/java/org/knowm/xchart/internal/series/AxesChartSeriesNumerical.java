package org.knowm.xchart.internal.series;

import java.util.Arrays;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes. xData can be Number
 * or Date(epochtime), hence a double[]
 */
public abstract class AxesChartSeriesNumerical extends MarkerSeries {

  // full unfiltered data — retained so zoom can be reset to the original range
  // TODO(zoom-memory): For large datasets, xData/yData/extraValues duplicate xDataAll/yDataAll/
  //   extraValuesAll for zoom filtering. A DataRange(startIndex, endIndex) abstraction could
  //   replace the duplicated arrays for the index-based zoom path (filterXByIndex). The
  //   value-based path (filterXByValue) is non-contiguous and would need a full index-mapping
  //   approach (e.g. int[] filteredIndices), which is more invasive. Skipping for now to avoid
  //   risk to ChartZoom and OHLCSeries which duplicate this same pattern.
  double[] xDataAll;
  double[] yDataAll;
  double[] extraValuesAll;

  // active (possibly zoom-filtered) data — what the chart actually renders
  double[] xData; // can be Number or Date(epochtime)
  double[] yData;
  double[] extraValues;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param xAxisDataType
   */
  public AxesChartSeriesNumerical(
      String name, double[] xData, double[] yData, double[] extraValues, DataType xAxisDataType) {

    super(name, xAxisDataType);

    this.xDataAll = xData;
    this.yDataAll = yData;
    this.extraValuesAll = extraValues;

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
  public void replaceData(double[] newXData, double[] newYData, double[] newExtraValues) {

    // Sanity check
    if (newExtraValues != null && newExtraValues.length != newYData.length) {
      throw new IllegalArgumentException("error bars and Y-Axis sizes are not the same!!!");
    }
    if (newXData.length != newYData.length) {
      throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
    }

    this.xDataAll = newXData;
    this.yDataAll = newYData;
    this.extraValuesAll = newExtraValues;

    xData = newXData;
    yData = newYData;
    extraValues = newExtraValues;

    calculateMinMax();
  }

  public void filterXByIndex(int startIndex, int endIndex) {

    startIndex = Math.max(0, startIndex);
    endIndex = Math.min(yDataAll.length, endIndex);

    xData = Arrays.copyOfRange(xDataAll, startIndex, endIndex);
    yData = Arrays.copyOfRange(yDataAll, startIndex, endIndex);
    if (extraValuesAll != null) {
      extraValues = Arrays.copyOfRange(extraValuesAll, startIndex, endIndex);
    }

    calculateMinMax();
  }

  public boolean filterXByValue(double minValue, double maxValue) {

    int length = xDataAll.length;
    boolean[] filterResult = new boolean[length];
    int remainingDataCount = 0;
    for (int i = 0; i < length; i++) {
      double val = xDataAll[i];
      boolean result = val >= minValue && val <= maxValue;
      filterResult[i] = result;
      if (result) {
        remainingDataCount++;
      }
    }

    // System.out.println("Filtering between " + String.format("%.2f %.2f", minValue, maxValue) + "
    // all: " + length + " rem: " + remainingDataCount);
    if (remainingDataCount == length) {
      return false;
    }

    xData = new double[remainingDataCount];
    yData = new double[remainingDataCount];
    boolean extra = extraValuesAll != null;

    if (extra) {
      extraValues = new double[remainingDataCount];
    }

    int ind = 0;
    for (int i = 0; i < length; i++) {
      if (!filterResult[i]) {
        continue;
      }
      xData[ind] = xDataAll[i];
      yData[ind] = yDataAll[i];
      if (extra) {
        extraValues[ind] = extraValuesAll[i];
      }
      ind++;
    }

    calculateMinMax();
    return true;
  }

  public void resetFilter() {

    xData = xDataAll;
    yData = yDataAll;
    extraValues = extraValuesAll;
    calculateMinMax();
  }

  /**
   * Finds the min and max of a dataset
   *
   * @param data
   * @return
   */
  double[] findMinMax(double[] data) {

    return SeriesMinMaxCalculator.findMinMax(data);
  }

  @Override
  protected void calculateMinMax() {

    // xData
    double[] xMinMax = SeriesMinMaxCalculator.findMinMax(xData);
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

  /**
   * Is xData.length equal to xDataAll.length
   *
   * @return true: equal; false: not equal
   */
  public boolean isAllXData() {

    return xData.length == xDataAll.length;
  }

  public double[] getXData() {

    return xData;
  }

  public double[] getYData() {

    return yData;
  }

  public double[] getExtraValues() {

    return extraValues;
  }
}
