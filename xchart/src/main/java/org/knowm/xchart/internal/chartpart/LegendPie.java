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
import org.knowm.xchart.StyleManagerPie;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.style.StyleManagerAxesChart;

/**
 * @author timmolter
 */
public class LegendPie<SM extends StyleManagerAxesChart, S extends Series> extends Legend {

  StyleManagerPie styleManagerPie;

  /**
   * Constructor
   *
   * @param chart
   */
  public LegendPie(Chart<StyleManagerPie, Series_Pie> chart) {

    super(chart);
    styleManagerPie = chart.getStyleManager();
  }

  @Override
  public void paint(Graphics2D g) {

    super.paint(g);

    // legend draw position
    double xOffset = 0;
    double yOffset = 0;
    switch (chart.getStyleManager().getLegendPosition()) {
    case OutsideE:
      xOffset = chart.getWidth() - bounds.getWidth() - chart.getStyleManager().getChartPadding();
      yOffset = chart.getPlot().getBounds().getY() + (chart.getPlot().getBounds().getHeight() - bounds.getHeight()) / 2.0;
      break;
    case InsideNW:
      xOffset = chart.getPlot().getBounds().getX() + LEGEND_MARGIN;
      yOffset = chart.getPlot().getBounds().getY() + LEGEND_MARGIN;
      break;
    case InsideNE:
      xOffset = chart.getPlot().getBounds().getX() + chart.getPlot().getBounds().getWidth() - bounds.getWidth() - LEGEND_MARGIN;
      yOffset = chart.getPlot().getBounds().getY() + LEGEND_MARGIN;
      break;
    case InsideSE:
      xOffset = chart.getPlot().getBounds().getX() + chart.getPlot().getBounds().getWidth() - bounds.getWidth() - LEGEND_MARGIN;
      yOffset = chart.getPlot().getBounds().getY() + chart.getPlot().getBounds().getHeight() - bounds.getHeight() - LEGEND_MARGIN;
      break;
    case InsideSW:
      xOffset = chart.getPlot().getBounds().getX() + LEGEND_MARGIN;
      yOffset = chart.getPlot().getBounds().getY() + chart.getPlot().getBounds().getHeight() - bounds.getHeight() - LEGEND_MARGIN;
      break;
    case InsideN:
      xOffset = chart.getPlot().getBounds().getX() + (chart.getPlot().getBounds().getWidth() - bounds.getWidth()) / 2 + LEGEND_MARGIN;
      yOffset = chart.getPlot().getBounds().getY() + LEGEND_MARGIN;
      break;

    default:
      break;
    }

    // TODO need this stroke?
    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 0.0f }, 0.0f));

    // draw legend box background and border
    Shape rect = new Rectangle2D.Double(xOffset + 1, yOffset + 1, bounds.getWidth() - 2, bounds.getHeight() - 2);
    g.setColor(chart.getStyleManager().getLegendBackgroundColor());
    g.fill(rect);
    g.setColor(chart.getStyleManager().getLegendBorderColor());
    g.draw(rect);

    // Draw legend content inside legend box
    double startx = xOffset + chart.getStyleManager().getLegendPadding();
    double starty = yOffset + chart.getStyleManager().getLegendPadding();

    // TODO 3.0.0 figure out this warning.
    Map<String, Series> map = chart.getSeriesMap();
    for (Series series : map.values()) {

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

      g.setColor(chart.getStyleManager().getChartFontColor());

      double multiLineOffset = 0.0;

      // bar/pie type series

      final double x = startx + BOX_SIZE + chart.getStyleManager().getLegendPadding();
      for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {

        double height = entry.getValue().getHeight();
        double centerOffsetY = (Math.max(BOX_SIZE, height) - height) / 2.0;

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout tl = new TextLayout(entry.getKey(), chart.getStyleManager().getLegendFont(), frc);
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
      starty += legendEntryHeight + chart.getStyleManager().getLegendPadding();

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
