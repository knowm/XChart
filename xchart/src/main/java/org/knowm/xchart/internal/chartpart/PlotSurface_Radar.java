/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

import org.knowm.xchart.RadarSeries;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.RadarStyler;
import org.knowm.xchart.style.Styler;

/**
 * Draws the plot background and the plot border
 */
public class PlotSurface_Radar<ST extends Styler, S extends Series> extends PlotSurface_ {

  private final RadarStyler stylerRadar;

  /**
   * Constructor
   *
   * @param chart
   */
  PlotSurface_Radar(Chart<RadarStyler, RadarSeries> chart) {

    super(chart);
    this.stylerRadar = chart.getStyler();
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle2D bounds = getBounds();

    // paint plot background
    Shape rect = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    g.setColor(stylerRadar.getPlotBackgroundColor());
    g.fill(rect);

    // paint plot border
    if (stylerRadar.isPlotBorderVisible()) {
      g.setColor(stylerRadar.getPlotBorderColor());
      // g.setStroke(getChartPainter().getstyler().getAxisTickMarksStroke());
      g.draw(rect);
    }
  }
}
