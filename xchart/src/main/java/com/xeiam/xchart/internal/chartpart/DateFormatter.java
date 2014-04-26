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

  /**
   * @param gridStepHint
   * @return
   */
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
  public String formatDate(double value, long timeUnit) {

    String datePattern;

    if (styleManager.getDatePattern() == null) {

      // intelligently set date pattern if none is given
      if (timeUnit == MILLIS_SCALE) {
        datePattern = "ss.SSS";
      }
      else if (timeUnit == SEC_SCALE) {
        datePattern = "mm:ss";
      }
      else if (timeUnit == MIN_SCALE) {
        datePattern = "HH:mm";
      }
      else if (timeUnit == HOUR_SCALE) {
        datePattern = "HH:mm";
      }
      else if (timeUnit == DAY_SCALE) {
        datePattern = "MM-dd";
      }
      else if (timeUnit == MONTH_SCALE) {
        datePattern = "yyyy-MM";
      }
      else {
        datePattern = "yyyy";
      }
    }
    else {
      datePattern = styleManager.getDatePattern();
    }

    SimpleDateFormat simpleDateformat = new SimpleDateFormat(datePattern, styleManager.getLocale());
    simpleDateformat.setTimeZone(styleManager.getTimezone());
    simpleDateformat.applyPattern(datePattern);

    return simpleDateformat.format(value);
  }

  Map<Long, int[]> getValidTickStepsMap() {

    return validTickStepsMap;
  }
}
