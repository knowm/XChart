/*******************************************************************************
 * Copyright (c) 2008-2011 SWTChart project. All rights reserved.
 *
 * This code is distributed under the terms of the Eclipse Public License v1.0
 * which is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.xeiam.xcharts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import com.xeiam.xcharts.interfaces.IHideable;

/**
 * Chart Title
 */
public class ChartTitle implements IHideable {

    /** the chart */
    protected Chart chart;

    /** the title text */
    protected String text = ""; // default to ""

    /** the visibility state of title */
    protected boolean isVisible = false; // default to false

    /** the font */
    private Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14); // default font

    /** the foreground color */
    private Color foreground = ChartColor.getAWTColor(ChartColor.DARK_GREY); // default foreground color

    /** the bounds */
    private Rectangle bounds = new Rectangle(); // default all-zero rectangle

    /**
     * Constructor
     */
    public ChartTitle(Chart pChart) {
        this.chart = pChart;
    }

    public void setText(String text) {
        if (text.trim().equalsIgnoreCase("")) {
            this.isVisible = false;
        } else {
            this.isVisible = true;
        }
        this.text = text;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public void paint(Graphics2D g) {

        if (isVisible) {

            FontRenderContext frc = g.getFontRenderContext();
            TextLayout textLayout = new TextLayout(this.text, this.font, frc);
            Rectangle rectangle = textLayout.getPixelBounds(null, 0, 0);
            // System.out.println(rectangle);
            int xOffset = (int) ((chart.getWidth() - rectangle.getWidth()) / 2.0);
            int yOffset = (int) (Chart.CHART_PADDING - rectangle.getY());

            g.setColor(foreground);
            textLayout.draw(g, xOffset, yOffset);

            bounds = new Rectangle(xOffset, (int) (yOffset + rectangle.getY()), (int) rectangle.getWidth(), (int) rectangle.getHeight());
            // g.setColor(Color.green);
            // g.draw(bounds);
        }

    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }
}