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
package com.xeiam.xchart.series.markers;

import java.awt.Graphics2D;
import java.awt.Polygon;

/**
 * @author timmolter
 */
public class TriangleUp extends Marker {

  @Override
  public void paint(Graphics2D g, int xOffset, int yOffset) {

    g.setStroke(stroke);

    int[] x = new int[3];
    int[] y = new int[3];
    int n = 3;

    // Make a triangle
    int halfSize = (int) (Math.ceil((Marker.SIZE + 1) / 2.0));
    x[0] = xOffset - halfSize + 0;
    x[1] = xOffset - halfSize + Marker.SIZE + 1;
    x[2] = xOffset - halfSize + halfSize;

    y[0] = yOffset - halfSize + Marker.SIZE + 1;
    y[1] = yOffset - halfSize + Marker.SIZE + 1;
    y[2] = yOffset - halfSize + 0;

    Polygon triangle = new Polygon(x, y, n);
    g.fillPolygon(triangle);

  }
}
