/**
 * Copyright 2011 - 2014 Xeiam LLC.
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
package com.xeiam.xchart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.markers.Marker;
import com.xeiam.xchart.internal.style.SeriesColorMarkerLineStyle;

/**
 * A Series containing X and Y data to be plotted on a Chart
 * 
 * @author timmolter
 */
public class Series {

  public enum SeriesType {
    Line, Area
  }

  private String name = "";

  private Collection<?> xData;
  private AxisType xAxisType;

  private Collection<? extends Number> yData;
  private AxisType yAxisType;

  private SeriesType seriesType;

  private Collection<? extends Number> errorBars;

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
  private Color strokeColor;

  /** Fill Colour */
  private Color fillColor;

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
   * @param seriesColorMarkerLineStyle
   */
  public Series(String name, Collection<?> xData, AxisType xAxisType, Collection<? extends Number> yData, AxisType yAxisType, Collection<? extends Number> errorBars,
      SeriesColorMarkerLineStyle seriesColorMarkerLineStyle) {

    if (name == null || name.length() < 1) {
      throw new IllegalArgumentException("Series name cannot be null or zero-length!!!");
    }
    this.name = name;
    this.xData = xData;
    this.xAxisType = xAxisType;
    this.yData = yData;
    this.yAxisType = yAxisType;
    this.errorBars = errorBars;

    strokeColor = seriesColorMarkerLineStyle.getColor();
    fillColor = seriesColorMarkerLineStyle.getColor();
    markerColor = seriesColorMarkerLineStyle.getColor();
    marker = seriesColorMarkerLineStyle.getMarker();
    stroke = seriesColorMarkerLineStyle.getStroke();

    calculateMinMax();
  }

  /**
   * Finds the min and max of a dataset
   * 
   * @param data
   * @return
   */
  private double[] findMinMax(Collection<?> data, AxisType axisType) {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;

    for (Object dataPoint : data) {

      if (dataPoint == null) {
        continue;
      }

      double value = 0.0;

      if (axisType == AxisType.Number) {
        value = ((Number) dataPoint).doubleValue();
      }
      else if (axisType == AxisType.Date) {
        Date date = (Date) dataPoint;
        value = date.getTime();
      }
      else if (axisType == AxisType.String) {
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
   * @param seriesLineStyle
   */
  public Series setLineStyle(SeriesLineStyle seriesLineStyle) {

    stroke = SeriesLineStyle.getBasicStroke(seriesLineStyle);
    return this;
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
   * @param seriesColor
   */
  public Series setLineColor(SeriesColor seriesColor) {

    strokeColor = seriesColor.getColor();
    return this;
  }

  /**
   * Set the line color of the series
   * 
   * @param color
   */
  public Series setLineColor(java.awt.Color color) {

    strokeColor = color;
    return this;
  }

  /**
   * Sets the marker for the series
   * 
   * @param seriesMarker
   */
  public Series setMarker(SeriesMarker seriesMarker) {

    this.marker = seriesMarker.getMarker();
    return this;
  }

  /**
   * Sets the marker color for the series
   * 
   * @param seriesColor
   */
  public Series setMarkerColor(SeriesColor seriesColor) {

    this.markerColor = seriesColor.getColor();
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

  public SeriesType getSeriesType() {

    return seriesType;
  }

  public void setSeriesType(SeriesType seriesType) {

    this.seriesType = seriesType;
  }

  public Collection<?> getXData() {

    return xData;
  }

  public Collection<? extends Number> getYData() {

    return yData;
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

  public BasicStroke getStroke() {

    return stroke;
  }

  public Marker getMarker() {

    return marker;
  }

  public Color getStrokeColor() {

    return strokeColor;
  }

  public Color getMarkerColor() {

    return markerColor;
  }

  public Color getFillColor() {

    return fillColor;
  }

  public void setFillColor(Color fillColor) {

    this.fillColor = fillColor;
  }

  public String getName() {

    return name;
  }

  public void replaceXData(Collection<?> newXData) {

    xData = newXData;
    calculateMinMax();
  }

  public void replaceYData(Collection<? extends Number> newYData) {

    yData = newYData;
    calculateMinMax();
  }

  public void replaceErrroBarData(Collection<? extends Number> newErrorBars) {

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
}
