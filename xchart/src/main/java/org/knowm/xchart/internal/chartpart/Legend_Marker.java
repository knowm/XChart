/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.internal.series.MarkersSeriesCategory;
import org.knowm.xchart.internal.series.MarkersSeriesNumerical;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;

/**
 * @author timmolter
 */
public class Legend_Marker<ST extends AxesChartStyler, S extends Series> extends Legend_ {

  private final AxesChartStyler axesChartStyler;

  /**
   * Constructor
   *
   * @param chart
   */
  public Legend_Marker(Chart<AxesChartStyler, XYSeries> chart) {

    super(chart);
    axesChartStyler = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    // Draw legend content inside legend box
    double startx = xOffset + chart.getStyler().getLegendPadding();
    double starty = yOffset + chart.getStyler().getLegendPadding();

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Map<String, AxesChartSeries> map = chart.getSeriesMap();
    for (AxesChartSeries series : map.values()) {

      if (!series.isShowInLegend()) {
        continue;
      }
      if (!series.isEnabled()) {
        continue;
      }

      Map<String, Rectangle2D> seriesTextBounds = getSeriesTextBounds(series);
      float legendEntryHeight = getLegendEntryHeight(seriesTextBounds, (series.getLegendRenderType() == LegendRenderType.Box ? BOX_SIZE : axesChartStyler.getMarkerSize()));

      // paint line and marker
      if (series.getLegendRenderType() != LegendRenderType.Box) {

        // paint line
        if (series.getLegendRenderType() == LegendRenderType.Line && series.getLineStyle() != SeriesLines.NONE) {
          g.setColor(series.getLineColor());
          g.setStroke(series.getLineStyle());
          Shape line = new Line2D.Double(startx, starty + legendEntryHeight / 2.0, startx + chart.getStyler().getLegendSeriesLineLength(), starty + legendEntryHeight / 2.0);
          g.draw(line);
        }

        // paint marker

        if (series instanceof MarkersSeriesNumerical) {

          MarkersSeriesNumerical markersSeriesNumerical = (MarkersSeriesNumerical) series;

          if (markersSeriesNumerical.getMarker() != null) {
            g.setColor(markersSeriesNumerical.getMarkerColor());
            markersSeriesNumerical.getMarker().paint(g, startx + chart.getStyler().getLegendSeriesLineLength() / 2.0, starty + legendEntryHeight / 2.0, axesChartStyler.getMarkerSize());
          }

        }
        else if (series instanceof MarkersSeriesCategory) {

          MarkersSeriesCategory markersSeriesCategory = (MarkersSeriesCategory) series;
          if (markersSeriesCategory.getMarker() != null) {
            g.setColor(markersSeriesCategory.getMarkerColor());
            markersSeriesCategory.getMarker().paint(g, startx + chart.getStyler().getLegendSeriesLineLength() / 2.0, starty + legendEntryHeight / 2.0, axesChartStyler.getMarkerSize());
          }

        }

      }
      else { // bar/pie type series

        // paint inner box
        Shape rectSmall = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
        g.setColor(series.getFillColor());
        g.fill(rectSmall);

        // Draw outline
        // At the time of writing CategorySeriesRenderStyle.Bar does not support outlines. To reflect this any LineColor value should be ignored.
        if (!(series instanceof CategorySeries && ((CategorySeries) series).getChartCategorySeriesRenderStyle() == CategorySeriesRenderStyle.Bar)) {

          // paint outer box
          g.setColor(series.getLineColor());

          // Only respect the existing stroke width up to BOX_OUTLINE_WIDTH, as the legend box is very small.
          // Note the simplified conversion of line width from user space to device space.
          BasicStroke existingLineStyle = series.getLineStyle();
          BasicStroke newLineStyle = new BasicStroke(existingLineStyle.getLineWidth() > BOX_OUTLINE_WIDTH * 0.5f ? BOX_OUTLINE_WIDTH * 0.5f : existingLineStyle.getLineWidth(), existingLineStyle
              .getEndCap(), existingLineStyle.getLineJoin(), existingLineStyle.getMiterLimit(), existingLineStyle.getDashArray(), existingLineStyle.getDashPhase());

          g.setPaint(series.getLineColor());
          g.setStroke(newLineStyle);

          Path2D.Double outlinePath = new Path2D.Double();

          double lineOffset = existingLineStyle.getLineWidth() * 0.5;
          outlinePath.moveTo(startx + lineOffset, starty + lineOffset);
          outlinePath.lineTo(startx + lineOffset, starty + BOX_SIZE - lineOffset);
          outlinePath.lineTo(startx + BOX_SIZE - lineOffset, starty + BOX_SIZE - lineOffset);
          outlinePath.lineTo(startx + BOX_SIZE - lineOffset, starty + lineOffset);
          outlinePath.closePath();

          g.draw(outlinePath);
        }
      }

      // paint series text
      if (series.getLegendRenderType() != LegendRenderType.Box) {

        double x = startx + chart.getStyler().getLegendSeriesLineLength() + chart.getStyler().getLegendPadding();
        paintSeriesText(g, seriesTextBounds, axesChartStyler.getMarkerSize(), x, starty);
      }
      else { // bar/pie type series

        double x = startx + BOX_SIZE + chart.getStyler().getLegendPadding();
        paintSeriesText(g, seriesTextBounds, BOX_SIZE, x, starty);
      }

      if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
        starty += legendEntryHeight + chart.getStyler().getLegendPadding();
      }
      else {
        int markerWidth = BOX_SIZE;
        if (series.getLegendRenderType() == LegendRenderType.Line) {
          markerWidth = chart.getStyler().getLegendSeriesLineLength();
        }
        float legendEntryWidth = getLegendEntryWidth(seriesTextBounds, markerWidth);
        startx += legendEntryWidth + chart.getStyler().getLegendPadding();
      }
    }
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  @Override
  public double getSeriesLegendRenderGraphicHeight(Series series) {

    return series.getLegendRenderType() == LegendRenderType.Box ? BOX_SIZE : axesChartStyler.getMarkerSize();
  }
}
