package org.knowm.xchart.internal.chartpart;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.function.Function;

/** @author Marc Jakobi */
public abstract class Formatter_Abstract extends Format {

  private final Function<Double, String> customFormattingFunction;
  
  protected Number number;
  protected double number_double;
  protected String string;

  /** Constructor */
  public Formatter_Abstract() {}

  @Override
  public StringBuffer format(Object object, StringBuffer toAppendTo, FieldPosition fieldPosition) {
    ObjectTransformation(object);
    String decimalPattern = decideDecimalPattern();
    DecimalFormat normalFormat = applyPattern(decimalPattern);
    toAppend(toAppendTo, normalFormat);
    return toAppendTo;
  }

//현재 각 formatter마다 object를 바꾸는 형식이 다르다. 따라서 3개의 object를 모두 각 형식으로 바꿔둔 뒤, 필요한 형식을 사용한다.
  protected abstract void ObjectTransformation(Object object);

  protected abstract String decideDecimalPattern();
  
  protected abstract DecimalFormat applyPattern(String decimalPattern);
  
  protected abstract void toAppend(StringBuffer toAppendTo, DecimalFormat normalFormat);
  
  @Override 
  public Object parseObject(String string, ParsePosition parsePosition) {
    return null;
  }
}
