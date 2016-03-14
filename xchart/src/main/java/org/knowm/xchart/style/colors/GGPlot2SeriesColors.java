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
public class GGPlot2SeriesColors implements SeriesColors {

  public static Color RED = new Color(248, 118, 109, 255);
  public static Color YELLOW_GREEN = new Color(163, 165, 0, 255);
  public static Color GREEN = new Color(0, 191, 125, 255);
  public static Color BLUE = new Color(0, 176, 246, 255);
  public static Color PURPLE = new Color(231, 107, 243, 255);

  private final Color[] seriesColors;

  /**
   * Constructor
   */
  public GGPlot2SeriesColors() {

    seriesColors = new Color[] { RED, YELLOW_GREEN, GREEN, BLUE, PURPLE };
  }

  @Override
  public Color[] getSeriesColors() {

    return seriesColors;
  }
}
