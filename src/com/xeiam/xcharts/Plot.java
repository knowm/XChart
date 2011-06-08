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

import com.xeiam.xcharts.interfaces.IChartPart;

/**
 * @author timmolter
 */
public class Plot implements IChartPart {

    private Chart chart;

    private PlotSurface plotSurface;

    private PlotContent plotContent;

    public static final int PLOT_PADDING = 5;

    /** the bounds */
    private Rectangle bounds = new Rectangle(); // default all-zero rectangle

    public Plot(Chart chart) {
        this.chart = chart;
        this.plotSurface = new PlotSurface(chart, this);
        this.plotContent = new PlotContent(chart, this);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void paint(Graphics2D g) {

        // calculate bounds
        int xOffset = (int) (chart.getAxisPair().getYAxis().getBounds().getX() + chart.getAxisPair().getYAxis().getBounds().getWidth() + PLOT_PADDING);
        int yOffset = (int) (chart.getAxisPair().getYAxis().getBounds().getY());
        int width = (int) chart.getAxisPair().getXAxis().getBounds().getWidth();
        int height = (int) chart.getAxisPair().getYAxis().getBounds().getHeight();
        bounds = new Rectangle(xOffset, yOffset, width, height);
        // g.setColor(Color.green);
        // g.draw(bounds);

        plotSurface.paint(g);
        plotContent.paint(g);

    }

}
