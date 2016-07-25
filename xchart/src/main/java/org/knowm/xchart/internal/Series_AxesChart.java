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
package org.knowm.xchart.internal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.knowm.xchart.internal.chartpart.Axis.AxisDataType;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes. Extra values allow for error bars of other uses such as bubbles.
 *
 * @author timmolter
 */
public abstract class Series_AxesChart extends Series {

  public abstract AxisDataType getAxesType(List<?> data);

  public abstract void calculateMinMax();

  List<?> xData; // can be Number or Date or String
  AxisDataType xAxisType;

  List<? extends Number> yData;
  AxisDataType yAxisType;

  List<? extends Number> extraValues;

  /** the minimum value of axis range */
  double xMin;

  /** the maximum value of axis range */
  double xMax;

  /** the minimum value of axis range */
  double yMin;

  /** the maximum value of axis range */
  double yMax;

  /** Line Style */
  BasicStroke stroke;

  /** Line Color */
  Color lineColor;

  /** Line Width */
  float lineWidth = -1.0f;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param xAxisType
   * @param yData
   * @param yAxisType
   * @param newExtraValues
   */
  public Series_AxesChart(String name, List<?> xData, List<? extends Number> yData, List<? extends Number> newExtraValues) {

    super(name);

    this.xData = xData;
    this.xAxisType = getAxesType(xData);
    this.yData = yData;
    this.yAxisType = AxisDataType.Number;

    calculateMinMax();
  }

  /**
   * This is an internal method which shouldn't be called from client code. Use XYChart.updateXYSeries or CategoryChart.updateXYSeries instead!
   *
   * @param newXData
   * @param newYData
   * @param newExtraValues
   */
  public void replaceData(List<?> newXData, List<? extends Number> newYData, List<? extends Number> newExtraValues) {

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
   * Finds the min and max of a dataset
   *
   * @param data
   * @return
   */
  double[] findMinMax(Collection<?> data, AxisDataType axisType) {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;

    for (Object dataPoint : data) {

      if (dataPoint == null) {
        continue;
      }

      double value = 0.0;

      if (axisType == AxisDataType.Number) {
        value = ((Number) dataPoint).doubleValue();
      }
      else if (axisType == AxisDataType.Date) {
        Date date = (Date) dataPoint;
        value = date.getTime();
      }
      else if (axisType == AxisDataType.String) {
        return new double[] { Double.NaN, Double.NaN };
      }
      if (value < min) {
        min = value;
      }
      if (value > max) {
        max = value;
      }
    }

    return new double[] { min, max };
  }

  /**
   * Set the line style of the series
   *
   * @param basicStroke
   */
  public Series_AxesChart setLineStyle(BasicStroke basicStroke) {

    stroke = basicStroke;
    if (this.lineWidth > 0.0f) {
      stroke = new BasicStroke(lineWidth, this.stroke.getEndCap(), this.stroke.getLineJoin(), this.stroke.getMiterLimit(), this.stroke.getDashArray(), this.stroke.getDashPhase());
    }
    return this;
  }

  /**
   * Set the line color of the series
   *
   * @param color
   */
  public Series_AxesChart setLineColor(java.awt.Color color) {

    this.lineColor = color;
    return this;
  }

  /**
   * Set the line width of the series
   *
   * @param color
   */
  public Series_AxesChart setLineWidth(float lineWidth) {

    this.lineWidth = lineWidth;
    return this;
  }

  public Collection<?> getXData() {

    return xData;
  }

  public AxisDataType getxAxisDataType() {

    return xAxisType;
  }

  public Collection<? extends Number> getYData() {

    return yData;
  }

  public AxisDataType getyAxisDataType() {

    return yAxisType;
  }

  public Collection<? extends Number> getExtraValues() {

    return extraValues;
  }

  public double getXMin() {

    return xMin;
  }

  public double getXMax() {

    return xMax;
  }

  public double getYMin() {

    return yMin;
  }

  public double getYMax() {

    return yMax;
  }

  public BasicStroke getLineStyle() {

    return stroke;
  }

  public Color getLineColor() {

    return lineColor;
  }

  public float getLineWidth() {

    return lineWidth;
  }

}