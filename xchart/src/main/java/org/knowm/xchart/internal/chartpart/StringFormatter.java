package org.knowm.xchart.internal.chartpart;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * Dummy Format class for labels
 *
 * @author timmolter
 */
class StringFormatter extends Format {

  /** Constructor */
  public StringFormatter() {}

  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {

    String string = obj.toString();

    toAppendTo.append(string);

    return toAppendTo;
  }

  @Override
  public Object parseObject(String source, ParsePosition pos) {

    return null;
  }
}
