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

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.internal.Utils;
import com.xeiam.xchart.internal.chartpart.Axis.Direction;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for rendering the axis ticks for decimal axes
 *
 * @author timmolter
 */
public class AxisTickNumericalCalculator extends AxisTickCalculator {

  NumberFormatter numberFormatter = null;

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styleManager
   */
  public AxisTickNumericalCalculator(Direction axisDirection, double workingSpace, double minValue, double maxValue, StyleManager styleManager) {

    super(axisDirection, workingSpace, minValue, maxValue, styleManager);
    numberFormatter = new NumberFormatter(styleManager);
    calculate();
  }

  private void calculate() {

    // a check if all axis data are the exact same values
    if (minValue == maxValue) {
      tickLabels.add(numberFormatter.formatNumber(BigDecimal.valueOf(maxValue), minValue, maxValue, axisDirection));
      tickLocations.add(workingSpace / 2.0);
      return;
    }

    // tick space - a percentage of the working space available for ticks
    double tickSpace = styleManager.getAxisTickSpacePercentage() * workingSpace; // in plot space

    // where the tick should begin in the working space in pixels
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace); // in plot space double gridStep = getGridStepForDecimal(tickSpace);

    BigDecimal gridStep = BigDecimal.valueOf(getNumericalGridStep(tickSpace));
    // System.out.println("***gridStep: " + gridStep);
    BigDecimal cleanedGridStep = gridStep.setScale(16, RoundingMode.HALF_UP); // chop off any double imprecision
    // System.out.println("cleanedGridStep: " + cleanedGridStep);
    BigDecimal firstPosition = BigDecimal.valueOf(getFirstPosition(cleanedGridStep.doubleValue()));
    // System.out.println("firstPosition: " + firstPosition); // chop off any double imprecision
    BigDecimal cleanedFirstPosition = firstPosition.setScale(16, RoundingMode.HALF_UP); // chop off any double imprecision
    // System.out.println("scaledfirstPosition: " + cleanedFirstPosition);

    // generate all tickLabels and tickLocations from the first to last position
    for (BigDecimal tickPosition = cleanedFirstPosition; tickPosition.compareTo(BigDecimal.valueOf(maxValue + 2 * cleanedGridStep.doubleValue())) < 0; tickPosition = tickPosition.add(cleanedGridStep)) {

      // System.out.println(tickPosition);
      String tickLabel = numberFormatter.formatNumber(tickPosition, minValue, maxValue, axisDirection);
      // System.out.println(tickLabel);

      tickLabels.add(tickLabel);
      // here we convert tickPosition finally to plot space, i.e. pixels
      double tickLabelPosition = margin + ((tickPosition.doubleValue() - minValue) / (maxValue - minValue) * tickSpace);
      tickLocations.add(tickLabelPosition);
    }
  }

}
