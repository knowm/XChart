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
package org.knowm.xchart.internal.style;

import java.awt.BasicStroke;
import java.awt.Color;

import org.knowm.xchart.style.markers.Marker;

/**
 * Cycles through the different colors, markers, and strokes in a predetermined way
 * <p>
 * This is an internal class that should not be used be clients
 *
 * @author timmolter
 */
public class SeriesColorMarkerLineStyleCycler {

  /** a List holding the Colors */
  private final Color[] seriesColorList;

  /** a map holding the SeriesMarkers */
  private final Marker[] seriesMarkerList;

  /** a map holding the SeriesLineStyles */
  private final BasicStroke[] seriesLineStyleList;

  /** an internal counter */
  private int colorCounter = 0;
  private int markerCounter = 0;
  private int strokeCounter = 0;

  /**
   * Constructor
   */
  public SeriesColorMarkerLineStyleCycler(Color[] seriesColorList, Marker[] seriesMarkerList, BasicStroke[] seriesLineStyleList) {

    this.seriesColorList = seriesColorList;
    this.seriesMarkerList = seriesMarkerList;
    this.seriesLineStyleList = seriesLineStyleList;
  }

  /**
   * Get the next ColorMarkerLineStyle
   *
   * @return the next ColorMarkerLineStyle
   */
  public SeriesColorMarkerLineStyle getNextSeriesColorMarkerLineStyle() {

    // 1. Color - cycle through colors one by one
    if (colorCounter >= seriesColorList.length) {
      colorCounter = 0;
      strokeCounter++;
    }
    Color seriesColor = seriesColorList[colorCounter++];

    // 2. Stroke - cycle through strokes one by one but only after a color cycle
    if (strokeCounter >= seriesLineStyleList.length) {
      strokeCounter = 0;
    }
    BasicStroke seriesLineStyle = seriesLineStyleList[strokeCounter];

    // 3. Marker - cycle through markers one by one
    if (markerCounter >= seriesMarkerList.length) {
      markerCounter = 0;
    }
    Marker marker = seriesMarkerList[markerCounter++];

    return new SeriesColorMarkerLineStyle(seriesColor, marker, seriesLineStyle);
  }
}
