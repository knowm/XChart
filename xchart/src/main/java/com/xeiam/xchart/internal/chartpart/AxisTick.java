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
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.chartpart.Axis.Direction;

/**
 * An axis tick
 */
public class AxisTick implements ChartPart {

  /** the default tick mark step hint for x axis */
  private static final int DEFAULT_TICK_MARK_STEP_HINT_X = 74;

  /** the default tick mark step hint for y axis */
  private static final int DEFAULT_TICK_MARK_STEP_HINT_Y = 44;

  /** parent */
  private Axis axis;

  /** the axisticklabels */
  private AxisTickLabels axisTickLabels;

  /** the axistickmarks */
  private AxisTickMarks axisTickMarks;

  /** the List of tick label position in pixels */
  private List<Integer> tickLocations;

  /** the List of tick label values */
  private List<String> tickLabels;

  private int workingSpace;

  /** the bounds */
  private Rectangle bounds;

  /** the visibility state of axistick */
  private boolean isVisible = true; // default to true

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

    if (axis.getDirection() == Axis.Direction.Y) {
      workingSpace = (int) axis.getPaintZone().getHeight(); // number of pixels the axis has to work with for drawing AxisTicks
      // System.out.println("workingspace= " + workingSpace);
    } else {
      workingSpace = (int) axis.getPaintZone().getWidth(); // number of pixels the axis has to work with for drawing AxisTicks
      // System.out.println("workingspace= " + workingSpace);
    }

    determineAxisTick();

    // for (Integer position : tickLocations) {
    // System.out.println(position);
    // }
    // for (String label : tickLabels) {
    // System.out.println(label);
    // }

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

  /**
   * 
   */
  private void determineAxisTick() {

    tickLocations = new LinkedList<Integer>();
    tickLabels = new LinkedList<String>();

    // System.out.println("workingSpace= " + workingSpace);

    int tickSpace = AxisPair.getTickSpace(workingSpace);
    // System.out.println("tickSpace= " + tickSpace);

    int margin = AxisPair.getTickStartOffset(workingSpace, tickSpace);

    // a check if all axis data are the exact same values
    if (axis.getMax() == axis.getMin()) {
      tickLabels.add(format(axis.getMax()));
      tickLocations.add((int) (margin + tickSpace / 2.0));
    } else {

      final BigDecimal min = new BigDecimal(axis.getMin().doubleValue());
      BigDecimal firstPosition;
      BigDecimal gridStep = getGridStep(tickSpace);

      double xyz = min.remainder(gridStep).doubleValue();
      if (xyz <= 0.0) {
        firstPosition = min.subtract(min.remainder(gridStep));
      } else {
        firstPosition = min.subtract(min.remainder(gridStep)).add(gridStep);
      }

      for (BigDecimal b = firstPosition; b.compareTo(axis.getMax()) <= 0; b = b.add(gridStep)) {

        // System.out.println("b= " + b);
        tickLabels.add(format(b));
        int tickLabelPosition = (int) (margin + ((b.subtract(axis.getMin())).doubleValue() / (axis.getMax().subtract(axis.getMin())).doubleValue() * tickSpace));
        // System.out.println("tickLabelPosition= " + tickLabelPosition);

        tickLocations.add(tickLabelPosition);
      }
    }
  }

  private BigDecimal getGridStep(int tickSpace) {

    double length = Math.abs(axis.getMax().subtract(axis.getMin()).doubleValue());
    // System.out.println(axis.getMax());
    // System.out.println(axis.min);
    // System.out.println(length);
    int tickMarkSpaceHint = (axis.getDirection() == Direction.X ? DEFAULT_TICK_MARK_STEP_HINT_X : DEFAULT_TICK_MARK_STEP_HINT_Y);
    // for very short plots, squeeze some more ticks in than normal
    if (axis.getDirection() == Direction.Y && tickSpace < 160) {
      tickMarkSpaceHint = 25;
    }
    double gridStepHint = length / tickSpace * tickMarkSpaceHint;

    // gridStepHint --> mantissa * 10 ** exponent
    // e.g. 724.1 --> 7.241 * 10 ** 2
    double mantissa = gridStepHint;
    int exponent = 0;
    if (mantissa == 0) {
      exponent = 1;
    } else if (mantissa < 1) {
      while (mantissa < 1) {
        mantissa *= 10.0;
        exponent--;
      }
    } else {
      while (mantissa >= 10) {
        mantissa /= 10.0;
        exponent++;
      }
    }

    // calculate the grid step with hint.
    BigDecimal gridStep;
    if (mantissa > 7.5) {
      // gridStep = 10.0 * 10 ** exponent
      gridStep = BigDecimal.TEN.multiply(pow(10, exponent));
    } else if (mantissa > 3.5) {
      // gridStep = 5.0 * 10 ** exponent
      gridStep = new BigDecimal(new Double(5).toString()).multiply(pow(10, exponent));
    } else if (mantissa > 1.5) {
      // gridStep = 2.0 * 10 ** exponent
      gridStep = new BigDecimal(new Double(2).toString()).multiply(pow(10, exponent));
    } else {
      // gridStep = 1.0 * 10 ** exponent
      gridStep = pow(10, exponent);
    }
    return gridStep;
  }

  /**
   * Calculates the value of the first argument raised to the power of the second argument.
   * 
   * @param base the base
   * @param exponent the exponent
   * @return the value <tt>a<sup>b</sup></tt> in <tt>BigDecimal</tt>
   */
  private BigDecimal pow(double base, int exponent) {

    BigDecimal value;
    if (exponent > 0) {
      value = new BigDecimal(new Double(base).toString()).pow(exponent);
    } else {
      value = BigDecimal.ONE.divide(new BigDecimal(new Double(base).toString()).pow(-exponent));
    }
    return value;
  }

  /**
   * Format the number
   * 
   * @param value The number to be formatted
   * @return The formatted number in String form
   */
  private String format(BigDecimal value) {

    if (axis.getAxisType() == AxisType.NUMBER) {

      return getChart().getValueFormatter().formatNumber(value);

    } else {

      return getChart().getValueFormatter().formatDateValue(value, axis.getMin(), axis.getMax());
    }

  }

  @Override
  public Chart getChart() {

    return axis.getChart();
  }

  // Getters /////////////////////////////////////////////////

  public List<Integer> getTickLocations() {

    return tickLocations;
  }

  public List<String> getTickLabels() {

    return tickLabels;
  }

  public Axis getAxis() {

    return axis;
  }

  public AxisTickLabels getAxisTickLabels() {

    return axisTickLabels;
  }
}
