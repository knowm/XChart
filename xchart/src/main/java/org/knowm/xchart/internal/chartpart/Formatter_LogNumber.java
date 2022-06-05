package org.knowm.xchart.internal.chartpart;

import java.text.*;
import org.knowm.xchart.style.AxesChartStyler;

/** @author timmolter */
class Formatter_LogNumber extends Formatter_Abstract {

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
  protected void toAppend(StringBuffer toAppendTo, DecimalFormat normalFormat) {
	  toAppendTo.append(normalFormat.format(this.number_double));
  }
  
  @Override
  protected DecimalFormat applyPattern(String decimalPattern) {
	  DecimalFormat normalFormat = (DecimalFormat) numberFormat;
	  normalFormat.applyPattern(decimalPattern);
	  
	  return normalFormat;
  }
  
  @Override
  protected String decideDecimalPattern() {
	  return setDecimalPattern(this.number_double);
  }
  
  @Override
  protected void ObjectTransformation(Object object) {
	  this.number_double = (Double) object;
  }
  
  public String setDecimalPattern(double number) {
	final double NUMBER_LOW_LIMIT = 0.001;
	final double NUMBER_HIGH_LIMIT = 1000.0;
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
    	if (Math.abs(number) > NUMBER_HIGH_LIMIT || Math.abs(number) < NUMBER_LOW_LIMIT) {
    		decimalPattern = "0E0";
	    } else {
	        decimalPattern = "0.###";
	    }
    }
    return decimalPattern;
}
}
