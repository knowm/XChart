/**
 * Copyright 2013 Xeiam LLC.
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
package com.xeiam.xchart.internal;

import java.math.BigDecimal;

/**
 * @author timmolter
 */
public class Utils {

  /**
   * Constructor
   */
  private Utils() {

  }

  /**
   * Gets the percentage of working space allowed for tick marks
   * 
   * @param workingSpace
   * @return
   */
  public static int getTickSpace(int workingSpace) {

    return (int) (workingSpace * 0.95);
  }

  /**
   * Gets the offset for the beginning of the tick marks
   * 
   * @param workingSpace
   * @param tickSpace
   * @return
   */
  public static int getTickStartOffset(int workingSpace, int tickSpace) {

    int marginSpace = workingSpace - tickSpace;
    return (int) (marginSpace / 2.0);
  }

  public static BigDecimal pow(double base, int exponent) {

    if (exponent > 0) {
      return new BigDecimal(base).pow(exponent);
    }
    else {
      return BigDecimal.ONE.divide(new BigDecimal(base).pow(-exponent));
    }
  }

}
