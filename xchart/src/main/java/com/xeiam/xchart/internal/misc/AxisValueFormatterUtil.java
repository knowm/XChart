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
package com.xeiam.xchart.internal.misc;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author timmolter
 */
public class AxisValueFormatterUtil {

  private static final String NORMAL_DECIMAL_PATTERN = "#.####";
  private static final String SCIENTIFIC_DECIMAL_PATTERN = "0.##E0";
  private static final String DATE_PATTERN = "HHmmss";
  private static final Locale LOCALE = Locale.getDefault();
  private static final TimeZone TIMEZONE = TimeZone.getDefault();

  private static final long SEC_SCALE = TimeUnit.SECONDS.toMillis(1L);
  private static final long MIN_SCALE = TimeUnit.MINUTES.toMillis(1L);
  private static final long HOUR_SCALE = TimeUnit.HOURS.toMillis(1L);
  private static final long DAY_SCALE = TimeUnit.DAYS.toMillis(1L);
  private static final long WEEK_SCALE = TimeUnit.DAYS.toMillis(1L) * 7;
  private static final long MONTH_SCALE = TimeUnit.DAYS.toMillis(1L) * 31;
  private static final long YEAR_SCALE = TimeUnit.DAYS.toMillis(1L) * 365;

  /**
   * Constructor
   */
  private AxisValueFormatterUtil() {

  }

  /**
   * Format a number value, if the override patterns are null, it uses defaults
   * 
   * @param value
   * @param normalDecimalPatternOverride
   * @param scientificDecimalPatternOverride
   * @param localeOverride
   * @return the formatted number as a String
   */
  public static String formatNumber(BigDecimal value, String normalDecimalPatternOverride, String scientificDecimalPatternOverride, Locale localeOverride) {

    NumberFormat numberFormat = NumberFormat.getNumberInstance(localeOverride == null ? LOCALE : localeOverride);

    BigDecimal absoluteValue = value.abs();

    if (absoluteValue.compareTo(new BigDecimal("10000.000001")) == -1 && absoluteValue.compareTo(new BigDecimal(".0009999999")) == 1 || BigDecimal.ZERO.compareTo(value) == 0) {

      DecimalFormat normalFormat = (DecimalFormat) numberFormat;
      normalFormat.applyPattern(normalDecimalPatternOverride == null ? NORMAL_DECIMAL_PATTERN : normalDecimalPatternOverride);
      return normalFormat.format(value);

    } else {

      DecimalFormat scientificFormat = (DecimalFormat) numberFormat;
      scientificFormat.applyPattern(scientificDecimalPatternOverride == null ? SCIENTIFIC_DECIMAL_PATTERN : scientificDecimalPatternOverride);
      return scientificFormat.format(value);

    }

  }

  /**
   * Format a date value
   * 
   * @param value
   * @param min
   * @param max
   * @param datePatternOverride
   * @param localeOverride
   * @return the formatted date value as a String
   */
  public static String formatDateValue(BigDecimal value, BigDecimal min, BigDecimal max, String datePatternOverride, Locale localeOverride, TimeZone timeZoneOverride) {

    // intelligently set datepattern if none is given
    String datePattern = datePatternOverride;
    if (datePatternOverride == null) {
      datePattern = DATE_PATTERN;
      long diff = max.subtract(min).longValue();

      if (diff < SEC_SCALE) {
        datePattern = "ss:S";
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

    }

    SimpleDateFormat simpleDateformat = new SimpleDateFormat(datePattern, localeOverride == null ? LOCALE : localeOverride);
    simpleDateformat.setTimeZone(timeZoneOverride == null ? TIMEZONE : timeZoneOverride);
    simpleDateformat.applyPattern(datePattern);
    return simpleDateformat.format(value.longValueExact());

  }
}
