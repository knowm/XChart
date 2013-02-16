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
import java.util.LinkedList;
import java.util.List;

import com.xeiam.xchart.internal.chartpart.Axis.Direction;
import com.xeiam.xchart.internal.chartpart.AxisPair;
import com.xeiam.xchart.style.StyleManager;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for rendering the axis ticks for decimal axes
 * 
 * @author timmolter
 */
public class DecimalAxisTickCalculator implements AxisTickCalculator {

  /** the default tick mark step hint for x axis */
  private static final int DEFAULT_TICK_MARK_STEP_HINT_X = 74;

  /** the default tick mark step hint for y axis */
  private static final int DEFAULT_TICK_MARK_STEP_HINT_Y = 44;

  /** the List of tick label position in pixels */
  private List<Integer> tickLocations = new LinkedList<Integer>();;

  /** the List of tick label values */
  private List<String> tickLabels = new LinkedList<String>();

  private final Direction axisDirection;

  private final int workingSpace;

  private final BigDecimal minValue;

  private final BigDecimal maxValue;

  private final StyleManager styleManager;

  /**
   * Constructor
   * 
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styleManager
   */
  public DecimalAxisTickCalculator(Direction axisDirection, int workingSpace, BigDecimal minValue, BigDecimal maxValue, StyleManager styleManager) {

    this.axisDirection = axisDirection;
    this.workingSpace = workingSpace;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.styleManager = styleManager;

    calculate();
  }

  private void calculate() {

    // a check if all axis data are the exact same values
    if (minValue == maxValue) {
      tickLabels.add(styleManager.getDecimalFormatter().formatNumber(maxValue));
      tickLocations.add((int) (workingSpace / 2.0));
      return;
    }

    // tick space - a percentage of the working space available for ticks, i.e. 95%
    int tickSpace = AxisPair.getTickSpace(workingSpace); // in plot space
    System.out.println("tickSpace= " + tickSpace);

    // where the tick should begin in the working space in pixels
    int margin = AxisPair.getTickStartOffset(workingSpace, tickSpace); // in plot space BigDecimal gridStep = getGridStepForDecimal(tickSpace);

    BigDecimal gridStep = getGridStepForDecimal(tickSpace);

    BigDecimal firstPosition = getFirstPosition(minValue, gridStep);

    // generate all tickLabels and tickLocations from the first to last position
    for (BigDecimal tickPosition = firstPosition; tickPosition.compareTo(maxValue) <= 0; tickPosition = tickPosition.add(gridStep)) {

      tickLabels.add(styleManager.getDecimalFormatter().formatNumber(tickPosition));
      // here we convert tickPosition finally to plot space, i.e. pixels
      int tickLabelPosition = (int) (margin + ((tickPosition.subtract(minValue)).doubleValue() / (maxValue.subtract(minValue)).doubleValue() * tickSpace));
      tickLocations.add(tickLabelPosition);
    }
  }

  /**
   * Determine the grid step for the data set given the space in pixels allocated for the axis
   * 
   * @param tickSpace in plot space
   * @return
   */
  private BigDecimal getGridStepForDecimal(int tickSpace) {

    // the span of the data
    double span = Math.abs(maxValue.subtract(minValue).doubleValue()); // in data space

    int tickMarkSpaceHint = (axisDirection == Direction.X ? DEFAULT_TICK_MARK_STEP_HINT_X : DEFAULT_TICK_MARK_STEP_HINT_Y);

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

  private BigDecimal getFirstPosition(final BigDecimal min, BigDecimal gridStep) {

    BigDecimal firstPosition;
    if (min.remainder(gridStep).doubleValue() <= 0.0) {
      firstPosition = min.subtract(min.remainder(gridStep));
    } else {
      firstPosition = min.subtract(min.remainder(gridStep)).add(gridStep);
    }
    return firstPosition;
  }

  @Override
  public List<Integer> getTickLocations() {

    return tickLocations;
  }

  @Override
  public List<String> getTickLabels() {

    return tickLabels;
  }

}
