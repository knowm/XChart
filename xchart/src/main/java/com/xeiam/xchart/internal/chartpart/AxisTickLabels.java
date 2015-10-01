/**
 * Copyright 2011 - 2015 Xeiam LLC.
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
import java.util.HashMap;
import java.util.Map;

/**
 * Axis tick labels
 */
public class AxisTickLabels implements ChartPart {

  /** parent */
  private final AxisTick axisTick;

  /** the bounds */
  private Rectangle2D bounds = new Rectangle2D.Double();

  /**
   * Constructor
   *
   * @param axisTick
   */
  protected AxisTickLabels(AxisTick axisTick) {

    this.axisTick = axisTick;
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    g.setFont(getChartPainter().getStyleManager().getAxisTickLabelsFont());

    g.setColor(getChartPainter().getStyleManager().getAxisTickLabelsColor());

    if (axisTick.getAxis().getDirection() == Axis.Direction.Y && getChartPainter().getStyleManager().isYAxisTicksVisible()) { // Y-Axis

      double xWidth = axisTick.getAxis().getAxisTitle().getBounds().getWidth();
      double xOffset = axisTick.getAxis().getAxisTitle().getBounds().getX() + xWidth;
      double yOffset = axisTick.getAxis().getPaintZone().getY();
      double height = axisTick.getAxis().getPaintZone().getHeight();
      double maxTickLabelWidth = 0;
      Map<Double, TextLayout> axisLabelTextLayouts = new HashMap<Double, TextLayout>();

      for (int i = 0; i < axisTick.getTickLabels().size(); i++) {

        String tickLabel = axisTick.getTickLabels().get(i);
        // System.out.println("** " + tickLabel);
        double tickLocation = axisTick.getTickLocations().get(i);
        double flippedTickLocation = yOffset + height - tickLocation;

        if (tickLabel != null && flippedTickLocation > yOffset && flippedTickLocation < yOffset + height) { // some are null for logarithmic axes
          FontRenderContext frc = g.getFontRenderContext();
          TextLayout axisLabelTextLayout = new TextLayout(tickLabel, getChartPainter().getStyleManager().getAxisTickLabelsFont(), frc);
          Rectangle2D tickLabelBounds = axisLabelTextLayout.getBounds();
          double boundWidth = tickLabelBounds.getWidth();
          if (boundWidth > maxTickLabelWidth) {
            maxTickLabelWidth = boundWidth;
          }
          axisLabelTextLayouts.put(tickLocation, axisLabelTextLayout);
        }
      }

      for (Double tickLocation : axisLabelTextLayouts.keySet()) {

        TextLayout axisLabelTextLayout = axisLabelTextLayouts.get(tickLocation);
        Shape shape = axisLabelTextLayout.getOutline(null);
        Rectangle2D tickLabelBounds = shape.getBounds();

        double flippedTickLocation = yOffset + height - tickLocation;

        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();
        double boundWidth = tickLabelBounds.getWidth();
        double xPos;
        switch (getChartPainter().getStyleManager().getYAxisLabelAlignment()) {
        case Right:
          xPos = xOffset + maxTickLabelWidth - boundWidth;
          break;
        case Centre:
          xPos = xOffset + (maxTickLabelWidth - boundWidth) / 2;
          break;
        case Left:
        default:
          xPos = xOffset;
        }
        at.translate(xPos, flippedTickLocation + tickLabelBounds.getHeight() / 2.0);
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);

      }

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset, maxTickLabelWidth, height);
      // g.setColor(Color.blue);
      // g.draw(bounds);

    }
    // X-Axis
    else if (axisTick.getAxis().getDirection() == Axis.Direction.X && getChartPainter().getStyleManager().isXAxisTicksVisible()) {

      double xOffset = axisTick.getAxis().getPaintZone().getX();
      double yOffset = axisTick.getAxis().getAxisTitle().getBounds().getY();
      double width = axisTick.getAxis().getPaintZone().getWidth();
      double maxTickLabelHeight = 0;

      // System.out.println("axisTick.getTickLabels().size(): " + axisTick.getTickLabels().size());
      for (int i = 0; i < axisTick.getTickLabels().size(); i++) {

        String tickLabel = axisTick.getTickLabels().get(i);
        // System.out.println("tickLabel: " + tickLabel);
        double tickLocation = axisTick.getTickLocations().get(i);
        double shiftedTickLocation = xOffset + tickLocation;

        // discard null and out of bounds labels
        if (tickLabel != null && shiftedTickLocation > xOffset && shiftedTickLocation < xOffset + width) { // some are null for logarithmic axes

          FontRenderContext frc = g.getFontRenderContext();
          TextLayout textLayout = new TextLayout(tickLabel, getChartPainter().getStyleManager().getAxisTickLabelsFont(), frc);
          // System.out.println(textLayout.getOutline(null).getBounds().toString());

          // Shape shape = v.getOutline();
          AffineTransform rot = AffineTransform.getRotateInstance(-1 * Math.toRadians(getChartPainter().getStyleManager().getXAxisLabelRotation()), 0, 0);
          Shape shape = textLayout.getOutline(rot);
          Rectangle2D tickLabelBounds = shape.getBounds2D();

          AffineTransform orig = g.getTransform();
          AffineTransform at = new AffineTransform();
          double xPos;
          switch (getChartPainter().getStyleManager().getXAxisLabelAlignment()) {
          case Left:
            xPos = shiftedTickLocation;
            break;
          case Right:
            xPos = shiftedTickLocation - tickLabelBounds.getWidth();
            break;
          case Centre:
          default:
            xPos = shiftedTickLocation - tickLabelBounds.getWidth() / 2.0;
          }
          // System.out.println("tickLabelBounds: " + tickLabelBounds.toString());
          double shiftX = -1 * tickLabelBounds.getX() * Math.sin(Math.toRadians(getChartPainter().getStyleManager().getXAxisLabelRotation()));
          double shiftY = -1 * (tickLabelBounds.getY() + tickLabelBounds.getHeight());
          // System.out.println(shiftX);
          // System.out.println("shiftY: " + shiftY);
          at.translate(xPos + shiftX, yOffset + shiftY);

          g.transform(at);
          g.fill(shape);
          g.setTransform(orig);

          // // debug box
          // g.setColor(Color.MAGENTA);
          // g.draw(new Rectangle2D.Double(xPos, yOffset - tickLabelBounds.getHeight(), tickLabelBounds.getWidth(), tickLabelBounds.getHeight()));
          // g.setColor(getChartPainter().getStyleManager().getAxisTickLabelsColor());

          if (tickLabelBounds.getHeight() > maxTickLabelHeight) {
            maxTickLabelHeight = tickLabelBounds.getHeight();
          }
        }
        // else {
        // System.out.println("discarding: " + tickLabel);
        // }
      }

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset - maxTickLabelHeight, width, maxTickLabelHeight);
      // g.setColor(Color.blue);
      // g.draw(bounds);

    }

  }

  @Override
  public ChartPainter getChartPainter() {

    return axisTick.getChartPainter();
  }
}
