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

import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.chartpart.Axis.Direction;
import com.xeiam.xchart.internal.chartpart.AxisPair;
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

    calculate();
  }

  private void calculate() {

    // a check if all axis data are the exact same values
    if (minValue == maxValue) {
      if (getAxisType() == AxisType.Number) {
        tickLabels.add(styleManager.getDecimalFormatter().formatNumber(maxValue));
      } else if (getAxisType() == AxisType.Date) {
        tickLabels.add(styleManager.getDateFormatter().formatDateValue(maxValue, maxValue, maxValue));
      }
      tickLocations.add((int) (workingSpace / 2.0));
      return;
    }

    // tick space - a percentage of the working space available for ticks, i.e. 95%
    int tickSpace = AxisPair.getTickSpace(workingSpace); // in plot space

    // where the tick should begin in the working space in pixels
    int margin = AxisPair.getTickStartOffset(workingSpace, tickSpace); // in plot space BigDecimal gridStep = getGridStepForDecimal(tickSpace);

    BigDecimal gridStep = getGridStep(tickSpace);
    BigDecimal firstPosition = getFirstPosition(minValue, gridStep);

    // generate all tickLabels and tickLocations from the first to last position
    for (BigDecimal tickPosition = firstPosition; tickPosition.compareTo(maxValue) <= 0; tickPosition = tickPosition.add(gridStep)) {

      if (getAxisType() == AxisType.Number) {
        tickLabels.add(styleManager.getDecimalFormatter().formatNumber(tickPosition));
      } else if (getAxisType() == AxisType.Date) {
        tickLabels.add(styleManager.getDateFormatter().formatDateValue(tickPosition, minValue, maxValue));
      }
      // here we convert tickPosition finally to plot space, i.e. pixels
      int tickLabelPosition = (int) (margin + ((tickPosition.subtract(minValue)).doubleValue() / (maxValue.subtract(minValue)).doubleValue() * tickSpace));
      tickLocations.add(tickLabelPosition);
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
