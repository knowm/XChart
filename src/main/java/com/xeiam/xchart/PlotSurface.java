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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import com.xeiam.xchart.interfaces.IChartPart;
import com.xeiam.xchart.interfaces.IHideable;

/**
 * @author timmolter
 */
public class PlotSurface implements IChartPart, IHideable {

  private Chart chart;

  private Plot plot;

  /** the foreground color */
  private Color foreground = ChartColor.getAWTColor(ChartColor.GREY); // default foreground color

  /** the background color */
  private Color background = ChartColor.getAWTColor(ChartColor.LIGHT_GREY); // default background color

  /** the line style */
  private BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 3.0f }, 0.0f);

  /** the visibility state of PlotSurface */
  protected boolean isVisible = true; // default to true

  /**
   * Constructor
   * 
   * @param chart
   * @param plot
   */
  public PlotSurface(Chart chart, Plot plot) {

    this.chart = chart;
    this.plot = plot;
  }

  @Override
  public Rectangle getBounds() {

    return plot.getBounds();
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle bounds = plot.getBounds();

    // paint background
    Rectangle backgroundRectangle = new Rectangle((int) bounds.getX() - 1, (int) bounds.getY(), (int) (bounds.getWidth()), (int) bounds.getHeight());
    g.setColor(background);
    g.fill(backgroundRectangle);
    Rectangle borderRectangle = new Rectangle((int) bounds.getX() - 1, (int) bounds.getY(), (int) (bounds.getWidth()), (int) bounds.getHeight());
    g.setColor(ChartColor.getAWTColor(ChartColor.DARK_GREY));
    g.draw(borderRectangle);

    // paint grid lines
    if (isVisible) {
      // horizontal
      List<Integer> yAxisTickLocations = chart.getAxisPair().getYAxis().getAxisTick().getTickLocations();
      for (int i = 0; i < yAxisTickLocations.size(); i++) {

        int tickLocation = yAxisTickLocations.get(i);

        g.setColor(foreground);
        g.setStroke(stroke);
        // System.out.println("bounds.getY()= " + bounds.getY());
        g.drawLine((int) bounds.getX(), (int) (bounds.getY() + bounds.getHeight() - tickLocation), (int) (bounds.getX() + bounds.getWidth() - 2), (int) (bounds.getY() + bounds.getHeight() - tickLocation));
      }

      // vertical
      List<Integer> xAxisTickLocations = chart.getAxisPair().getXAxis().getAxisTick().getTickLocations();
      for (int i = 0; i < xAxisTickLocations.size(); i++) {

        int tickLocation = xAxisTickLocations.get(i);

        g.setColor(foreground);
        g.setStroke(stroke);

        g.drawLine((int) (bounds.getX() + tickLocation - 1), (int) (bounds.getY() + 1), (int) (bounds.getX() + tickLocation - 1), (int) (bounds.getY() + bounds.getHeight() - 1));
      }
    }
  }

  @Override
  public void setVisible(boolean isVisible) {

    this.isVisible = isVisible;

  }
}
