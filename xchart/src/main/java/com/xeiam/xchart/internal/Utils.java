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
package com.xeiam.xchart.internal;

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
   * Gets the offset for the beginning of the tick marks
   * 
   * @param workingSpace
   * @param tickSpace
   * @return
   */
  public static double getTickStartOffset(double workingSpace, double tickSpace) {

    double marginSpace = workingSpace - tickSpace;
    return marginSpace / 2.0;
  }

  public static double pow(double base, int exponent) {

    if (exponent > 0) {
      return Math.pow(base, exponent);
    }
    else {
      return 1.0 / Math.pow(base, -1 * exponent);

    }
  }

}
