/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
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
package org.knowm.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * @author timmolter
 */
public class NumberFormatter {

  private final AxesChartStyler styler;

  /**
   * Constructor
   */
  public NumberFormatter(AxesChartStyler styler) {

    this.styler = styler;
  }

  public String getFormatPattern(BigDecimal value, double min, double max) {

    // System.out.println("value: " + value);
    // System.out.println("min: " + min);
    // System.out.println("max: " + max);

    // some special cases first
    if (value.compareTo(BigDecimal.ZERO) == 0) {
      return "0";
    }

    double difference = max - min;
    int placeOfDifference;
    if (difference == 0.0) {
      placeOfDifference = 0;
    }
    else {
      placeOfDifference = (int) Math.floor(Math.log(difference) / Math.log(10));
    }
    int placeOfValue;
    if (value.doubleValue() == 0.0) {
      placeOfValue = 0;
    }
    else {
      placeOfValue = (int) Math.floor(Math.log(value.doubleValue()) / Math.log(10));
    }

    // System.out.println("difference: " + difference);
    // System.out.println("placeOfDifference: " + placeOfDifference);
    // System.out.println("placeOfValue: " + placeOfValue);

    if (placeOfDifference <= 4 && placeOfDifference >= -4) {
      // System.out.println("getNormalDecimalPattern");
      return getNormalDecimalPatternPositive(placeOfValue, placeOfDifference);
    }
    else {
      // System.out.println("getScientificDecimalPattern");
      return getScientificDecimalPattern();
    }
  }

  private String getNormalDecimalPatternPositive(int placeOfValue, int placeOfDifference) {

    int maxNumPlaces = 15;
    StringBuilder sb = new StringBuilder();
    for (int i = maxNumPlaces - 1; i >= -1 * maxNumPlaces; i--) {

      if (i >= 0 && (i < placeOfValue)) {
        sb.append("0");
      }
      else if (i < 0 && (i > placeOfValue)) {
        sb.append("0");
      }
      else {
        sb.append("#");
      }
      if (i % 3 == 0 && i > 0) {
        sb.append(",");
      }
      if (i == 0) {
        sb.append(".");
      }
    }
    // System.out.println(sb.toString());
    return sb.toString();
  }

  private String getScientificDecimalPattern() {

    return "0.###############E0";
  }

  /**
   * Format a number value, if the override patterns are null, it uses defaults
   *
   * @param value
   * @param min
   * @param max
   * @param axisDirection
   * @return
   */
  public String formatNumber(BigDecimal value, double min, double max, Direction axisDirection) {

    NumberFormat numberFormat = NumberFormat.getNumberInstance(styler.getLocale());

    String decimalPattern;

    if (axisDirection == Direction.X && styler.getXAxisDecimalPattern() != null) {

      decimalPattern = styler.getXAxisDecimalPattern();
    }
    else if (axisDirection == Direction.Y && styler.getYAxisDecimalPattern() != null) {
      decimalPattern = styler.getYAxisDecimalPattern();
    }
    else if (styler.getDecimalPattern() != null) {

      decimalPattern = styler.getDecimalPattern();
    }
    else {
      decimalPattern = getFormatPattern(value, min, max);
    }
    // System.out.println(decimalPattern);

    DecimalFormat normalFormat = (DecimalFormat) numberFormat;
    normalFormat.applyPattern(decimalPattern);
    return normalFormat.format(value);

  }

  /**
   * Format a log number value for log Axes which show only decade tick labels. if the override patterns are null, it uses defaults
   *
   * @param value
   * @return
   */
  public String formatLogNumber(double value, Direction axisDirection) {

    NumberFormat numberFormat = NumberFormat.getNumberInstance(styler.getLocale());

    String decimalPattern;

    if (axisDirection == Direction.X && styler.getXAxisDecimalPattern() != null) {

      decimalPattern = styler.getXAxisDecimalPattern();
    }
    else if (axisDirection == Direction.Y && styler.getYAxisDecimalPattern() != null) {
      decimalPattern = styler.getYAxisDecimalPattern();
    }
    else if (styler.getDecimalPattern() != null) {

      decimalPattern = styler.getDecimalPattern();
    }
    else {
      if (Math.abs(value) > 1000.0 || Math.abs(value) < 0.001) {
        decimalPattern = "0E0";
      }
      else {
        decimalPattern = "0.###";
      }
    }

    DecimalFormat normalFormat = (DecimalFormat) numberFormat;
    normalFormat.applyPattern(decimalPattern);
    return normalFormat.format(value);

  }
}
