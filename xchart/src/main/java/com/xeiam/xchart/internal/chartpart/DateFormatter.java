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
package com.xeiam.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.xeiam.xchart.StyleManager;

/**
 * @author timmolter
 */
public class DateFormatter {

  public static final long MILLIS_SCALE = TimeUnit.MILLISECONDS.toMillis(1L);
  public static final long SEC_SCALE = TimeUnit.SECONDS.toMillis(1L);
  public static final long MIN_SCALE = TimeUnit.MINUTES.toMillis(1L);
  public static final long HOUR_SCALE = TimeUnit.HOURS.toMillis(1L);
  public static final long DAY_SCALE = TimeUnit.DAYS.toMillis(1L);
  public static final long MONTH_SCALE = TimeUnit.DAYS.toMillis(1L) * 31;
  public static final long YEAR_SCALE = TimeUnit.DAYS.toMillis(1L) * 365;

  private Map<Long, int[]> validTickStepsMap;

  private final StyleManager styleManager;

  /**
   * Constructor
   */
  public DateFormatter(StyleManager styleManager) {

    this.styleManager = styleManager;

    validTickStepsMap = new TreeMap<Long, int[]>();
    validTickStepsMap.put(MILLIS_SCALE, new int[] { 1, 2, 5, 10, 20, 50, 100, 200, 500, 1000 });
    validTickStepsMap.put(SEC_SCALE, new int[] { 1, 2, 5, 10, 15, 20, 30, 60 });
    validTickStepsMap.put(MIN_SCALE, new int[] { 1, 2, 3, 5, 10, 15, 20, 30, 60 });
    validTickStepsMap.put(HOUR_SCALE, new int[] { 1, 2, 4, 6, 12, 24 });
    validTickStepsMap.put(DAY_SCALE, new int[] { 1, 2, 3, 5, 10, 15, 31 });
    validTickStepsMap.put(MONTH_SCALE, new int[] { 1, 2, 3, 4, 6, 12 });
    validTickStepsMap.put(YEAR_SCALE, new int[] { 1, 2, 5, 10, 20, 50, 100, 200, 500, 1000 });
  }

  public long getTimeUnit(long gridStepHint) {

    for (Entry<Long, int[]> entry : validTickStepsMap.entrySet()) {

      long groupMagnitude = entry.getKey();
      int[] steps = entry.getValue();
      long validTickStepMagnitude = (long) ((groupMagnitude * steps[steps.length - 2] + groupMagnitude * steps[steps.length - 1]) / 2.0);
      if (gridStepHint < validTickStepMagnitude) {
        return groupMagnitude;
      }
    }
    return YEAR_SCALE;
  }

  /**
   * Format a date value
   * 
   * @param value
   * @param min
   * @param max
   * @return
   */
  public String formatDate(BigDecimal value, long timeUnit) {

    String datePattern;

    if (styleManager.getDatePattern() == null) {
      // intelligently set date pattern if none is given
      if (timeUnit == MILLIS_SCALE) {
        datePattern = "ss.SSS";
      } else if (timeUnit == SEC_SCALE) {
        datePattern = "mm:ss";
      } else if (timeUnit == MIN_SCALE) {
        datePattern = "HH:mm";
      } else if (timeUnit == HOUR_SCALE) {
        datePattern = "dd-HH";
      } else if (timeUnit == DAY_SCALE) {
        datePattern = "MM-dd";
      } else if (timeUnit == MONTH_SCALE) {
        datePattern = "yyyy-MM";
      } else {
        datePattern = "yyyy";
      }
    } else {
      datePattern = styleManager.getDatePattern();
    }

    SimpleDateFormat simpleDateformat = new SimpleDateFormat(datePattern, styleManager.getLocale());
    simpleDateformat.setTimeZone(styleManager.getTimezone());
    simpleDateformat.applyPattern(datePattern);

    return simpleDateformat.format(value.longValueExact());
  }

  Map<Long, int[]> getValidTickStepsMap() {

    return validTickStepsMap;
  }
}
