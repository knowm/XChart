/**
 * Copyright 2011 Xeiam LLC.
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
package com.xeiam.xchart.series;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.xeiam.xchart.series.markers.Circle;
import com.xeiam.xchart.series.markers.Diamond;
import com.xeiam.xchart.series.markers.Marker;
import com.xeiam.xchart.series.markers.Square;
import com.xeiam.xchart.series.markers.TriangleDown;
import com.xeiam.xchart.series.markers.TriangleUp;


/**
 * @author timmolter
 */
public enum SeriesMarker {

  /** NONE */
  NONE(-1, null),

  /** CIRCLE */
  CIRCLE(0, new Circle()),

  /** DIAMOND */
  DIAMOND(1, new Diamond()),

  /** SQUARE */
  SQUARE(2, new Square()),

  /** TRIANGLE_DOWN */
  TRIANGLE_DOWN(3, new TriangleDown()),

  /** TRIANGLE_UP */
  TRIANGLE_UP(4, new TriangleUp());

  int id;
  Marker marker;
  private static int nextId = 0;

  private static final Map<Integer, SeriesMarker> idLookup = new HashMap<Integer, SeriesMarker>();
  static {
    for (SeriesMarker seriesMarker : EnumSet.allOf(SeriesMarker.class)) {
      idLookup.put(seriesMarker.getId(), seriesMarker);
    }
  }

  private Integer getId() {

    return id;
  }

  public static void resetId() {

    nextId = 0;
  }

  protected static Marker getMarker(SeriesMarker seriesMarker) {

    return seriesMarker.marker;
  }

  protected static Marker getNextMarker() {

    SeriesMarker seriesMarker = idLookup.get(nextId);
    if (seriesMarker == null) {
      // rotate thru from beginning
      resetId();
    }
    return idLookup.get(nextId++).marker;
  }

  /**
   * Constructor
   * 
   * @param id
   * @param color
   */
  private SeriesMarker(int id, Marker marker) {

    this.id = id;
    this.marker = marker;
  }

}
