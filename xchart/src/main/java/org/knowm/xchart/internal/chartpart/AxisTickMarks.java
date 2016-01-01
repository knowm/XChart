/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
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
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Axis tick marks. This includes the little tick marks and the line that hugs the plot area.
 */
public class AxisTickMarks implements ChartPart {

  /** parent */
  private AxisTick axisTick;

  /** the bounds */
  private Rectangle2D bounds = new Rectangle2D.Double();

  /**
   * Constructor
   *
   * @param axisTick
   */
  protected AxisTickMarks(AxisTick axisTick) {

    this.axisTick = axisTick;
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    g.setColor(getChartInternal().getStyleManager().getAxisTickMarksColor());
    g.setStroke(getChartInternal().getStyleManager().getAxisTickMarksStroke());

    if (axisTick.getAxis().getDirection() == Axis.Direction.Y && getChartInternal().getStyleManager().isYAxisTicksVisible()) { // Y-Axis

      double xOffset = axisTick.getAxisTickLabels().getBounds().getX() + axisTick.getAxisTickLabels().getBounds().getWidth() + getChartInternal().getStyleManager().getAxisTickPadding();
      double yOffset = axisTick.getAxis().getPaintZone().getY();

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset, getChartInternal().getStyleManager().getAxisTickMarkLength(), axisTick.getAxis().getPaintZone().getHeight());
      // g.setColor(Color.yellow);
      // g.draw(bounds);

      // tick marks
      if (getChartInternal().getStyleManager().isAxisTicksMarksVisible()) {

        for (int i = 0; i < axisTick.getAxis().getAxisTickCalculator().getTickLabels().size(); i++) {

          double tickLocation = axisTick.getAxis().getAxisTickCalculator().getTickLocations().get(i);
          double flippedTickLocation = yOffset + axisTick.getAxis().getPaintZone().getHeight() - tickLocation;
          if (flippedTickLocation > bounds.getY() && flippedTickLocation < bounds.getY() + bounds.getHeight()) {

            Shape line = new Line2D.Double(xOffset, flippedTickLocation, xOffset + getChartInternal().getStyleManager().getAxisTickMarkLength(), flippedTickLocation);
            g.draw(line);
          }
        }
      }

      // Line
      if (getChartInternal().getStyleManager().isAxisTicksLineVisible()) {

        Shape line = new Line2D.Double(xOffset + getChartInternal().getStyleManager().getAxisTickMarkLength(), yOffset, xOffset + getChartInternal().getStyleManager().getAxisTickMarkLength(), yOffset
            + axisTick.getAxis().getPaintZone().getHeight());
        g.draw(line);

      }

    }
    // X-Axis
    else if (axisTick.getAxis().getDirection() == Axis.Direction.X && getChartInternal().getStyleManager().isXAxisTicksVisible()) {

      double xOffset = axisTick.getAxis().getPaintZone().getX();
      double yOffset = axisTick.getAxisTickLabels().getBounds().getY() - getChartInternal().getStyleManager().getAxisTickPadding();

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset - getChartInternal().getStyleManager().getAxisTickMarkLength(), axisTick.getAxis().getPaintZone().getWidth(), getChartInternal().getStyleManager()
          .getAxisTickMarkLength());
          // g.setColor(Color.yellow);
          // g.draw(bounds);

      // tick marks
      if (getChartInternal().getStyleManager().isAxisTicksMarksVisible()) {

        for (int i = 0; i < axisTick.getAxis().getAxisTickCalculator().getTickLabels().size(); i++) {

          double tickLocation = axisTick.getAxis().getAxisTickCalculator().getTickLocations().get(i);
          double shiftedTickLocation = xOffset + tickLocation;

          if (shiftedTickLocation > bounds.getX() && shiftedTickLocation < bounds.getX() + bounds.getWidth()) {

            Shape line = new Line2D.Double(shiftedTickLocation, yOffset, xOffset + tickLocation, yOffset - getChartInternal().getStyleManager().getAxisTickMarkLength());
            g.draw(line);
          }
        }
      }

      // Line
      if (getChartInternal().getStyleManager().isAxisTicksLineVisible()) {

        g.setStroke(getChartInternal().getStyleManager().getAxisTickMarksStroke());
        g.drawLine((int) xOffset, (int) (yOffset - getChartInternal().getStyleManager().getAxisTickMarkLength()), (int) (xOffset + axisTick.getAxis().getPaintZone().getWidth()), (int) (yOffset
            - getChartInternal().getStyleManager().getAxisTickMarkLength()));
      }

    }
  }

  @Override
  public ChartInternal getChartInternal() {

    return axisTick.getChartInternal();
  }
}
