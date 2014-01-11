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

    String stringValue = numberFormatter.formatNumber(1);
    assertThat(stringValue, equalTo("1"));

    stringValue = numberFormatter.formatNumber(1000);
    assertThat(stringValue, equalTo("1000"));

    stringValue = numberFormatter.formatNumber(9999);
    assertThat(stringValue, equalTo("9999"));

    stringValue = numberFormatter.formatNumber(20000);
    assertThat(stringValue, equalTo("2E4"));

    stringValue = numberFormatter.formatNumber(200.23);
    assertThat(stringValue, equalTo("200.23"));

    // small

    stringValue = numberFormatter.formatNumber(0.01);
    assertThat(stringValue, equalTo("0.01"));

    stringValue = numberFormatter.formatNumber(0.001);
    assertThat(stringValue, equalTo("0.001"));

    stringValue = numberFormatter.formatNumber(0.0012);
    assertThat(stringValue, equalTo("0.0012"));

    stringValue = numberFormatter.formatNumber(0.0001);
    assertThat(stringValue, equalTo("1E-4"));

    stringValue = numberFormatter.formatNumber(.00012);
    assertThat(stringValue, equalTo("1.2E-4"));

    stringValue = numberFormatter.formatNumber(0.0);
    assertThat(stringValue, equalTo("0"));

    stringValue = numberFormatter.formatNumber(0);
    assertThat(stringValue, equalTo("0"));

    // non-default
    styleManager.setLocale(Locale.GERMANY);

    stringValue = numberFormatter.formatNumber(0.01);
    assertThat(stringValue, equalTo("0,01"));

    stringValue = numberFormatter.formatNumber(200.23);
    assertThat(stringValue, equalTo("200,23"));

    styleManager.setNormalDecimalPattern("#.#");
    stringValue = numberFormatter.formatNumber(200.23);
    assertThat(stringValue, equalTo("200,2"));

    styleManager.setScientificDecimalPattern("0.#E0");
    stringValue = numberFormatter.formatNumber(2009764.23);
    assertThat(stringValue, equalTo("2E6"));

  }

}
