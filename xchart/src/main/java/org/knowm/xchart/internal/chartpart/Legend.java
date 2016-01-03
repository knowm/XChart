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
  private final ChartInternal chartInternal;

  /**
   * the bounds
   */
  private Rectangle2D bounds;

  /**
   * Constructor
   *
   * @param chartInternal
   */
  public Legend(ChartInternal chartInternal) {

    this.chartInternal = chartInternal;
  }

  /**
   * determine the width and height of the chart legend
   */
  public void determineLegendBoxSize() {

    if (!chartInternal.getStyleManager().isLegendVisible()) {
      return;
    }

    boolean containsBarOrPie = false;

    // determine legend text content max width
    double legendTextContentMaxWidth = 0;

    // determine legend content height
    double legendContentHeight = 0;

    for (Series series : chartInternal.getSeriesMap().values()) {

      Map<String, Rectangle2D> seriesBounds = getSeriesTextBounds(series);

      double blockHeight = 0;
      for (Map.Entry<String, Rectangle2D> entry : seriesBounds.entrySet()) {
        blockHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
        legendTextContentMaxWidth = Math.max(legendTextContentMaxWidth, entry.getValue().getWidth());
      }

      blockHeight -= MULTI_LINE_SPACE;
      blockHeight = Math.max(blockHeight, (series.getSeriesType() == Series.SeriesType.Bar || series.getSeriesType() == Series.SeriesType.Pie) ? BOX_SIZE : getChartInternal().getStyleManager()
          .getMarkerSize());

      legendContentHeight += blockHeight + getChartInternal().getStyleManager().getLegendPadding();

      if (series.getSeriesType() == Series.SeriesType.Bar || series.getSeriesType() == Series.SeriesType.Pie) {
        containsBarOrPie = true;
      }
    }

    // determine legend content width
    double legendContentWidth = 0;
    if (!containsBarOrPie) {
      legendContentWidth = getChartInternal().getStyleManager().getLegendSeriesLineLength() + getChartInternal().getStyleManager().getLegendPadding() + legendTextContentMaxWidth;
    }
    else {
      legendContentWidth = BOX_SIZE + getChartInternal().getStyleManager().getLegendPadding() + legendTextContentMaxWidth;
    }

    // Legend Box
    legendBoxWidth = legendContentWidth + 2 * getChartInternal().getStyleManager().getLegendPadding();
    legendBoxHeight = legendContentHeight + 1 * getChartInternal().getStyleManager().getLegendPadding();
  }

  @Override
  public void paint(Graphics2D g) {

    if (!getChartInternal().getStyleManager().isLegendVisible()) {
      return;
    }

    // if the area to draw a chart on is so small, don't even bother
    if (chartInternal.getPlot().getBounds().getWidth() < 30) {
      return;
    }

    bounds = new Rectangle2D.Double();
    // g.setFont(chartInternal.getStyleManager().getLegendFont());

    // legend draw position
    double xOffset = 0;
    double yOffset = 0;
    switch (getChartInternal().getStyleManager().getLegendPosition()) {
    case OutsideE:
      xOffset = chartInternal.getWidth() - legendBoxWidth - getChartInternal().getStyleManager().getChartPadding();
      yOffset = chartInternal.getPlot().getBounds().getY() + (chartInternal.getPlot().getBounds().getHeight() - legendBoxHeight) / 2.0;
      break;
    case InsideNW:
      xOffset = chartInternal.getPlot().getBounds().getX() + LEGEND_MARGIN;
      yOffset = chartInternal.getPlot().getBounds().getY() + LEGEND_MARGIN;
      break;
    case InsideNE:
      xOffset = chartInternal.getPlot().getBounds().getX() + chartInternal.getPlot().getBounds().getWidth() - legendBoxWidth - LEGEND_MARGIN;
      yOffset = chartInternal.getPlot().getBounds().getY() + LEGEND_MARGIN;
      break;
    case InsideSE:
      xOffset = chartInternal.getPlot().getBounds().getX() + chartInternal.getPlot().getBounds().getWidth() - legendBoxWidth - LEGEND_MARGIN;
      yOffset = chartInternal.getPlot().getBounds().getY() + chartInternal.getPlot().getBounds().getHeight() - legendBoxHeight - LEGEND_MARGIN;
      break;
    case InsideSW:
      xOffset = chartInternal.getPlot().getBounds().getX() + LEGEND_MARGIN;
      yOffset = chartInternal.getPlot().getBounds().getY() + chartInternal.getPlot().getBounds().getHeight() - legendBoxHeight - LEGEND_MARGIN;
      break;
    case InsideN:
      xOffset = chartInternal.getPlot().getBounds().getX() + (chartInternal.getPlot().getBounds().getWidth() - legendBoxWidth) / 2 + LEGEND_MARGIN;
      yOffset = chartInternal.getPlot().getBounds().getY() + LEGEND_MARGIN;
      break;

    default:
      break;
    }

    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 0.0f }, 0.0f));

    Shape rect = new Rectangle2D.Double(xOffset + 1, yOffset + 1, legendBoxWidth - 2, legendBoxHeight - 2);
    g.setColor(getChartInternal().getStyleManager().getLegendBackgroundColor());
    g.fill(rect);
    g.setColor(getChartInternal().getStyleManager().getLegendBorderColor());
    g.draw(rect);

    // Draw legend content inside legend box
    double startx = xOffset + getChartInternal().getStyleManager().getLegendPadding();
    double starty = yOffset + getChartInternal().getStyleManager().getLegendPadding();

    for (Series series : chartInternal.getSeriesMap().values()) {

      Map<String, Rectangle2D> seriesTextBounds = getSeriesTextBounds(series);

      float blockHeight = 0;
      double legendTextContentMaxWidth = 0;
      for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
        blockHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
        legendTextContentMaxWidth = Math.max(legendTextContentMaxWidth, entry.getValue().getWidth());
      }
      blockHeight -= MULTI_LINE_SPACE;

      blockHeight = Math.max(blockHeight, (series.getSeriesType() == Series.SeriesType.Bar || series.getSeriesType() == Series.SeriesType.Pie) ? BOX_SIZE : getChartInternal().getStyleManager()
          .getMarkerSize());

      if (series.getSeriesType() != Series.SeriesType.Bar && series.getSeriesType() != Series.SeriesType.Pie) {

        // paint line
        if (series.getSeriesType() != Series.SeriesType.Scatter && series.getStroke() != null) {
          g.setColor(series.getStrokeColor());
          g.setStroke(series.getStroke());
          Shape line = new Line2D.Double(startx, starty + blockHeight / 2.0, startx + getChartInternal().getStyleManager().getLegendSeriesLineLength(), starty + blockHeight / 2.0);
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
          series.getMarker().paint(g, startx + getChartInternal().getStyleManager().getLegendSeriesLineLength() / 2.0, starty + blockHeight / 2.0, getChartInternal().getStyleManager()
              .getMarkerSize());

        }
      }
      else { // bar/pie type series

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

      // paint series text /////////////////////////////////////////////////////
      g.setColor(chartInternal.getStyleManager().getChartFontColor());

      double multiLineOffset = 0.0;

      if (series.getSeriesType() != Series.SeriesType.Bar && series.getSeriesType() != Series.SeriesType.Pie) {

        double x = startx + getChartInternal().getStyleManager().getLegendSeriesLineLength() + getChartInternal().getStyleManager().getLegendPadding();
        for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {

          double height = entry.getValue().getHeight();
          double centerOffsetY = (Math.max(getChartInternal().getStyleManager().getMarkerSize(), height) - height) / 2.0;

          FontRenderContext frc = g.getFontRenderContext();
          TextLayout tl = new TextLayout(entry.getKey(), getChartInternal().getStyleManager().getLegendFont(), frc);
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

        starty += blockHeight + getChartInternal().getStyleManager().getLegendPadding();
      }
      else { // bar/pie type series

        final double x = startx + BOX_SIZE + getChartInternal().getStyleManager().getLegendPadding();
        for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {

          double height = entry.getValue().getHeight();
          double centerOffsetY = (Math.max(BOX_SIZE, height) - height) / 2.0;

          FontRenderContext frc = g.getFontRenderContext();
          TextLayout tl = new TextLayout(entry.getKey(), getChartInternal().getStyleManager().getLegendFont(), frc);
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
        starty += blockHeight + getChartInternal().getStyleManager().getLegendPadding();
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
      TextLayout tl = new TextLayout(line, getChartInternal().getStyleManager().getLegendFont(), new FontRenderContext(null, true, false));
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
  public ChartInternal getChartInternal() {

    return chartInternal;
  }

}
