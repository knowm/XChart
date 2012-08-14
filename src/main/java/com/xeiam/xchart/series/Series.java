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

import com.xeiam.xchart.series.markers.Marker;


/**
 * @author timmolter
 */
public class Series {

  private String name = "";

  protected double[] xData;

  protected double[] yData;

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
  public Series(String name, double[] xData, double[] yData) {

    this.name = name;
    this.xData = xData;
    this.yData = yData;

    // xData
    double[] xMinMax = findMinMax(xData);
    this.xMin = xMinMax[0];
    this.xMax = xMinMax[1];

    // yData
    double[] yMinMax = findMinMax(yData);
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

  private double[] findMinMax(double[] data) {

    Double min = null;
    Double max = null;
    for (int i = 0; i < data.length; i++) {
      if (min == null || data[i] < min) {
        if (!Double.isNaN(data[i])) {
          min = data[i];
        }
      }
      if (max == null || data[i] > max) {
        if (!Double.isNaN(data[i])) {
          max = data[i];
        }
      }
    }
    return new double[] { min, max };
  }

  public String getName() {

    return name;
  }

  public double[] getxData() {

    return xData;
  }

  public double[] getyData() {

    return yData;
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
