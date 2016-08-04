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

import java.util.List;

import org.knowm.xchart.internal.chartpart.Axis.AxisDataType;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes, values associated with each X-Y point, could be used for bubble sizes for example, but error bars, as the min and max
 * are calculated differently. No markers.
 *
 * @author timmolter
 */
public abstract class Series_Bubble extends Series_AxesChart {

  @Override
  public abstract AxisDataType getAxesType(List<?> data);

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param extraValues
   */
  public Series_Bubble(String name, List<?> xData, List<? extends Number> yData, List<? extends Number> extraValues) {

    super(name, xData, yData, extraValues);

    this.extraValues = extraValues;
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
    yMinMax = findMinMax(yData, yAxisType);
    yMin = yMinMax[0];
    yMax = yMinMax[1];
    // System.out.println(yMin);
    // System.out.println(yMax);
  }

}