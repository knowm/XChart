/**
 * Copyright 2011 - 2014 Xeiam LLC.
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
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.internal.chartpart.Axis.AxisType;

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

  AxisTickCalculator axisTickCalculator = null;

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
    if (axis.getDirection() == Axis.Direction.Y) {
      workingSpace = axis.getPaintZone().getHeight(); // number of pixels the axis has to work with for drawing AxisTicks
      // System.out.println("workingspace= " + workingSpace);
    }
    else if (axis.getDirection() == Axis.Direction.X) {
      workingSpace = axis.getPaintZone().getWidth(); // number of pixels the axis has to work with for drawing AxisTicks
      // System.out.println("workingspace= " + workingSpace);
    }

    if (axis.getDirection() == Axis.Direction.X && getChartPainter().getStyleManager().getChartType() == ChartType.Bar) {

      axisTickCalculator = new AxisTickBarChartCalculator(axis.getDirection(), workingSpace, axis.getMin(), axis.getMax(), getChartPainter());

    }
    else if (axis.getDirection() == Axis.Direction.X && getChartPainter().getStyleManager().isXAxisLogarithmic() && axis.getAxisType() != AxisType.Date) {

      axisTickCalculator = new AxisTickLogarithmicCalculator(axis.getDirection(), workingSpace, axis.getMin(), axis.getMax(), getChartPainter().getStyleManager());

    }
    else if (axis.getDirection() == Axis.Direction.Y && getChartPainter().getStyleManager().isYAxisLogarithmic() && axis.getAxisType() != AxisType.Date) {

      axisTickCalculator = new AxisTickLogarithmicCalculator(axis.getDirection(), workingSpace, axis.getMin(), axis.getMax(), getChartPainter().getStyleManager());

    }
    else if (axis.getAxisType() == AxisType.Number) {

      axisTickCalculator = new AxisTickNumericalCalculator(axis.getDirection(), workingSpace, axis.getMin(), axis.getMax(), getChartPainter().getStyleManager());

    }
    else if (axis.getAxisType() == AxisType.Date) {

      axisTickCalculator = new AxisTickDateCalculator(axis.getDirection(), workingSpace, axis.getMin(), axis.getMax(), getChartPainter().getStyleManager());

    }

    if (axis.getDirection() == Axis.Direction.Y && getChartPainter().getStyleManager().isYAxisTicksVisible()) {

      axisTickLabels.paint(g);
      axisTickMarks.paint(g);

      bounds = new Rectangle2D.Double(

      axisTickLabels.getBounds().getX(),

      axisTickLabels.getBounds().getY(),

      axisTickLabels.getBounds().getWidth() + getChartPainter().getStyleManager().getAxisTickPadding() + axisTickMarks.getBounds().getWidth(),

      axisTickMarks.getBounds().getHeight()

      );

      // g.setColor(Color.red);
      // g.draw(bounds);

    }
    else if (axis.getDirection() == Axis.Direction.X && getChartPainter().getStyleManager().isXAxisTicksVisible()) {

      axisTickLabels.paint(g);
      axisTickMarks.paint(g);

      bounds =
          new Rectangle2D.Double(axisTickMarks.getBounds().getX(), axisTickMarks.getBounds().getY(), axisTickLabels.getBounds().getWidth(), axisTickMarks.getBounds().getHeight()
              + getChartPainter().getStyleManager().getAxisTickPadding() + axisTickLabels.getBounds().getHeight());
      // g.setColor(Color.red);
      // g.draw(bounds);

    }

  }

  @Override
  public ChartPainter getChartPainter() {

    return axis.getChartPainter();
  }

  // Getters /////////////////////////////////////////////////

  public Axis getAxis() {

    return axis;
  }

  public AxisTickLabels getAxisTickLabels() {

    return axisTickLabels;
  }

  public List<Double> getTickLocations() {

    return axisTickCalculator.getTickLocations();
  }

  public List<String> getTickLabels() {

    return axisTickCalculator.getTickLabels();
  }
}
