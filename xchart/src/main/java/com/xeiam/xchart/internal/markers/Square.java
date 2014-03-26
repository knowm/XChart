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
package com.xeiam.xchart.internal.markers;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * @author timmolter
 */
public class Square extends Marker {

  @Override
  public void paint(Graphics2D g, double xOffset, double yOffset) {
    double halfSize = size / 2.0;
    
    g.setStroke(stroke);
    Shape square = new Rectangle2D.Double(xOffset - halfSize, yOffset - halfSize, size, size);
    g.fill(square);

  }

}
