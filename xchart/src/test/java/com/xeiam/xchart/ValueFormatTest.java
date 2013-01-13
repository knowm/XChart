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
package com.xeiam.xchart;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.Locale;

import org.junit.Test;

import com.xeiam.xchart.internal.misc.AxisValueFormatterUtil;

/**
 * @author timmolter
 */
public class ValueFormatTest {

  // private final String normalDecimalPattern = "#.####";
  // private final String scientificDecimalPattern = "0.##E0";
  // private final String datePattern = "HHmmss";
  private final Locale locale = Locale.US;

  @Test
  public void testNumberFormatting() {

    // big

    BigDecimal value = new BigDecimal("1");
    String stringValue = AxisValueFormatterUtil.formatNumber(value, null, null, locale);
    assertThat(stringValue, equalTo("1"));

    value = new BigDecimal(1000L);
    stringValue = AxisValueFormatterUtil.formatNumber(value, null, null, locale);
    assertThat(stringValue, equalTo("1000"));

    value = new BigDecimal("9999");
    stringValue = AxisValueFormatterUtil.formatNumber(value, null, null, locale);
    assertThat(stringValue, equalTo("9999"));

    value = new BigDecimal(20000L);
    stringValue = AxisValueFormatterUtil.formatNumber(value, null, null, locale);
    assertThat(stringValue, equalTo("2E4"));

    value = new BigDecimal("200.23");
    stringValue = AxisValueFormatterUtil.formatNumber(value, null, null, locale);
    assertThat(stringValue, equalTo("200.23"));

    // small

    value = new BigDecimal("0.01");
    stringValue = AxisValueFormatterUtil.formatNumber(value, null, null, locale);
    assertThat(stringValue, equalTo("0.01"));

    value = new BigDecimal("0.001");
    stringValue = AxisValueFormatterUtil.formatNumber(value, null, null, locale);
    assertThat(stringValue, equalTo("0.001"));

    value = new BigDecimal("0.0001");
    stringValue = AxisValueFormatterUtil.formatNumber(value, null, null, locale);
    assertThat(stringValue, equalTo("1E-4"));

    // other case

    // TODO handle these cases better

    // value = new BigDecimal("12228120");
    // stringValue = NumberFormatterUtil.formatNumber(value, null, null, locale);
    // assertThat(stringValue, equalTo("0.01"));

    // value = new BigDecimal("0.00000000230000056765");
    // stringValue = NumberFormatterUtil.formatNumber(value, null, null, locale);
    // assertThat(stringValue, equalTo("0.01"));

    // non-default

    value = new BigDecimal("0.01");
    stringValue = AxisValueFormatterUtil.formatNumber(value, null, null, Locale.GERMANY);
    assertThat(stringValue, equalTo("0,01"));

    value = new BigDecimal("200.23");
    stringValue = AxisValueFormatterUtil.formatNumber(value, null, null, Locale.GERMANY);
    assertThat(stringValue, equalTo("200,23"));

    value = new BigDecimal("200.23");
    stringValue = AxisValueFormatterUtil.formatNumber(value, "#.#", null, Locale.GERMANY);
    assertThat(stringValue, equalTo("200,2"));

    value = new BigDecimal("2009764.23");
    stringValue = AxisValueFormatterUtil.formatNumber(value, null, "0.#E0", Locale.GERMANY);
    assertThat(stringValue, equalTo("2E6"));

  }

  @Test
  public void testDateFormatting() {

    // ms
    BigDecimal value = new BigDecimal(1358108105531L);
    BigDecimal min = new BigDecimal(1358108105100L);
    BigDecimal max = new BigDecimal(1358108105900L);
    String stringValue = AxisValueFormatterUtil.formatDateValue(value, min, max, null, locale);
    assertThat(stringValue, equalTo("05:531"));

    // sec
    value = new BigDecimal(1358108105000L);
    min = new BigDecimal(1358108101000L);
    max = new BigDecimal(1358108109000L);
    stringValue = AxisValueFormatterUtil.formatDateValue(value, min, max, null, locale);
    assertThat(stringValue, equalTo("15:05"));

    // min
    value = new BigDecimal(1358111750000L);
    min = new BigDecimal(1358111690000L);
    max = new BigDecimal(1358111870000L);
    stringValue = AxisValueFormatterUtil.formatDateValue(value, min, max, null, locale);
    assertThat(stringValue, equalTo("22:15"));

    // hour
    value = new BigDecimal(1358111870000L);
    min = new BigDecimal(1358101070000L);
    max = new BigDecimal(1358115470000L);
    stringValue = AxisValueFormatterUtil.formatDateValue(value, min, max, null, locale);
    assertThat(stringValue, equalTo("13:22"));

    // day
    value = new BigDecimal(1358112317000L);
    min = new BigDecimal(1357939517000L);
    max = new BigDecimal(1358285117000L);
    stringValue = AxisValueFormatterUtil.formatDateValue(value, min, max, null, locale);
    assertThat(stringValue, equalTo("Sun"));

    // week
    value = new BigDecimal(1358112317000L);
    min = new BigDecimal(1357075517000L);
    max = new BigDecimal(1359149117000L);
    stringValue = AxisValueFormatterUtil.formatDateValue(value, min, max, null, locale);
    assertThat(stringValue, equalTo("Jan-13"));

    // month
    value = new BigDecimal(1358112838000L);
    min = new BigDecimal(1354397638000L);
    max = new BigDecimal(1361223238000L);
    stringValue = AxisValueFormatterUtil.formatDateValue(value, min, max, null, locale);
    assertThat(stringValue, equalTo("2013:Jan"));

    // year
    value = new BigDecimal(1358113402000L);
    min = new BigDecimal(1263419002000L);
    max = new BigDecimal(1421185402000L);
    stringValue = AxisValueFormatterUtil.formatDateValue(value, min, max, null, locale);
    assertThat(stringValue, equalTo("2013"));

  }
}
