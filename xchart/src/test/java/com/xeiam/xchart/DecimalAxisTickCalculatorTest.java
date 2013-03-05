/**
 * Copyright (C) 2013 Xeiam LLC http://xeiam.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchart;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.internal.chartpart.AxisTickNumericalCalculator;
import com.xeiam.xchart.internal.chartpart.Axis.Direction;

/**
 * @author timmolter
 */
public class DecimalAxisTickCalculatorTest {

  @Test
  public void testDateOneMinuteTimespan() {

    AxisTickNumericalCalculator decimalAxisTickCalculator = new AxisTickNumericalCalculator(Direction.X, 600, new BigDecimal(-15), new BigDecimal(15), new StyleManager());

    List<String> tickLabels = decimalAxisTickCalculator.getTickLabels();
    System.out.println(Arrays.toString(tickLabels.toArray()));
    assertThat(tickLabels.size(), equalTo(7));
    assertThat(tickLabels.get(0), equalTo("-15"));

    List<Integer> tickLocations = decimalAxisTickCalculator.getTickLocations();
    System.out.println(Arrays.toString(tickLocations.toArray()));
    assertThat(tickLocations.size(), equalTo(7));
    assertThat(tickLocations.get(0), equalTo(15));
  }
}
