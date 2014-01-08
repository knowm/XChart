/**
 * Copyright 2011 - 2013 Xeiam LLC.
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

import java.math.BigDecimal;

import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.internal.Utils;
import com.xeiam.xchart.internal.chartpart.Axis.Direction;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for rendering the axis ticks for date axes
 * 
 * @author timmolter
 */
public class AxisTickDateCalculator extends AxisTickCalculator {

  DateFormatter dateFormatter;

  /**
   * Constructor
   * 
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styleManager
   */
  public AxisTickDateCalculator(Direction axisDirection, int workingSpace, BigDecimal minValue, BigDecimal maxValue, StyleManager styleManager) {

    super(axisDirection, workingSpace, minValue, maxValue, styleManager);
    dateFormatter = new DateFormatter(styleManager);
    calculate();
  }

  private void calculate() {

    // tick space - a percentage of the working space available for ticks, i.e. 95%
    int tickSpace = Utils.getTickSpace(workingSpace); // in plot space

    // where the tick should begin in the working space in pixels
    int margin = Utils.getTickStartOffset(workingSpace, tickSpace); // in plot space BigDecimal gridStep = getGridStepForDecimal(tickSpace);

    // the span of the data
    long span = Math.abs(maxValue.subtract(minValue).longValue()); // in data space

    long gridStepHint = (long) (span / (double) tickSpace * styleManager.getXAxisTickMarkSpacingHint());

    long timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    BigDecimal gridStep = null;
    int[] steps = dateFormatter.getValidTickStepsMap().get(timeUnit);
    for (int i = 0; i < steps.length - 1; i++) {
      if (gridStepHint < (timeUnit * steps[i] + timeUnit * steps[i + 1]) / 2.0) {
        gridStep = new BigDecimal(timeUnit * steps[i]);
        break;
      }
    }

    BigDecimal firstPosition = getFirstPosition(gridStep);

    // generate all tickLabels and tickLocations from the first to last position
    for (BigDecimal tickPosition = firstPosition; tickPosition.compareTo(maxValue) <= 0; tickPosition = tickPosition.add(gridStep)) {

      tickLabels.add(dateFormatter.formatDate(tickPosition, timeUnit));
      // here we convert tickPosition finally to plot space, i.e. pixels
      int tickLabelPosition = (int) (margin + ((tickPosition.subtract(minValue)).doubleValue() / (maxValue.subtract(minValue)).doubleValue() * tickSpace));
      tickLocations.add(tickLabelPosition);
    }
  }

}
