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

/**
 * Chart Title
 */
public class ChartTitle implements ChartPart {

  private final Chart<?, ?> chart;
  private Rectangle2D bounds;

  /**
   * Constructor
   *
   * @param chart
   */
  public ChartTitle(Chart<?, ?> chart) {

    this.chart = chart;
  }

  @Override
  public void paint(Graphics2D g) {

    g.setFont(chart.getStyler().getChartTitleFont());

    if (!chart.getStyler().isChartTitleVisible() || chart.getTitle().length() == 0) {
      return;
    }

    // create rectangle first for sizing
    FontRenderContext frc = g.getFontRenderContext();
    TextLayout textLayout = new TextLayout(chart.getTitle(), chart.getStyler().getChartTitleFont(), frc);
    Rectangle2D textBounds = textLayout.getBounds();

    double xOffset = chart.getPlot().getBounds().getX(); // of plot left edge
    double yOffset = chart.getStyler().getChartPadding();

    // title box
    if (chart.getStyler().isChartTitleBoxVisible()) {

      // paint the chart title box
      double chartTitleBoxWidth = chart.getPlot().getBounds().getWidth();
      double chartTitleBoxHeight = textBounds.getHeight() + 2 * chart.getStyler().getChartTitlePadding();

      g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
      Shape rect = new Rectangle2D.Double(xOffset, yOffset, chartTitleBoxWidth, chartTitleBoxHeight);
      g.setColor(chart.getStyler().getChartTitleBoxBackgroundColor());
      g.fill(rect);
      g.setColor(chart.getStyler().getChartTitleBoxBorderColor());
      g.draw(rect);
    }

    // paint title
    xOffset = chart.getPlot().getBounds().getX() + (chart.getPlot().getBounds().getWidth() - textBounds.getWidth()) / 2.0;
    yOffset = chart.getStyler().getChartPadding() + textBounds.getHeight() + chart.getStyler().getChartTitlePadding();

    g.setColor(chart.getStyler().getChartFontColor());
    Shape shape = textLayout.getOutline(null);
    AffineTransform orig = g.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(xOffset, yOffset);
    g.transform(at);
    g.fill(shape);
    g.setTransform(orig);

    double width = 2 * chart.getStyler().getChartTitlePadding() + textBounds.getWidth();
    double height = 2 * chart.getStyler().getChartTitlePadding() + textBounds.getHeight();
    bounds = new Rectangle2D.Double(xOffset - chart.getStyler().getChartTitlePadding(), yOffset - textBounds.getHeight() - chart.getStyler().getChartTitlePadding(), width, height);
    // g.setColor(Color.blue);
    // g.draw(bounds);

  }

  /**
   * get the height of the chart title including the chart title padding
   *
   * @return
   */
  private Rectangle2D getBoundsHint() {

    if (chart.getStyler().isChartTitleVisible() && chart.getTitle().length() > 0) {

      TextLayout textLayout = new TextLayout(chart.getTitle(), chart.getStyler().getChartTitleFont(), new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();
      double width = 2 * chart.getStyler().getChartTitlePadding() + rectangle.getWidth();
      double height = 2 * chart.getStyler().getChartTitlePadding() + rectangle.getHeight();

      return new Rectangle2D.Double(Double.NaN, Double.NaN, width, height); // Double.NaN indicates not sure yet.
    }
    else {
      return new Rectangle2D.Double(); // Constructs a new Rectangle2D, initialized to location (0, 0) and size (0, 0).
    }
  }

  @Override
  public Rectangle2D getBounds() {

    if (bounds == null) { // was not drawn fully yet, just need the height hint. The Plot object will be asking for it.
      bounds = getBoundsHint();
    }
    return bounds;
  }
}