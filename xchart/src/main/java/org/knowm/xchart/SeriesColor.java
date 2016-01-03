/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
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
package org.knowm.xchart;

import java.awt.Color;

/**
 * Pre-defined Colors used for Series Lines and Markers
 *
 * @author timmolter
 */
public class SeriesColor {

  /** The AWT Color */
  private Color color;

  /**
   * Constructor
   *
   * @param color
   */
  public SeriesColor(Color color) {

    this.color = color;
  }

  /**
   * Constructor
   *
   * @param color
   */
  public SeriesColor(int r, int g, int b, int a) {

    this.color = new Color(r, g, b, a);
  }

  /**
   * Gets the SeriesColor's AWT Color
   *
   * @return the AWT Color
   */
  public Color getColor() {

    return color;
  }

  // /**
  // * get the AWT Color given a SeriesColor
  // *
  // * @param seriesColor
  // * @return the AWT Color
  // */
  // public Color getAWTColor(SeriesColor seriesColor) {
  //
  // return seriesColor.color;
  // }

}
