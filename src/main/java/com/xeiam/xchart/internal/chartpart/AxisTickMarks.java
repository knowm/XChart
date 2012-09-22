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
package com.xeiam.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import com.xeiam.xchart.internal.interfaces.IChartPart;

/**
 * Axis tick marks.
 */
public class AxisTickMarks implements IChartPart {

  /** the tick length */
  public static final int TICK_LENGTH = 3;

  /** parent */
  private AxisTick axisTick;

  /** the line style */
  private Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

  /** the bounds */
  private Rectangle bounds;

  /**
   * Constructor
   * 
   * @param axisTick
   */
  protected AxisTickMarks(AxisTick axisTick) {

    this.axisTick = axisTick;
  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle();

    g.setColor(axisTick.axis.axisPair.chart.bordersColor);

    if (axisTick.axis.direction == Axis.Direction.Y) { // Y-Axis

      int xOffset = (int) (axisTick.axisTickLabels.getBounds().getX() + axisTick.axisTickLabels.getBounds().getWidth() + AxisTick.AXIS_TICK_PADDING);
      int yOffset = (int) (axisTick.axis.getPaintZone().getY());

      // tick marks
      for (int i = 0; i < axisTick.tickLabels.size(); i++) {

        int tickLocation = axisTick.tickLocations.get(i);

        g.setColor(axisTick.axis.axisPair.chart.bordersColor);
        g.setStroke(stroke);

        g.drawLine(xOffset, yOffset + (int) (axisTick.axis.getPaintZone().getHeight() - tickLocation), xOffset + TICK_LENGTH, yOffset + (int) (axisTick.axis.getPaintZone().getHeight() - tickLocation));

      }
      // Line
      g.drawLine(xOffset + TICK_LENGTH, yOffset, xOffset + TICK_LENGTH, yOffset + (int) axisTick.axis.getPaintZone().getHeight());

      // bounds
      bounds = new Rectangle(xOffset, yOffset, TICK_LENGTH, (int) axisTick.axis.getPaintZone().getHeight());
      // g.setColor(Color.yellow);
      // g.draw(bounds);

    } else { // X-Axis

      int xOffset = (int) (axisTick.axis.getPaintZone().getX());
      int yOffset = (int) (axisTick.axisTickLabels.getBounds().getY() - AxisTick.AXIS_TICK_PADDING);

      // tick marks
      for (int i = 0; i < axisTick.tickLabels.size(); i++) {

        int tickLocation = axisTick.tickLocations.get(i);

        g.setColor(axisTick.axis.axisPair.chart.bordersColor);
        g.setStroke(stroke);

        g.drawLine(xOffset + tickLocation, yOffset, xOffset + tickLocation, yOffset - TICK_LENGTH);
      }
      // Line
      g.drawLine(xOffset, yOffset - TICK_LENGTH, xOffset + (int) axisTick.axis.getPaintZone().getWidth(), yOffset - TICK_LENGTH);

      // bounds
      bounds = new Rectangle(xOffset, yOffset - TICK_LENGTH, (int) axisTick.axis.getPaintZone().getWidth(), TICK_LENGTH);
      // g.setColor(Color.yellow);
      // g.draw(bounds);
    }
  }
}
