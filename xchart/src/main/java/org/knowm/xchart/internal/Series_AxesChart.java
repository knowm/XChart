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
import java.util.Iterator;
import java.util.List;

import org.knowm.xchart.internal.chartpart.Axis.AxisDataType;
import org.knowm.xchart.internal.style.markers.Marker;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes
 *
 * @author timmolter
 */
public abstract class Series_AxesChart extends Series {

  public abstract AxisDataType getAxesType(List<?> data);

  private List<?> xData; // can be Number or Date or String
  private AxisDataType xAxisType;

  private List<? extends Number> yData;
  private AxisDataType yAxisType;

  private List<? extends Number> errorBars;

  /** the minimum value of axis range */
  private double xMin;

  /** the maximum value of axis range */
  private double xMax;

  /** the minimum value of axis range */
  private double yMin;

  /** the maximum value of axis range */
  private double yMax;

  /** Line Style */
  private BasicStroke stroke;

  /** Line Color */
  private Color lineColor;

  /** Marker Style */
  private Marker marker;

  /** Marker Color */
  private Color markerColor;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param xAxisType
   * @param yData
   * @param yAxisType
   * @param errorBars
   */
  public Series_AxesChart(String name, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {

    super(name);

    this.xData = xData;
    this.xAxisType = getAxesType(xData);
    this.yData = yData;
    this.yAxisType = AxisDataType.Number;
    this.errorBars = errorBars;

    calculateMinMax();
  }

  public void replaceData(List<?> newXData, List<? extends Number> newYData, List<? extends Number> newErrorBars) {

    // Sanity check
    if (newErrorBars != null && newErrorBars.size() != newYData.size()) {
      throw new IllegalArgumentException("error bars and Y-Axis sizes are not the same!!!");
    }
    if (newXData.size() != newYData.size()) {
      throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
    }

    xData = newXData;
    yData = newYData;
    errorBars = newErrorBars;
    calculateMinMax();
  }

  private void calculateMinMax() {

    // xData
    double[] xMinMax = findMinMax(xData, xAxisType);
    xMin = xMinMax[0];
    xMax = xMinMax[1];
    // System.out.println(xMin);
    // System.out.println(xMax);

    // yData
    double[] yMinMax = null;
    if (errorBars == null) {
      yMinMax = findMinMax(yData, yAxisType);
    }
    else {
      yMinMax = findMinMaxWithErrorBars(yData, errorBars);
    }
    yMin = yMinMax[0];
    yMax = yMinMax[1];
    // System.out.println(yMin);
    // System.out.println(yMax);
  }

  /**
   * Finds the min and max of a dataset
   *
   * @param data
   * @return
   */
  private double[] findMinMax(Collection<?> data, AxisDataType axisType) {

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
   * Finds the min and max of a dataset accounting for error bars
   *
   * @param data
   * @return
   */
  private double[] findMinMaxWithErrorBars(Collection<? extends Number> data, Collection<? extends Number> errorBars) {

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
    return new double[] { min, max };
  }

  /**
   * Set the line style of the series
   *
   * @param basicStroke
   */
  public Series setLineStyle(BasicStroke basicStroke) {

    stroke = basicStroke;
    return this;
  }

  /**
   * Set the line color of the series
   *
   * @param color
   */
  public Series setLineColor(java.awt.Color color) {

    this.lineColor = color;
    return this;
  }

  /**
   * Sets the marker for the series
   *
   * @param marker
   */
  public Series setMarker(Marker marker) {

    this.marker = marker;
    return this;
  }

  /**
   * Sets the marker color for the series
   *
   * @param color
   */
  public Series setMarkerColor(java.awt.Color color) {

    this.markerColor = color;
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

  public Collection<? extends Number> getErrorBars() {

    return errorBars;
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

  public Marker getMarker() {

    return marker;
  }

  public Color getLineColor() {

    return lineColor;
  }

  public Color getMarkerColor() {

    return markerColor;
  }

}