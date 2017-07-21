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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeriesNumericalNoErrorBars;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

/**
 * @author timmolter
 */
public class Legend_Bubble<ST extends AxesChartStyler, S extends Series> extends Legend_ {

  private final AxesChartStyler axesChartStyler;

  /**
   * Constructor
   *
   * @param chart
   */
  public Legend_Bubble(Chart<AxesChartStyler, XYSeries> chart) {

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

    Map<String, AxesChartSeriesNumericalNoErrorBars> map = chart.getSeriesMap();
    for (AxesChartSeriesNumericalNoErrorBars series : map.values()) {

      if (!series.isShowInLegend()) {
        continue;
      }
      if (!series.isEnabled()) {
        continue;
      }

      Map<String, Rectangle2D> seriesTextBounds = getSeriesTextBounds(series);
      float legendEntryHeight = getLegendEntryHeight(seriesTextBounds, BOX_SIZE);

      // paint little circle
      Shape rectSmall = new Ellipse2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
      g.setColor(series.getFillColor());
      g.fill(rectSmall);
      g.setStroke(series.getLineStyle());
      g.setColor(series.getLineColor());
      g.draw(rectSmall);

      // paint series text
      final double x = startx + BOX_SIZE + chart.getStyler().getLegendPadding();
      paintSeriesText(g, seriesTextBounds, BOX_SIZE, x, starty);

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
