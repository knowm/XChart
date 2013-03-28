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

import com.xeiam.xchart.StyleManager.ChartType;

/**
 * @author timmolter
 */
public class Plot implements ChartPart {

  /** parent */
  private final ChartPainter chartPainter;

  /** the bounds */
  private Rectangle bounds;

  private PlotSurface plotSurface;

  private PlotContent plotContent;

  /**
   * Constructor
   * 
   * @param chartPainter
   */
  public Plot(ChartPainter chartPainter) {

    this.chartPainter = chartPainter;
    this.plotSurface = new PlotSurface(this);

  }

  @Override
  public Rectangle getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle();

    // calculate bounds
    int xOffset = (int) (chartPainter.getAxisPair().getyAxis().getBounds().getX()

    + chartPainter.getAxisPair().getyAxis().getBounds().getWidth()

    + (chartPainter.getStyleManager().isYAxisTicksVisible() ? (chartPainter.getStyleManager().getPlotPadding() + 1) : 0)

    );

    int yOffset = (int) (chartPainter.getAxisPair().getyAxis().getBounds().getY());
    int width = (int) chartPainter.getAxisPair().getxAxis().getBounds().getWidth();
    int height = (int) chartPainter.getAxisPair().getyAxis().getBounds().getHeight();
    bounds = new Rectangle(xOffset, yOffset, width, height);
    // g.setColor(Color.green);
    // g.draw(bounds);

    plotSurface.paint(g);
    if (getChartPainter().getStyleManager().getChartType() == ChartType.Bar) {
      this.plotContent = new PlotContentBarChart(this);
    } else {
      this.plotContent = new PlotContentLineChart(this);
    }
    plotContent.paint(g);

  }

  @Override
  public ChartPainter getChartPainter() {

    return chartPainter;
  }
}
