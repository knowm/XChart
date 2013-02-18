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
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.chartpart.Axis.Direction;
import com.xeiam.xchart.internal.chartpart.AxisPair;
import com.xeiam.xchart.style.StyleManager;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for rendering the axis ticks for date axes
 * 
 * @author timmolter
 */
public class DateAxisTickCalculator extends AxisTickCalculator {

  public static final long SEC_SCALE = TimeUnit.SECONDS.toMillis(2L);
  public static final long MIN_SCALE = TimeUnit.MINUTES.toMillis(2L);
  public static final long HOUR_SCALE = TimeUnit.HOURS.toMillis(2L);
  public static final long DAY_SCALE = TimeUnit.DAYS.toMillis(2L);
  public static final long WEEK_SCALE = TimeUnit.DAYS.toMillis(2L) * 7;
  public static final long MONTH_SCALE = TimeUnit.DAYS.toMillis(2L) * 31;
  public static final long YEAR_SCALE = TimeUnit.DAYS.toMillis(2L) * 365;

  /**
   * Constructor
   * 
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styleManager
   */
  public DateAxisTickCalculator(Direction axisDirection, int workingSpace, BigDecimal minValue, BigDecimal maxValue, StyleManager styleManager) {

    super(axisDirection, workingSpace, minValue, maxValue, styleManager);
    calculate();
  }

  private void calculate() {

    // a check if all axis data are the exact same values
    if (minValue == maxValue) {
      tickLabels.add(formatDateValue(maxValue, maxValue, maxValue));
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

      tickLabels.add(formatDateValue(tickPosition, minValue, maxValue));
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
  @Override
  public BigDecimal getGridStep(int tickSpace) {

    // the span of the data
    double span = Math.abs(maxValue.subtract(minValue).doubleValue()); // in data space

    double gridStepHint = span / tickSpace * DEFAULT_TICK_MARK_STEP_HINT_X;

    BigDecimal gridStep;
    if (span < SEC_SCALE) {
      // decimal
      gridStep = getGridStepDecimal(gridStepHint);

    } else {

      // date
      gridStep = getGridStepDecimal(gridStepHint);

    }

    return gridStep;
  }

  @Override
  public BigDecimal getFirstPosition(final BigDecimal min, BigDecimal gridStep) {

    BigDecimal firstPosition;
    if (min.remainder(gridStep).doubleValue() <= 0.0) {
      firstPosition = min.subtract(min.remainder(gridStep));
    } else {
      firstPosition = min.subtract(min.remainder(gridStep)).add(gridStep);
    }
    return firstPosition;
  }

  /**
   * Format a date value
   * 
   * @param value
   * @param min
   * @param max
   * @return
   */
  public String formatDateValue(BigDecimal value, BigDecimal min, BigDecimal max) {

    String datePattern;
    // TODO check if min and max are the same, then calculate this differently

    // intelligently set date pattern if none is given
    long diff = max.subtract(min).longValue();

    if (diff < SEC_SCALE) {
      datePattern = "ss.SSS";
    } else if (diff < MIN_SCALE) {
      datePattern = "mm:ss";
    } else if (diff < HOUR_SCALE) {
      datePattern = "HH:mm";
    } else if (diff < DAY_SCALE) {
      datePattern = "EEE HH:mm";
    } else if (diff < WEEK_SCALE) {
      datePattern = "EEE";
    } else if (diff < MONTH_SCALE) {
      datePattern = "MMM-dd";
    } else if (diff < YEAR_SCALE) {
      datePattern = "yyyy:MMM";
    } else {
      datePattern = "yyyy";
    }

    SimpleDateFormat simpleDateformat = new SimpleDateFormat(datePattern, styleManager.getLocale());
    simpleDateformat.setTimeZone(styleManager.getTimezone());
    simpleDateformat.applyPattern(datePattern);

    return simpleDateformat.format(value.longValueExact());
  }

  @Override
  public AxisType getAxisType() {

    return AxisType.Date;
  }

}
