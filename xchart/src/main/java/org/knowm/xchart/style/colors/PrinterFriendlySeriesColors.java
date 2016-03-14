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
public class PrinterFriendlySeriesColors implements SeriesColors {

  // printer-friendly colors from http://colorbrewer2.org/
  public static Color RED = new Color(228, 26, 28, 180);
  public static Color GREEN = new Color(55, 126, 184, 180);
  public static Color BLUE = new Color(77, 175, 74, 180);
  public static Color PURPLE = new Color(152, 78, 163, 180);
  public static Color ORANGE = new Color(255, 127, 0, 180);
  // public static Color YELLOW = new Color(255, 255, 51, 180);
  // public static Color BROWN = new Color(166, 86, 40, 180);
  // public static Color PINK = new Color(247, 129, 191, 180);

  private final Color[] seriesColors;

  /**
   * Constructor
   */
  public PrinterFriendlySeriesColors() {

    seriesColors = new Color[] { RED, GREEN, BLUE, PURPLE, ORANGE };
  }

  @Override
  public Color[] getSeriesColors() {

    return seriesColors;
  }
}
