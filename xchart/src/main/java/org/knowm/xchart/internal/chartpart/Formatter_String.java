package org.knowm.xchart.internal.chartpart;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * Dummy Format class for labels
 *
 * @author timmolter
 */
class Formatter_String extends Formatter_Abstract {

  /** Constructor */
  public Formatter_String() {}
  
  @Override
  protected void toAppend(StringBuffer toAppendTo, DecimalFormat normalFormat) {
	  toAppendTo.append(this.string);
  }
  
  @Override
  protected DecimalFormat applyPattern(String decimalPattern) {
	  return null;
  }
  
  @Override
  protected void ObjectTransformation(Object object) {
	  this.string = object.toString();
  }
  
  @Override
  protected String decideDecimalPattern() {
	  return null;
  }
}
