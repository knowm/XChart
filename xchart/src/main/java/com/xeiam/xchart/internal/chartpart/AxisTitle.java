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
import java.awt.geom.AffineTransform;

import com.xeiam.xchart.Chart;

/**
 * AxisTitle
 */
public class AxisTitle implements ChartPart {

  /** parent */
  private final Axis axis;

  /** the title text */
  public String text = ""; // default to ""

  /** the bounds */
  private Rectangle bounds;

  /**
   * Constructor
   * 
   * @param axis the axis
   */
  protected AxisTitle(Axis axis, boolean isVisible) {

    this.axis = axis;
  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle();

    g.setColor(getChart().getStyleManager().getChartFontColor());
    g.setFont(getChart().getStyleManager().getAxisTitleFont());

    if (axis.direction == Axis.Direction.Y) {
      if (getChart().getStyleManager().isyAxisTitleVisible()) {

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout nonRotatedTextLayout = new TextLayout(text, getChart().getStyleManager().getAxisTitleFont(), frc);
        Rectangle nonRotatedRectangle = nonRotatedTextLayout.getPixelBounds(null, 0, 0);
        // System.out.println(nonRotatedRectangle);

        // ///////////////////////////////////////////////

        // AffineTransform at = new AffineTransform();
        // // Tx.translate(anchorx, anchory); // S3: final translation
        // double theta = Math.PI / -2.0;
        // at.rotate(theta); // S2: rotate around anchor
        // // Tx.translate(-anchorx, -anchory); // S1: translate anchor to origin
        // Font derivedFont = font.deriveFont(at);
        // TextLayout rotatedTextLayout = new TextLayout(text, derivedFont, frc);
        // // TextLayout rotatedTextLayout = new TextLayout(text, font.deriveFont(AffineTransform.getRotateInstance(Math.PI / -2.0, 0, 0)), frc);
        // // Rectangle rotatedRectangle = rotatedTextLayout.getPixelBounds(null, 0, 0);
        // // System.out.println(rotatedRectangle);
        //
        int xOffset = (int) (axis.getPaintZone().getX() + nonRotatedRectangle.getHeight());
        int yOffset = (int) ((axis.getPaintZone().getHeight() + nonRotatedRectangle.getWidth()) / 2.0 + axis.getPaintZone().getY());
        AffineTransform orig = g.getTransform();
        g.transform(AffineTransform.getRotateInstance(Math.PI / -2.0, xOffset, yOffset));
        g.drawString(text, xOffset, yOffset);
        // rotatedTextLayout.draw(g, xOffset, yOffset);

        // ///////////////////////////////////////////////
        g.setTransform(orig);

        // bounds
        bounds = new Rectangle((int) (xOffset - nonRotatedRectangle.getHeight()), (int) (yOffset - nonRotatedRectangle.getWidth()), (int) nonRotatedRectangle.getHeight()
            + getChart().getStyleManager().getAxisTitlePadding(), (int) nonRotatedRectangle.getWidth());
        // g.setColor(Color.blue);
        // g.draw(bounds);
      } else {
        bounds = new Rectangle((int) axis.getPaintZone().getX(), (int) axis.getPaintZone().getY(), 0, (int) axis.getPaintZone().getHeight());
      }

    } else {

      if (getChart().getStyleManager().isxAxisTitleVisible()) {

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(text, getChart().getStyleManager().getAxisTitleFont(), frc);
        Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
        // System.out.println(rectangle);

        int xOffset = (int) (axis.getPaintZone().getX() + (axis.getPaintZone().getWidth() - rectangle.getWidth()) / 2.0);
        int yOffset = (int) (axis.getPaintZone().getY() + axis.getPaintZone().getHeight() - rectangle.getHeight());

        textLayout.draw(g, xOffset, (float) (yOffset - rectangle.getY()));

        bounds = new Rectangle(xOffset, yOffset - getChart().getStyleManager().getAxisTitlePadding(), (int) rectangle.getWidth(), (int) rectangle.getHeight()
            + getChart().getStyleManager().getAxisTitlePadding());
        // g.setColor(Color.blue);
        // g.draw(bounds);

      } else {
        bounds = new Rectangle((int) axis.getPaintZone().getX(), (int) (axis.getPaintZone().getY() + axis.getPaintZone().getHeight()), (int) axis.getPaintZone().getWidth(), 0);
        // g.setColor(Color.blue);
        // g.draw(bounds);

      }
    }
  }

  @Override
  public Chart getChart() {

    return axis.getChart();
  }
}
