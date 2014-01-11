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

import java.awt.BasicStroke;
import java.awt.Color;

import com.xeiam.xchart.internal.markers.Marker;

/**
 * A DTO to hold the Series' Color, Marker, and LineStyle
 * 
 * @author timmolter
 */
public final class SeriesColorMarkerLineStyle {

  private final Color color;
  private final Marker marker;
  private final BasicStroke stroke;

  /**
   * Constructor
   * 
   * @param color
   * @param marker
   * @param stroke
   */
  public SeriesColorMarkerLineStyle(Color color, Marker marker, BasicStroke stroke) {

    this.color = color;
    this.marker = marker;
    this.stroke = stroke;
  }

  public Color getColor() {

    return color;
  }

  public Marker getMarker() {

    return marker;
  }

  public BasicStroke getStroke() {

    return stroke;
  }

}
