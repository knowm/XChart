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
package com.xeiam.xchart.internal.chartpart.gridstep;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author timmolter
 */
public class DecimalFormatter {

  private String normalDecimalPattern;
  private String scientificDecimalPattern;
  // TODO move to parent class??
  private Locale locale;

  /**
   * Constructor
   */
  public DecimalFormatter() {

    normalDecimalPattern = "#.####";
    scientificDecimalPattern = "0.##E0";
    locale = Locale.getDefault();

  }

  /**
   * Format a number value, if the override patterns are null, it uses defaults
   * 
   * @param value
   * @return
   */
  public String formatNumber(BigDecimal value) {

    NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);

    BigDecimal absoluteValue = value.abs();

    if (absoluteValue.compareTo(new BigDecimal("10000.000001")) == -1 && absoluteValue.compareTo(new BigDecimal(".0009999999")) == 1 || BigDecimal.ZERO.compareTo(value) == 0) {

      DecimalFormat normalFormat = (DecimalFormat) numberFormat;
      normalFormat.applyPattern(normalDecimalPattern);
      return normalFormat.format(value);

    } else {

      DecimalFormat scientificFormat = (DecimalFormat) numberFormat;
      scientificFormat.applyPattern(scientificDecimalPattern);
      return scientificFormat.format(value);

    }

  }

  /**
   * Set the decimal formatter for all tick labels
   * 
   * @param pattern - the pattern describing the decimal format
   */
  public void setNormalDecimalPattern(String normalDecimalPattern) {

    this.normalDecimalPattern = normalDecimalPattern;
  }

  /**
   * Set the scientific notation formatter for all tick labels
   * 
   * @param pattern - the pattern describing the scientific notation format
   */
  public void setScientificDecimalPattern(String scientificDecimalPattern) {

    this.scientificDecimalPattern = scientificDecimalPattern;
  }

  /**
   * Set the locale to use for rendering the chart
   * 
   * @param locale - the locale to use when formatting Strings and dates for the axis tick labels
   */
  public void setLocale(Locale locale) {

    this.locale = locale;
  }

}
