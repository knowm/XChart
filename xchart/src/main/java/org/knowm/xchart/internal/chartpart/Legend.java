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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;

import org.knowm.xchart.Series;
import org.knowm.xchart.StyleManager.ChartType;

/**
 * @author timmolter
 */
public class Legend implements ChartPart {

  private static final int LEGEND_MARGIN = 6;
  private static final int BOX_SIZE = 20;
  private static final int MULTI_LINE_SPACE = 3;

  private double legendBoxWidth = 0.0;
  private double legendBoxHeight = 0.0;

  /**
   * parent
   */
  private final ChartPainter chartPainter;

  /**
   * the bounds
   */
  private Rectangle2D bounds;

  /**
   * Constructor
   *
   * @param chartPainter
   */
  public Legend(ChartPainter chartPainter) {

    this.chartPainter = chartPainter;
  }

  /**
   * determine the width and height of the chart legend
   */
  public void determineLegendBoxSize() {

    if (!chartPainter.getStyleManager().isLegendVisible()) {
      return;
    }

    boolean isBar = getChartPainter().getStyleManager().getChartType() == ChartType.Bar;

    // determine legend text content max width
    double legendTextContentMaxWidth = 0;

    // determine legend content height
    double legendContentHeight = 0;

    for (Series series : chartPainter.getAxisPair().getSeriesMap().values()) {

      Map<String, Rectangle2D> seriesBounds = getSeriesTextBounds(series);

      double blockHeight = 0;
      for (Map.Entry<String, Rectangle2D> entry : seriesBounds.entrySet()) {
        blockHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
        legendTextContentMaxWidth = Math.max(legendTextContentMaxWidth, entry.getValue().getWidth());
      }

      blockHeight -= MULTI_LINE_SPACE;
      blockHeight = Math.max(blockHeight, isBar ? BOX_SIZE : getChartPainter().getStyleManager().getMarkerSize());

      legendContentHeight += blockHeight + getChartPainter().getStyleManager().getLegendPadding();
    }

    // determine legend content width
    double legendContentWidth = 0;
    if (!isBar) {
      legendContentWidth = getChartPainter().getStyleManager().getLegendSeriesLineLength() + getChartPainter().getStyleManager().getLegendPadding() + legendTextContentMaxWidth;
    }
    else {
      legendContentWidth = BOX_SIZE + getChartPainter().getStyleManager().getLegendPadding() + legendTextContentMaxWidth;
    }

    // Legend Box
    legendBoxWidth = legendContentWidth + 2 * getChartPainter().getStyleManager().getLegendPadding();
    legendBoxHeight = legendContentHeight + 1 * getChartPainter().getStyleManager().getLegendPadding();
  }

  @Override
  public void paint(Graphics2D g) {

    if (!getChartPainter().getStyleManager().isLegendVisible()) {
      return;
    }

    // if the area to draw a chart on is so small, don't even bother
    if (chartPainter.getPlot().getBounds().getWidth() < 30) {
      return;
    }

    bounds = new Rectangle2D.Double();
    // g.setFont(chartPainter.getStyleManager().getLegendFont());

    // legend draw position
    double xOffset = 0;
    double yOffset = 0;
    switch (getChartPainter().getStyleManager().getLegendPosition()) {
    case OutsideE:
      xOffset = chartPainter.getWidth() - legendBoxWidth - getChartPainter().getStyleManager().getChartPadding();
      yOffset = chartPainter.getPlot().getBounds().getY() + (chartPainter.getPlot().getBounds().getHeight() - legendBoxHeight) / 2.0;
      break;
    case InsideNW:
      xOffset = chartPainter.getPlot().getBounds().getX() + LEGEND_MARGIN;
      yOffset = chartPainter.getPlot().getBounds().getY() + LEGEND_MARGIN;
      break;
    case InsideNE:
      xOffset = chartPainter.getPlot().getBounds().getX() + chartPainter.getPlot().getBounds().getWidth() - legendBoxWidth - LEGEND_MARGIN;
      yOffset = chartPainter.getPlot().getBounds().getY() + LEGEND_MARGIN;
      break;
    case InsideSE:
      xOffset = chartPainter.getPlot().getBounds().getX() + chartPainter.getPlot().getBounds().getWidth() - legendBoxWidth - LEGEND_MARGIN;
      yOffset = chartPainter.getPlot().getBounds().getY() + chartPainter.getPlot().getBounds().getHeight() - legendBoxHeight - LEGEND_MARGIN;
      break;
    case InsideSW:
      xOffset = chartPainter.getPlot().getBounds().getX() + LEGEND_MARGIN;
      yOffset = chartPainter.getPlot().getBounds().getY() + chartPainter.getPlot().getBounds().getHeight() - legendBoxHeight - LEGEND_MARGIN;
      break;
    case InsideN:
      xOffset = chartPainter.getPlot().getBounds().getX() + (chartPainter.getPlot().getBounds().getWidth() - legendBoxWidth) / 2 + LEGEND_MARGIN;
      yOffset = chartPainter.getPlot().getBounds().getY() + LEGEND_MARGIN;
      break;

    default:
      break;
    }

    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 0.0f }, 0.0f));

    Shape rect = new Rectangle2D.Double(xOffset + 1, yOffset + 1, legendBoxWidth - 2, legendBoxHeight - 2);
    g.setColor(getChartPainter().getStyleManager().getLegendBackgroundColor());
    g.fill(rect);
    g.setColor(getChartPainter().getStyleManager().getLegendBorderColor());
    g.draw(rect);

    // Draw legend content inside legend box
    double startx = xOffset + getChartPainter().getStyleManager().getLegendPadding();
    double starty = yOffset + getChartPainter().getStyleManager().getLegendPadding();

    for (Series series : chartPainter.getAxisPair().getSeriesMap().values()) {

      Map<String, Rectangle2D> seriesTextBounds = getSeriesTextBounds(series);

      float blockHeight = 0;
      double legendTextContentMaxWidth = 0;
      for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
        blockHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
        legendTextContentMaxWidth = Math.max(legendTextContentMaxWidth, entry.getValue().getWidth());
      }
      blockHeight -= MULTI_LINE_SPACE;

      blockHeight = Math.max(blockHeight, getChartPainter().getStyleManager().getChartType() == ChartType.Bar ? BOX_SIZE : getChartPainter().getStyleManager().getMarkerSize());

      if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar) {

        // paint line
        if (getChartPainter().getStyleManager().getChartType() != ChartType.Scatter && series.getStroke() != null) {
          g.setColor(series.getStrokeColor());
          g.setStroke(series.getStroke());
          Shape line = new Line2D.Double(startx, starty + blockHeight / 2.0, startx + getChartPainter().getStyleManager().getLegendSeriesLineLength(), starty + blockHeight / 2.0);
          g.draw(line);
        }

        // // debug box
        // Rectangle2D boundsTemp = new Rectangle2D.Double(startx, starty, styleManager.getLegendSeriesLineLength(), blockHeight);
        // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        // g.setColor(Color.red);
        // g.draw(boundsTemp);

        // paint marker
        if (series.getMarker() != null) {
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, startx + getChartPainter().getStyleManager().getLegendSeriesLineLength() / 2.0, starty + blockHeight / 2.0, getChartPainter().getStyleManager().getMarkerSize());

        }
      }
      else {
        // paint little box
        if (series.getStroke() != null) {
          g.setColor(series.getStrokeColor());
          Shape rectSmall = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
          g.fill(rectSmall);
        }
        // // debug box
        // Rectangle2D boundsTemp = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
        // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        // g.setColor(Color.red);
        // g.draw(boundsTemp);
      }

      // paint series text
      g.setColor(chartPainter.getStyleManager().getChartFontColor());

      double multiLineOffset = 0.0;

      if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar) {

        double x = startx + getChartPainter().getStyleManager().getLegendSeriesLineLength() + getChartPainter().getStyleManager().getLegendPadding();
        for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {

          double height = entry.getValue().getHeight();
          double centerOffsetY = (Math.max(getChartPainter().getStyleManager().getMarkerSize(), height) - height) / 2.0;

          FontRenderContext frc = g.getFontRenderContext();
          TextLayout tl = new TextLayout(entry.getKey(), getChartPainter().getStyleManager().getLegendFont(), frc);
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

        starty += blockHeight + getChartPainter().getStyleManager().getLegendPadding();
      }
      else {

        final double x = startx + BOX_SIZE + getChartPainter().getStyleManager().getLegendPadding();
        for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {

          double height = entry.getValue().getHeight();
          double centerOffsetY = (Math.max(BOX_SIZE, height) - height) / 2.0;

          FontRenderContext frc = g.getFontRenderContext();
          TextLayout tl = new TextLayout(entry.getKey(), getChartPainter().getStyleManager().getLegendFont(), frc);
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
        starty += blockHeight + getChartPainter().getStyleManager().getLegendPadding();
      }

    }

    // bounds
    bounds = new Rectangle2D.Double(xOffset, yOffset, legendBoxWidth, legendBoxHeight);

    // g.setColor(Color.blue);
    // g.draw(bounds);

  }

  private Map<String, Rectangle2D> getSeriesTextBounds(Series series) {

    // FontMetrics fontMetrics = g.getFontMetrics(getChartPainter().getStyleManager().getLegendFont());
    // float fontDescent = fontMetrics.getDescent();

    String lines[] = series.getName().split("\\n");
    Map<String, Rectangle2D> seriesTextBounds = new LinkedHashMap<String, Rectangle2D>(lines.length);
    for (String line : lines) {
      TextLayout tl = new TextLayout(line, getChartPainter().getStyleManager().getLegendFont(), new FontRenderContext(null, true, false));
      Shape shape = tl.getOutline(null);
      Rectangle2D bounds = shape.getBounds2D();
      // System.out.println(tl.getAscent());
      // System.out.println(tl.getDescent());
      // System.out.println(tl.getBounds());
      // seriesTextBounds.put(line, new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight() - tl.getDescent()));
      // seriesTextBounds.put(line, new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), tl.getAscent()));
      seriesTextBounds.put(line, bounds);
    }
    return seriesTextBounds;
  }

  public double getLegendBoxWidth() {

    return legendBoxWidth;
  }

  public double getLegendBoxHeight() {

    return legendBoxHeight;
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public ChartPainter getChartPainter() {

    return chartPainter;
  }

}
