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

import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
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

  public AxisTickCalculator(Direction axisDirection, int workingSpace, BigDecimal minValue, BigDecimal maxValue, StyleManager styleManager) {

    this.axisDirection = axisDirection;
    this.workingSpace = workingSpace;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.styleManager = styleManager;

  }

  BigDecimal getGridStepDecimal(double gridStepHint) {

    // gridStepHint --> significand * 10 ** exponent
    // e.g. 724.1 --> 7.241 * 10 ** 2
    double significand = gridStepHint;
    int exponent = 0;
    if (significand == 0) {
      exponent = 1;
    } else if (significand < 1) {
      while (significand < 1) {
        significand *= 10.0;
        exponent--;
      }
    } else {
      while (significand >= 10) {
        significand /= 10.0;
        exponent++;
      }
    }

    // calculate the grid step with hint.
    BigDecimal gridStep;
    if (significand > 7.5) {
      // gridStep = 10.0 * 10 ** exponent
      gridStep = BigDecimal.TEN.multiply(pow(10, exponent));
    } else if (significand > 3.5) {
      // gridStep = 5.0 * 10 ** exponent
      gridStep = new BigDecimal(new Double(5).toString()).multiply(pow(10, exponent));
    } else if (significand > 1.5) {
      // gridStep = 2.0 * 10 ** exponent
      gridStep = new BigDecimal(new Double(2).toString()).multiply(pow(10, exponent));
    } else {
      // gridStep = 1.0 * 10 ** exponent
      gridStep = pow(10, exponent);
    }
    return gridStep;
  }

  /**
   * Calculates the value of the first argument raised to the power of the second argument.
   * 
   * @param base the base
   * @param exponent the exponent
   * @return the value <tt>a<sup>b</sup></tt> in <tt>BigDecimal</tt>
   */
  private BigDecimal pow(double base, int exponent) {

    BigDecimal value;
    if (exponent > 0) {
      value = new BigDecimal(new Double(base).toString()).pow(exponent);
    } else {
      value = BigDecimal.ONE.divide(new BigDecimal(new Double(base).toString()).pow(-exponent));
    }
    return value;
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

  public List<Integer> getTickLocations() {

    return tickLocations;
  }

  public List<String> getTickLabels() {

    return tickLabels;
  }

  public abstract BigDecimal getGridStep(int tickSpace);

  public abstract BigDecimal getFirstPosition(BigDecimal minValue, BigDecimal gridStep);

  public abstract AxisType getAxisType();

}
