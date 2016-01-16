/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
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

import java.util.List;

import org.knowm.xchart.SeriesColor;
import org.knowm.xchart.SeriesLineStyle;
import org.knowm.xchart.SeriesMarker;

/**
 * Cycles through the different colors, markers, and strokes in a predetermined way
 * <p>
 * This is an internal class that should not be used be clients
 *
 * @author timmolter
 */
public abstract class SeriesColorMarkerLineStyleCycler {

  public abstract List<SeriesColor> getSeriesColorList();

  public abstract List<SeriesMarker> getSeriesMarkerList();

  public abstract List<SeriesLineStyle> getLineStyleList();

  /** a List holding the SeriesColors */
  private final List<SeriesColor> seriesColorList;

  /** a map holding the SeriesMarkers */
  private final List<SeriesMarker> seriesMarkerList;

  /** a map holding the SeriesLineStyles */
  private final List<SeriesLineStyle> seriesLineStyleList;

  /** an internal counter */
  private int colorCounter = 0;
  private int markerCounter = 0;
  private int strokeCounter = 0;

  /**
   * Constructor
   */
  public SeriesColorMarkerLineStyleCycler() {

    seriesColorList = getSeriesColorList();
    seriesMarkerList = getSeriesMarkerList();
    seriesLineStyleList = getLineStyleList();
  }

  /**
   * Get the next SeriesColorMarkerLineStyle
   *
   * @return the next SeriesColorMarkerLineStyle
   */
  public SeriesColorMarkerLineStyle getNextSeriesColorMarkerLineStyle() {

    // 1. Color - cycle through colors one by one
    if (colorCounter >= seriesColorList.size()) {
      colorCounter = 0;
      strokeCounter++;
    }
    SeriesColor seriesColor = seriesColorList.get(colorCounter++);

    // 2. Stroke - cycle through strokes one by one but only after a color cycle
    if (strokeCounter >= seriesLineStyleList.size()) {
      strokeCounter = 0;
    }
    SeriesLineStyle seriesLineStyle = seriesLineStyleList.get(strokeCounter);

    // 3. Marker - cycle through markers one by one
    if (markerCounter >= seriesMarkerList.size()) {
      markerCounter = 0;
    }
    SeriesMarker marker = seriesMarkerList.get(markerCounter++);

    return new SeriesColorMarkerLineStyle(seriesColor.getColor(), marker.getMarker(), seriesLineStyle.getBasicStroke());
  }
}
