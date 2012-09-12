/**
 * Copyright 2011-2012 Xeiam LLC.
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
package com.xeiam.xchart;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import com.xeiam.xchart.Axis.AxisType;
import com.xeiam.xchart.interfaces.IChartPart;
import com.xeiam.xchart.interfaces.IHideable;

/**
 * An axis tick.
 */
public class AxisTick implements IChartPart, IHideable {

  /** the default tick mark step hint */
  private static final int DEFAULT_TICK_MARK_STEP_HINT = 64;

  /** the padding between the tick labels and the tick marks */
  protected final static int AXIS_TICK_PADDING = 4;

  /** parent */
  protected Axis axis;

  /** the axisticklabels */
  protected AxisTickLabels axisTickLabels;

  /** the axistickmarks */
  protected AxisTickMarks axisTickMarks;

  /** the arraylist of tick label position in pixels */
  protected List<Integer> tickLocations;

  /** the arraylist of tick label values */
  protected List<String> tickLabels;

  private int workingSpace;

  /** the normal format for tick labels */
  private Format normalFormat;

  /** the scientific format for tick labels */
  private Format scientificFormat;

  private SimpleDateFormat simpleDateformat;

  /** the bounds */
  private Rectangle bounds;

  /** the visibility state of axistick */
  protected boolean isVisible = true; // default to true

  /**
   * Constructor
   * 
   * @param axis the axis
   */
  protected AxisTick(Axis axis) {

    this.axis = axis;
    axisTickLabels = new AxisTickLabels(this);
    axisTickMarks = new AxisTickMarks(this);

    normalFormat = new DecimalFormat("#.###########");
    scientificFormat = new DecimalFormat("0.###E0");
    simpleDateformat = new SimpleDateFormat("MM-dd");

  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle();

    if (axis.direction == Axis.Direction.Y) {
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

      if (axis.direction == Axis.Direction.Y) {
        bounds = new Rectangle((int) axisTickLabels.getBounds().getX(), (int) (axisTickLabels.getBounds().getY()),
            (int) (axisTickLabels.getBounds().getWidth() + AXIS_TICK_PADDING + axisTickMarks.getBounds().getWidth()), (int) (axisTickMarks.getBounds().getHeight()));
        // g.setColor(Color.red);
        // g.draw(bounds);
      } else {
        bounds = new Rectangle((int) axisTickMarks.getBounds().getX(), (int) (axisTickMarks.getBounds().getY()), (int) axisTickLabels.getBounds().getWidth(), (int) (axisTickMarks.getBounds().getHeight()
            + AXIS_TICK_PADDING + axisTickLabels.getBounds().getHeight()));
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

    int margin = AxisPair.getMargin(workingSpace, tickSpace);

    // a check if all axis data are the exact same values
    if (axis.max == axis.min) {
      tickLabels.add(format(axis.max));
      tickLocations.add((int) (margin + tickSpace / 2.0));
    } else {

      final BigDecimal MIN = new BigDecimal(axis.min.doubleValue());
      BigDecimal firstPosition;
      BigDecimal gridStep = getGridStep(tickSpace);

      double xyz = MIN.remainder(gridStep).doubleValue();
      if (xyz <= 0.0) {
        firstPosition = MIN.subtract(MIN.remainder(gridStep));
      } else {
        firstPosition = MIN.subtract(MIN.remainder(gridStep)).add(gridStep);
      }

      for (BigDecimal b = firstPosition; b.compareTo(axis.max) <= 0; b = b.add(gridStep)) {

        // System.out.println("b= " + b);
        tickLabels.add(format(b));
        int tickLabelPosition = (int) (margin + ((b.subtract(axis.min)).doubleValue() / (axis.max.subtract(axis.min)).doubleValue() * tickSpace));
        // System.out.println("tickLabelPosition= " + tickLabelPosition);

        tickLocations.add(tickLabelPosition);
      }
    }
  }

  private BigDecimal getGridStep(int tickSpace) {

    double length = Math.abs(axis.max.subtract(axis.min).doubleValue());
    // System.out.println(axis.getMax());
    // System.out.println(axis.min);
    // System.out.println(length);
    double gridStepHint = length / tickSpace * DEFAULT_TICK_MARK_STEP_HINT;

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

  private String format(BigDecimal value) {

    if (axis.axisType == AxisType.NUMBER) {
      if (Math.abs(value.doubleValue()) < 9999 && Math.abs(value.doubleValue()) > .0001 || value.doubleValue() == 0) {
        return normalFormat.format(value.doubleValue());
      } else {
        return scientificFormat.format(value.doubleValue());
      }
    } else {
      return simpleDateformat.format(value.longValueExact());
    }

  }

  @Override
  public void setVisible(boolean isVisible) {

    this.isVisible = isVisible;
  }
}
