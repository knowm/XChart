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

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes.
 *
 * @author timmolter
 */
public abstract class AxesChartSeries extends Series {

  protected abstract void calculateMinMax();

  final DataType xAxisDataType;
  final DataType yAxisType;

  /**
   * the minimum value of axis range
   */
  double xMin;

  /**
   * the maximum value of axis range
   */
  double xMax;

  /**
   * the minimum value of axis range
   */
  double yMin;

  /**
   * the maximum value of axis range
   */
  double yMax;

  /**
   * Line Style
   */
  private BasicStroke stroke;

  /**
   * Line Color
   */
  private Color lineColor;

  /**
   * Line Width
   */
  private float lineWidth = -1.0f;

  /**
   * Constructor
   *
   * @param name
   * @param xAxisDataType
   */
  AxesChartSeries(String name, DataType xAxisDataType) {

    super(name);
    this.xAxisDataType = xAxisDataType;
    yAxisType = DataType.Number;
  }

  /**
   * Set the line style of the series
   *
   * @param basicStroke
   */
  public AxesChartSeries setLineStyle(BasicStroke basicStroke) {

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
  public AxesChartSeries setLineColor(java.awt.Color color) {

    this.lineColor = color;
    return this;
  }

  /**
   * Set the line width of the series
   *
   * @param lineWidth
   */
  public AxesChartSeries setLineWidth(float lineWidth) {

    this.lineWidth = lineWidth;
    return this;
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

  public DataType getxAxisDataType() {

    return xAxisDataType;
  }

  public DataType getyAxisDataType() {

    return yAxisType;
  }
}