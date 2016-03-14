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
package org.knowm.xchart.style.markers;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

/**
 * @author timmolter
 */
public class TriangleDown extends Marker {

  @Override
  public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {

    g.setStroke(stroke);
    double halfSize = (double) markerSize / 2;

    // Make a triangle
    Path2D.Double path = new Path2D.Double();
    path.moveTo(xOffset - halfSize, 1 + yOffset - halfSize);
    path.lineTo(xOffset, 1 + yOffset - halfSize + markerSize);
    path.lineTo(xOffset - halfSize + markerSize, 1 + yOffset - halfSize);
    path.closePath();
    g.fill(path);

  }
}
