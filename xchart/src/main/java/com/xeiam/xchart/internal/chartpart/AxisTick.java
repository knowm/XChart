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
import java.util.List;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.chartpart.axistickcalculator.AxisTickCalculator;
import com.xeiam.xchart.internal.chartpart.axistickcalculator.BarChartAxisTickCalculator;
import com.xeiam.xchart.internal.chartpart.axistickcalculator.DateAxisTickCalculator;
import com.xeiam.xchart.internal.chartpart.axistickcalculator.LogarithmicAxisTickCalculator;
import com.xeiam.xchart.internal.chartpart.axistickcalculator.NumberAxisTickCalculator;
import com.xeiam.xchart.style.StyleManager.ChartType;

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
  private Rectangle bounds;

  /** the visibility state of axistick */
  private boolean isVisible = true; // default to true

  AxisTickCalculator gridStep = null;

  /**
   * Constructor
   * 
   * @param axis
   * @param isVisible
   */
  protected AxisTick(Axis axis, boolean isVisible) {

    this.axis = axis;
    this.isVisible = isVisible;
    axisTickLabels = new AxisTickLabels(this);
    axisTickMarks = new AxisTickMarks(this);
  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle();

    int workingSpace = 0;
    if (axis.getDirection() == Axis.Direction.Y) {
      workingSpace = (int) axis.getPaintZone().getHeight(); // number of pixels the axis has to work with for drawing AxisTicks
      // System.out.println("workingspace= " + workingSpace);
    } else {
      workingSpace = (int) axis.getPaintZone().getWidth(); // number of pixels the axis has to work with for drawing AxisTicks
      // System.out.println("workingspace= " + workingSpace);
    }

    if (axis.getDirection() == Axis.Direction.X && getChart().getStyleManager().getChartType() == ChartType.Bar) {

      gridStep = new BarChartAxisTickCalculator(axis.getDirection(), workingSpace, axis.getMin(), axis.getMax(), getChart());

    } else if (axis.getDirection() == Axis.Direction.X && getChart().getStyleManager().isXAxisLogarithmic() && axis.getAxisType() != AxisType.Date) {

      gridStep = new LogarithmicAxisTickCalculator(axis.getDirection(), workingSpace, axis.getMin(), axis.getMax(), getChart().getStyleManager());

    } else if (axis.getDirection() == Axis.Direction.Y && getChart().getStyleManager().isYAxisLogarithmic() && axis.getAxisType() != AxisType.Date) {

      gridStep = new LogarithmicAxisTickCalculator(axis.getDirection(), workingSpace, axis.getMin(), axis.getMax(), getChart().getStyleManager());

    } else if (axis.getAxisType() == AxisType.Number) {

      gridStep = new NumberAxisTickCalculator(axis.getDirection(), workingSpace, axis.getMin(), axis.getMax(), getChart().getStyleManager());

    } else if (axis.getAxisType() == AxisType.Date) {

      gridStep = new DateAxisTickCalculator(axis.getDirection(), workingSpace, axis.getMin(), axis.getMax(), getChart().getStyleManager());

    }

    if (isVisible) {

      axisTickLabels.paint(g);
      axisTickMarks.paint(g);

      if (axis.getDirection() == Axis.Direction.Y) {
        bounds = new Rectangle((int) axisTickLabels.getBounds().getX(), (int) (axisTickLabels.getBounds().getY()), (int) (axisTickLabels.getBounds().getWidth()
            + getChart().getStyleManager().getAxisTickPadding() + axisTickMarks.getBounds().getWidth()), (int) (axisTickMarks.getBounds().getHeight()));
        // g.setColor(Color.red);
        // g.draw(bounds);
      } else {
        bounds = new Rectangle((int) axisTickMarks.getBounds().getX(), (int) (axisTickMarks.getBounds().getY()), (int) axisTickLabels.getBounds().getWidth(), (int) (axisTickMarks.getBounds()
            .getHeight()
            + getChart().getStyleManager().getAxisTickPadding() + axisTickLabels.getBounds().getHeight()));
        // g.setColor(Color.red);
        // g.draw(bounds);
      }
    }

  }

  @Override
  public Chart getChart() {

    return axis.getChart();
  }

  // Getters /////////////////////////////////////////////////

  public Axis getAxis() {

    return axis;
  }

  public AxisTickLabels getAxisTickLabels() {

    return axisTickLabels;
  }

  public List<Integer> getTickLocations() {

    return gridStep.getTickLocations();
  }

  public List<String> getTickLabels() {

    return gridStep.getTickLabels();
  }
}
