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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import com.xeiam.xchart.interfaces.IHideable;

/**
 * AxisTitle
 */
public class AxisTitle implements IHideable {

  /** the chart */
  private Axis axis;

  /** the title text */
  protected String text = ""; // default to ""

  /** the visibility state of title */
  protected boolean isVisible = false; // default to false

  /** the font */
  private Font font = new Font(Font.SANS_SERIF, Font.BOLD, 12); // default font

  /** the foreground color */
  private Color foreground = ChartColor.getAWTColor(ChartColor.DARK_GREY); // default foreground color

  /** the bounds */
  private Rectangle bounds;

  protected final static int AXIS_TITLE_PADDING = 10;

  /**
   * Constructor.
   * 
   * @param axis the axis
   */
  public AxisTitle(Axis axis) {

    this.axis = axis;
  }

  protected String getText() {

    return text;
  }

  protected void setText(String text) {

    if (text.trim().equalsIgnoreCase("")) {
      this.isVisible = false;
    } else {
      this.isVisible = true;
    }
    this.text = text;
  }

  protected Font getFont() {

    return font;
  }

  @Override
  public void setVisible(boolean isVisible) {

    this.isVisible = isVisible;
  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle();

    g.setColor(foreground);

    if (axis.getDirection() == Axis.Direction.Y) {
      if (isVisible) {

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout nonRotatedTextLayout = new TextLayout(text, font, frc);
        Rectangle nonRotatedRectangle = nonRotatedTextLayout.getPixelBounds(null, 0, 0);
        // System.out.println(nonRotatedRectangle);

        TextLayout rotatedTextLayout = new TextLayout(text, font.deriveFont(AffineTransform.getRotateInstance(Math.PI / -2.0, 0, 0)), frc);
        // Rectangle rotatedRectangle = rotatedTextLayout.getPixelBounds(null, 0, 0);
        // System.out.println(rotatedRectangle);

        int xOffset = (int) (axis.getPaintZone().getX() + nonRotatedRectangle.getHeight());
        int yOffset = (int) ((axis.getPaintZone().getHeight() + nonRotatedRectangle.getWidth()) / 2.0 + axis.getPaintZone().getY());
        rotatedTextLayout.draw(g, xOffset, yOffset);

        // bounds
        bounds = new Rectangle((int) (xOffset - nonRotatedRectangle.getHeight()), (int) (yOffset - nonRotatedRectangle.getWidth()), (int) nonRotatedRectangle.getHeight() + AXIS_TITLE_PADDING,
            (int) nonRotatedRectangle.getWidth());
        // g.setColor(Color.blue);
        // g.draw(bounds);
      } else {
        bounds = new Rectangle((int) axis.getPaintZone().getX(), (int) axis.getPaintZone().getY(), 0, (int) axis.getPaintZone().getHeight());
      }

    } else {

      if (isVisible) {

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(text, font, frc);
        Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
        // System.out.println(rectangle);

        int xOffset = (int) (axis.getPaintZone().getX() + (axis.getPaintZone().getWidth() - rectangle.getWidth()) / 2.0);
        int yOffset = (int) (axis.getPaintZone().getY() + axis.getPaintZone().getHeight() - rectangle.getHeight());

        textLayout.draw(g, xOffset, (float) (yOffset - rectangle.getY()));

        bounds = new Rectangle(xOffset, yOffset - AXIS_TITLE_PADDING, (int) rectangle.getWidth(), (int) rectangle.getHeight() + AXIS_TITLE_PADDING);
        // g.setColor(Color.blue);
        // g.draw(bounds);

      } else {
        bounds = new Rectangle((int) axis.getPaintZone().getX(), (int) (axis.getPaintZone().getY() + axis.getPaintZone().getHeight()), (int) axis.getPaintZone().getWidth(), 0);
        // g.setColor(Color.blue);
        // g.draw(bounds);

      }
    }
  }
}
