/**
 * Copyright 2011 Xeiam LLC.
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import com.xeiam.xchart.interfaces.IChartPart;


/**
 * Axis tick marks.
 */
public class AxisTickMarks implements IChartPart {

  /** the axis */
  private Axis axis;

  private AxisTick axisTick;

  /** the foreground color */
  private Color foreground = ChartColor.getAWTColor(ChartColor.DARK_GREY);// default foreground color

  /** the line style */
  private Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

  /** the tick length */
  public static final int TICK_LENGTH = 3;

  /** the bounds */
  private Rectangle bounds = new Rectangle(); // default all-zero rectangle

  /**
   * Constructor
   * 
   * @param axis
   * @param axisTick
   */
  public AxisTickMarks(Axis axis, AxisTick axisTick) {

    this.axis = axis;
    this.axisTick = axisTick;
  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    g.setColor(foreground);

    if (axis.getDirection() == Axis.Direction.Y) { // Y-Axis

      int xOffset = (int) (axisTick.getAxisTickLabels().getBounds().getX() + axisTick.getAxisTickLabels().getBounds().getWidth() + AxisTick.AXIS_TICK_PADDING);
      int yOffset = (int) (axis.getPaintZone().getY());
      for (int i = 0; i < axisTick.getTickLabels().size(); i++) {

        int tickLocation = axisTick.getTickLocations().get(i);

        g.setColor(foreground);
        g.setStroke(stroke);

        g.drawLine(xOffset, yOffset + (int) (axis.getPaintZone().getHeight() - tickLocation), xOffset + TICK_LENGTH, yOffset + (int) (axis.getPaintZone().getHeight() - tickLocation));

      }

      // bounds
      bounds = new Rectangle(xOffset, yOffset, TICK_LENGTH, (int) axis.getPaintZone().getHeight());
      // g.setColor(Color.blue);
      // g.draw(bounds);

    } else { // X-Axis

      int xOffset = (int) (axis.getPaintZone().getX());
      int yOffset = (int) (axisTick.getAxisTickLabels().getBounds().getY() - AxisTick.AXIS_TICK_PADDING);
      for (int i = 0; i < axisTick.getTickLabels().size(); i++) {

        int tickLocation = axisTick.getTickLocations().get(i);

        g.setColor(foreground);
        g.setStroke(stroke);

        g.drawLine(xOffset + tickLocation, yOffset, xOffset + tickLocation, yOffset - TICK_LENGTH);
      }

      // bounds
      bounds = new Rectangle(xOffset, yOffset - TICK_LENGTH, (int) axis.getPaintZone().getWidth(), TICK_LENGTH);
      // g.setColor(Color.blue);
      // g.draw(bounds);
    }
  }

}
