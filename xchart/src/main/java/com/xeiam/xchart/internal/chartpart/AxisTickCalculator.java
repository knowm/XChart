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
  protected List<Double> tickLocations = new LinkedList<Double>();;

  /** the List of tick label values */
  protected List<String> tickLabels = new LinkedList<String>();

  protected final Direction axisDirection;

  protected final double workingSpace;

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
  public AxisTickCalculator(Direction axisDirection, double workingSpace, double minValue, double maxValue, StyleManager styleManager) {

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
    if (axisDirection == Direction.X && styleManager.getXAxisMin() != null && styleManager.getChartType() != ChartType.Bar) {
      overrideMinValue = styleManager.getXAxisMin();
    }
    if (axisDirection == Direction.Y && styleManager.getYAxisMin() != null) {
      overrideMinValue = styleManager.getYAxisMin();
    }
    if (axisDirection == Direction.X && styleManager.getXAxisMax() != null && styleManager.getChartType() != ChartType.Bar) {
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

    // System.out.println("******");

    // double firstPosition = minValue - (minValue % gridStep) + gridStep;
    double firstPosition = minValue - (minValue % gridStep) - gridStep;
    //
    // if ((firstPosition - minValue) > gridStep) {
    // firstPosition = minValue - (minValue % gridStep);
    // }
    return firstPosition;
  }

  public List<Double> getTickLocations() {

    return tickLocations;
  }

  public List<String> getTickLabels() {

    return tickLabels;
  }

  /**
   * Determine the grid step for the data set given the space in pixels allocated for the axis
   *
   * @param tickSpace in plot space
   * @return
   */
  public double getNumericalGridStep(double tickSpace) {

    // this prevents an infinite loop when the plot gets sized really small.
    if (tickSpace < 10) {
      return 1.0;
    }

    // the span of the data
    double span = Math.abs(maxValue - minValue); // in data space

    int tickMarkSpaceHint = (axisDirection == Direction.X ? styleManager.getXAxisTickMarkSpacingHint() : styleManager.getYAxisTickMarkSpacingHint());

    // for very short plots, squeeze some more ticks in than normal
    if (axisDirection == Direction.Y && tickSpace < 160) {
      tickMarkSpaceHint = 25;
    }

    double gridStepHint = span / tickSpace * tickMarkSpaceHint;

    // gridStepHint --> significand * 10 ** exponent
    // e.g. 724.1 --> 7.241 * 10 ** 2
    double significand = gridStepHint;
    int exponent = 0;
    if (significand == 0) {
      exponent = 1;
    }
    else if (significand < 1) {
      while (significand < 1) {
        significand *= 10.0;
        exponent--;
      }
    }
    else {
      while (significand >= 10 || significand == Double.NEGATIVE_INFINITY) {
        significand /= 10.0;
        exponent++;
      }
    }

    // calculate the grid step with hint.
    double gridStep;
    if (significand > 7.5) {
      // gridStep = 10.0 * 10 ** exponent
      gridStep = 10.0 * Utils.pow(10, exponent);
    }
    else if (significand > 3.5) {
      // gridStep = 5.0 * 10 ** exponent
      gridStep = 5.0 * Utils.pow(10, exponent);
    }
    else if (significand > 1.5) {
      // gridStep = 2.0 * 10 ** exponent
      gridStep = 2.0 * Utils.pow(10, exponent);
    }
    else {
      // gridStep = 1.0 * 10 ** exponent
      gridStep = Utils.pow(10, exponent);
    }
    return gridStep;
  }

}
