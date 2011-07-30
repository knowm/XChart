/**
 * Copyright 2011 Xeiam LLC.
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
package com.xeiam.xcharts;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import com.xeiam.xcharts.interfaces.IChartPart;

/**
 * Axis
 * 
 * @author timmolter
 */
public class Axis implements IChartPart {

    /** the chart */
    private Chart chart;

    /** the axisPair */
    private AxisPair axisPair;

    /** the axis title */
    private AxisTitle axisTitle;

    /** the axis tick */
    private AxisTick axisTick;

    /** the grid */
    private AxisLine axisLine;

    /** the axis direction */
    private Direction direction;

    private Double min = null;

    private Double max = null;

    /** the bounds */
    private Rectangle bounds = new Rectangle(); // default all-zero rectangle

    /** the paint zone */
    private Rectangle paintZone = new Rectangle(); // default all-zero rectangle

    /** An axis direction */
    public enum Direction {

        /** the constant to represent X axis */
        X,

        /** the constant to represent Y axis */
        Y
    }

    /**
     * Constructor
     * 
     * @param direction the axis direction (X or Y)
     * @param chart the chart
     */
    public Axis(Chart chart, AxisPair axisPair, Direction direction) {

        this.chart = chart;
        this.axisPair = axisPair;
        this.direction = direction;

        axisTitle = new AxisTitle(this);
        axisTick = new AxisTick(this);
        axisLine = new AxisLine(this);
    }

    /**
     * @param min
     * @param max
     */
    public void addMinMax(double min, double max) {

        // System.out.println(min);
        // System.out.println(max);

        if (this.min == null || min < this.min) {
            this.min = min;
        }
        if (this.max == null || max > this.max) {
            this.max = max;
        }

        // System.out.println(this.min);
        // System.out.println(this.max);
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    public Rectangle getPaintZone() {
        return paintZone;
    }

    protected AxisTitle getAxisTitle() {
        return axisTitle;
    }

    public void setAxisTitle(String title) {
        this.axisTitle.setText(title);
    }

    public void setAxisTitle(AxisTitle axisTitle) {
        this.axisTitle = axisTitle;
    }

    public AxisTick getAxisTick() {
        return axisTick;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    /**
     * 
     */
    public int getSizeHint() {

        if (direction == Direction.X) { // X-Axis

            // Axis title
            double titleHeight = 0;
            if (axisTitle.isVisible) {
                TextLayout textLayout = new TextLayout(axisTitle.getText(), axisTitle.getFont(), new FontRenderContext(null, true, false));
                Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
                titleHeight = rectangle.getHeight() + AxisTitle.AXIS_TITLE_PADDING;
            }

            // Axis tick labels
            TextLayout textLayout = new TextLayout("0", axisTick.getAxisTickLabels().getFont(), new FontRenderContext(null, true, false));
            Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
            double axisTickLabelsHeight = rectangle.getHeight();

            double gridStrokeWidth = axisLine.getStroke().getLineWidth();
            return (int) (titleHeight + axisTickLabelsHeight + AxisTick.AXIS_TICK_PADDING + AxisTickMarks.TICK_LENGTH + gridStrokeWidth + Plot.PLOT_PADDING);
        } else { // Y-Axis
            return 0; // We layout the yAxis first depending in the xAxis height hint. We don't care about the yAxis height hint
        }
    }

    @Override
    public void paint(Graphics2D g) {

        // determine Axis bounds
        if (direction == Direction.Y) { // Y-Axis

            // calculate paint zone
            // ----
            // |
            // |
            // |
            // |
            // ----
            int xOffset = Chart.CHART_PADDING;
            int yOffset = (int) (axisPair.getChartTitleBounds().getY() + axisPair.getChartTitleBounds().getHeight() + Chart.CHART_PADDING);
            int width = 80; // arbitrary, final width depends on Axis tick labels
            int height = chart.getHeight() - yOffset - axisPair.getXAxis().getSizeHint() - Chart.CHART_PADDING;
            Rectangle yAxisRectangle = new Rectangle(xOffset, yOffset, width, height);
            this.paintZone = yAxisRectangle;
            // g.setColor(Color.green);
            // g.draw(yAxisRectangle);

            // fill in Axis with sub-components
            axisTitle.paint(g);
            axisTick.paint(g);
            axisLine.paint(g);

            xOffset = (int) paintZone.getX();
            yOffset = (int) paintZone.getY();
            width = (int) (axisTitle.isVisible ? axisTitle.getBounds().getWidth() : 0) + (int) axisTick.getBounds().getWidth() + (int) axisLine.getBounds().getWidth();
            height = (int) paintZone.getHeight();
            bounds = new Rectangle(xOffset, yOffset, width, height);
            // g.setColor(Color.yellow);
            // g.draw(bounds);

        } else { // X-Axis

            // calculate paint zone
            // |____________________|

            int xOffset = (int) (axisPair.getYAxis().getBounds().getWidth() + Plot.PLOT_PADDING + Chart.CHART_PADDING - 1);
            int yOffset = (int) (axisPair.getYAxis().getBounds().getY() + axisPair.getYAxis().getBounds().getHeight());
            int width = (int) (chart.getWidth() - axisPair.getYAxis().getBounds().getWidth() - axisPair.getChartLegendBounds().getWidth() - 3 * Chart.CHART_PADDING);
            int height = this.getSizeHint();
            Rectangle xAxisRectangle = new Rectangle(xOffset, yOffset, width, height);
            this.paintZone = xAxisRectangle;
            // g.setColor(Color.green);
            // g.draw(xAxisRectangle);

            axisTitle.paint(g);
            axisTick.paint(g);
            axisLine.paint(g);

            xOffset = (int) paintZone.getX();
            yOffset = (int) paintZone.getY();
            width = ((int) paintZone.getWidth());
            height = (int) (axisTitle.isVisible ? axisTitle.getBounds().getHeight() : 0 + axisTick.getBounds().getHeight() + axisLine.getBounds().getHeight());
            bounds = new Rectangle(xOffset, yOffset, width, height);
            bounds = new Rectangle(xOffset, yOffset, width, height);
            // g.setColor(Color.yellow);
            // g.draw(bounds);
        }

    }
}
