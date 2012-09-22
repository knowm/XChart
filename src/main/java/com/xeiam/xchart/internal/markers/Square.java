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
import java.awt.Rectangle;

/**
 * @author timmolter
 */
public class Square extends Marker {

  @Override
  public void paint(Graphics2D g, int xOffset, int yOffset) {

    g.setStroke(stroke);
    g.fill(new Rectangle(xOffset + Marker.X_OFFSET, yOffset + Marker.Y_OFFSET, Marker.SIZE, Marker.SIZE));

  }

}
