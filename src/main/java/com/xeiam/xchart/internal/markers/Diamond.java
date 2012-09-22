/**
 * Copyright 2011-2012 Xeiam LLC.
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
package com.xeiam.xchart.internal.markers;

import java.awt.Graphics2D;
import java.awt.Polygon;

/**
 * @author timmolter
 */
public class Diamond extends Marker {

  @Override
  public void paint(Graphics2D g, int xOffset, int yOffset) {

    g.setStroke(stroke);

    int[] x = new int[4];
    int[] y = new int[4];
    int n = 4;

    // Make a diamond
    int halfSize = (int) (Math.ceil((Marker.SIZE + 3) / 2.0));
    x[0] = xOffset - halfSize + 0;
    x[1] = xOffset - halfSize + halfSize;
    x[2] = xOffset - halfSize + Marker.SIZE + 3;
    x[3] = xOffset - halfSize + halfSize;

    y[0] = 1 + yOffset - halfSize + halfSize;
    y[1] = 1 + yOffset - halfSize + Marker.SIZE + 3;
    y[2] = 1 + yOffset - halfSize + halfSize;
    y[3] = 1 + yOffset - halfSize + 0;

    Polygon diamond = new Polygon(x, y, n);
    g.fillPolygon(diamond);

  }

}
