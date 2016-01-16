/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
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

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * @author timmolter
 */
public class AxisPair implements ChartPart {

  /** parent */
  private final ChartInternal chartInternal;

  private Axis xAxis;
  private Axis yAxis;

  /**
   * Constructor
   *
   * @param the parent chartInternal
   */
  public AxisPair(ChartInternal chartInternal) {

    this.chartInternal = chartInternal;

    // add axes
    xAxis = new Axis(this, Axis.Direction.X);
    yAxis = new Axis(this, Axis.Direction.Y);
  }

  @Override
  public void paint(Graphics2D g) {

    yAxis.paint(g);
    xAxis.paint(g);
  }

  @Override
  public Rectangle2D getBounds() {

    return null; // should never be called
  }

  @Override
  public ChartInternal getChartInternal() {

    return chartInternal;
  }

  // Getters /////////////////////////////////////////////////

  public Axis getXAxis() {

    return xAxis;
  }

  public Axis getYAxis() {

    return yAxis;
  }

}
