/**
 * Copyright 2011 - 2013 Xeiam LLC.
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
package com.xeiam.xchart;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.xeiam.xchart.internal.chartpart.DateFormatter;

/**
 * @author timmolter
 */
public class DateFormatterTest {

  private final Locale locale = Locale.US;

  @Test
  public void testDateFormatting() {

    StyleManager styleManager = new StyleManager();
    DateFormatter dateFormatter = new DateFormatter(styleManager);

    TimeZone timeZone = TimeZone.getTimeZone("UTC");

    styleManager.setLocale(locale);
    styleManager.setTimezone(timeZone);

    // ms
    BigDecimal value = new BigDecimal(1358108105531L);
    BigDecimal min = new BigDecimal(1358108105100L);
    BigDecimal max = new BigDecimal(1358108105900L);
    long span = Math.abs(max.subtract(min).longValue()); // in data space
    long gridStepHint = (long) (span / (double) 1000 * 74);
    long timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    String stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("05.531"));

    // sec
    value = new BigDecimal(1358108105000L);
    min = new BigDecimal(1358108101000L);
    max = new BigDecimal(1358108109000L);
    span = Math.abs(max.subtract(min).longValue()); // in data space
    gridStepHint = (long) (span / (double) 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("05.000"));

    // min
    value = new BigDecimal(1358111750000L);
    min = new BigDecimal(1358111690000L);
    max = new BigDecimal(1358111870000L);
    span = Math.abs(max.subtract(min).longValue()); // in data space
    gridStepHint = (long) (span / (double) 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("15:50"));

    // hour
    value = new BigDecimal(1358111870000L);
    min = new BigDecimal(1358101070000L);
    max = new BigDecimal(1358115470000L);
    span = Math.abs(max.subtract(min).longValue()); // in data space
    gridStepHint = (long) (span / (double) 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("21:17"));

    // day
    value = new BigDecimal(1358112317000L);
    min = new BigDecimal(1357939517000L);
    max = new BigDecimal(1358285117000L);
    span = Math.abs(max.subtract(min).longValue()); // in data space
    gridStepHint = (long) (span / (double) 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("13-21"));

    // week
    value = new BigDecimal(1358112317000L);
    min = new BigDecimal(1357075517000L);
    max = new BigDecimal(1359149117000L);
    span = Math.abs(max.subtract(min).longValue()); // in data space
    gridStepHint = (long) (span / (double) 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("01-13"));

    // month
    value = new BigDecimal(1358112838000L);
    min = new BigDecimal(1354397638000L);
    max = new BigDecimal(1361223238000L);
    span = Math.abs(max.subtract(min).longValue()); // in data space
    gridStepHint = (long) (span / (double) 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("01-13"));

    // year
    value = new BigDecimal(1358113402000L);
    min = new BigDecimal(1263419002000L);
    max = new BigDecimal(1421185402000L);
    span = Math.abs(max.subtract(min).longValue()); // in data space
    gridStepHint = (long) (span / (double) 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("2013-01"));

  }

}
