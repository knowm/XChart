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
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.style.StyleManager;

/**
 * @author timmolter
 */
public abstract class Legend<SM extends StyleManager, S extends Series> implements ChartPart {

  public abstract double getSeriesLegendRenderGraphicHeight(Series series);

  protected static final int LEGEND_MARGIN = 6;
  protected static final int BOX_SIZE = 20;
  protected static final int MULTI_LINE_SPACE = 3;

  protected Chart<SM, S> chart;
  protected Rectangle2D bounds = null;

  protected double xOffset = 0;
  protected double yOffset = 0;

  /**
   * Constructor
   *
   * @param chart
   */
  public Legend(Chart<SM, S> chart) {

    this.chart = chart;

  }

  @Override
  public void paint(Graphics2D g) {

    // if the area to draw a chart on is so small, don't even bother
    if (chart.getPlot().getBounds().getWidth() < 30) {
      return;
    }

    // We call get bounds hint because sometimes the Axis object needs it to know it's bounds (if Legend is outside Plot). If it's null, we just need to calulate it before painting, because the paint
    // methods needs the bounds.
    if (bounds == null) { // No other part asked for the bounds yet. Probably because it's an "inside" legend location
      bounds = getBoundsHint(); // Actually, the only information contained in thsi bounds is the width and height.
    }

    // legend draw position

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

    // draw legend box background and border
    Shape rect = new Rectangle2D.Double(xOffset, yOffset, bounds.getWidth(), bounds.getHeight());
    g.setColor(chart.getStyleManager().getLegendBackgroundColor());
    g.fill(rect);
    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 0.0f }, 0.0f));
    g.setColor(chart.getStyleManager().getLegendBorderColor());
    g.draw(rect);
  }

  /**
   * determine the width and height of the chart legend
   */
  public Rectangle2D getBoundsHint() {

    if (!chart.getStyleManager().isLegendVisible()) {
      return new Rectangle2D.Double(); // Constructs a new Rectangle2D, initialized to location (0, 0) and size (0, 0).
    }

    boolean containsBox = false;

    // determine legend text content max width
    double legendTextContentMaxWidth = 0;

    // determine total legend content height
    double legendContentHeight = 0;

    Map<String, S> map = chart.getSeriesMap();
    for (Series series : map.values()) {

      Map<String, Rectangle2D> seriesTextBounds = getSeriesTextBounds(series);

      double legendEntryHeight = 0; // could be multi-line
      for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
        legendEntryHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
        legendTextContentMaxWidth = Math.max(legendTextContentMaxWidth, entry.getValue().getWidth());
      }

      legendEntryHeight -= MULTI_LINE_SPACE; // subtract away the bottom MULTI_LINE_SPACE
      legendEntryHeight = Math.max(legendEntryHeight, (getSeriesLegendRenderGraphicHeight(series)));

      legendContentHeight += legendEntryHeight + chart.getStyleManager().getLegendPadding();

      if (series.getLegendRenderType() == LegendRenderType.Box) {
        containsBox = true;
      }
    }

    // determine legend content width
    double legendContentWidth = 0;
    if (!containsBox) {
      legendContentWidth = chart.getStyleManager().getLegendSeriesLineLength() + chart.getStyleManager().getLegendPadding() + legendTextContentMaxWidth;
    }
    else {
      legendContentWidth = BOX_SIZE + chart.getStyleManager().getLegendPadding() + legendTextContentMaxWidth;
    }

    // Legend Box
    double width = legendContentWidth + 2 * chart.getStyleManager().getLegendPadding();
    double height = legendContentHeight + 1 * chart.getStyleManager().getLegendPadding();

    return new Rectangle2D.Double(Double.NaN, Double.NaN, width, height); // Double.NaN indicates not sure yet.
  }

  // TODO possibly cache the first hint result, so as to not re-calculate this.
  /**
   * Normally each legend entry just has one line of text, but it can be made multi-line by adding "\\n". This method returns a Map for each single legend entry, which is normally just a Map with one
   * single entry.
   *
   * @param series
   * @return
   */
  protected Map<String, Rectangle2D> getSeriesTextBounds(Series series) {

    // FontMetrics fontMetrics = g.getFontMetrics(getChartPainter().getStyleManager().getLegendFont());
    // float fontDescent = fontMetrics.getDescent();

    String lines[] = series.getName().split("\\n");
    Map<String, Rectangle2D> seriesTextBounds = new LinkedHashMap<String, Rectangle2D>(lines.length);
    for (String line : lines) {
      TextLayout textLayout = new TextLayout(line, chart.getStyleManager().getLegendFont(), new FontRenderContext(null, true, false));
      Shape shape = textLayout.getOutline(null);
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

}
