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
package com.xeiam.xcharts.series;

import java.awt.BasicStroke;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author timmolter
 */
public enum SeriesLineStyle {

  /** NONE */
  NONE(-1, null),

  /** SOLID */
  SOLID(0, new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL)),

  /** DASH_DOT */
  DASH_DOT(1, new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 1.0f }, 0.0f)),

  /** DASH_DASH */
  DASH_DASH(2, new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 3.0f }, 0.0f)),

  /** DOT_DOT */
  DOT_DOT(3, new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 1.0f, 1.0f }, 0.0f));

  int id;
  BasicStroke basicStroke;
  private static int nextId = 0;

  private static final Map<Integer, SeriesLineStyle> idLookup = new HashMap<Integer, SeriesLineStyle>();
  static {
    for (SeriesLineStyle seriesLineStyle : EnumSet.allOf(SeriesLineStyle.class)) {
      idLookup.put(seriesLineStyle.getId(), seriesLineStyle);
    }
  }

  private Integer getId() {

    return id;
  }

  public static void resetId() {

    nextId = 0;
  }

  protected static BasicStroke getBasicStroke(SeriesLineStyle seriesMarker) {

    return seriesMarker.basicStroke;
  }

  protected static BasicStroke getNextBasicStroke() {

    SeriesLineStyle seriesLineStyle = idLookup.get(nextId);
    if (seriesLineStyle == null) {
      // rotate thru from beginning
      resetId();
    }
    return idLookup.get(nextId++).basicStroke;
  }

  /**
   * Constructor
   * 
   * @param id
   * @param color
   */
  private SeriesLineStyle(int id, BasicStroke basicStroke) {

    this.id = id;
    this.basicStroke = basicStroke;
  }
}
