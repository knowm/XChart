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
package org.knowm.xchart.demo.charts.theme;

import java.awt.Color;

import org.knowm.xchart.style.colors.SeriesColors;

/**
 * @author timmolter
 */
public class MyCustomSeriesColors implements SeriesColors {

  public static Color GREEN = new Color(0, 205, 0, 180);
  public static Color RED = new Color(205, 0, 0, 180);
  public static Color BLACK = new Color(0, 0, 0, 180);

  private final Color[] seriesColors;

  /**
   * Constructor
   */
  public MyCustomSeriesColors() {

    seriesColors = new Color[] { GREEN, RED, BLACK };
  }

  @Override
  public Color[] getSeriesColors() {

    return seriesColors;
  }
}
