package org.knowm.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.text.*;
import org.knowm.xchart.style.AxesChartStyler;

/** @author timmolter */
class NumberFormatter extends Format {

  private final AxesChartStyler styler;
  private final Axis.Direction axisDirection;
  private final double min;
  private final double max;
  private final NumberFormat numberFormat;

  /** Constructor */
  public NumberFormatter(
      AxesChartStyler styler, Axis.Direction axisDirection, double min, double max) {

    this.styler = styler;
    this.axisDirection = axisDirection;
    this.min = min;
    this.max = max;
    numberFormat = NumberFormat.getNumberInstance(styler.getLocale());
  }

  private String getFormatPattern(double value) {

    // System.out.println("value: " + value);
    // System.out.println("min: " + min);
    // System.out.println("max: " + max);

    // some special cases first
    if (BigDecimal.valueOf(value).compareTo(BigDecimal.ZERO) == 0) {
      return "0";
    }

    double difference = max - min;
    int placeOfDifference;
    if (difference == 0.0) {
      placeOfDifference = 0;
    } else {
      placeOfDifference = (int) Math.floor(Math.log(difference) / Math.log(10));
    }
    int placeOfValue;
    if (value == 0.0) {
      placeOfValue = 0;
    } else {
      placeOfValue = (int) Math.floor(Math.log(value) / Math.log(10));
    }

    // System.out.println("difference: " + difference);
    // System.out.println("placeOfDifference: " + placeOfDifference);
    // System.out.println("placeOfValue: " + placeOfValue);

    if (placeOfDifference <= 4 && placeOfDifference >= -4) {
      // System.out.println("getNormalDecimalPattern");
      return getNormalDecimalPatternPositive(placeOfValue, placeOfDifference);
    } else {
      // System.out.println("getScientificDecimalPattern");
      return "0.###############E0";
    }
  }

  private String getNormalDecimalPatternPositive(int placeOfValue, int placeOfDifference) {

    int maxNumPlaces = 15;
    StringBuilder sb = new StringBuilder();
    for (int i = maxNumPlaces - 1; i >= -1 * maxNumPlaces; i--) {

      if (i >= 0 && (i < placeOfValue)) {
        sb.append("0");
      } else if (i < 0 && (i > placeOfValue)) {
        sb.append("0");
      } else {
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

  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

    // BigDecimal number = (BigDecimal) obj;
    Number number = (Number) obj;

    String decimalPattern;

    if (axisDirection == Axis.Direction.X && styler.getXAxisDecimalPattern() != null) {

      decimalPattern = styler.getXAxisDecimalPattern();
    } else if (axisDirection == Axis.Direction.Y && styler.getYAxisDecimalPattern() != null) {
      decimalPattern = styler.getYAxisDecimalPattern();
    } else if (styler.getDecimalPattern() != null) {
      decimalPattern = styler.getDecimalPattern();
    } else {
      decimalPattern = getFormatPattern(number.doubleValue());
    }
    // System.out.println(decimalPattern);

    DecimalFormat normalFormat = (DecimalFormat) numberFormat;
    normalFormat.applyPattern(decimalPattern);
    toAppendTo.append(normalFormat.format(number));

    return toAppendTo;
  }

  @Override
  public Object parseObject(String source, ParsePosition pos) {

    return null;
  }
}
