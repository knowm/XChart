package com.xeiam.xchart.unit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

/**
 * @author timmolter
 * @create Aug 14, 2012
 */
public class TestNumberCollection {

  @Test
  public void testNumber2double() {

    Number myInteger = new Integer(688);
    assertThat(myInteger.doubleValue(), is(equalTo(new Double(688).doubleValue())));

    Number myLong = new Long(976320011);
    assertThat(myLong.doubleValue(), is(equalTo(new Long(976320011).doubleValue())));

    Number myFloat = new Float(95.4765217889);
    assertThat(myFloat.doubleValue(), is(equalTo(new Float(95.4765217889).doubleValue())));

    Number myDouble = new Double(33.2);
    assertThat(myDouble.doubleValue(), is(equalTo(33.2)));

  }

  @Test
  public void testDoubleInfinity() {

    Number myDouble = new Double(33.2);
    assertThat(myDouble.doubleValue(), is(not(equalTo(Double.POSITIVE_INFINITY))));

    myDouble = new Double(Double.POSITIVE_INFINITY);
    assertThat(myDouble.doubleValue(), is(equalTo(Double.POSITIVE_INFINITY)));

    Number myInteger = new Integer(688);
    assertThat(myInteger.doubleValue(), is(not(equalTo(Double.POSITIVE_INFINITY))));

  }
}
