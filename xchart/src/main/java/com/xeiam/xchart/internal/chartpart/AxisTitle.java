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

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * AxisTitle
 */
public class AxisTitle implements ChartPart {

  /** parent */
  private final Axis axis;

  /** the title text */
  private String text = ""; // default to ""

  /** the bounds */
  private Rectangle2D bounds;

  /**
   * Constructor
   * 
   * @param axis the axis
   */
  protected AxisTitle(Axis axis) {

    this.axis = axis;
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle2D.Double();

    g.setColor(getChartPainter().getStyleManager().getChartFontColor());
    g.setFont(getChartPainter().getStyleManager().getAxisTitleFont());

    if (axis.getDirection() == Axis.Direction.Y) {

      if (text != null && !text.trim().equalsIgnoreCase("") && getChartPainter().getStyleManager().isYAxisTitleVisible()) {

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout nonRotatedTextLayout = new TextLayout(text, getChartPainter().getStyleManager().getAxisTitleFont(), frc);
        Rectangle2D nonRotatedRectangle = nonRotatedTextLayout.getBounds();

        // ///////////////////////////////////////////////

        int xOffset = (int) (axis.getPaintZone().getX() + nonRotatedRectangle.getHeight());
        int yOffset = (int) ((axis.getPaintZone().getHeight() + nonRotatedRectangle.getWidth()) / 2.0 + axis.getPaintZone().getY());
        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();
        at.rotate(Math.PI / -2.0, xOffset, yOffset);
        g.transform(at);
        g.drawString(text, xOffset, yOffset);
        g.setTransform(orig);

        // ///////////////////////////////////////////////
        // System.out.println(nonRotatedRectangle.getHeight());

        // bounds
        bounds =
            new Rectangle2D.Double(xOffset - nonRotatedRectangle.getHeight(), yOffset - nonRotatedRectangle.getWidth(), nonRotatedRectangle.getHeight()
                + getChartPainter().getStyleManager().getAxisTitlePadding(), nonRotatedRectangle.getWidth());
        // g.setColor(Color.blue);
        // g.draw(bounds);
      }
      else {
        bounds = new Rectangle2D.Double(axis.getPaintZone().getX(), axis.getPaintZone().getY(), 0, axis.getPaintZone().getHeight());
      }

    }
    else {

      if (text != null && !text.trim().equalsIgnoreCase("") && getChartPainter().getStyleManager().isXAxisTitleVisible()) {

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(text, getChartPainter().getStyleManager().getAxisTitleFont(), frc);
        Rectangle2D rectangle = textLayout.getBounds();
        // System.out.println(rectangle);

        double xOffset = axis.getPaintZone().getX() + (axis.getPaintZone().getWidth() - rectangle.getWidth()) / 2.0;
        double yOffset = axis.getPaintZone().getY() + axis.getPaintZone().getHeight() - rectangle.getHeight();

        // textLayout.draw(g, (float) xOffset, (float) (yOffset - rectangle.getY()));
        Shape shape = textLayout.getOutline(null);
        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();
        at.translate((float) xOffset, (float) (yOffset - rectangle.getY()));
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);

        bounds =
            new Rectangle2D.Double(xOffset, yOffset - getChartPainter().getStyleManager().getAxisTitlePadding(), rectangle.getWidth(), rectangle.getHeight()
                + getChartPainter().getStyleManager().getAxisTitlePadding());
        // g.setColor(Color.blue);
        // g.draw(bounds);

      }
      else {
        bounds = new Rectangle2D.Double(axis.getPaintZone().getX(), axis.getPaintZone().getY() + axis.getPaintZone().getHeight(), axis.getPaintZone().getWidth(), 0);
        // g.setColor(Color.blue);
        // g.draw(bounds);

      }
    }
  }

  @Override
  public ChartPainter getChartPainter() {

    return axis.getChartPainter();
  }

  // Getters /////////////////////////////////////////////////

  public String getText() {

    return text;
  }

  public void setText(String text) {

    this.text = text;
  }
}
