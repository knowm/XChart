/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.internal.chartpart;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.knowm.xchart.style.AxesChartStyler;

/**
 * @author timmolter
 */
class NumberLogFormatter extends Format {

  private final AxesChartStyler styler;
  private final Axis.Direction axisDirection;
  private final NumberFormat numberFormat;

  /**
   * Constructor
   */
  public NumberLogFormatter(AxesChartStyler styler, Axis.Direction axisDirection) {

    this.styler = styler;
    this.axisDirection = axisDirection;
    numberFormat = NumberFormat.getNumberInstance(styler.getLocale());
  }

  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

    double number = (Double) obj;

    String decimalPattern;

    if (axisDirection == Axis.Direction.X && styler.getXAxisDecimalPattern() != null) {

      decimalPattern = styler.getXAxisDecimalPattern();
    } else if (axisDirection == Axis.Direction.Y && styler.getYAxisDecimalPattern() != null) {
      decimalPattern = styler.getYAxisDecimalPattern();
    } else if (styler.getDecimalPattern() != null) {

      decimalPattern = styler.getDecimalPattern();
    } else {
      if (Math.abs(number) > 1000.0 || Math.abs(number) < 0.001) {
        decimalPattern = "0E0";
      } else {
        decimalPattern = "0.###";
      }
    }

    DecimalFormat normalFormat = (DecimalFormat) numberFormat;
    normalFormat.applyPattern(decimalPattern);
    toAppendTo.append(normalFormat.format(number));

    return toAppendTo;
  }

  @Override
  public Object parseObject(String source, ParsePosition pos) {
    return null;
  }

}
