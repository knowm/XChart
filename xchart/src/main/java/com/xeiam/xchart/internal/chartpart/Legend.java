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
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
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
  private Rectangle bounds;

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
  protected int[] getSizeHint() {

    if (chartPainter.getStyleManager().isLegendVisible()) {

      Map<Integer, Series> seriesMap = chartPainter.getAxisPair().getSeriesMap();

      // determine legend text content max width
      int legendTextContentMaxWidth = 0;
      int legendTextContentMaxHeight = 0;

      for (Integer seriesId : seriesMap.keySet()) {
        Series series = seriesMap.get(seriesId);
        TextLayout textLayout = new TextLayout(series.getName(), chartPainter.getStyleManager().getLegendFont(), new FontRenderContext(null, true, false));
        Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
        // System.out.println(rectangle);
        if (rectangle.getWidth() > legendTextContentMaxWidth) {
          legendTextContentMaxWidth = (int) rectangle.getWidth();
        }
        if (rectangle.getHeight() > legendTextContentMaxHeight) {
          legendTextContentMaxHeight = (int) rectangle.getHeight();
        }
      }

      // determine legend content height
      int maxContentHeight = 0;
      if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar) {
        maxContentHeight = Math.max(legendTextContentMaxHeight, Marker.SIZE);
      } else {
        maxContentHeight = Math.max(legendTextContentMaxHeight, BOX_SIZE);
      }
      int legendContentHeight = maxContentHeight * seriesMap.size() + chartPainter.getStyleManager().getLegendPadding() * (seriesMap.size() - 1);

      // determine legend content width
      int legendContentWidth = 0;
      if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar) {
        legendContentWidth = (int) (3.0 * Marker.SIZE + chartPainter.getStyleManager().getLegendPadding() + legendTextContentMaxWidth);
      } else {
        legendContentWidth = BOX_SIZE + chartPainter.getStyleManager().getLegendPadding() + legendTextContentMaxWidth;
      }
      // Legend Box
      int legendBoxWidth = legendContentWidth + 2 * chartPainter.getStyleManager().getLegendPadding();
      int legendBoxHeight = legendContentHeight + 2 * chartPainter.getStyleManager().getLegendPadding();
      return new int[] { legendBoxWidth, legendBoxHeight, maxContentHeight };
    } else {
      return new int[] { 0, 0, 0 };
    }
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle();
    g.setFont(chartPainter.getStyleManager().getLegendFont());

    if (chartPainter.getStyleManager().isLegendVisible()) {

      Map<Integer, Series> seriesMap = chartPainter.getAxisPair().getSeriesMap();

      int legendBoxWidth = getSizeHint()[0];
      int legendBoxHeight = getSizeHint()[1];
      int maxContentHeight = getSizeHint()[2];

      // legend draw position
      int xOffset = 0;
      int yOffset = 0;
      switch (chartPainter.getStyleManager().getLegendPosition()) {
      case OutsideW:
        xOffset = chartPainter.getWidth() - legendBoxWidth - chartPainter.getStyleManager().getChartPadding();
        yOffset = (int) (chartPainter.getPlot().getBounds().getY() + (chartPainter.getPlot().getBounds().getHeight() - legendBoxHeight) / 2.0);
        break;
      case InsideNW:
        xOffset = (int) (chartPainter.getPlot().getBounds().getX() + LEGEND_MARGIN);
        yOffset = (int) (chartPainter.getPlot().getBounds().getY() + LEGEND_MARGIN);
        break;
      case InsideNE:
        xOffset = (int) (chartPainter.getPlot().getBounds().getX() + chartPainter.getPlot().getBounds().getWidth() - legendBoxWidth - LEGEND_MARGIN);
        yOffset = (int) (chartPainter.getPlot().getBounds().getY() + LEGEND_MARGIN);
        break;
      case InsideSE:
        xOffset = (int) (chartPainter.getPlot().getBounds().getX() + chartPainter.getPlot().getBounds().getWidth() - legendBoxWidth - LEGEND_MARGIN);
        yOffset = (int) (chartPainter.getPlot().getBounds().getY() + chartPainter.getPlot().getBounds().getHeight() - legendBoxHeight - LEGEND_MARGIN);
        break;
      case InsideSW:
        xOffset = (int) (chartPainter.getPlot().getBounds().getX() + LEGEND_MARGIN);
        yOffset = (int) (chartPainter.getPlot().getBounds().getY() + chartPainter.getPlot().getBounds().getHeight() - legendBoxHeight - LEGEND_MARGIN);
        break;

      default:
        break;
      }
      g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
      g.setColor(chartPainter.getStyleManager().getLegendBorderColor());
      g.drawRect(xOffset, yOffset, legendBoxWidth, legendBoxHeight);
      g.setColor(chartPainter.getStyleManager().getLegendBackgroundColor());
      g.fillRect(xOffset + 1, yOffset + 1, legendBoxWidth - 1, legendBoxHeight - 1);

      // Draw legend content inside legend box
      int startx = xOffset + chartPainter.getStyleManager().getLegendPadding();
      int starty = yOffset + chartPainter.getStyleManager().getLegendPadding();
      for (Integer seriesId : seriesMap.keySet()) {
        Series series = seriesMap.get(seriesId);

        if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar) {

          // paint line
          if (getChartPainter().getStyleManager().getChartType() != ChartType.Scatter && series.getStroke() != null) {
            g.setColor(series.getStrokeColor());
            g.setStroke(series.getStroke());
            g.drawLine(startx, starty + (int) (maxContentHeight / 2.0), (int) (startx + Marker.SIZE * 3.0), starty + (int) (maxContentHeight / 2.0));
          }

          // paint marker
          if (series.getMarker() != null) {
            g.setColor(series.getMarkerColor());
            series.getMarker().paint(g, (int) (startx + (Marker.SIZE * 1.5)), starty + (int) (maxContentHeight / 2.0));
          }
        } else {
          // paint little box
          if (series.getStroke() != null) {
            g.setColor(series.getStrokeColor());
            g.fillPolygon(new int[] { startx, startx + BOX_SIZE, startx + BOX_SIZE, startx }, new int[] { starty, starty, starty + BOX_SIZE, starty + BOX_SIZE }, 4);
            // g.setStroke(series.getStroke());
            // g.drawPolygon(new int[] { startx, startx + BOX_SIZE, startx + BOX_SIZE, startx }, new int[] { starty, starty, starty + BOX_SIZE, starty + BOX_SIZE }, 4);
          }
        }

        // paint series name
        g.setColor(chartPainter.getStyleManager().getChartFontColor());
        TextLayout layout = new TextLayout(series.getName(), chartPainter.getStyleManager().getLegendFont(), new FontRenderContext(null, true, false));
        if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar) {
          layout.draw(g, (float) (startx + Marker.SIZE + (Marker.SIZE * 1.5) + chartPainter.getStyleManager().getLegendPadding()), starty
              + (int) ((maxContentHeight + layout.getPixelBounds(null, 0, 0).getHeight()) / 2.0));
        } else {
          layout.draw(g, startx + BOX_SIZE + chartPainter.getStyleManager().getLegendPadding(), starty + (int) ((maxContentHeight + layout.getPixelBounds(null, 0, 0).getHeight()) / 2.0));
        }
        starty = starty + maxContentHeight + chartPainter.getStyleManager().getLegendPadding();
      }

      // bounds
      bounds = new Rectangle(xOffset, yOffset, legendBoxWidth, legendBoxHeight);
      // g.setColor(Color.blue);
      // g.draw(bounds);
    }

  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }

  @Override
  public ChartPainter getChartPainter() {

    return chartPainter;
  }

}
