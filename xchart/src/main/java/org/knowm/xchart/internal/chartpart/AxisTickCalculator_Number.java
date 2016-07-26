/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
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
package org.knowm.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for rendering the axis ticks for decimal axes
 *
 * @author timmolter
 */
public class AxisTickCalculator_Number extends AxisTickCalculator_ {

  NumberFormatter numberFormatter = null;

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styler
   */
  public AxisTickCalculator_Number(Direction axisDirection, double workingSpace, double minValue, double maxValue, AxesChartStyler styler) {

    super(axisDirection, workingSpace, minValue, maxValue, styler);
    numberFormatter = new NumberFormatter(styler);
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
    double tickSpace = styler.getPlotContentSize() * workingSpace; // in plot space

    // this prevents an infinite loop when the plot gets sized really small.
    if (tickSpace < styler.getXAxisTickMarkSpacingHint()) {
      return;
    }

    // where the tick should begin in the working space in pixels
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace); // in plot space double gridStep = getGridStepForDecimal(tickSpace);
    // the span of the data
    double span = Math.abs(Math.min((maxValue - minValue), Double.MAX_VALUE - 1)); // in data space

    //////////////////////////

    int tickSpacingHint = (axisDirection == Direction.X ? styler.getXAxisTickMarkSpacingHint() : styler.getYAxisTickMarkSpacingHint()) - 5;

    // for very short plots, squeeze some more ticks in than normal into the Y-Axis
    if (axisDirection == Direction.Y && tickSpace < 160) {
      tickSpacingHint = 25 - 5;
    }

    int gridStepInChartSpace = 0;

    do {

      // System.out.println("calculating ticks...");
      tickLabels.clear();
      tickLocations.clear();
      tickSpacingHint += 5;
      // System.out.println("tickSpacingHint: " + tickSpacingHint);

      double gridStepHint = span / tickSpace * tickSpacingHint;

      // gridStepHint --> significand * 10 ** exponent
      // e.g. 724.1 --> 7.241 * 10 ** 2
      double significand = gridStepHint;
      int exponent = 0;
      if (significand == 0) {
        exponent = 1;
      }
      else if (significand < 1) {
        while (significand < 1) {
          significand *= 10.0;
          exponent--;
        }
      }
      else {
        while (significand >= 10 || significand == Double.NEGATIVE_INFINITY) {
          significand /= 10.0;
          exponent++;
        }
      }

      // calculate the grid step width hint.
      double gridStep;
      if (significand > 7.5) {
        // gridStep = 10.0 * 10 ** exponent
        gridStep = 10.0 * Utils.pow(10, exponent);
      }
      else if (significand > 3.5) {
        // gridStep = 5.0 * 10 ** exponent
        gridStep = 5.0 * Utils.pow(10, exponent);
      }
      else if (significand > 1.5) {
        // gridStep = 2.0 * 10 ** exponent
        gridStep = 2.0 * Utils.pow(10, exponent);
      }
      else {
        // gridStep = 1.0 * 10 ** exponent
        gridStep = Utils.pow(10, exponent);
      }

      //////////////////////////
      // System.out.println("******************");
      // System.out.println("gridStep: " + gridStep);
      // System.out.println("***gridStepInChartSpace: " + gridStep / span * tickSpace);
      gridStepInChartSpace = (int) (gridStep / span * tickSpace);
      // System.out.println("gridStepInChartSpace: " + gridStepInChartSpace);
      BigDecimal gridStepBigDecimal = new BigDecimal(gridStep, MathContext.DECIMAL64);
      // BigDecimal gridStepBigDecimal = BigDecimal.valueOf(gridStep);
      int scale = Math.min(10, gridStepBigDecimal.scale());
      // int scale = gridStepBigDecimal.scale();
      // System.out.println("scale: " + scale);
      BigDecimal cleanedGridStep0 = gridStepBigDecimal.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros(); // chop off any double imprecision
      BigDecimal cleanedGridStep = cleanedGridStep0.setScale(scale, BigDecimal.ROUND_DOWN).stripTrailingZeros(); // chop off any double imprecision
      // System.out.println("cleanedGridStep: " + cleanedGridStep);
      // TODO figure this out. It happens once in a blue moon.
      BigDecimal firstPosition = null;
      try {
        firstPosition = BigDecimal.valueOf(getFirstPosition(cleanedGridStep.doubleValue()));
      } catch (java.lang.NumberFormatException e) {
        System.out.println("exponent: " + exponent);
        System.out.println("gridStep: " + gridStep);
        System.out.println("cleanedGridStep: " + cleanedGridStep);
        System.out.println("cleanedGridStep.doubleValue(): " + cleanedGridStep.doubleValue());
        System.out.println("NumberFormatException caused by this number: " + getFirstPosition(cleanedGridStep.doubleValue()));
      }
      // System.out.println("firstPosition: " + firstPosition); // chop off any double imprecision
      BigDecimal cleanedFirstPosition = firstPosition.setScale(10, RoundingMode.HALF_UP).stripTrailingZeros(); // chop off any double imprecision
      // System.out.println("cleanedFirstPosition: " + cleanedFirstPosition);

      // generate all tickLabels and tickLocations from the first to last position
      for (BigDecimal value = cleanedFirstPosition; value.compareTo(BigDecimal.valueOf(maxValue + 2 * cleanedGridStep.doubleValue())) < 0; value = value.add(cleanedGridStep)) {

        // if (value.compareTo(BigDecimal.valueOf(maxValue)) <= 0 && value.compareTo(BigDecimal.valueOf(minValue)) >= 0) {
        // System.out.println(value);
        String tickLabel = numberFormatter.formatNumber(value, minValue, maxValue, axisDirection);
        // System.out.println(tickLabel);
        tickLabels.add(tickLabel);

        // here we convert tickPosition finally to plot space, i.e. pixels
        double tickLabelPosition = margin + ((value.doubleValue() - minValue) / (maxValue - minValue) * tickSpace);
        tickLocations.add(tickLabelPosition);
        // }
      }
    } while (!willLabelsFitInTickSpaceHint(tickLabels, gridStepInChartSpace));

  }

}
