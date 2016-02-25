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
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.knowm.xchart.Series_XY;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.style.Styler_AxesChart;
import org.knowm.xchart.internal.style.lines.SeriesLines;

/**
 * @author timmolter
 */
public class Legend_AxesChart<ST extends Styler_AxesChart, S extends Series> extends Legend_ {

  Styler_AxesChart stylerAxesChart;

  /**
   * Constructor
   *
   * @param chart
   */
  public Legend_AxesChart(Chart<Styler_AxesChart, Series_XY> chart) {

    super(chart);
    stylerAxesChart = chart.getStyler();
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

    Map<String, Series_AxesChart> map = chart.getSeriesMap();
    for (Series_AxesChart series : map.values()) {

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

      legendEntryHeight = Math.max(legendEntryHeight, (series.getLegendRenderType() == LegendRenderType.Box ? BOX_SIZE : stylerAxesChart.getMarkerSize()));

      // ////// paint series render graphic /////////

      // paint line and marker
      if (series.getLegendRenderType() != LegendRenderType.Box) {

        // paint line
        if (series.getLegendRenderType() == LegendRenderType.Line && series.getLineStyle() != SeriesLines.NONE) {
          g.setColor(series.getLineColor());
          g.setStroke(series.getLineStyle());
          Shape line = new Line2D.Double(startx, starty + legendEntryHeight / 2.0, startx + chart.getStyler().getLegendSeriesLineLength(), starty + legendEntryHeight / 2.0);
          g.draw(line);
        }

        // // debug box
        // Rectangle2D boundsTemp = new Rectangle2D.Double(startx, starty, styler.getLegendSeriesLineLength(), blockHeight);
        // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        // g.setColor(Color.red);
        // g.draw(boundsTemp);

        // paint marker
        if ((stylerAxesChart.shouldShowMarkers() || series.getLegendRenderType() == LegendRenderType.Scatter) && series.getMarker() != null) {
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, startx + chart.getStyler().getLegendSeriesLineLength() / 2.0, starty + legendEntryHeight / 2.0, stylerAxesChart.getMarkerSize());

        }
      }
      else { // bar/pie type series

        // paint little box
        Shape rectSmall = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
        // g.setStroke(series.getLineStyle());
        // g.setColor(series.getLineColor());
        // g.draw(rectSmall);
        g.setColor(series.getFillColor());
        g.fill(rectSmall);
        // // debug box
        // Rectangle2D boundsTemp = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
        // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        // g.setColor(Color.red);
        // g.draw(boundsTemp);
      }

      //
      // ////// paint series text /////////

      g.setColor(chart.getStyler().getChartFontColor());

      double multiLineOffset = 0.0;

      if (series.getLegendRenderType() != LegendRenderType.Box) {

        double x = startx + chart.getStyler().getLegendSeriesLineLength() + chart.getStyler().getLegendPadding();
        for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {

          double height = entry.getValue().getHeight();
          double centerOffsetY = (Math.max(stylerAxesChart.getMarkerSize(), height) - height) / 2.0;

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
          // Rectangle2D boundsTemp = new Rectangle2D.Double(x, starty + centerOffsetY + multiLineOffset, entry.getValue().getWidth(), height);
          // g.setColor(Color.blue);
          // g.draw(boundsTemp);

          multiLineOffset += height + MULTI_LINE_SPACE;
        }

      }
      else { // bar/pie type series

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

    return series.getLegendRenderType() == LegendRenderType.Box ? BOX_SIZE : stylerAxesChart.getMarkerSize();
  }
}
