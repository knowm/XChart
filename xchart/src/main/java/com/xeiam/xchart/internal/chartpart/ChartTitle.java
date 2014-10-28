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

  /** parent */
  private final ChartPainter chartPainter;

  /** the title text */
  private String text = ""; // default to ""

  /** the bounds */
  private Rectangle2D bounds;

  /**
   * Constructor
   *
   * @param chartPainter
   */
  public ChartTitle(ChartPainter chartPainter) {

    this.chartPainter = chartPainter;
  }

  /**
   * set the chart title's text
   *
   * @param text
   */
  public void setText(String text) {

    if (text.trim().equalsIgnoreCase("")) {
      chartPainter.getStyleManager().setChartTitleVisible(false);
    }
    else {
      chartPainter.getStyleManager().setChartTitleVisible(true);
    }
    this.text = text;
  }

  /**
   * get the height of the chart title including the chart padding
   *
   * @return
   */
  protected int getSizeHint() {

    if (chartPainter.getStyleManager().isChartTitleVisible()) {

      TextLayout textLayout = new TextLayout(text, chartPainter.getStyleManager().getChartTitleFont(), new FontRenderContext(null, true, false));
      Rectangle2D rectangle = textLayout.getBounds();
      int titleHeight = (int) ((chartPainter.getStyleManager().isChartTitleVisible() ? rectangle.getHeight() : 0));
      return chartPainter.getStyleManager().getChartPadding() + 2 * chartPainter.getStyleManager().getChartTitlePadding() + titleHeight;
    }
    else {
      return chartPainter.getStyleManager().getChartPadding();
    }
  }

  @Override
  public void paint(Graphics2D g) {

    g.setFont(chartPainter.getStyleManager().getChartTitleFont());

    if (chartPainter.getStyleManager().isChartTitleVisible()) {

      // create rectangle first for sizing
      FontRenderContext frc = g.getFontRenderContext();
      TextLayout textLayout = new TextLayout(text, chartPainter.getStyleManager().getChartTitleFont(), frc);
      Rectangle2D rectangle = textLayout.getBounds();

      double xOffset = (int) chartPainter.getPlot().getBounds().getX();
      double yOffset = chartPainter.getStyleManager().getChartPadding();

      if (chartPainter.getStyleManager().isChartTitleBoxVisible()) {

        // paint the chart title box
        double chartTitleBoxWidth = chartPainter.getPlot().getBounds().getWidth();
        double chartTitleBoxHeight = rectangle.getHeight() + 2 * chartPainter.getStyleManager().getChartTitlePadding();

        g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        Shape rect = new Rectangle2D.Double(xOffset, yOffset, chartTitleBoxWidth, chartTitleBoxHeight);
        g.setColor(chartPainter.getStyleManager().getChartTitleBoxBackgroundColor());
        g.fill(rect);
        g.setColor(chartPainter.getStyleManager().getChartTitleBoxBorderColor());
        g.draw(rect);
      }

      // paint title
      xOffset = chartPainter.getPlot().getBounds().getX() + (chartPainter.getPlot().getBounds().getWidth() - rectangle.getWidth()) / 2.0;
      yOffset = chartPainter.getStyleManager().getChartPadding() - rectangle.getY() + chartPainter.getStyleManager().getChartTitlePadding();

      bounds = new Rectangle2D.Double(xOffset, yOffset + rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
      // g.setColor(Color.green);
      // g.draw(bounds);

      g.setColor(chartPainter.getStyleManager().getChartFontColor());
      // textLayout.draw(g, xOffset, yOffset);

      Shape shape = textLayout.getOutline(null);
      AffineTransform orig = g.getTransform();
      AffineTransform at = new AffineTransform();
      at.translate(xOffset, yOffset);
      g.transform(at);
      g.fill(shape);
      g.setTransform(orig);
    }

  }

  @Override
  public Rectangle2D getBounds() {

    return null; // this should never be needed
  }

  @Override
  public ChartPainter getChartPainter() {

    return chartPainter;
  }
}