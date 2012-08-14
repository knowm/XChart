/**
 * Copyright 2011-2012 Xeiam LLC.
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
package com.xeiam.xchart.series;

import java.awt.Color;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author timmolter
 */
public enum SeriesColor {

  /** BLUE */
  BLUE(0, new Color(0, 55, 255)),

  /** ORANGE */
  ORANGE(1, new Color(255, 172, 0)),

  /** PURPLE */
  PURPLE(2, new Color(128, 0, 255)),

  /** GREEN */
  GREEN(3, new Color(0, 205, 0)),

  /** RED */
  RED(4, new Color(205, 0, 0)),

  /** YELLOW */
  YELLOW(5, new Color(255, 215, 0)),

  /** MAGENTA */
  MAGENTA(6, new Color(255, 0, 255)),

  /** PINK */
  PINK(7, new Color(255, 166, 201)),

  /** LIGHT_GREY */
  LIGHT_GREY(8, new Color(207, 207, 207)),

  /** CYAN */
  CYAN(9, new Color(0, 255, 255)),

  /** BROWN */
  BROWN(10, new Color(150, 74, 0)),

  /** BLACK */
  BLACK(11, new Color(0, 0, 0)),

  /** RANDOM */
  RANDOM(12, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));

  int id;
  Color color;

  private static int nextId = 0;

  private static final Map<Integer, SeriesColor> idLookup = new HashMap<Integer, SeriesColor>();
  static {
    for (SeriesColor seriesColor : EnumSet.allOf(SeriesColor.class)) {
      idLookup.put(seriesColor.getId(), seriesColor);
    }
  }

  private Integer getId() {

    return id;
  }

  public static void resetId() {

    nextId = 0;
  }

  protected static Color getAWTColor(SeriesColor seriesColor) {

    return seriesColor.color;
  }

  protected static Color getNextAWTColor() {

    SeriesColor seriesColor = idLookup.get(nextId);
    if (seriesColor == null) {
      // rotate thru from beginning
      resetId();
    }
    return idLookup.get(nextId++).color;
  }

  /**
   * Constructor
   * 
   * @param id
   * @param color
   */
  private SeriesColor(int id, Color color) {

    this.id = id;
    this.color = color;
  }

}
