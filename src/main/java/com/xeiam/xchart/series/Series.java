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
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.xeiam.xchart.Axis.AxisType;
import com.xeiam.xchart.series.markers.Marker;

/**
 * @author timmolter
 */
public class Series {

  private String name = "";

  protected Collection<?> xData;

  protected Collection<Number> yData;

  protected Collection<Number> errorBars;

  /** the minimum value of axis range */
  private BigDecimal xMin;

  /** the maximum value of axis range */
  private BigDecimal xMax;

  /** the minimum value of axis range */
  private BigDecimal yMin;

  /** the maximum value of axis range */
  private BigDecimal yMax;

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
   * @param <?>
   * @param name
   * @param xData
   * @param yData
   */
  public Series(String name, Collection<?> xData, AxisType xAxisType, Collection<Number> yData, AxisType yAxisType, Collection<Number> errorBars) {

    this.name = name;
    this.xData = xData;
    this.yData = yData;
    this.errorBars = errorBars;

    // xData
    BigDecimal[] xMinMax = findMinMax(xData, xAxisType);
    xMin = xMinMax[0];
    xMax = xMinMax[1];

    // yData
    BigDecimal[] yMinMax = null;
    if (errorBars == null) {
      yMinMax = findMinMax(yData, yAxisType);
    } else {
      yMinMax = findMinMaxWithErrorBars(yData, errorBars);
    }
    yMin = yMinMax[0];
    yMax = yMinMax[1];
    // System.out.println(yMin);
    // System.out.println(yMax);

    Color color = SeriesColor.getNextAWTColor();
    strokeColor = color;
    markerColor = color;

    marker = SeriesMarker.getNextMarker();
    stroke = SeriesLineStyle.getNextBasicStroke();

  }

  /**
   * Finds the min and max of a dataset
   * 
   * @param data
   * @return
   */
  private BigDecimal[] findMinMax(Collection<?> data, AxisType axisType) {

    BigDecimal min = null;
    BigDecimal max = null;

    for (Object dataPoint : data) {

      BigDecimal bigDecimal = null;

      if (axisType == AxisType.NUMBER) {
        bigDecimal = new BigDecimal(((Number) dataPoint).toString());

      } else if (axisType == AxisType.DATE) {
        Date date = (Date) dataPoint;
        bigDecimal = new BigDecimal(date.getTime());
      }
      if (min == null || bigDecimal.compareTo(min) < 0) {
        min = bigDecimal;
      }
      if (max == null || bigDecimal.compareTo(max) > 0) {
        max = bigDecimal;
      }
    }

    return new BigDecimal[] { min, max };
  }

  /**
   * Finds the min and max of a dataset accounting for error bars
   * 
   * @param data
   * @return
   */
  private BigDecimal[] findMinMaxWithErrorBars(Collection<Number> data, Collection<Number> errorBars) {

    BigDecimal min = null;
    BigDecimal max = null;

    Iterator<Number> itr = data.iterator();
    Iterator<Number> ebItr = errorBars.iterator();
    while (itr.hasNext()) {
      BigDecimal bigDecimal = new BigDecimal(itr.next().doubleValue());
      BigDecimal eb = new BigDecimal(ebItr.next().doubleValue());
      if (min == null || (bigDecimal.subtract(eb)).compareTo(min) < 0) {
        min = bigDecimal.subtract(eb);
      }
      if (max == null || (bigDecimal.add(eb)).compareTo(max) > 0) {
        max = bigDecimal.add(eb);
      }
    }
    return new BigDecimal[] { min, max };
  }

  public String getName() {

    return name;
  }

  public Collection<?> getxData() {

    return xData;
  }

  public Collection<Number> getyData() {

    return yData;
  }

  public Collection<Number> getErrorBars() {

    return errorBars;
  }

  public BigDecimal getxMin() {

    return xMin;
  }

  public BigDecimal getxMax() {

    return xMax;
  }

  public BigDecimal getyMin() {

    return yMin;
  }

  public BigDecimal getyMax() {

    return yMax;
  }

  public BasicStroke getLineStyle() {

    return stroke;
  }

  public void setLineStyle(SeriesLineStyle lineStyle) {

    stroke = SeriesLineStyle.getBasicStroke(lineStyle);
  }

  public void setLineStyle(BasicStroke lineStyle) {

    stroke = lineStyle;
  }

  public Color getLineColor() {

    return strokeColor;
  }

  public void setLineColor(SeriesColor lineColor) {

    strokeColor = SeriesColor.getAWTColor(lineColor);
  }

  public void setLineColor(java.awt.Color lineColor) {

    strokeColor = lineColor;
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
