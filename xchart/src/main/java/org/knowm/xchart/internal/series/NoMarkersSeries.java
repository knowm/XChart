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

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes, values associated with each X-Y point, could be used for bubble sizes for example, but no error bars, as the min and max
 * are calculated differently. No markers.
 *
 * @author timmolter
 */
public abstract class NoMarkersSeries extends AxesChartSeriesNumericalNoErrorBars {

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param extraValues
   */
  protected NoMarkersSeries(String name, double[] xData, double[] yData, double[] extraValues, Series.DataType axisType) {

    super(name, xData, yData, extraValues, axisType);

    this.extraValues = extraValues;
    calculateMinMax();
  }

  @Override
  protected void calculateMinMax() {

    // xData
    double[] xMinMax = findMinMax(xData);
    xMin = xMinMax[0];
    xMax = xMinMax[1];
    // System.out.println(xMin);
    // System.out.println(xMax);

    // yData
    double[] yMinMax = null;
    yMinMax = findMinMax(yData);
    yMin = yMinMax[0];
    yMax = yMinMax[1];
    // System.out.println(yMin);
    // System.out.println(yMax);
  }
}