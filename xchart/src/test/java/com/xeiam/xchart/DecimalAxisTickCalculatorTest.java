/**
 * Copyright 2011 - 2014 Xeiam LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.xchart;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.xeiam.xchart.internal.chartpart.Axis.Direction;
import com.xeiam.xchart.internal.chartpart.AxisTickNumericalCalculator;

/**
 * @author timmolter
 */
public class DecimalAxisTickCalculatorTest {

  @Test
  public void testDateOneMinuteTimespan() {

    AxisTickNumericalCalculator decimalAxisTickCalculator = new AxisTickNumericalCalculator(Direction.X, 600, -15, 15, new StyleManager());

    List<String> tickLabels = decimalAxisTickCalculator.getTickLabels();
    System.out.println(Arrays.toString(tickLabels.toArray()));
    assertThat(tickLabels.size(), equalTo(7));
    assertThat(tickLabels.get(0), equalTo("-15"));

    List<Double> tickLocations = decimalAxisTickCalculator.getTickLocations();
    System.out.println(Arrays.toString(tickLocations.toArray()));
    assertThat(tickLocations.size(), equalTo(7));
    assertThat(tickLocations.get(0), equalTo(15.0));
  }
}
