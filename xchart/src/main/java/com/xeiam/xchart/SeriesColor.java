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

import java.awt.Color;

/**
 * Pre-defined Colors used for Series Lines and Markers
 * 
 * @author timmolter
 */
public enum SeriesColor {

  /** BLUE */
  BLUE(0, new Color(0, 55, 255, 180)),

  /** ORANGE */
  ORANGE(1, new Color(255, 172, 0, 180)),

  /** PURPLE */
  PURPLE(2, new Color(128, 0, 255, 180)),

  /** GREEN */
  GREEN(3, new Color(0, 205, 0, 180)),

  /** RED */
  RED(4, new Color(205, 0, 0, 180)),

  /** YELLOW */
  YELLOW(5, new Color(255, 215, 0, 180)),

  /** MAGENTA */
  MAGENTA(6, new Color(255, 0, 255, 180)),

  /** PINK */
  PINK(7, new Color(255, 166, 201, 180)),

  /** LIGHT_GREY */
  LIGHT_GREY(8, new Color(207, 207, 207, 180)),

  /** CYAN */
  CYAN(9, new Color(0, 255, 255, 180)),

  /** BROWN */
  BROWN(10, new Color(102, 56, 10, 180)),

  /** BLACK */
  BLACK(11, new Color(0, 0, 0, 180));

  /** The index */
  private int index;

  /** The AWT Color */
  private Color color;

  /**
   * Constructor
   * 
   * @param index
   * @param color
   */
  private SeriesColor(int index, Color color) {

    this.index = index;
    this.color = color;
  }

  /**
   * Gets the SeriesColor's index
   * 
   * @return
   */
  public Integer getIndex() {

    return index;
  }

  /**
   * Gets the SeriesColor's AWT Color
   * 
   * @return the AWT Color
   */
  public Color getColor() {

    return color;
  }

  /**
   * get the AWT Color given a SeriesColor
   * 
   * @param seriesColor
   * @return the AWT Color
   */
  public Color getAWTColor(SeriesColor seriesColor) {

    return seriesColor.color;
  }

}
