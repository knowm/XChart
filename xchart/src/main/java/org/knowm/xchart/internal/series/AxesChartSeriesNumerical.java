/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.internal.series;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes. xData can be Number or Date(epochtime), hence a double[]
 *
 * @author timmolter
 */
public abstract class AxesChartSeriesNumerical extends AxesChartSeries {

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
  AxesChartSeriesNumerical(String name, double[] xData, double[] yData, DataType xAxisDataType) {

    super(name, xAxisDataType);

    this.xData = xData;
    this.yData = yData;

    calculateMinMax();
  }

  /**
   * This is an internal method which shouldn't be called from client code. Use XYChart.updateXYSeries or CategoryChart.updateXYSeries instead!
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

    xData = newXData;
    yData = newYData;
    extraValues = newExtraValues;
    calculateMinMax();
  }

  /**
   * Finds the min and max of a dataset
   *
   * @param data
   * @return
   */
  double[] findMinMax(double[] data, DataType dataType) {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;

    for (double dataPoint : data) {

      if (dataPoint == Double.NaN) {
        continue;
      }

      if (dataType == DataType.String) {
        return new double[] { Double.NaN, Double.NaN };
      }
      else {
        if (dataPoint < min) {
          min = dataPoint;
        }
        if (dataPoint > max) {
          max = dataPoint;
        }
      }
    }

    return new double[] { min, max };
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