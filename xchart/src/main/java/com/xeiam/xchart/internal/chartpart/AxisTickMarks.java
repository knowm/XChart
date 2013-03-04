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

/**
 * Axis tick marks.
 */
public class AxisTickMarks implements ChartPart {

  /** parent */
  private AxisTick axisTick;

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

    g.setColor(getChartPainter().getStyleManager().getAxisTickMarksColor());
    g.setStroke(getChartPainter().getStyleManager().getAxisTickMarksStroke());

    if (axisTick.getAxis().getDirection() == Axis.Direction.Y) { // Y-Axis

      int xOffset = (int) (axisTick.getAxisTickLabels().getBounds().getX() + axisTick.getAxisTickLabels().getBounds().getWidth() + getChartPainter().getStyleManager().getAxisTickPadding());
      int yOffset = (int) (axisTick.getAxis().getPaintZone().getY());

      // tick marks
      for (int i = 0; i < axisTick.getTickLabels().size(); i++) {

        int tickLocation = axisTick.getTickLocations().get(i);

        // g.setColor(getChart().getStyleManager().getChartBordersColor());
        // g.setStroke(stroke);

        g.drawLine(xOffset, yOffset + (int) (axisTick.getAxis().getPaintZone().getHeight() - tickLocation), xOffset + getChartPainter().getStyleManager().getAxisTickMarkLength(), yOffset
            + (int) (axisTick.getAxis().getPaintZone().getHeight() - tickLocation));

      }

      // Line
      if (getChartPainter().getStyleManager().isAxisTicksLineVisible()) {
        g.drawLine(xOffset + getChartPainter().getStyleManager().getAxisTickMarkLength(), yOffset, xOffset + getChartPainter().getStyleManager().getAxisTickMarkLength(), yOffset
            + (int) axisTick.getAxis().getPaintZone().getHeight());
      }

      // bounds
      bounds = new Rectangle(xOffset, yOffset, getChartPainter().getStyleManager().getAxisTickMarkLength(), (int) axisTick.getAxis().getPaintZone().getHeight());
      // g.setColor(Color.yellow);
      // g.draw(bounds);

    } else { // X-Axis

      int xOffset = (int) (axisTick.getAxis().getPaintZone().getX());
      // int yOffset = (int) (axisTick.getAxisTickLabels().getBounds().getY() - getChart().getStyleManager().getAxisTickPadding());
      int yOffset = (int) (axisTick.getAxisTickLabels().getBounds().getY() - getChartPainter().getStyleManager().getAxisTickPadding());

      // tick marks
      for (int i = 0; i < axisTick.getTickLabels().size(); i++) {

        int tickLocation = axisTick.getTickLocations().get(i);

        // g.setColor(getChart().getStyleManager().getChartBordersColor());
        // g.setStroke(stroke);

        g.drawLine(xOffset + tickLocation, yOffset, xOffset + tickLocation, yOffset - getChartPainter().getStyleManager().getAxisTickMarkLength());
      }

      // Line
      if (getChartPainter().getStyleManager().isAxisTicksLineVisible()) {

        g.drawLine(xOffset, yOffset - getChartPainter().getStyleManager().getAxisTickMarkLength(), xOffset + (int) axisTick.getAxis().getPaintZone().getWidth(), yOffset
            - getChartPainter().getStyleManager().getAxisTickMarkLength());
      }

      // bounds
      bounds = new Rectangle(xOffset, yOffset - getChartPainter().getStyleManager().getAxisTickMarkLength(), (int) axisTick.getAxis().getPaintZone().getWidth(), getChartPainter().getStyleManager()
          .getAxisTickMarkLength());
      // g.setColor(Color.yellow);
      // g.draw(bounds);
    }
  }

  @Override
  public ChartPainter getChartPainter() {

    return axisTick.getChartPainter();
  }
}
