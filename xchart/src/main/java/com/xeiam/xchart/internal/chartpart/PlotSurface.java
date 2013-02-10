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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import com.xeiam.xchart.internal.interfaces.IChartPart;
import com.xeiam.xchart.style.ChartColor;

/**
 * @author timmolter
 */
public class PlotSurface implements IChartPart {

  /** parent */
  private Plot plot;

  /** the gridLines Color */
  private Color gridLinesColor;

  /** the background color */
  private Color foregroundColor;

  /** the line style */
  private BasicStroke stroke;

  /**
   * Constructor
   * 
   * @param plot
   */
  protected PlotSurface(Plot plot) {

    this.plot = plot;
    gridLinesColor = ChartColor.getAWTColor(ChartColor.GREY); // default gridLines color
    foregroundColor = ChartColor.getAWTColor(ChartColor.LIGHT_GREY); // default foreground Color color
    stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 3.0f }, 0.0f);
  }

  @Override
  public Rectangle getBounds() {

    return plot.getBounds();
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle bounds = plot.getBounds();

    // paint foreground
    Rectangle backgroundRectangle = new Rectangle((int) bounds.getX() - 1, (int) bounds.getY(), (int) (bounds.getWidth()), (int) bounds.getHeight());
    g.setColor(foregroundColor);
    g.fill(backgroundRectangle);
    Rectangle borderRectangle = new Rectangle((int) bounds.getX() - 1, (int) bounds.getY(), (int) (bounds.getWidth()), (int) bounds.getHeight());
    g.setColor(plot.chart.getStyleManager().getChartBordersColor());
    g.draw(borderRectangle);

    // paint grid lines
    if (plot.chart.getStyleManager().isPlotGridLinesVisible()) {
      // horizontal
      List<Integer> yAxisTickLocations = plot.chart.axisPair.yAxis.axisTick.tickLocations;
      for (int i = 0; i < yAxisTickLocations.size(); i++) {

        int tickLocation = yAxisTickLocations.get(i);

        g.setColor(gridLinesColor);
        g.setStroke(stroke);
        // System.out.println("bounds.getY()= " + bounds.getY());
        g.drawLine((int) bounds.getX(), (int) (bounds.getY() + bounds.getHeight() - tickLocation), (int) (bounds.getX() + bounds.getWidth() - 2),
            (int) (bounds.getY() + bounds.getHeight() - tickLocation));
      }

      // vertical
      List<Integer> xAxisTickLocations = plot.chart.axisPair.xAxis.axisTick.tickLocations;
      for (int i = 0; i < xAxisTickLocations.size(); i++) {

        int tickLocation = xAxisTickLocations.get(i);

        g.setColor(gridLinesColor);
        g.setStroke(stroke);

        g.drawLine((int) (bounds.getX() + tickLocation - 1), (int) (bounds.getY() + 1), (int) (bounds.getX() + tickLocation - 1), (int) (bounds.getY() + bounds.getHeight() - 1));
      }
    }
  }

  /**
   * @param gridLinesColor the gridLinesColor to set
   */
  public void setGridLinesColor(Color gridLinesColor) {

    this.gridLinesColor = gridLinesColor;
  }

  /**
   * @param foregroundColor the foregroundColor to set
   */
  public void setForegroundColor(Color foregroundColor) {

    this.foregroundColor = foregroundColor;
  }

}
