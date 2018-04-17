package org.knowm.xchart.internal.chartpart;

import java.text.*;
import org.knowm.xchart.style.AxesChartStyler;

/** @author timmolter */
class NumberLogFormatter extends Format {

  private final AxesChartStyler styler;
  private final Axis.Direction axisDirection;
  private final NumberFormat numberFormat;

  /** Constructor */
  public NumberLogFormatter(AxesChartStyler styler, Axis.Direction axisDirection) {

    this.styler = styler;
    this.axisDirection = axisDirection;
    numberFormat = NumberFormat.getNumberInstance(styler.getLocale());
  }

  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

    double number = (Double) obj;

    String decimalPattern;

    if (axisDirection == Axis.Direction.X && styler.getXAxisDecimalPattern() != null) {

      decimalPattern = styler.getXAxisDecimalPattern();
    } else if (axisDirection == Axis.Direction.Y && styler.getYAxisDecimalPattern() != null) {
      decimalPattern = styler.getYAxisDecimalPattern();
    } else if (styler.getDecimalPattern() != null) {

      decimalPattern = styler.getDecimalPattern();
    } else {
      if (Math.abs(number) > 1000.0 || Math.abs(number) < 0.001) {
        decimalPattern = "0E0";
      } else {
        decimalPattern = "0.###";
      }
    }

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
