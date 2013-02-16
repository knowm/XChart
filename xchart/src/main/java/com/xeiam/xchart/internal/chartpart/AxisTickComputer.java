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

import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.chartpart.Axis.Direction;
import com.xeiam.xchart.internal.chartpart.gridstep.DecimalGridStep;
import com.xeiam.xchart.style.ValueFormatter;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for rendering the axis ticks
 * 
 * @author timmolter
 */
public class AxisTickComputer {

  /** the List of tick label position in pixels */
  private List<Integer> tickLocations = new LinkedList<Integer>();;

  /** the List of tick label values */
  private List<String> tickLabels = new LinkedList<String>();

  private final Direction axisDirection;

  private final int workingSpace;

  private final BigDecimal minValue;

  private final BigDecimal maxValue;

  private final ValueFormatter valueFormatter;

  private final AxisType axisType;

  /**
   * Constructor
   * 
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param valueFormatter
   * @param axisType
   */
  public AxisTickComputer(Direction axisDirection, int workingSpace, BigDecimal minValue, BigDecimal maxValue, ValueFormatter valueFormatter, AxisType axisType) {

    this.axisDirection = axisDirection;
    this.workingSpace = workingSpace;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.valueFormatter = valueFormatter;
    this.axisType = axisType;

    computeAxisTick();
  }

  private void computeAxisTick() {

    System.out.println("workingSpace= " + workingSpace);

    // a check if all axis data are the exact same values
    if (maxValue == minValue) {
      tickLabels.add(format(maxValue));
      tickLocations.add((int) (workingSpace / 2.0));
      return;
    }

    // tick space - a percentage of the working space available for ticks, i.e. 95%
    int tickSpace = AxisPair.getTickSpace(workingSpace); // in plot space
    System.out.println("tickSpace= " + tickSpace);

    // where the tick should begin in the working space in pixels
    int margin = AxisPair.getTickStartOffset(workingSpace, tickSpace); // in plot space

    // the span of the data
    double span = Math.abs(maxValue.subtract(minValue).doubleValue()); // in data space

    BigDecimal gridStep = null;
    BigDecimal firstPosition = null;
    if (axisType == AxisType.Number) {

      DecimalGridStep decimalGridStepHelper = new DecimalGridStep();
      gridStep = decimalGridStepHelper.getGridStepForDecimal(axisDirection, span, tickSpace);
      firstPosition = decimalGridStepHelper.getFirstPosition(minValue, gridStep);

    } else if (axisType == AxisType.Date) {

    } else if (axisType == AxisType.Logarithmic) {

    }

    // generate all tickLabels and tickLocations from the first to last position
    for (BigDecimal tickPosition = firstPosition; tickPosition.compareTo(maxValue) <= 0; tickPosition = tickPosition.add(gridStep)) {

      tickLabels.add(format(tickPosition));
      // here we convert tickPosition finally to plot space, i.e. pixels
      int tickLabelPosition = (int) (margin + ((tickPosition.subtract(minValue)).doubleValue() / (maxValue.subtract(minValue)).doubleValue() * tickSpace));
      tickLocations.add(tickLabelPosition);
    }

  }

  /**
   * Format the number
   * 
   * @param value The number to be formatted
   * @return The formatted number in String form
   */
  private String format(BigDecimal value) {

    if (axisType == AxisType.Number) {

      return valueFormatter.formatNumber(value);
    } else {

      return valueFormatter.formatDateValue(value, minValue, maxValue);
    }
  }

  public List<Integer> getTickLocations() {

    return tickLocations;
  }

  public List<String> getTickLabels() {

    return tickLabels;
  }

}
