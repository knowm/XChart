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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import com.xeiam.xchart.internal.interfaces.IChartPart;

/**
 * Axis tick marks.
 */
public class AxisTickMarks implements IChartPart {

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

    g.setColor(axisTick.axis.axisPair.chart.getStyleManager().getChartBordersColor());

    if (axisTick.axis.direction == Axis.Direction.Y) { // Y-Axis

      int xOffset = (int) (axisTick.axisTickLabels.getBounds().getX() + axisTick.axisTickLabels.getBounds().getWidth() + axisTick.axis.axisPair.chart.getStyleManager().getAxisTickPadding());
      int yOffset = (int) (axisTick.axis.getPaintZone().getY());

      // tick marks
      for (int i = 0; i < axisTick.tickLabels.size(); i++) {

        int tickLocation = axisTick.tickLocations.get(i);

        g.setColor(axisTick.axis.axisPair.chart.getStyleManager().getChartBordersColor());
        g.setStroke(stroke);

        g.drawLine(xOffset, yOffset + (int) (axisTick.axis.getPaintZone().getHeight() - tickLocation), xOffset + axisTick.axis.axisPair.chart.getStyleManager().getAxisTickMarkLength(), yOffset
            + (int) (axisTick.axis.getPaintZone().getHeight() - tickLocation));

      }
      // Line
      if (axisTick.axis.axisPair.chart.getStyleManager().isAxisTicksLineVisible()) {
        g.drawLine(xOffset + axisTick.axis.axisPair.chart.getStyleManager().getAxisTickMarkLength(), yOffset, xOffset + axisTick.axis.axisPair.chart.getStyleManager().getAxisTickMarkLength(), yOffset
            + (int) axisTick.axis.getPaintZone().getHeight());
      }
      // bounds
      bounds = new Rectangle(xOffset, yOffset, axisTick.axis.axisPair.chart.getStyleManager().getAxisTickMarkLength(), (int) axisTick.axis.getPaintZone().getHeight());
      // g.setColor(Color.yellow);
      // g.draw(bounds);

    } else { // X-Axis

      int xOffset = (int) (axisTick.axis.getPaintZone().getX());
      int yOffset = (int) (axisTick.axisTickLabels.getBounds().getY() - axisTick.axis.axisPair.chart.getStyleManager().getAxisTickPadding());

      // tick marks
      for (int i = 0; i < axisTick.tickLabels.size(); i++) {

        int tickLocation = axisTick.tickLocations.get(i);

        g.setColor(axisTick.axis.axisPair.chart.getStyleManager().getChartBordersColor());
        g.setStroke(stroke);

        g.drawLine(xOffset + tickLocation, yOffset, xOffset + tickLocation, yOffset - axisTick.axis.axisPair.chart.getStyleManager().getAxisTickMarkLength());
      }

      // Line
      if (axisTick.axis.axisPair.chart.getStyleManager().isAxisTicksLineVisible()) {

        g.drawLine(xOffset, yOffset - axisTick.axis.axisPair.chart.getStyleManager().getAxisTickMarkLength(), xOffset + (int) axisTick.axis.getPaintZone().getWidth(), yOffset
            - axisTick.axis.axisPair.chart.getStyleManager().getAxisTickMarkLength());
      }

      // bounds
      bounds = new Rectangle(xOffset, yOffset - axisTick.axis.axisPair.chart.getStyleManager().getAxisTickMarkLength(), (int) axisTick.axis.getPaintZone().getWidth(), axisTick.axis.axisPair.chart
          .getStyleManager().getAxisTickMarkLength());
      // g.setColor(Color.yellow);
      // g.draw(bounds);
    }
  }
}
