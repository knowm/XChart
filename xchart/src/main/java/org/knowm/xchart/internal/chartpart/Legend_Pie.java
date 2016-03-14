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
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.knowm.xchart.Series_Pie;
import org.knowm.xchart.Styler_Pie;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.style.Styler_AxesChart;

/**
 * @author timmolter
 */
public class Legend_Pie<ST extends Styler_AxesChart, S extends Series> extends Legend_ {

  Styler_Pie stylerPie;

  /**
   * Constructor
   *
   * @param chart
   */
  public Legend_Pie(Chart<Styler_Pie, Series_Pie> chart) {

    super(chart);
    stylerPie = chart.getStyler();
  }

  @Override
  public void paint(Graphics2D g) {

    if (!chart.getStyler().isLegendVisible()) {
      return;
    }

    super.paint(g);

    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 0.0f }, 0.0f));

    // Draw legend content inside legend box
    double startx = xOffset + chart.getStyler().getLegendPadding();
    double starty = yOffset + chart.getStyler().getLegendPadding();

    Map<String, Series> map = chart.getSeriesMap();
    for (Series series : map.values()) {

      if (!series.isShowInLegend()) {
        continue;
      }

      Map<String, Rectangle2D> seriesTextBounds = getSeriesTextBounds(series);

      float legendEntryHeight = 0;
      double legendTextContentMaxWidth = 0; // TODO 3.0.0 don't need this
      for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
        legendEntryHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
        legendTextContentMaxWidth = Math.max(legendTextContentMaxWidth, entry.getValue().getWidth());
      }
      legendEntryHeight -= MULTI_LINE_SPACE;

      legendEntryHeight = Math.max(legendEntryHeight, BOX_SIZE);

      // ////// paint series render graphic /////////

      // bar/pie type series

      // paint little box
      if (series.getFillColor() != null) {
        g.setColor(series.getFillColor());
        Shape rectSmall = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
        g.fill(rectSmall);
      }
      // // debug box
      // Rectangle2D boundsTemp = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
      // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
      // g.setColor(Color.red);
      // g.draw(boundsTemp);

      //
      // ////// paint series text /////////

      g.setColor(chart.getStyler().getChartFontColor());

      double multiLineOffset = 0.0;

      // bar/pie type series

      final double x = startx + BOX_SIZE + chart.getStyler().getLegendPadding();
      for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {

        double height = entry.getValue().getHeight();
        double centerOffsetY = (Math.max(BOX_SIZE, height) - height) / 2.0;

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout tl = new TextLayout(entry.getKey(), chart.getStyler().getLegendFont(), frc);
        Shape shape = tl.getOutline(null);
        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();
        at.translate(x, starty + height + centerOffsetY + multiLineOffset);
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);

        // // debug box
        // Rectangle2D boundsTemp = new Rectangle2D.Double(x, starty + centerOffsetY, entry.getValue().getWidth(), height);
        // g.setColor(Color.blue);
        // g.draw(boundsTemp);
        multiLineOffset += height + MULTI_LINE_SPACE;

      }
      starty += legendEntryHeight + chart.getStyler().getLegendPadding();

    }

    // bounds
    bounds = new Rectangle2D.Double(xOffset, yOffset, bounds.getWidth(), bounds.getHeight());

    // g.setColor(Color.blue);
    // g.draw(bounds);

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
