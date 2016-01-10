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
package org.knowm.xchart;

import org.knowm.xchart.internal.style.markers.Circle;
import org.knowm.xchart.internal.style.markers.Diamond;
import org.knowm.xchart.internal.style.markers.Marker;
import org.knowm.xchart.internal.style.markers.Square;
import org.knowm.xchart.internal.style.markers.TriangleDown;
import org.knowm.xchart.internal.style.markers.TriangleUp;

/**
 * Pre-defined Markers used for Series Lines
 * 
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

  /** The index */
  private int index;

  /** The Marker */
  private Marker marker;

  /**
   * Constructor
   * 
   * @param index
   * @param marker
   */
  private SeriesMarker(int index, Marker marker) {

    this.index = index;
    this.marker = marker;
  }

  /**
   * Gets the SeriesMarker index
   * 
   * @return
   */
  public Integer getIndex() {

    return index;
  }

  /**
   * Gets the SeriesMarker marker
   * 
   * @return
   */
  public Marker getMarker() {

    return marker;
  }

}
