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

import org.junit.Test;

import com.xeiam.xchart.internal.chartpart.NumberFormatter;

/**
 * @author timmolter
 */
public class NumberFormatterTest {

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
