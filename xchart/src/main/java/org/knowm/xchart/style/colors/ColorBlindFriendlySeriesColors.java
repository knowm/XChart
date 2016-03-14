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
public class ColorBlindFriendlySeriesColors implements SeriesColors {

  // The color blind friendly palette
  public static Color BLACK = new Color(0, 0, 0, 255);
  public static Color ORANGE = new Color(230, 159, 0, 255);
  public static Color SKY_BLUE = new Color(86, 180, 233, 255);
  public static Color BLUISH_GREEN = new Color(0, 158, 115, 255);
  public static Color YELLOW = new Color(240, 228, 66, 255);
  public static Color BLUE = new Color(0, 114, 178, 255);
  public static Color VERMILLION = new Color(213, 94, 0, 255);
  public static Color REDDISH_PURPLE = new Color(204, 121, 167, 255);

  private final Color[] seriesColors;

  /**
   * Constructor
   */
  public ColorBlindFriendlySeriesColors() {

    seriesColors = new Color[] { BLACK, ORANGE, SKY_BLUE, BLUISH_GREEN, YELLOW, BLUE, VERMILLION, REDDISH_PURPLE };
  }

  @Override
  public Color[] getSeriesColors() {

    return seriesColors;
  }
}
