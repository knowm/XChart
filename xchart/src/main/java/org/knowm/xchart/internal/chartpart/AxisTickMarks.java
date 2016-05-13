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

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * Axis tick marks. This includes the little tick marks and the line that hugs the plot area.
 */
public class AxisTickMarks<ST extends AxesChartStyler, S extends Series> implements ChartPart {

  private final Chart<AxesChartStyler, Series_AxesChart> chart;
  private Rectangle2D bounds;
  private final Direction direction;

  /**
   * Constructor
   *
   * @param chart
   * @param direction
   */
  protected AxisTickMarks(Chart<AxesChartStyler, Series_AxesChart> chart, Direction direction) {

    this.chart = chart;
    this.direction = direction;
  }

  @Override
  public void paint(Graphics2D g) {

    g.setColor(chart.getStyler().getAxisTickMarksColor());
    g.setStroke(chart.getStyler().getAxisTickMarksStroke());

    if (direction == Axis.Direction.Y && chart.getStyler().isYAxisTicksVisible()) { // Y-Axis

      double xOffset = chart.getYAxis().getAxisTick().getAxisTickLabels().getBounds().getX() + chart.getYAxis().getAxisTick().getAxisTickLabels().getBounds().getWidth() + chart.getStyler()
          .getAxisTickPadding();
      double yOffset = chart.getYAxis().getBounds().getY();

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset, chart.getStyler().getAxisTickMarkLength(), chart.getYAxis().getBounds().getHeight());
      // g.setColor(Color.yellow);
      // g.draw(bounds);

      // tick marks
      if (chart.getStyler().isAxisTicksMarksVisible()) {

        for (int i = 0; i < chart.getYAxis().getAxisTickCalculator().getTickLabels().size(); i++) {

          double tickLocation = chart.getYAxis().getAxisTickCalculator().getTickLocations().get(i);
          double flippedTickLocation = yOffset + chart.getYAxis().getBounds().getHeight() - tickLocation;
          if (flippedTickLocation > bounds.getY() && flippedTickLocation < bounds.getY() + bounds.getHeight()) {

            Shape line = new Line2D.Double(xOffset, flippedTickLocation, xOffset + chart.getStyler().getAxisTickMarkLength(), flippedTickLocation);
            g.draw(line);
          }
        }
      }

      // Line
      if (chart.getStyler().isAxisTicksLineVisible()) {

        Shape line = new Line2D.Double(xOffset + chart.getStyler().getAxisTickMarkLength(), yOffset, xOffset + chart.getStyler().getAxisTickMarkLength(), yOffset + chart.getYAxis().getBounds()
            .getHeight());
        g.draw(line);

      }

    }
    // X-Axis
    else if (direction == Axis.Direction.X && chart.getStyler().isXAxisTicksVisible()) {

      double xOffset = chart.getXAxis().getBounds().getX();
      double yOffset = chart.getXAxis().getAxisTick().getAxisTickLabels().getBounds().getY() - chart.getStyler().getAxisTickPadding();

      // bounds
      bounds = new Rectangle2D.Double(xOffset, yOffset - chart.getStyler().getAxisTickMarkLength(), chart.getXAxis().getBounds().getWidth(), chart.getStyler().getAxisTickMarkLength());
      // g.setColor(Color.yellow);
      // g.draw(bounds);

      // tick marks
      if (chart.getStyler().isAxisTicksMarksVisible()) {

        for (int i = 0; i < chart.getXAxis().getAxisTickCalculator().getTickLabels().size(); i++) {

          double tickLocation = chart.getXAxis().getAxisTickCalculator().getTickLocations().get(i);
          double shiftedTickLocation = xOffset + tickLocation;

          if (shiftedTickLocation > bounds.getX() && shiftedTickLocation < bounds.getX() + bounds.getWidth()) {

            Shape line = new Line2D.Double(shiftedTickLocation, yOffset, xOffset + tickLocation, yOffset - chart.getStyler().getAxisTickMarkLength());
            g.draw(line);
          }
        }
      }

      // Line
      if (chart.getStyler().isAxisTicksLineVisible()) {

        g.setStroke(chart.getStyler().getAxisTickMarksStroke());
        g.drawLine((int) xOffset, (int) (yOffset - chart.getStyler().getAxisTickMarkLength()), (int) (xOffset + chart.getXAxis().getBounds().getWidth()), (int) (yOffset - chart.getStyler()
            .getAxisTickMarkLength()));
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
