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

import org.knowm.xchart.graphics.Graphics;

import java.awt.geom.Path2D;

/**
 * @author timmolter
 */
public class Diamond extends Marker {

  @Override
  public void paint(Graphics g, double xOffset, double yOffset, int markerSize) {

    g.setStroke(stroke);

    // Make a diamond
    double diamondHalfSize = (double) markerSize / 2 * 1.3;

    Path2D.Double path = new Path2D.Double();
    path.moveTo(xOffset - diamondHalfSize, yOffset);
    path.lineTo(xOffset, yOffset - diamondHalfSize);
    path.lineTo(xOffset + diamondHalfSize, yOffset);
    path.lineTo(xOffset, yOffset + diamondHalfSize);
    path.closePath();
    g.fill(path);

  }

}
