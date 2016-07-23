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
package org.knowm.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * All components of a chart that need to be painted should implement this interface
 *
 * @author timmolter
 */
public interface ChartPart {

  public static BasicStroke SOLID_STROKE = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 0.0f }, 0.0f);

  public Rectangle2D getBounds();

  public void paint(final Graphics2D g);

}
