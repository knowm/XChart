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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import com.xeiam.xchart.Chart;

/**
 * Chart Title
 */
public class ChartTitle implements ChartPart {

  /** parent */
  private final Chart chart;

  // /** the bounds */
  // private Rectangle bounds;

  /** the title text */
  private String text = ""; // default to ""

  /**
   * Constructor
   * 
   * @param chart
   */
  public ChartTitle(Chart chart) {

    this.chart = chart;
  }

  /**
   * set the chart title's text
   * 
   * @param text
   */
  public void setText(String text) {

    if (text.trim().equalsIgnoreCase("")) {
      chart.getStyleManager().setChartTitleVisible(false);
    } else {
      chart.getStyleManager().setChartTitleVisible(true);
    }
    this.text = text;
  }

  /**
   * get the height of the chart title including the chart padding
   * 
   * @return
   */
  protected int getSizeHint() {

    if (chart.getStyleManager().isChartTitleVisible()) {

      TextLayout textLayout = new TextLayout(text, chart.getStyleManager().getChartTitleFont(), new FontRenderContext(null, true, false));
      Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
      int titleHeight = (int) ((chart.getStyleManager().isChartTitleVisible() ? rectangle.getHeight() : 0));
      return chart.getStyleManager().getChartPadding() + 2 * chart.getStyleManager().getChartTitlePadding() + titleHeight;
    } else {
      return chart.getStyleManager().getChartPadding();
    }
  }

  @Override
  public void paint(Graphics2D g) {

    // bounds = new Rectangle();
    g.setFont(chart.getStyleManager().getChartTitleFont());

    if (chart.getStyleManager().isChartTitleVisible()) {

      // create rectangle first for sizing
      FontRenderContext frc = g.getFontRenderContext();
      TextLayout textLayout = new TextLayout(text, chart.getStyleManager().getChartTitleFont(), frc);
      Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);

      // paint the chart title box
      int chartTitleBoxWidth = (int) chart.getPlot().getBounds().getWidth();
      int chartTitleBoxHeight = (int) (rectangle.getHeight() + 2 * chart.getStyleManager().getChartTitlePadding());
      int xOffset = (int) chart.getPlot().getBounds().getX();
      int yOffset = chart.getStyleManager().getChartPadding();

      g.setColor(chart.getStyleManager().getChartTitleBorderColor());
      g.drawRect(xOffset - 1, yOffset, chartTitleBoxWidth - 1, chartTitleBoxHeight - 1);
      g.setColor(chart.getStyleManager().getChartTitleBackgroundColor());
      g.fillRect(xOffset - 1, yOffset + 1, chartTitleBoxWidth, chartTitleBoxHeight - 1);

      // paint title
      xOffset = (int) (chart.getPlot().getBounds().getX() + (chart.getPlot().getBounds().getWidth() - rectangle.getWidth()) / 2.0);
      yOffset = (int) (chart.getStyleManager().getChartPadding() - rectangle.getY() + chart.getStyleManager().getChartTitlePadding());

      // bounds = new Rectangle(xOffset, yOffset + ((int) rectangle.getY()), (int) rectangle.getWidth(), (int) (rectangle.getHeight()));
      // g.setColor(Color.green);
      // g.draw(bounds);

      g.setColor(chart.getStyleManager().getChartFontColor());
      textLayout.draw(g, xOffset, yOffset);
    }

  }

  @Override
  public Rectangle getBounds() {

    return null; // this should never be needed
  }

  @Override
  public Chart getChart() {

    return chart;
  }
}