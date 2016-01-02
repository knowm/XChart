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
import java.awt.geom.Rectangle2D;

/**
 * An axis tick
 */
public class AxisTick implements ChartPart {

  /** parent */
  private Axis axis;

  /** the axisticklabels */
  private AxisTickLabels axisTickLabels;

  /** the axistickmarks */
  private AxisTickMarks axisTickMarks;

  /** the bounds */
  private Rectangle2D bounds = new Rectangle2D.Double();

  /**
   * Constructor
   *
   * @param axis
   */
  protected AxisTick(Axis axis) {

    this.axis = axis;
    axisTickLabels = new AxisTickLabels(this);
    axisTickMarks = new AxisTickMarks(this);
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    double workingSpace = 0.0;
    // Y-Axis
    if (axis.getDirection() == Axis.Direction.Y) {
      workingSpace = axis.getPaintZone().getHeight(); // number of pixels the axis has to work with for drawing AxisTicks
      // System.out.println("workingspace= " + workingSpace);
    }
    // X-Axis
    else if (axis.getDirection() == Axis.Direction.X) {
      workingSpace = axis.getPaintZone().getWidth(); // number of pixels the axis has to work with for drawing AxisTicks
      // System.out.println("workingspace= " + workingSpace);
    }

    // System.out.println("AxisTick: " + axis.getDirection());
    // System.out.println("workingSpace: " + workingSpace);

    if (axis.getDirection() == Axis.Direction.Y && getChartInternal().getStyleManager().isYAxisTicksVisible()) {

      axisTickLabels.paint(g);
      axisTickMarks.paint(g);

      bounds = new Rectangle2D.Double(

          axisTickLabels.getBounds().getX(),

          axisTickLabels.getBounds().getY(),

          axisTickLabels.getBounds().getWidth() + getChartInternal().getStyleManager().getAxisTickPadding() + axisTickMarks.getBounds().getWidth(),

          axisTickMarks.getBounds().getHeight()

      );

      // g.setColor(Color.red);
      // g.draw(bounds);

    }
    else if (axis.getDirection() == Axis.Direction.X && getChartInternal().getStyleManager().isXAxisTicksVisible()) {

      axisTickLabels.paint(g);
      axisTickMarks.paint(g);

      bounds = new Rectangle2D.Double(axisTickMarks.getBounds().getX(), axisTickMarks.getBounds().getY(), axisTickLabels.getBounds().getWidth(), axisTickMarks.getBounds().getHeight()
          + getChartInternal().getStyleManager().getAxisTickPadding() + axisTickLabels.getBounds().getHeight());

      // g.setColor(Color.red);
      // g.draw(bounds);

    }

  }

  @Override
  public ChartInternal getChartInternal() {

    return axis.getChartInternal();
  }

  // Getters /////////////////////////////////////////////////

  public Axis getAxis() {

    return axis;
  }

  public AxisTickLabels getAxisTickLabels() {

    return axisTickLabels;
  }

}
