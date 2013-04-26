/**
 * Copyright 2011-2013 Xeiam LLC.
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
import java.awt.geom.Path2D;

/**
 * @author timmolter
 */
public class Diamond extends Marker {

  @Override
  public void paint(Graphics2D g, double xOffset, double yOffset) {

    g.setStroke(stroke);

    // Make a diamond
    double halfSize = Math.ceil((Marker.SIZE + 3) / 2.0);

    Path2D.Double path = new Path2D.Double();
    path.moveTo(xOffset - halfSize, yOffset - halfSize + halfSize);
    path.lineTo(xOffset - halfSize + halfSize, yOffset - halfSize + Marker.SIZE + 3);
    path.lineTo(xOffset - halfSize + Marker.SIZE + 3, yOffset - halfSize + halfSize);
    path.lineTo(xOffset - halfSize + halfSize, yOffset - halfSize);
    path.closePath();
    g.fill(path);

  }

}
