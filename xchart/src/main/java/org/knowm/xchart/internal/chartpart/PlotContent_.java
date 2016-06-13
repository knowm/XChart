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
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * @author timmolter
 */
public abstract class PlotContent_<ST extends AxesChartStyler, S extends Series> implements ChartPart {

  protected final Chart<ST, S> chart;

  protected final Stroke errorBarStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

  public abstract void doPaint(Graphics2D g);

  /**
   * Constructor
   *
   * @param chart - The Chart
   */
  protected PlotContent_(Chart<ST, S> chart) {

    this.chart = chart;
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle2D bounds = getBounds();
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.red);
    // g.draw(bounds);

    // if the area to draw a chart on is so small, don't even bother
    if (bounds.getWidth() < 30) {
      return;
    }

    // this is for preventing the series to be drawn outside the plot area if min and max is overridden to fall inside the data range
    g.setClip(bounds.createIntersection(bounds));

    doPaint(g);

    g.setClip(null);

  }

  @Override
  public Rectangle2D getBounds() {

    return chart.getPlot().getBounds();
  }

  /**
   * Closes a path for area charts if one is available.
   */
  void closePath(Graphics2D g, Path2D.Double path, double previousX, Rectangle2D bounds, double yTopMargin) {

    if (path != null) {
      double yBottomOfArea = getBounds().getY() + getBounds().getHeight() - yTopMargin;
      path.lineTo(previousX, yBottomOfArea);
      path.closePath();
      g.fill(path);
    }
  }

}
