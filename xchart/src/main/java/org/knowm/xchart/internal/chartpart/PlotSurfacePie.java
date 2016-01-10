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

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import org.knowm.xchart.Series_Pie;
import org.knowm.xchart.StyleManagerPie;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.style.StyleManager;

/**
 * Draws the plot background and the plot border
 *
 * @author timmolter
 */
public class PlotSurfacePie<SM extends StyleManager, S extends Series> extends PlotSurface {

  private final StyleManagerPie styleManagerPie;

  /**
   * Constructor
   *
   * @param chart
   */
  protected PlotSurfacePie(Chart<StyleManagerPie, Series_Pie> chart) {

    super(chart);
    this.styleManagerPie = chart.getStyleManager();
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle2D bounds = getBounds();

    // paint plot background
    Shape rect = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    g.setColor(styleManagerPie.getPlotBackgroundColor());
    g.fill(rect);

    // paint plot border
    if (styleManagerPie.isPlotBorderVisible()) {
      g.setColor(styleManagerPie.getPlotBorderColor());
      // g.setStroke(getChartPainter().getStyleManager().getAxisTickMarksStroke());
      g.draw(rect);
    }

  }

}
