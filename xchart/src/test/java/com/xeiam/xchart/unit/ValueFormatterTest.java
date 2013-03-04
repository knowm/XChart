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
package com.xeiam.xchart.unit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.Locale;

import org.junit.Test;

import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.internal.chartpart.NumberFormatter;

/**
 * @author timmolter
 */
public class ValueFormatterTest {

  private final Locale locale = Locale.US;

  @Test
  public void testNumberFormatting() {

    StyleManager styleManager = new StyleManager();
    NumberFormatter numberFormatter = new NumberFormatter(styleManager);

    // big
    styleManager.setLocale(locale);

    BigDecimal value = new BigDecimal("1");
    String stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("1"));

    value = new BigDecimal(1000L);
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("1000"));

    value = new BigDecimal("9999");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("9999"));

    value = new BigDecimal(20000L);
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("2E4"));

    value = new BigDecimal("200.23");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("200.23"));

    // small

    value = new BigDecimal("0.01");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("0.01"));

    value = new BigDecimal("0.001");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("0.001"));

    value = new BigDecimal("0.0012");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("0.0012"));

    value = new BigDecimal("0.0001");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("1E-4"));

    value = new BigDecimal(".00012");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("1.2E-4"));

    value = new BigDecimal("0.0");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("0"));

    value = new BigDecimal("0");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("0"));

    // other case

    // TODO handle these cases better

    // value = new BigDecimal("12228120");
    // stringValue = NumberFormatterUtil.formatNumber(value, null, null, locale);
    // assertThat(stringValue, equalTo("0.01"));

    // value = new BigDecimal("0.00000000230000056765");
    // stringValue = NumberFormatterUtil.formatNumber(value, null, null, locale);
    // assertThat(stringValue, equalTo("0.01"));

    // non-default
    styleManager.setLocale(Locale.GERMANY);

    value = new BigDecimal("0.01");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("0,01"));

    value = new BigDecimal("200.23");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("200,23"));

    styleManager.setNormalDecimalPattern("#.#");
    value = new BigDecimal("200.23");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("200,2"));

    styleManager.setScientificDecimalPattern("0.#E0");
    value = new BigDecimal("2009764.23");
    stringValue = numberFormatter.formatNumber(value);
    assertThat(stringValue, equalTo("2E6"));

  }

}
