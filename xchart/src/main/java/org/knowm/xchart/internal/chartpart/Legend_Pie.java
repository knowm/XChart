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
import java.util.Map;

import org.knowm.xchart.PieSeries;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.PieStyler;

/**
 * @author timmolter
 */
public class Legend_Pie<ST extends AxesChartStyler, S extends Series> extends Legend_ {

  PieStyler stylerPie;

  /**
   * Constructor
   *
   * @param chart
   */
  public Legend_Pie(Chart<PieStyler, PieSeries> chart) {

    super(chart);
    stylerPie = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    // Draw legend content inside legend box
    double startx = xOffset + chart.getStyler().getLegendPadding();
    double starty = yOffset + chart.getStyler().getLegendPadding();

    Map<String, Series> map = chart.getSeriesMap();
    for (Series series : map.values()) {

      if (!series.isShowInLegend()) {
        continue;
      }

      Map<String, Rectangle2D> seriesTextBounds = getSeriesTextBounds(series);
      float legendEntryHeight = getLegendEntryHeight(seriesTextBounds, BOX_SIZE);

      // paint little box
      Shape rectSmall = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
      g.setColor(series.getFillColor());
      g.fill(rectSmall);

      // paint series text
      final double x = startx + BOX_SIZE + chart.getStyler().getLegendPadding();
      paintSeriesText(g, seriesTextBounds, BOX_SIZE, x, starty);
      starty += legendEntryHeight + chart.getStyler().getLegendPadding();

    }

  }

  @Override
  public Rectangle2D getBounds() {

    if (bounds == null) { // was not drawn fully yet, just need the height hint. The Axis object may be asking for it.
      bounds = getBoundsHint();
    }
    return bounds;
  }

  @Override
  public double getSeriesLegendRenderGraphicHeight(Series series) {

    return BOX_SIZE;
  }
}
