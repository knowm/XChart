/**
 * Copyright 2011-2013 Xeiam LLC.
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
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import com.xeiam.xchart.Series;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.internal.markers.Marker;

/**
 * @author timmolter
 */
public class Legend implements ChartPart {

  private static final int LEGEND_MARGIN = 6;
  private static final int BOX_SIZE = 20;

  /** parent */
  private final ChartPainter chartPainter;

  /** the bounds */
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
   * @return
   */
  protected double[] getSizeHint() {

    if (chartPainter.getStyleManager().isLegendVisible()) {

      Map<Integer, Series> seriesMap = chartPainter.getAxisPair().getSeriesMap();

      // determine legend text content max width
      double legendTextContentMaxWidth = 0;
      double legendTextContentMaxHeight = 0;

      for (Integer seriesId : seriesMap.keySet()) {
        Series series = seriesMap.get(seriesId);
        TextLayout textLayout = new TextLayout(series.getName(), chartPainter.getStyleManager().getLegendFont(), new FontRenderContext(null, true, false));
        Rectangle2D rectangle = textLayout.getBounds();
        // System.out.println(rectangle);
        if (rectangle.getWidth() > legendTextContentMaxWidth) {
          legendTextContentMaxWidth = rectangle.getWidth();
        }
        if (rectangle.getHeight() > legendTextContentMaxHeight) {
          legendTextContentMaxHeight = rectangle.getHeight();
        }
      }

      // determine legend content height
      double maxContentHeight = 0;
      if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar) {
        maxContentHeight = Math.max(legendTextContentMaxHeight, Marker.SIZE);
      } else {
        maxContentHeight = Math.max(legendTextContentMaxHeight, BOX_SIZE);
      }
      double legendContentHeight = maxContentHeight * seriesMap.size() + chartPainter.getStyleManager().getLegendPadding() * (seriesMap.size() - 1);

      // determine legend content width
      double legendContentWidth = 0;
      if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar) {
        legendContentWidth = (int) (3.0 * Marker.SIZE + chartPainter.getStyleManager().getLegendPadding() + legendTextContentMaxWidth);
      } else {
        legendContentWidth = BOX_SIZE + chartPainter.getStyleManager().getLegendPadding() + legendTextContentMaxWidth;
      }
      // Legend Box
      double legendBoxWidth = legendContentWidth + 2 * chartPainter.getStyleManager().getLegendPadding();
      double legendBoxHeight = legendContentHeight + 2 * chartPainter.getStyleManager().getLegendPadding();
      return new double[] { legendBoxWidth, legendBoxHeight, maxContentHeight };
    } else {
      return new double[] { 0, 0, 0 };
    }
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle2D.Double();
    g.setFont(chartPainter.getStyleManager().getLegendFont());

    if (chartPainter.getStyleManager().isLegendVisible()) {

      Map<Integer, Series> seriesMap = chartPainter.getAxisPair().getSeriesMap();

      double legendBoxWidth = getSizeHint()[0];
      double legendBoxHeight = getSizeHint()[1];
      double maxContentHeight = getSizeHint()[2];

      // legend draw position
      double xOffset = 0;
      double yOffset = 0;
      switch (chartPainter.getStyleManager().getLegendPosition()) {
      case OutsideE:
        xOffset = chartPainter.getWidth() - legendBoxWidth - chartPainter.getStyleManager().getChartPadding();
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

      default:
        break;
      }
      g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
      Shape rect = new Rectangle2D.Double(xOffset + 1, yOffset + 1, legendBoxWidth - 2, legendBoxHeight - 2);
      g.setColor(chartPainter.getStyleManager().getLegendBackgroundColor());
      g.fill(rect);
      g.setColor(chartPainter.getStyleManager().getLegendBorderColor());
      g.draw(rect);

      // Draw legend content inside legend box
      double startx = xOffset + chartPainter.getStyleManager().getLegendPadding();
      double starty = yOffset + chartPainter.getStyleManager().getLegendPadding();
      for (Integer seriesId : seriesMap.keySet()) {
        Series series = seriesMap.get(seriesId);

        if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar) {

          // paint line
          if (getChartPainter().getStyleManager().getChartType() != ChartType.Scatter && series.getStroke() != null) {
            g.setColor(series.getStrokeColor());
            g.setStroke(series.getStroke());
            Shape line = new Line2D.Double(startx, starty + maxContentHeight / 2.0, startx + Marker.SIZE * 3.0, starty + maxContentHeight / 2.0);
            g.draw(line);
          }

          // paint marker
          if (series.getMarker() != null) {
            g.setColor(series.getMarkerColor());
            series.getMarker().paint(g, (int) (startx + (Marker.SIZE * 1.5)), (int) (starty + maxContentHeight / 2.0));
          }
        } else {
          // paint little box
          if (series.getStroke() != null) {
            g.setColor(series.getStrokeColor());
            Shape rectSmall = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
            g.fill(rectSmall);
          }
        }

        // paint series name

        g.setColor(chartPainter.getStyleManager().getChartFontColor());
        TextLayout layout = new TextLayout(series.getName(), chartPainter.getStyleManager().getLegendFont(), new FontRenderContext(null, true, false));
        if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar) {
          layout.draw(g, (float) (startx + Marker.SIZE + (Marker.SIZE * 1.5) + chartPainter.getStyleManager().getLegendPadding()), (float) (starty + (maxContentHeight - 1 + layout.getBounds()
              .getHeight()) / 2.0));
        } else {
          layout.draw(g, (float) (startx + BOX_SIZE + chartPainter.getStyleManager().getLegendPadding()), (float) (starty + (maxContentHeight + layout.getBounds().getHeight()) / 2.0));
        }
        starty = (int) (starty + maxContentHeight + chartPainter.getStyleManager().getLegendPadding());
      }

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset, legendBoxWidth, legendBoxHeight);
      // g.setColor(Color.blue);
      // g.draw(bounds);
    }

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
