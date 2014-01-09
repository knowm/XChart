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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.xeiam.xchart.StyleManager;

/**
 * @author timmolter
 */
public class NumberFormatter {

  private final StyleManager styleManager;

  /**
   * Constructor
   */
  public NumberFormatter(StyleManager styleManager) {

    this.styleManager = styleManager;
  }

  /**
   * Format a number value, if the override patterns are null, it uses defaults
   * 
   * @param value
   * @return
   */
  public String formatNumber(BigDecimal value) {

    NumberFormat numberFormat = NumberFormat.getNumberInstance(styleManager.getLocale());

    BigDecimal absoluteValue = value.abs();

    if (absoluteValue.compareTo(new BigDecimal("10000.000001")) == -1 && absoluteValue.compareTo(new BigDecimal(".0009999999")) == 1 || BigDecimal.ZERO.compareTo(value) == 0) {

      DecimalFormat normalFormat = (DecimalFormat) numberFormat;
      normalFormat.applyPattern(styleManager.getNormalDecimalPattern());
      return normalFormat.format(value);

    }
    else {

      DecimalFormat scientificFormat = (DecimalFormat) numberFormat;
      scientificFormat.applyPattern(styleManager.getScientificDecimalPattern());
      return scientificFormat.format(value);

    }

  }
}
