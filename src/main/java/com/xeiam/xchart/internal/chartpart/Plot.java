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
package com.xeiam.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.internal.interfaces.IChartPart;

/**
 * @author timmolter
 */
public class Plot implements IChartPart {

  /** parent */
  protected Chart chart;

  public PlotSurface plotSurface;

  protected PlotContent plotContent;

  public static final int PLOT_PADDING = 3;

  /** the bounds */
  private Rectangle bounds;

  public Plot(Chart chart) {

    this.chart = chart;
    this.plotSurface = new PlotSurface(this);
    this.plotContent = new PlotContent(this);
  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle();

    // calculate bounds
    int xOffset = (int) (chart.axisPair.yAxis.getBounds().getX() + chart.axisPair.yAxis.getBounds().getWidth() + (chart.axisPair.yAxis.axisTick.isVisible ? (Plot.PLOT_PADDING + 1) : 0));
    int yOffset = (int) (chart.axisPair.yAxis.getBounds().getY());
    int width = (int) chart.axisPair.xAxis.getBounds().getWidth();
    int height = (int) chart.axisPair.yAxis.getBounds().getHeight();
    bounds = new Rectangle(xOffset, yOffset, width, height);
    // g.setColor(Color.green);
    // g.draw(bounds);

    plotSurface.paint(g);
    plotContent.paint(g);

  }

}
