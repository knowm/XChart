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
package com.xeiam.xchart.style;

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
