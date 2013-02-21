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
package com.xeiam.xchart.internal.chartpart.axistickcalculator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import com.xeiam.xchart.internal.chartpart.Axis.Direction;
import com.xeiam.xchart.style.StyleManager;

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

    this.axisDirection = axisDirection;
    this.workingSpace = workingSpace;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.styleManager = styleManager;
  }

  BigDecimal pow(double base, int exponent) {

    if (exponent > 0) {
      return new BigDecimal(base).pow(exponent);
    } else {
      return BigDecimal.ONE.divide(new BigDecimal(base).pow(-exponent));
    }
  }

  /**
   * Format a number value, if the override patterns are null, it uses defaults
   * 
   * @param value
   * @return
   */
  public String formatNumber(BigDecimal value) {

    NumberFormat numberFormat = NumberFormat.getNumberInstance(styleManager.getLocale());

    BigDecimal absoluteValue = value.abs();

    if (absoluteValue.compareTo(new BigDecimal("10000.000001")) == -1 && absoluteValue.compareTo(new BigDecimal(".0009999999")) == 1 || BigDecimal.ZERO.compareTo(value) == 0) {

      DecimalFormat normalFormat = (DecimalFormat) numberFormat;
      normalFormat.applyPattern(styleManager.getNormalDecimalPattern());
      return normalFormat.format(value);

    } else {

      DecimalFormat scientificFormat = (DecimalFormat) numberFormat;
      scientificFormat.applyPattern(styleManager.getScientificDecimalPattern());
      return scientificFormat.format(value);

    }

  }

  BigDecimal getFirstPosition(final BigDecimal min, BigDecimal gridStep) {

    BigDecimal firstPosition;
    if (min.remainder(gridStep).doubleValue() <= 0.0) {
      firstPosition = min.subtract(min.remainder(gridStep));
    } else {
      firstPosition = min.subtract(min.remainder(gridStep)).add(gridStep);
    }
    return firstPosition;
  }

  public List<Integer> getTickLocations() {

    return tickLocations;
  }

  public List<String> getTickLabels() {

    return tickLabels;
  }

  // public abstract BigDecimal getGridStep(int tickSpace);
  //
  // public abstract BigDecimal getFirstPosition(BigDecimal minValue, BigDecimal gridStep);

}
