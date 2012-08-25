/**
 * Copyright 2011-2012 Xeiam LLC.
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
package com.xeiam.xchart.series;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import com.xeiam.xchart.series.markers.Marker;

/**
 * @author timmolter
 */
public class Series {

  private String name = "";

  protected Collection<Number> xData;

  protected Collection<Number> yData;

  protected Collection<Number> errorBars;

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

  /** Marker Style */
  private Marker marker;

  /** Marker Color */
  private Color markerColor;

  /**
   * Constructor
   * 
   * @param name
   * @param xData
   * @param yData
   */
  public Series(String name, Collection<Number> xData, Collection<Number> yData, Collection<Number> errorBars) {

    this.name = name;
    this.xData = xData;
    this.yData = yData;
    this.errorBars = errorBars;

    // xData
    double[] xMinMax = findMinMax(xData);
    this.xMin = xMinMax[0];
    this.xMax = xMinMax[1];

    // yData
    double[] yMinMax = null;
    if (errorBars == null) {
      yMinMax = findMinMax(yData);
    } else {
      yMinMax = findMinMaxWithErrorBars(yData, errorBars);
    }
    this.yMin = yMinMax[0];
    this.yMax = yMinMax[1];
    // System.out.println(yMin);
    // System.out.println(yMax);

    Color color = SeriesColor.getNextAWTColor();
    this.strokeColor = color;
    this.markerColor = color;

    this.marker = SeriesMarker.getNextMarker();
    this.stroke = SeriesLineStyle.getNextBasicStroke();

  }

  /**
   * Finds the min and max of a dataset
   * 
   * @param data
   * @return
   */
  private double[] findMinMax(Collection<Number> data) {

    Double min = null;
    Double max = null;
    for (Number number : data) {
      verify(number.doubleValue());
      if (min == null || number.doubleValue() < min) {
        if (!Double.isNaN(number.doubleValue())) {
          min = number.doubleValue();
        }
      }
      if (max == null || number.doubleValue() > max) {
        if (!Double.isNaN(number.doubleValue())) {
          max = number.doubleValue();
        }
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
  private double[] findMinMaxWithErrorBars(Collection<Number> data, Collection<Number> errorBars) {

    Double min = null;
    Double max = null;

    Iterator<Number> itr = data.iterator();
    Iterator<Number> ebItr = errorBars.iterator();
    while (itr.hasNext()) {
      double number = itr.next().doubleValue();
      double eb = ebItr.next().doubleValue();
      verify(number);
      if (min == null || (number - eb) < min) {
        if (!Double.isNaN(number)) {
          min = number - eb;
        }
      }
      if (max == null || (number + eb) > max) {
        if (!Double.isNaN(number)) {
          max = number + eb;
        }
      }
    }
    return new double[] { min, max };
  }

  /**
   * Checks for invalid values in data array
   * 
   * @param data
   */
  private void verify(double value) {

    if (value == Double.POSITIVE_INFINITY) {
      throw new RuntimeException("Axis data cannot contain Double.POSITIVE_INFINITY!!!");
    } else if (value == Double.NEGATIVE_INFINITY) {
      throw new RuntimeException("Axis data cannot contain Double.NEGATIVE_INFINITY!!!");
    }
  }

  public String getName() {

    return name;
  }

  public Collection<Number> getxData() {

    return xData;
  }

  public Collection<Number> getyData() {

    return yData;
  }

  public Collection<Number> getErrorBars() {

    return errorBars;
  }

  public double getxMin() {

    return xMin;
  }

  public double getxMax() {

    return xMax;
  }

  public double getyMin() {

    return yMin;
  }

  public double getyMax() {

    return yMax;
  }

  public BasicStroke getLineStyle() {

    return stroke;
  }

  public void setLineStyle(SeriesLineStyle lineStyle) {

    this.stroke = SeriesLineStyle.getBasicStroke(lineStyle);
  }

  public void setLineStyle(BasicStroke lineStyle) {

    this.stroke = lineStyle;
  }

  public Color getLineColor() {

    return strokeColor;
  }

  public void setLineColor(SeriesColor lineColor) {

    this.strokeColor = SeriesColor.getAWTColor(lineColor);
  }

  public void setLineColor(java.awt.Color lineColor) {

    this.strokeColor = lineColor;
  }

  public Marker getMarker() {

    return marker;
  }

  public void setMarker(SeriesMarker marker) {

    this.marker = SeriesMarker.getMarker(marker);
  }

  public void setMarker(Marker marker) {

    this.marker = marker;
  }

  public Color getMarkerColor() {

    return markerColor;
  }

  public void setMarkerColor(SeriesColor lineColor) {

    this.markerColor = SeriesColor.getAWTColor(lineColor);
  }

  public void setMarkerColor(java.awt.Color lineColor) {

    this.markerColor = lineColor;
  }

}
