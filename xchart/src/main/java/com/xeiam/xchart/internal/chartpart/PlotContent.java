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
package com.xeiam.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 * @author timmolter
 */
public abstract class PlotContent implements ChartPart {

  /** parent */
  protected Plot plot;

  protected final Stroke errorBarStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

  /**
   * Constructor
   * 
   * @param plot
   */
  protected PlotContent(Plot plot) {

    this.plot = plot;
  }

  @Override
  public Rectangle2D getBounds() {

    return plot.getBounds();
  }

  @Override
  public ChartPainter getChartPainter() {

    return plot.getChartPainter();
  }

}
