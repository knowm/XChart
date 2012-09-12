/**
 * Copyright 2011-2012 Xeiam LLC.
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
package com.xeiam.xchart;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import com.xeiam.xchart.interfaces.IHideable;

/**
 * Chart Title
 */
public class ChartTitle implements IHideable {

  /** parent */
  private Chart chart;

  /** the title text */
  protected String text = ""; // default to ""

  /** the visibility state of title */
  protected boolean isVisible = false; // default to false

  /** the font */
  protected Font font;

  /** the bounds */
  private Rectangle bounds;

  /**
   * Constructor
   * 
   * @param chart
   */
  public ChartTitle(Chart chart) {

    this.chart = chart;
    font = new Font(Font.SANS_SERIF, Font.BOLD, 14); // default font
  }

  protected void setText(String text) {

    if (text.trim().equalsIgnoreCase("")) {
      this.isVisible = false;
    } else {
      this.isVisible = true;
    }
    this.text = text;
  }

  @Override
  public void setVisible(boolean isVisible) {

    this.isVisible = isVisible;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle();

    if (isVisible) {

      FontRenderContext frc = g.getFontRenderContext();
      TextLayout textLayout = new TextLayout(text, font, frc);
      Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
      int xOffset = (int) ((chart.width - rectangle.getWidth()) / 2.0);
      int yOffset = (int) ((isVisible ? (Chart.CHART_PADDING - rectangle.getY()) : 0));

      bounds = new Rectangle(xOffset, yOffset + (isVisible ? (int) rectangle.getY() : 0), (int) rectangle.getWidth(), (int) (isVisible ? rectangle.getHeight() : 0));
      // g.setColor(Color.green);
      // g.draw(bounds);

      g.setColor(chart.fontColor);
      textLayout.draw(g, xOffset, yOffset);
    }

  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }
}