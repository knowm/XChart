/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
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

import org.knowm.xchart.Series_AxesChart;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.internal.style.StyleManagerAxesChart;

/**
 * Axis tick marks. This includes the little tick marks and the line that hugs the plot area.
 */
public class AxisTickMarks<SM extends StyleManagerAxesChart, S extends Series> implements ChartPart {

  private final Chart<StyleManagerAxesChart, Series_AxesChart> chart;
  private Rectangle2D bounds;
  private final Direction direction;

  /**
   * Constructor
   *
   * @param chart
   * @param direction
   */
  protected AxisTickMarks(Chart<StyleManagerAxesChart, Series_AxesChart> chart, Direction direction) {

    this.chart = chart;
    this.direction = direction;
  }

  @Override
  public void paint(Graphics2D g) {

    g.setColor(chart.getStyleManager().getAxisTickMarksColor());
    g.setStroke(chart.getStyleManager().getAxisTickMarksStroke());

    if (direction == Axis.Direction.Y && chart.getStyleManager().isYAxisTicksVisible()) { // Y-Axis

      double xOffset = chart.getYAxis().getAxisTick().getAxisTickLabels().getBounds().getX() + chart.getYAxis().getAxisTick().getAxisTickLabels().getBounds().getWidth() + chart.getStyleManager()
          .getAxisTickPadding();
      double yOffset = chart.getYAxis().getPaintZone().getY();

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset, chart.getStyleManager().getAxisTickMarkLength(), chart.getYAxis().getPaintZone().getHeight());
      // g.setColor(Color.yellow);
      // g.draw(bounds);

      // tick marks
      if (chart.getStyleManager().isAxisTicksMarksVisible()) {

        for (int i = 0; i < chart.getYAxis().getAxisTickCalculator().getTickLabels().size(); i++) {

          double tickLocation = chart.getYAxis().getAxisTickCalculator().getTickLocations().get(i);
          double flippedTickLocation = yOffset + chart.getYAxis().getPaintZone().getHeight() - tickLocation;
          if (flippedTickLocation > bounds.getY() && flippedTickLocation < bounds.getY() + bounds.getHeight()) {

            Shape line = new Line2D.Double(xOffset, flippedTickLocation, xOffset + chart.getStyleManager().getAxisTickMarkLength(), flippedTickLocation);
            g.draw(line);
          }
        }
      }

      // Line
      if (chart.getStyleManager().isAxisTicksLineVisible()) {

        Shape line = new Line2D.Double(xOffset + chart.getStyleManager().getAxisTickMarkLength(), yOffset, xOffset + chart.getStyleManager().getAxisTickMarkLength(), yOffset + chart.getYAxis()
            .getPaintZone().getHeight());
        g.draw(line);

      }

    }
    // X-Axis
    else if (direction == Axis.Direction.X && chart.getStyleManager().isXAxisTicksVisible()) {

      double xOffset = chart.getXAxis().getPaintZone().getX();
      double yOffset = chart.getXAxis().getAxisTick().getAxisTickLabels().getBounds().getY() - chart.getStyleManager().getAxisTickPadding();

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset - chart.getStyleManager().getAxisTickMarkLength(), chart.getXAxis().getPaintZone().getWidth(), chart.getStyleManager().getAxisTickMarkLength());
      // g.setColor(Color.yellow);
      // g.draw(bounds);

      // tick marks
      if (chart.getStyleManager().isAxisTicksMarksVisible()) {

        for (int i = 0; i < chart.getXAxis().getAxisTickCalculator().getTickLabels().size(); i++) {

          double tickLocation = chart.getXAxis().getAxisTickCalculator().getTickLocations().get(i);
          double shiftedTickLocation = xOffset + tickLocation;

          if (shiftedTickLocation > bounds.getX() && shiftedTickLocation < bounds.getX() + bounds.getWidth()) {

            Shape line = new Line2D.Double(shiftedTickLocation, yOffset, xOffset + tickLocation, yOffset - chart.getStyleManager().getAxisTickMarkLength());
            g.draw(line);
          }
        }
      }

      // Line
      if (chart.getStyleManager().isAxisTicksLineVisible()) {

        g.setStroke(chart.getStyleManager().getAxisTickMarksStroke());
        g.drawLine((int) xOffset, (int) (yOffset - chart.getStyleManager().getAxisTickMarkLength()), (int) (xOffset + chart.getXAxis().getPaintZone().getWidth()), (int) (yOffset - chart
            .getStyleManager().getAxisTickMarkLength()));
      }

    }
    else {
      bounds = new Rectangle2D.Double();
    }

  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }
}
