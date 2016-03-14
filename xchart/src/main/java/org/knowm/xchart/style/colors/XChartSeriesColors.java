/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
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
package org.knowm.xchart.style.colors;

import java.awt.Color;

/**
 * @author timmolter
 */
public class XChartSeriesColors implements SeriesColors {

  // original XChart colors
  public static Color BLUE = new Color(0, 55, 255, 180);
  public static Color ORANGE = new Color(255, 172, 0, 180);
  public static Color PURPLE = new Color(128, 0, 255, 180);
  public static Color GREEN = new Color(0, 205, 0, 180);
  public static Color RED = new Color(205, 0, 0, 180);
  public static Color YELLOW = new Color(255, 215, 0, 180);
  public static Color MAGENTA = new Color(255, 0, 255, 180);
  public static Color PINK = new Color(255, 166, 201, 180);
  public static Color LIGHT_GREY = new Color(207, 207, 207, 180);
  public static Color CYAN = new Color(0, 255, 255, 180);
  public static Color BROWN = new Color(102, 56, 10, 180);
  public static Color BLACK = new Color(0, 0, 0, 180);

  private final Color[] seriesColors;

  /**
   * Constructor
   */
  public XChartSeriesColors() {

    seriesColors = new Color[] { BLUE, ORANGE, PURPLE, GREEN, RED, YELLOW, MAGENTA, PINK, LIGHT_GREY, CYAN, BROWN, BLACK };
  }

  @Override
  public Color[] getSeriesColors() {

    return seriesColors;
  }
}
