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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.xeiam.xcharts.interfaces.IChartPart;

/**
 * AxisLine
 */
public class AxisLine implements IChartPart {

    /** the axis */
    private Axis axis;

    /** the visibility state of grid */
    protected boolean isVisible = true; // default to true

    /** the foreground color */
    private Color foreground = ChartColor.getAWTColor(ChartColor.DARK_GREY); // default foreground color

    /** the line style */
    private BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

    /** the bounds */
    private Rectangle bounds = new Rectangle(); // default all-zero rectangle

    /**
     * Constructor
     * 
     * @param axis the axis
     */
    public AxisLine(Axis axis) {
        this.axis = axis;

    }

    public BasicStroke getStroke() {
        return stroke;
    }

    @Override
    public void paint(Graphics2D g) {

        g.setColor(foreground);

        if (axis.getDirection() == Axis.Direction.Y) {

            int xOffset = (int) (axis.getAxisTick().getBounds().getX() + axis.getAxisTick().getBounds().getWidth());
            int yOffset = (int) (axis.getPaintZone().getY());

            g.setColor(foreground);
            g.setStroke(stroke);

            g.drawLine(xOffset, yOffset, xOffset, (int) (yOffset + axis.getPaintZone().getHeight()));

            // bounds
            bounds = new Rectangle(xOffset, yOffset, (int) stroke.getLineWidth(), (int) axis.getPaintZone().getHeight());
            // g.setColor(Color.green);
            // g.draw(bounds);

        } else {

            int xOffset = (int) (axis.getPaintZone().getX());
            int yOffset = (int) (axis.getAxisTick().getBounds().getY() - stroke.getLineWidth());

            g.setColor(foreground);
            g.setStroke(stroke);

            g.drawLine(xOffset, yOffset, (int) (xOffset + axis.getPaintZone().getWidth()), yOffset);

            // bounds
            bounds = new Rectangle(xOffset, yOffset, (int) axis.getPaintZone().getWidth(), (int) stroke.getLineWidth());
            // g.setColor(Color.green);
            // g.draw(bounds);

        }
    }

    @Override
    public Rectangle getBounds() {
        // TODO Auto-generated method stub
        return bounds;
    }
}
