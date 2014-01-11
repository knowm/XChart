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
package com.xeiam.xchart;

import java.awt.BasicStroke;

/**
 * Pre-defined Line Styles used for Series Lines
 * 
 * @author timmolter
 */
public enum SeriesLineStyle {

  /** NONE */
  NONE(-1, null),

  /** SOLID */
  SOLID(0, new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER)),

  /** DASH_DOT */
  DASH_DOT(1, new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 3.0f, 1.0f }, 0.0f)),

  /** DASH_DASH */
  DASH_DASH(2, new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 3.0f, 3.0f }, 0.0f)),

  /** DOT_DOT */
  DOT_DOT(3, new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND, 10.0f, new float[] { 2.0f }, 0.0f));

  /** The index */
  private int index;

  /** The basicStroke */
  private BasicStroke basicStroke;

  /**
   * Constructor
   * 
   * @param index
   * @param basicStroke
   */
  private SeriesLineStyle(int index, BasicStroke basicStroke) {

    this.index = index;
    this.basicStroke = basicStroke;
  }

  /**
   * Gets the SeriesLineStyle's index
   * 
   * @return
   */
  public Integer getIndex() {

    return index;
  }

  /**
   * Gets the SeriesLineStyle's BasicStroke
   * 
   * @return the BasicStroke
   */
  public BasicStroke getBasicStroke() {

    return basicStroke;
  }

  /**
   * Get an AWT Stroke
   * 
   * @param seriesMarker
   * @return an AWT Stroke
   */
  public static BasicStroke getBasicStroke(SeriesLineStyle seriesMarker) {

    return seriesMarker.basicStroke;
  }

}
