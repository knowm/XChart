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
package com.xeiam.xchart.internal.chartpart;

import java.util.LinkedList;
import java.util.List;

import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.internal.Utils;
import com.xeiam.xchart.internal.chartpart.Axis.Direction;

/**
 * @author timmolter
 */
public abstract class AxisTickCalculator {

  /** the List of tick label position in pixels */
  protected List<Integer> tickLocations = new LinkedList<Integer>();;

  /** the List of tick label values */
  protected List<String> tickLabels = new LinkedList<String>();

  protected final Direction axisDirection;

  protected final int workingSpace;

  protected final double minValue;

  protected final double maxValue;

  protected final StyleManager styleManager;

  /**
   * Constructor
   * 
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styleManager
   */
  public AxisTickCalculator(Direction axisDirection, int workingSpace, double minValue, double maxValue, StyleManager styleManager) {

    // override min/max value for bar charts' Y-Axis
    double overrideMinValue = minValue;
    double overrideMaxValue = maxValue;
    if (styleManager.getChartType() == ChartType.Bar && axisDirection == Direction.Y) { // this is the Y-Axis for a bar chart
      if (minValue > 0.0 && maxValue > 0.0) {
        overrideMinValue = 0.0;
      }
      if (minValue < 0.0 && maxValue < 0.0) {
        overrideMaxValue = 0.0;
      }
    }

    if (styleManager.getChartType() == ChartType.Bar && styleManager.isYAxisLogarithmic()) {
      int logMin = (int) Math.floor(Math.log10(minValue));
      overrideMinValue = Utils.pow(10, logMin);
    }

    // override min and maxValue if specified
    if (axisDirection == Direction.X && styleManager.getXAxisMin() != null) {
      overrideMinValue = styleManager.getXAxisMin();
    }
    if (axisDirection == Direction.Y && styleManager.getYAxisMin() != null) {
      overrideMinValue = styleManager.getYAxisMin();
    }
    if (axisDirection == Direction.X && styleManager.getXAxisMax() != null) {
      overrideMaxValue = styleManager.getXAxisMax();
    }
    if (axisDirection == Direction.Y && styleManager.getYAxisMax() != null) {
      overrideMaxValue = styleManager.getYAxisMax();
    }
    this.axisDirection = axisDirection;
    this.workingSpace = workingSpace;
    this.minValue = overrideMinValue;
    this.maxValue = overrideMaxValue;
    this.styleManager = styleManager;
  }

  /**
   * Gets the first position
   * 
   * @param gridStep
   * @return
   */
  double getFirstPosition(double gridStep) {

    double firstPosition;
    if (minValue % gridStep <= 0.0) {
      firstPosition = minValue - (minValue % gridStep);
    }
    else {
      firstPosition = minValue - (minValue % gridStep) + gridStep;
    }
    return firstPosition;
  }

  public List<Integer> getTickLocations() {

    return tickLocations;
  }

  public List<String> getTickLabels() {

    return tickLabels;
  }

}
