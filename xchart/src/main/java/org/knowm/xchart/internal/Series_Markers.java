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

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.knowm.xchart.internal.chartpart.Axis.AxisDataType;
import org.knowm.xchart.style.markers.Marker;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes, contains series markers and error bars.
 *
 * @author timmolter
 */
public abstract class Series_Markers extends Series_AxesChart {

  @Override
  public abstract AxisDataType getAxesType(List<?> data);

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
   * @param errorBars
   */
  public Series_Markers(String name, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {

    super(name, xData, yData, errorBars);

    this.extraValues = errorBars;
    calculateMinMax();
  }

  @Override
  public void calculateMinMax() {

    // xData
    double[] xMinMax = findMinMax(xData, xAxisType);
    xMin = xMinMax[0];
    xMax = xMinMax[1];
    // System.out.println(xMin);
    // System.out.println(xMax);

    // yData
    double[] yMinMax = null;
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
   * Sets the marker for the series
   *
   * @param marker
   */
  public Series_Markers setMarker(Marker marker) {

    this.marker = marker;
    return this;
  }

  /**
   * Sets the marker color for the series
   *
   * @param color
   */
  public Series_Markers setMarkerColor(java.awt.Color color) {

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