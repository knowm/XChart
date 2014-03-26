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
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xeiam.xchart.Series;
import com.xeiam.xchart.StyleManager;
import com.xeiam.xchart.StyleManager.ChartType;

/**
 * @author timmolter
 */
public class Legend implements ChartPart {

  private static final int LEGEND_MARGIN = 6;
  private static final int BOX_SIZE = 20;

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
   * get the width of the chart legend
   * 
   * @param g
   * @return
   */
  protected double[] getSizeHint(Graphics2D g) {

    if (!chartPainter.getStyleManager().isLegendVisible()) {
      return new double[] { 0, 0 };
    }

    StyleManager styleManager = getChartPainter().getStyleManager();
    FontMetrics fontMetrics = g.getFontMetrics(chartPainter.getStyleManager().getLegendFont());
    boolean isBar = styleManager.getChartType() == ChartType.Bar;

    // determine legend text content max width
    double legendTextContentMaxWidth = 0;
    // double legendTextContentMaxHeight = 0;

    // determine legend content height
    double legendContentHeight = 0;

    for (Series series : chartPainter.getAxisPair().getSeriesMap().values()) {

      List<Map.Entry<String, Rectangle2D>> seriesBounds = getSeriesNameBounds(series, g);

      double blockHeight = 0;
      for (Map.Entry<String, Rectangle2D> entry : seriesBounds) {
        blockHeight += entry.getValue().getHeight();
        legendTextContentMaxWidth = Math.max(legendTextContentMaxWidth, entry.getValue().getWidth());
      }

      blockHeight = Math.max(blockHeight, isBar ? BOX_SIZE : getChartPainter().getStyleManager().getMarkerSize());

      legendContentHeight += blockHeight + styleManager.getLegendPadding();
    }

    // determine legend content width
    double legendContentWidth = 0;
    if (!isBar) {
      legendContentWidth = styleManager.getLegendSeriesLineLength() + styleManager.getLegendPadding() + legendTextContentMaxWidth;
    }
    else {
      legendContentWidth = BOX_SIZE + styleManager.getLegendPadding() + legendTextContentMaxWidth;
    }
    // Legend Box
    double legendBoxWidth = legendContentWidth + 2 * styleManager.getLegendPadding();
    double legendBoxHeight = legendContentHeight + 1 * styleManager.getLegendPadding();
    return new double[] { legendBoxWidth, legendBoxHeight };
  }

  private List<Map.Entry<String, Rectangle2D>> getSeriesNameBounds(Series series, Graphics2D g) {

    String lines[] = series.getName().split("\\n");
    List<Map.Entry<String, Rectangle2D>> stringBounds = new ArrayList<Map.Entry<String, Rectangle2D>>(lines.length);
    for (String line : lines) {
      Rectangle2D bounds = g.getFontMetrics().getStringBounds(line, g);
      stringBounds.add(new AbstractMap.SimpleEntry<String, Rectangle2D>(line, bounds));
    }
    return stringBounds;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle2D.Double();
    g.setFont(chartPainter.getStyleManager().getLegendFont());

    StyleManager styleManager = getChartPainter().getStyleManager();

    if (!styleManager.isLegendVisible()) {
      return;
    }

    final double[] sizeHint = getSizeHint(g);

    double legendBoxWidth = sizeHint[0];
    double legendBoxHeight = sizeHint[1];

    FontMetrics fontMetrics = g.getFontMetrics(styleManager.getLegendFont());

    // legend draw position
    double xOffset = 0;
    double yOffset = 0;
    switch (styleManager.getLegendPosition()) {
    case OutsideE:
      xOffset = chartPainter.getWidth() - legendBoxWidth - styleManager.getChartPadding();
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

    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    Shape rect = new Rectangle2D.Double(xOffset + 1, yOffset + 1, legendBoxWidth - 2, legendBoxHeight - 2);
    g.setColor(styleManager.getLegendBackgroundColor());
    g.fill(rect);
    g.setColor(styleManager.getLegendBorderColor());
    g.draw(rect);

    // Draw legend content inside legend box
    double startx = xOffset + styleManager.getLegendPadding();
    double starty = yOffset + styleManager.getLegendPadding();

    for (Series series : chartPainter.getAxisPair().getSeriesMap().values()) {

      List<Map.Entry<String, Rectangle2D>> seriesNameBounds = getSeriesNameBounds(series, g);

      float blockHeight = 0;
      for (Map.Entry<String, Rectangle2D> entry : seriesNameBounds) {
        blockHeight += entry.getValue().getHeight();
      }

      blockHeight = Math.max(blockHeight, styleManager.getChartType() == ChartType.Bar ? BOX_SIZE : getChartPainter().getStyleManager().getMarkerSize());

      if (styleManager.getChartType() != ChartType.Bar) {
        // paint line
        if (styleManager.getChartType() != ChartType.Scatter && series.getStroke() != null) {
          g.setColor(series.getStrokeColor());
          g.setStroke(series.getStroke());
          Shape line = new Line2D.Double(startx, starty + blockHeight / 2.0, startx + styleManager.getLegendSeriesLineLength(), starty + blockHeight / 2.0);
          g.draw(line);
        }

        // paint marker
        if (series.getMarker() != null) {
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, startx + styleManager.getLegendSeriesLineLength() / 2.0, starty + blockHeight / 2.0, getChartPainter().getStyleManager().getMarkerSize());
        }
      }
      else {
        // paint little box
        if (series.getStroke() != null) {
          g.setColor(series.getStrokeColor());
          Shape rectSmall = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
          g.fill(rectSmall);
        }
      }

      // paint series name
      g.setColor(chartPainter.getStyleManager().getChartFontColor());

      float itemOffsetY = -fontMetrics.getDescent();
      if (styleManager.getChartType() != ChartType.Bar) {
        final float x = (float) (startx + styleManager.getLegendSeriesLineLength() + styleManager.getLegendPadding());
        for (Map.Entry<String, Rectangle2D> entry : seriesNameBounds) {
          g.drawString(entry.getKey(), x, (float) (starty + entry.getValue().getHeight()) + itemOffsetY);
          itemOffsetY += entry.getValue().getHeight();
        }
        starty += blockHeight + styleManager.getLegendPadding();
      }
      else {
        final float x = (float) (startx + BOX_SIZE + styleManager.getLegendPadding());
        for (Map.Entry<String, Rectangle2D> entry : seriesNameBounds) {
          double height = entry.getValue().getHeight();
          double centerOffsetY = (Math.max(BOX_SIZE, height) - height) / 2.0;
          g.drawString(entry.getKey(), x, (float) (starty + height + itemOffsetY + centerOffsetY));
          itemOffsetY += height;
        }
        starty += blockHeight + styleManager.getLegendPadding();
      }

    }

    // bounds
    bounds = new Rectangle2D.Double(xOffset, yOffset, legendBoxWidth, legendBoxHeight);

    // g.setColor(Color.blue);
    // g.draw(bounds);

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
