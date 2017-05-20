/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * Colors are taken from http://colorbrewer2.org/#type=qualitative&scheme=Set3&n=12.
 *
 * @author timmolter
 * @author ekleinod
 */
public class BaseSeriesColors implements SeriesColors {

  private final Color[] seriesColors;

  /**
   * Constructor.
   */
  public BaseSeriesColors() {

    seriesColors = new Color[]{
        new Color(141, 211, 199),
        new Color(255, 255, 179),
        new Color(190, 186, 218),
        new Color(251, 128, 114),
        new Color(128, 177, 211),
        new Color(253, 180, 98),
        new Color(179, 222, 105),
        new Color(252, 205, 229),
        new Color(217, 217, 217),
        new Color(188, 128, 189),
        new Color(204, 235, 197),
        new Color(255, 237, 111)
    };

  }

  @Override
  public Color[] getSeriesColors() {

    return seriesColors;
  }
}
