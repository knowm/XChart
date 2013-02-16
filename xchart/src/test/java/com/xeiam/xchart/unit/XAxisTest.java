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
package com.xeiam.xchart.unit;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.chartpart.Axis.Direction;
import com.xeiam.xchart.internal.chartpart.AxisTickComputer;
import com.xeiam.xchart.style.ValueFormatter;

/**
 * @author timmolter
 */
public class XAxisTest {

  // @Test
  public void testNumber() {

    AxisTickComputer axisTickComputer = new AxisTickComputer(Direction.X, 1000, new BigDecimal(0), new BigDecimal(10), new ValueFormatter(), AxisType.Number);
    // Labels
    List<String> tickLabels = axisTickComputer.getTickLabels();
    System.out.println(Arrays.toString(tickLabels.toArray()));
    // Locations
    List<Integer> tickLocations = axisTickComputer.getTickLocations();
    System.out.println(Arrays.toString(tickLocations.toArray()));
  }

  @Test
  public void testDateOneMinuteTimespan() {

    AxisTickComputer axisTickComputer = new AxisTickComputer(Direction.X, 1000, new BigDecimal(1361031254000L), new BigDecimal(1361031314000L), new ValueFormatter(), AxisType.Date);
    // Labels
    List<String> tickLabels = axisTickComputer.getTickLabels();
    System.out.println(Arrays.toString(tickLabels.toArray()));
    // Locations
    List<Integer> tickLocations = axisTickComputer.getTickLocations();
    System.out.println(Arrays.toString(tickLocations.toArray()));
  }

}
