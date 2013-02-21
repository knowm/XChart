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

import com.xeiam.xchart.internal.chartpart.Axis.Direction;
import com.xeiam.xchart.internal.chartpart.AxisPair;
import com.xeiam.xchart.style.StyleManager;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for rendering the axis ticks for logarithmic axes
 * 
 * @author timmolter
 */
public class LogarithmicAxisTickCalculator extends AxisTickCalculator {

  /**
   * Constructor
   * 
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styleManager
   */
  public LogarithmicAxisTickCalculator(Direction axisDirection, int workingSpace, BigDecimal minValue, BigDecimal maxValue, StyleManager styleManager) {

    super(axisDirection, workingSpace, minValue, maxValue, styleManager);
    calculate();
  }

  private void calculate() {

    // a check if all axis data are the exact same values
    if (minValue == maxValue) {
      tickLabels.add(formatNumber(maxValue));
      tickLocations.add((int) (workingSpace / 2.0));
      return;
    }

    // tick space - a percentage of the working space available for ticks, i.e. 95%
    int tickSpace = AxisPair.getTickSpace(workingSpace); // in plot space

    // where the tick should begin in the working space in pixels
    int margin = AxisPair.getTickStartOffset(workingSpace, tickSpace); // in plot space BigDecimal gridStep = getGridStepForDecimal(tickSpace);

    int logMin = (int) Math.floor(Math.log10(minValue.doubleValue()));
    int logMax = (int) Math.ceil(Math.log10(maxValue.doubleValue()));

    final BigDecimal min = new BigDecimal(minValue.doubleValue());
    BigDecimal tickStep = pow(10, logMin - 1);

    BigDecimal firstPosition = getFirstPosition(minValue, tickStep);

    for (int i = logMin; i <= logMax; i++) { // for each decade

      for (BigDecimal j = firstPosition; j.doubleValue() <= pow(10, i).doubleValue(); j = j.add(tickStep)) {

        // System.out.println(Math.log10(j.doubleValue()) % 1);

        if (j.doubleValue() > maxValue.doubleValue()) {
          break;
        }

        // only add labels for the decades
        if (Math.log10(j.doubleValue()) % 1 == 0.0) {
          tickLabels.add(formatNumber(j));
        } else {
          tickLabels.add(null);
        }

        // add all the tick marks though
        int tickLabelPosition = (int) (margin + (Math.log10(j.doubleValue()) - Math.log10(min.doubleValue())) / (Math.log10(maxValue.doubleValue()) - Math.log10(min.doubleValue())) * tickSpace);
        tickLocations.add(tickLabelPosition);
      }
      tickStep = tickStep.multiply(pow(10, 1));
      firstPosition = tickStep.add(pow(10, i));
    }
  }

}
