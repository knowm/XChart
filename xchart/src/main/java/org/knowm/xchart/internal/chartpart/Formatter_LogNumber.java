package org.knowm.xchart.internal.chartpart;

import java.text.*;
import org.knowm.xchart.style.AxesChartStyler;

/** @author timmolter */
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
  public StringBuffer format(Object numberObject, StringBuffer toAppendTo, FieldPosition pos) {

    double number = (Double) numberObject;


    String decimalPattern = setDecimalPattern(number);

    DecimalFormat normalFormat = (DecimalFormat) numberFormat;
    normalFormat.applyPattern(decimalPattern);
    toAppendTo.append(normalFormat.format(number));

    return toAppendTo;
  }
  
  public String setDecimalPattern(double number) {
	final double NUMBER_LOW_LIMIT = 0.001;
	final double NULBER_HIGH_LIMIT = 1000.0;
    String decimalPattern;

    boolean isNotEmptyXaxis = ((axisDirection == Axis.Direction.X) && (styler.getXAxisDecimalPattern() != null));
	boolean isNotEmptyYaxis = (axisDirection == Axis.Direction.Y && (styler.getYAxisGroupDecimalPatternMap().get(yIndex) != null
            || styler.getYAxisDecimalPattern() != null));
	boolean isNotEmpty = (styler.getDecimalPattern() != null); 
	
    if (isNotEmptyXaxis) {
      decimalPattern = styler.getXAxisDecimalPattern();
    } 
    
    else if (isNotEmptyYaxis) {
    	if (styler.getYAxisGroupDecimalPatternMap().get(yIndex) != null) {
    		decimalPattern = styler.getYAxisGroupDecimalPatternMap().get(yIndex);
    	} else {
        decimalPattern = styler.getYAxisDecimalPattern();
    	}
    } 
    
    else if (isNotEmpty) {
    	decimalPattern = styler.getDecimalPattern();
    } 
    
    else {
    	if (Math.abs(number) > NULBER_HIGH_LIMIT || Math.abs(number) < NUMBER_LOW_LIMIT) {
    		decimalPattern = "0E0";
	    } else {
	        decimalPattern = "0.###";
	    }
    }
    return decimalPattern;
}

  @Override
  public Object parseObject(String source, ParsePosition position) {

    return null;
  }
}
