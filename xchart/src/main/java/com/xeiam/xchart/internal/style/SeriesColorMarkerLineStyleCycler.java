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
package com.xeiam.xchart.internal.style;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;

/**
 * Cycles through the different colors, markers, and strokes in a predetermined way
 * <p>
 * This is an internal class that should not be used be clients
 * 
 * @author timmolter
 */
public class SeriesColorMarkerLineStyleCycler {

  /** a map holding the SeriesColors */
  private final Map<Integer, SeriesColor> seriesColorMap = new HashMap<Integer, SeriesColor>();

  /** a map holding the SeriesMarkers */
  private final Map<Integer, SeriesMarker> seriesMarkerMap = new HashMap<Integer, SeriesMarker>();

  /** a map holding the SeriesLineStyles */
  private final Map<Integer, SeriesLineStyle> seriesLineStyleMap = new HashMap<Integer, SeriesLineStyle>();

  /** an internal counter */
  private int colorCounter = 0;
  private int markerCounter = 0;
  private int strokeCounter = 0;

  /**
   * Constructor
   */
  public SeriesColorMarkerLineStyleCycler() {

    // 1. Color
    for (SeriesColor seriesColor : EnumSet.allOf(SeriesColor.class)) {
      seriesColorMap.put(seriesColor.getIndex(), seriesColor);
    }

    // 2. Marker
    for (SeriesMarker seriesMarker : EnumSet.allOf(SeriesMarker.class)) {
      if (seriesMarker.getIndex() >= 0) { // skip SeriesMarker.NONE
        seriesMarkerMap.put(seriesMarker.getIndex(), seriesMarker);
      }
    }

    // 3. Stroke
    for (SeriesLineStyle seriesLineStyle : EnumSet.allOf(SeriesLineStyle.class)) {
      if (seriesLineStyle.getIndex() >= 0) { // skip SeriesLineStyle.NONE
        seriesLineStyleMap.put(seriesLineStyle.getIndex(), seriesLineStyle);
      }
    }
  }

  /**
   * Get the next SeriesColorMarkerLineStyle
   * 
   * @return the next SeriesColorMarkerLineStyle
   */
  public SeriesColorMarkerLineStyle getNextSeriesColorMarkerLineStyle() {

    // 1. Color - cycle through colors one by one
    if (colorCounter >= seriesColorMap.size()) {
      colorCounter = 0;
      strokeCounter++;
    }
    SeriesColor seriesColor = seriesColorMap.get(colorCounter++);

    // 2. Stroke - cycle through strokes one by one but only after a color cycle
    if (strokeCounter >= seriesLineStyleMap.size()) {
      strokeCounter = 0;
    }
    SeriesLineStyle seriesLineStyle = seriesLineStyleMap.get(strokeCounter);

    // 3. Marker - cycle through markers one by one
    if (markerCounter >= seriesMarkerMap.size()) {
      markerCounter = 0;
    }
    SeriesMarker marker = seriesMarkerMap.get(markerCounter++);

    return new SeriesColorMarkerLineStyle(seriesColor.getColor(), marker.getMarker(), seriesLineStyle.getBasicStroke());
  }
}
