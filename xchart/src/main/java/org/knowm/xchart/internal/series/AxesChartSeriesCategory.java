package org.knowm.xchart.internal.series;

import java.util.Collection;
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
		boolean notSameErrorBarsAndY_Axis = newExtraValues != null && newExtraValues.size() != newYData.size();
		boolean notSameXAndY_Axis = newXData.size() != newYData.size();
		
		if (notSameErrorBarsAndY_Axis) {
	      throw new IllegalArgumentException("error bars and Y-Axis sizes are not the same!!!");
	    }    
		if (notSameXAndY_Axis) {
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
	  setXYMinMax(MinMaxFactory
			.getMinMaxCalculator(extraValues, xAxisDataType, yAxisType)
			.calculateMinMax(xData, yData));
    // xData
    // System.out.println(xMin);
    // System.out.println(xMax);

    // yData
    // System.out.println(yMin);
    // System.out.println(yMax);
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
