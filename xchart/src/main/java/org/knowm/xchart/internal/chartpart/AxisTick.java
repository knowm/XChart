/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * An axis tick
 */
public class AxisTick<ST extends AxesChartStyler, S extends AxesChartSeries> implements ChartPart {

  private final Chart<ST, S> chart;
  private Rectangle2D bounds;
  private final Direction direction;

  /**
   * the axisticklabels
   */
  private final AxisTickLabels<ST, S> axisTickLabels;

  /**
   * the axistickmarks
   */
  private final AxisTickMarks<ST, S> axisTickMarks;

  /**
   * Constructor
   *
   * @param chart
   * @param direction
   * @param yAxis
   */
  AxisTick(Chart<ST, S> chart, Direction direction, Axis yAxis) {

    this.chart = chart;
    this.direction = direction;
    axisTickLabels = new AxisTickLabels<ST, S>(chart, direction, yAxis);
    axisTickMarks = new AxisTickMarks<ST, S>(chart, direction, yAxis);
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    if (direction == Axis.Direction.Y && chart.getStyler().isYAxisTicksVisible()) {

      axisTickLabels.paint(g);
      axisTickMarks.paint(g);

      bounds = new Rectangle2D.Double(

          axisTickLabels.getBounds().getX(),

          axisTickLabels.getBounds().getY(),

          axisTickLabels.getBounds().getWidth() + chart.getStyler().getAxisTickPadding() + axisTickMarks.getBounds().getWidth(),

          axisTickMarks.getBounds().getHeight()

      );

      // g.setColor(Color.red);
      // g.draw(bounds);

    } else if (direction == Axis.Direction.X && chart.getStyler().isXAxisTicksVisible()) {

      axisTickLabels.paint(g);
      axisTickMarks.paint(g);

      bounds = new Rectangle2D.Double(

          axisTickMarks.getBounds().getX(),

          axisTickMarks.getBounds().getY(),

          axisTickLabels.getBounds().getWidth(),

          axisTickMarks.getBounds().getHeight() + chart.getStyler().getAxisTickPadding() + axisTickLabels.getBounds().getHeight()

      );

      // g.setColor(Color.red);
      // g.draw(bounds);

    } else {
      bounds = new Rectangle2D.Double();
    }
  }

  // Getters /////////////////////////////////////////////////

  AxisTickLabels<ST, S> getAxisTickLabels() {

    return axisTickLabels;
  }
}
