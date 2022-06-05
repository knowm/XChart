package org.knowm.xchart.internal.chartpart;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.function.Function;

/** @author Marc Jakobi */
public class Formatter_Custom extends Formatter_Abstract {

  private final Function<Double, String> customFormattingFunction;

  public Formatter_Custom(Function<Double, String> customFormattingFunction) {
    this.customFormattingFunction = customFormattingFunction;
  }
  
  @Override
  protected void toAppend(StringBuffer toAppendTo, DecimalFormat normalFormat) {
	  toAppendTo.append(customFormattingFunction.apply(number.doubleValue()));
  }
  
  @Override
  protected DecimalFormat applyPattern(String decimalPattern) {
	  return null;
  }
  
  @Override
  protected void ObjectTransformation(Object object) {
	  this.number = (Number) object;
  }
  
  @Override
  protected String decideDecimalPattern() {
	  return null;//not working
  }
}
