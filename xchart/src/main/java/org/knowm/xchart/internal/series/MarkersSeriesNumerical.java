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

import java.awt.Color;

import org.knowm.xchart.style.markers.Marker;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes, contains series markers and error bars.
 *
 * @author timmolter
 */
public abstract class MarkersSeriesNumerical extends AxesChartSeriesNumerical {

  /**
   * Marker
   */
  private Marker marker;

  /**
   * Marker Color
   */
  private Color markerColor;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param extraValues
   * @param axisType
   */
  protected MarkersSeriesNumerical(String name, double[] xData, double[] yData, double[] extraValues, DataType axisType) {

    super(name, xData, yData, axisType);

    this.extraValues = extraValues;
    calculateMinMax();
  }

  @Override
  protected void calculateMinMax() {

    // xData
    double[] xMinMax = findMinMax(xData, xAxisDataType);
    xMin = xMinMax[0];
    xMax = xMinMax[1];
    // System.out.println(xMin);
    // System.out.println(xMax);

    // yData
    double[] yMinMax;
    if (extraValues == null) {
      yMinMax = findMinMax(yData, yAxisType);
    }
    else {
      yMinMax = findMinMaxWithErrorBars(yData, extraValues);
    }
    yMin = yMinMax[0];
    yMax = yMinMax[1];
    // System.out.println(yMin);
    // System.out.println(yMax);
  }

  /**
   * Finds the min and max of a dataset accounting for error bars
   *
   * @param data
   * @param errorBars
   * @return
   */
  private double[] findMinMaxWithErrorBars(double[] data, double[] errorBars) {

    double min = Double.MAX_VALUE;
    double max = -Double.MAX_VALUE;

    for (int i = 0; i < data.length; i++) {

      double d = data[i];
      double eb = errorBars[i];
      if (d - eb < min) {
        min = d - eb;
      }
      if (d + eb > max) {
        max = d + eb;
      }
    }
    return new double[] { min, max };
  }

  /**
   * Sets the marker for the series
   *
   * @param marker
   */
  public MarkersSeriesNumerical setMarker(Marker marker) {

    this.marker = marker;
    return this;
  }

  /**
   * Sets the marker color for the series
   *
   * @param color
   */
  public MarkersSeriesNumerical setMarkerColor(java.awt.Color color) {

    this.markerColor = color;
    return this;
  }

  public Marker getMarker() {

    return marker;
  }

  public Color getMarkerColor() {

    return markerColor;
  }
}