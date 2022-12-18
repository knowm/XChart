package org.knowm.xchart.internal.chartpart;

import java.text.*;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * @author timmolter
 */
class Formatter_LogNumber extends Format {

  private final AxesChartStyler styler;
  private final Axis.Direction axisDirection;
  private final NumberFormat numberFormat;
  private int yIndex;

  /** Constructor */
  public Formatter_LogNumber(AxesChartStyler styler, Axis.Direction axisDirection) {

    this.styler = styler;
    this.axisDirection = axisDirection;
    numberFormat = NumberFormat.getNumberInstance(styler.getLocale());
  }

  /**
   * Constructor
   *
   * @param styler
   * @param axisDirection
   * @param yIndex
   */
  public Formatter_LogNumber(AxesChartStyler styler, Axis.Direction axisDirection, int yIndex) {

    this.styler = styler;
    this.axisDirection = axisDirection;
    this.yIndex = yIndex;
    numberFormat = NumberFormat.getNumberInstance(styler.getLocale());
  }

  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

    double number = (Double) obj;

    String decimalPattern;

    if (axisDirection == Axis.Direction.X && styler.getXAxisDecimalPattern() != null) {

      decimalPattern = styler.getXAxisDecimalPattern();
    } else if (axisDirection == Axis.Direction.Y
        && (styler.getYAxisGroupDecimalPatternMap().get(yIndex) != null
            || styler.getYAxisDecimalPattern() != null)) {
      if (styler.getYAxisGroupDecimalPatternMap().get(yIndex) != null) {
        decimalPattern = styler.getYAxisGroupDecimalPatternMap().get(yIndex);
      } else {
        decimalPattern = styler.getYAxisDecimalPattern();
      }
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
