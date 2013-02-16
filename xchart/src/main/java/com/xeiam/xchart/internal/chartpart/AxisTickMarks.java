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

import com.xeiam.xchart.Chart;

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

    g.setColor(getChart().getStyleManager().getAxisTickMarksColor());
    g.setStroke(getChart().getStyleManager().getAxisTickMarksStroke());

    if (axisTick.getAxis().getDirection() == Axis.Direction.Y) { // Y-Axis

      int xOffset = (int) (axisTick.getAxisTickLabels().getBounds().getX() + axisTick.getAxisTickLabels().getBounds().getWidth() + getChart().getStyleManager().getAxisTickPadding());
      int yOffset = (int) (axisTick.getAxis().getPaintZone().getY());

      // tick marks
      for (int i = 0; i < axisTick.getTickLabels().size(); i++) {

        int tickLocation = axisTick.getTickLocations().get(i);

        // g.setColor(getChart().getStyleManager().getChartBordersColor());
        // g.setStroke(stroke);

        g.drawLine(xOffset, yOffset + (int) (axisTick.getAxis().getPaintZone().getHeight() - tickLocation), xOffset + getChart().getStyleManager().getAxisTickMarkLength(), yOffset
            + (int) (axisTick.getAxis().getPaintZone().getHeight() - tickLocation));

      }

      // Line
      if (getChart().getStyleManager().isAxisTicksLineVisible()) {
        g.drawLine(xOffset + getChart().getStyleManager().getAxisTickMarkLength(), yOffset, xOffset + getChart().getStyleManager().getAxisTickMarkLength(), yOffset
            + (int) axisTick.getAxis().getPaintZone().getHeight());
      }

      // bounds
      bounds = new Rectangle(xOffset, yOffset, getChart().getStyleManager().getAxisTickMarkLength(), (int) axisTick.getAxis().getPaintZone().getHeight());
      // g.setColor(Color.yellow);
      // g.draw(bounds);

    } else { // X-Axis

      int xOffset = (int) (axisTick.getAxis().getPaintZone().getX());
      // int yOffset = (int) (axisTick.getAxisTickLabels().getBounds().getY() - getChart().getStyleManager().getAxisTickPadding());
      int yOffset = (int) (axisTick.getAxisTickLabels().getBounds().getY() - getChart().getStyleManager().getAxisTickPadding());

      // tick marks
      for (int i = 0; i < axisTick.getTickLabels().size(); i++) {

        int tickLocation = axisTick.getTickLocations().get(i);

        // g.setColor(getChart().getStyleManager().getChartBordersColor());
        // g.setStroke(stroke);

        g.drawLine(xOffset + tickLocation, yOffset, xOffset + tickLocation, yOffset - getChart().getStyleManager().getAxisTickMarkLength());
      }

      // Line
      if (getChart().getStyleManager().isAxisTicksLineVisible()) {

        g.drawLine(xOffset, yOffset - getChart().getStyleManager().getAxisTickMarkLength(), xOffset + (int) axisTick.getAxis().getPaintZone().getWidth(), yOffset
            - getChart().getStyleManager().getAxisTickMarkLength());
      }

      // bounds
      bounds = new Rectangle(xOffset, yOffset - getChart().getStyleManager().getAxisTickMarkLength(), (int) axisTick.getAxis().getPaintZone().getWidth(), getChart().getStyleManager()
          .getAxisTickMarkLength());
      // g.setColor(Color.yellow);
      // g.draw(bounds);
    }
  }

  @Override
  public Chart getChart() {

    return axisTick.getChart();
  }
}
