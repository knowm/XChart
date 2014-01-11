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
package com.xeiam.xchart;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

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
    double value = 1358108105531L;
    double min = 1358108105100L;
    double max = 1358108105900L;
    double span = Math.abs(max - min); // in data space
    long gridStepHint = (long) (span / 1000 * 74);
    long timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    String stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("05.531"));

    // sec
    value = 1358108105000L;
    min = 1358108101000L;
    max = 1358108109000L;
    span = Math.abs(max - min); // in data space
    gridStepHint = (long) (span / 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("05.000"));

    // min
    value = 1358111750000L;
    min = 1358111690000L;
    max = 1358111870000L;
    span = Math.abs(max - min); // in data space
    gridStepHint = (long) (span / 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("15:50"));

    // hour
    value = 1358111870000L;
    min = 1358101070000L;
    max = 1358115470000L;
    span = Math.abs(max - min); // in data space
    gridStepHint = (long) (span / 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("21:17"));

    // day
    value = 1358112317000L;
    min = 1357939517000L;
    max = 1358285117000L;
    span = Math.abs(max - min); // in data space
    gridStepHint = (long) (span / 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("21:25"));

    // week
    value = 1358112317000L;
    min = 1357075517000L;
    max = 1359149117000L;
    span = Math.abs(max - min); // in data space
    gridStepHint = (long) (span / 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("01-13"));

    // month
    value = 1358112838000L;
    min = 1354397638000L;
    max = 1361223238000L;
    span = Math.abs(max - min); // in data space
    gridStepHint = (long) (span / 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("01-13"));

    // year
    value = 1358113402000L;
    min = 1263419002000L;
    max = 1421185402000L;
    span = Math.abs(max - min); // in data space
    gridStepHint = (long) (span / 1000 * 74);
    timeUnit = dateFormatter.getTimeUnit(gridStepHint);
    stringValue = dateFormatter.formatDate(value, timeUnit);
    assertThat(stringValue, equalTo("2013-01"));

  }

}
