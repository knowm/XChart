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

import org.junit.Test;

import com.xeiam.xchart.internal.chartpart.NumberFormatter;

/**
 * @author timmolter
 */
public class NumberFormatterTest2 {

  @Test
  public void testNumberFormatting() {

    StyleManager styleManager = new StyleManager();
    NumberFormatter numberFormatter = new NumberFormatter(styleManager);

    String pattern = numberFormatter.getFormatPattern(1000000, 1000010);
    System.out.println(pattern);
    assertThat(pattern, equalTo("00000.00E0"));
  }

}
