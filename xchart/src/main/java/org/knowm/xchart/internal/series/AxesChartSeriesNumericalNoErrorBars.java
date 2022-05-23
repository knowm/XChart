package org.knowm.xchart.internal.series;

import org.knowm.xchart.internal.Utils;

import java.util.Arrays;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes. xData can be Number
 * or Date(epochtime), hence a double[]
 *
 * @author timmolter
 */
// TODO weird name of class since it does contain extravalues for error bars!
public abstract class AxesChartSeriesNumericalNoErrorBars extends MarkerSeries {

  // permanent data
  double[] xDataAll;
  double[] yDataAll;
  double[] extraValuesAll;

  // temporary data different from permanent data if some is filter out for zooming
  double[] xData; // can be Number or Date(epochtime)
  double[] yData;
  double[] extraValues;

  /**
   * Constructor
   *
   * @param name
   * @param seriesDataValues
   * @param xAxisDataType
   */
  public AxesChartSeriesNumericalNoErrorBars(
      String name, SeriesDataValues seriesDataValues, DataType xAxisDataType) {

    super(name, xAxisDataType);

    this.xDataAll = seriesDataValues.getXData();
    this.yDataAll = seriesDataValues.getYData();
    this.extraValuesAll = seriesDataValues.getXData();

    this.xData = seriesDataValues.getXData();
    this.yData = seriesDataValues.getYData();
    this.extraValues = seriesDataValues.getXData();

    calculateMinMax();
  }

  /**
   * This is an internal method which shouldn't be called from client code. Use
   * XYChart.updateXYSeries or CategoryChart.updateXYSeries instead!
   *
   * @param seriesDataValues
   */
  public void replaceData(SeriesDataValues seriesDataValues) {
	dataSanityCheck(seriesDataValues);

    this.xDataAll = seriesDataValues.getXData();
    this.yDataAll = seriesDataValues.getYData();
    this.extraValuesAll = seriesDataValues.getExtraValues();

    xData = seriesDataValues.getXData();
    yData = seriesDataValues.getYData();
    extraValues = seriesDataValues.getExtraValues();

    calculateMinMax();
  }

	public void dataSanityCheck(SeriesDataValues seriesDataValues) {
        seriesDataValues.dataSanityCheck();
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

  @Override
  protected void calculateMinMax() {

    // xData
    xMin = Utils.findMin(xData);
    xMax = Utils.findMax(xData);

    // yData
    if (extraValues == null) {
      yMin = Utils.findMin(yData);
      yMax = Utils.findMax(yData);
    } else {
      double[] yMinMax = findMinMaxWithErrorBars(yData, extraValues);
      yMin = yMinMax[0];
      yMax = yMinMax[1];
    }
  }

  /**
   * Finds the min and max of a dataset accounting for error bars
   *
   * @param data
   * @param errorBars
   * @return
   */
  private double[] findMinMaxWithErrorBars(double[] data, double[] errorBars) {
    assert data.length == errorBars.length;
    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;
    for (int i = 0; i < data.length; i++) {
      double datum = data[i];
      double errorBar = errorBars[i];
      if (datum - errorBar < min) {
        min = datum - errorBar;
      }
      if (datum + errorBar > max) {
        max = datum + errorBar;
      }
    }
    return new double[] {min, max};
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
