/*******************************************************************************
 * Copyright (c) 2008-2011 SWTChart project. All rights reserved. 
 * 
 * This code is distributed under the terms of the Eclipse Public License v1.0
 * which is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.xeiam.xcharts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.LinkedList;
import java.util.List;

import com.xeiam.xcharts.interfaces.IChartPart;

/**
 * An axis tick.
 */
public class AxisTick implements IChartPart {

    /** the axis */
    private Axis axis;

    /** the axisticklabels */
    private AxisTickLabels axisTickLabels;

    /** the axistickmarks */
    private AxisTickMarks axisTickMarks;

    /** the arraylist of tick label position in pixels */
    private List<Integer> tickLocations = new LinkedList<Integer>();

    /** the arraylist of tick label vales */
    private List<String> tickLabels = new LinkedList<String>();

    private int workingSpace;

    // private double[] data;

    // /** the default tick mark step hint */
    private static final int DEFAULT_TICK_MARK_STEP_HINT = 64;

    protected final static int AXIS_TICK_PADDING = 4;

    /** the format for tick labels */
    private Format format = new DecimalFormat("#.###########");

    /** the bounds */
    private Rectangle bounds = new Rectangle(); // default all-zero rectangle

    /**
     * Constructor.
     * 
     * @param chart the chart
     * @param axis the axis
     */
    protected AxisTick(Axis axis) {
        this.axis = axis;
        axisTickLabels = new AxisTickLabels(axis, this);
        axisTickMarks = new AxisTickMarks(axis, this);

    }

    public AxisTickLabels getAxisTickLabels() {
        return axisTickLabels;
    }

    public AxisTickMarks getAxisTickMarks() {
        return axisTickMarks;
    }

    public List<String> getTickLabels() {
        return tickLabels;
    }

    public List<Integer> getTickLocations() {
        return tickLocations;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    protected int getWorkingSpace() {
        return this.workingSpace;
    }

    @Override
    public void paint(Graphics2D g) {

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

        axisTickLabels.paint(g);
        axisTickMarks.paint(g);

        if (axis.getDirection() == Axis.Direction.Y) {
            bounds = new Rectangle((int) axisTickLabels.getBounds().getX(), (int) (axisTickLabels.getBounds().getY()), (int) (axisTickLabels.getBounds().getWidth() + AXIS_TICK_PADDING + axisTickMarks.getBounds()
                    .getWidth()), (int) (axisTickMarks.getBounds().getHeight()));
            // g.setColor(Color.red);
            // g.draw(bounds);
        } else {
            bounds = new Rectangle((int) axisTickMarks.getBounds().getX(), (int) (axisTickMarks.getBounds().getY()), (int) axisTickLabels.getBounds().getWidth(), (int) (axisTickMarks.getBounds().getHeight()
                    + AXIS_TICK_PADDING + axisTickLabels.getBounds().getHeight()));
            // g.setColor(Color.red);
            // g.draw(bounds);
        }

    }

    /**
     * 
     */
    private void determineAxisTick() {

        System.out.println("workingSpace= " + workingSpace);

        int tickSpace = AxisPair.getTickSpace(workingSpace);
        System.out.println("tickSpace= " + tickSpace);

        int margin = AxisPair.getMargin(workingSpace, tickSpace);

        final BigDecimal MIN = new BigDecimal(new Double(axis.getMin()).toString());
        BigDecimal firstPosition;
        BigDecimal gridStep = getGridStep(tickSpace);

        double xyz = MIN.remainder(gridStep).doubleValue();
        if (xyz <= 0.0) {
            firstPosition = MIN.subtract(MIN.remainder(gridStep));
        } else {
            firstPosition = MIN.subtract(MIN.remainder(gridStep)).add(gridStep);
        }

        for (BigDecimal b = firstPosition; b.doubleValue() <= axis.getMax(); b = b.add(gridStep)) {

            // System.out.println("b= " + b);
            tickLabels.add(format(b.doubleValue()));
            int tickLabelPosition = (int) (margin + ((b.doubleValue() - axis.getMin()) / (axis.getMax() - axis.getMin()) * tickSpace));
            // System.out.println("tickLabelPosition= " + tickLabelPosition);

            // a check if all axis data are the exact same values
            if (Math.abs(axis.getMax() - axis.getMin()) / 5 == 0.0) {
                tickLabelPosition = (int) (margin + tickSpace / 2.0);
            }

            tickLocations.add(tickLabelPosition);
        }
    }

    private BigDecimal getGridStep(int tickSpace) {

        double length = Math.abs(axis.getMax() - axis.getMin());
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

    private String format(double value) {

        return this.format.format(value);
    }

}
