/**
 * Copyright 2011 - 2015 Xeiam LLC.
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
  public AxisTickDateCalculator(Direction axisDirection, double workingSpace, double minValue, double maxValue, StyleManager styleManager) {

    super(axisDirection, workingSpace, minValue, maxValue, styleManager);
    dateFormatter = new DateFormatter(styleManager);
    calculate();
  }

  private void calculate() {

    // tick space - a percentage of the working space available for ticks
    double tickSpace = styleManager.getAxisTickSpacePercentage() * workingSpace; // in plot space

    // this prevents an infinite loop when the plot gets sized really small.
    if (tickSpace < 10) {
      return;
    }

    // where the tick should begin in the working space in pixels
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace); // in plot space double gridStep = getGridStepForDecimal(tickSpace);

    // the span of the data
    long span = (long) Math.abs(maxValue - minValue); // in data space

    // Generate the labels first, see if they "look" OK and reiterate with an increased tickSpacingHint
    int tickSpacingHint = styleManager.getXAxisTickMarkSpacingHint() - 5;
    do {

      // System.out.println("calculating ticks...");
      tickLabels.clear();
      tickLocations.clear();
      tickSpacingHint += 5;
      long gridStepHint = (long) (span / tickSpace * tickSpacingHint);

      long timeUnit = dateFormatter.getTimeUnit(gridStepHint);
      double gridStep = 0.0;
      int[] steps = dateFormatter.getValidTickStepsMap().get(timeUnit);
      for (int i = 0; i < steps.length - 1; i++) {
        if (gridStepHint < (timeUnit * steps[i] + timeUnit * steps[i + 1]) / 2.0) {
          gridStep = timeUnit * steps[i];
          break;
        }
      }

      // System.out.println("gridStep: " + gridStep);

      double firstPosition = getFirstPosition(gridStep);

      // generate all tickLabels and tickLocations from the first to last position
      for (double value = firstPosition; value <= maxValue + 2 * gridStep; value = value + gridStep) {

        // if (value <= maxValue && value >= minValue) {

        tickLabels.add(dateFormatter.formatDate(value, timeUnit));
        // here we convert tickPosition finally to plot space, i.e. pixels
        double tickLabelPosition = margin + ((value - minValue) / (maxValue - minValue) * tickSpace);
        tickLocations.add(tickLabelPosition);
        // }
      }
    } while (!willLabelsFitInTickSpaceHint(tickLabels, tickSpacingHint));
  }

}
