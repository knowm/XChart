/**
 * Copyright (C) 2013 Xeiam LLC http://xeiam.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchart.internal.misc;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.xeiam.xchart.SeriesColor;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SeriesMarker;

/**
 * Cycles through the different colors, markers, and strokes in a predetermined way
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
