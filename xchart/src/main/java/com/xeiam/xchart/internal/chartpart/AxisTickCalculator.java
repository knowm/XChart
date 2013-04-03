/**
 * Copyright (C) 2013 Xeiam LLC http://xeiam.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchart.internal.chartpart;

import java.math.BigDecimal;
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

  /** the default tick mark step hint for x axis */
  protected static final int DEFAULT_TICK_MARK_STEP_HINT_X = 74;

  /** the default tick mark step hint for y axis */
  protected static final int DEFAULT_TICK_MARK_STEP_HINT_Y = 44;

  /** the List of tick label position in pixels */
  protected List<Integer> tickLocations = new LinkedList<Integer>();;

  /** the List of tick label values */
  protected List<String> tickLabels = new LinkedList<String>();

  protected final Direction axisDirection;

  protected final int workingSpace;

  protected final BigDecimal minValue;

  protected final BigDecimal maxValue;

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
  public AxisTickCalculator(Direction axisDirection, int workingSpace, BigDecimal minValue, BigDecimal maxValue, StyleManager styleManager) {

    // override min/max value for bar charts' Y-Axis
    BigDecimal overrideMinValue = minValue;
    BigDecimal overrideMaxValue = maxValue;
    if (styleManager.getChartType() == ChartType.Bar && axisDirection == Direction.Y) { // this is the Y-Axis for a bar chart
      if (minValue.compareTo(BigDecimal.ZERO) > 0 && maxValue.compareTo(BigDecimal.ZERO) > 0) {
        overrideMinValue = BigDecimal.ZERO;
      }
      if (minValue.compareTo(BigDecimal.ZERO) < 0 && maxValue.compareTo(BigDecimal.ZERO) < 0) {
        overrideMaxValue = BigDecimal.ZERO;
      }
    }

    if (styleManager.getChartType() == ChartType.Bar && styleManager.isYAxisLogarithmic()) {
      int logMin = (int) Math.floor(Math.log10(minValue.doubleValue()));
      overrideMinValue = new BigDecimal(Utils.pow(10, logMin).doubleValue());
    }

    // override min and maxValue if specified
    if (axisDirection == Direction.X && styleManager.getXAxisMin() != null) {
      overrideMinValue = new BigDecimal(styleManager.getXAxisMin());
    }
    if (axisDirection == Direction.Y && styleManager.getYAxisMin() != null) {
      overrideMinValue = new BigDecimal(styleManager.getYAxisMin());
    }
    if (axisDirection == Direction.X && styleManager.getXAxisMax() != null) {
      overrideMaxValue = new BigDecimal(styleManager.getXAxisMax());
    }
    if (axisDirection == Direction.Y && styleManager.getYAxisMax() != null) {
      overrideMaxValue = new BigDecimal(styleManager.getYAxisMax());
    }
    this.axisDirection = axisDirection;
    this.workingSpace = workingSpace;
    this.minValue = overrideMinValue;
    // this.minValue = new BigDecimal(10000);
    this.maxValue = overrideMaxValue;
    this.styleManager = styleManager;
  }

  BigDecimal getFirstPosition(BigDecimal gridStep) {

    BigDecimal firstPosition;
    if (minValue.remainder(gridStep).doubleValue() <= 0.0) {
      firstPosition = minValue.subtract(minValue.remainder(gridStep));
    } else {
      firstPosition = minValue.subtract(minValue.remainder(gridStep)).add(gridStep);
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
