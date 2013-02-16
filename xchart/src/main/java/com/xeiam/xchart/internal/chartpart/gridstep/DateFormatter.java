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
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author timmolter
 */
public class DateFormatter {

  private String datePattern;
  private Locale locale;
  private TimeZone timezone;

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
  public DateFormatter() {

    datePattern = "HHmmss";
    locale = Locale.getDefault();
    timezone = TimeZone.getDefault();
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

    // TODO check if min and max are the same, then calculate this differently

    // intelligently set date pattern if none is given
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

    SimpleDateFormat simpleDateformat = new SimpleDateFormat(datePattern, locale);
    simpleDateformat.setTimeZone(timezone);
    simpleDateformat.applyPattern(datePattern);
    return simpleDateformat.format(value.longValueExact());

  }

  /**
   * Set the String formatter for Data x-axis
   * 
   * @param pattern - the pattern describing the date and time format
   */
  public void setDatePattern(String datePattern) {

    this.datePattern = datePattern;
  }

  /**
   * Set the locale to use for rendering the chart
   * 
   * @param locale - the locale to use when formatting Strings and dates for the axis tick labels
   */
  public void setLocale(Locale locale) {

    this.locale = locale;
  }

  /**
   * Set the timezone to use for formatting Date axis tick labels
   * 
   * @param timezone the timezone to use when formatting date data
   */
  public void setTimezone(TimeZone timezone) {

    this.timezone = timezone;
  }

}
