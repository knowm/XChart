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
import java.util.List;

import com.xeiam.xchart.StyleManager.ChartType;

/**
 * @author timmolter
 */
public class PlotSurface implements ChartPart {

  /** parent */
  private Plot plot;

  /**
   * Constructor
   * 
   * @param plot
   */
  protected PlotSurface(Plot plot) {

    this.plot = plot;
  }

  @Override
  public Rectangle getBounds() {

    return plot.getBounds();
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle bounds = plot.getBounds();

    // paint plot background
    Rectangle backgroundRectangle = new Rectangle((int) bounds.getX() - 1, (int) bounds.getY(), (int) (bounds.getWidth()), (int) bounds.getHeight());
    g.setColor(getChartPainter().getStyleManager().getPlotBackgroundColor());
    g.fill(backgroundRectangle);

    // paint plot border
    if (getChartPainter().getStyleManager().isPlotBorderVisible()) {
      Rectangle borderRectangle = new Rectangle((int) bounds.getX() - 1, (int) bounds.getY(), (int) (bounds.getWidth()), (int) bounds.getHeight());
      g.setColor(getChartPainter().getStyleManager().getPlotBorderColor());
      g.draw(borderRectangle);
    }

    // paint grid lines and/or inner plot tics
    if (getChartPainter().getStyleManager().isPlotGridLinesVisible() || getChartPainter().getStyleManager().isPlotTicksMarksVisible()) {

      // horizontal
      List<Integer> yAxisTickLocations = getChartPainter().getAxisPair().getyAxis().getAxisTick().getTickLocations();
      for (int i = 0; i < yAxisTickLocations.size(); i++) {

        int tickLocation = yAxisTickLocations.get(i);
        int yOffset = (int) (bounds.getY() + bounds.getHeight() - tickLocation);

        // draw lines
        if (getChartPainter().getStyleManager().isPlotGridLinesVisible()) {

          g.setColor(getChartPainter().getStyleManager().getPlotGridLinesColor());
          g.setStroke(getChartPainter().getStyleManager().getPlotGridLinesStroke());
          // System.out.println("bounds.getY()= " + bounds.getY());
          g.drawLine((int) bounds.getX(), yOffset, (int) (bounds.getX() + bounds.getWidth() - 2), yOffset);
        }
        // tick marks
        if (getChartPainter().getStyleManager().isPlotTicksMarksVisible()) {

          g.setColor(getChartPainter().getStyleManager().getAxisTickMarksColor());
          g.setStroke(getChartPainter().getStyleManager().getAxisTickMarksStroke());

          g.drawLine((int) bounds.getX(), yOffset, (int) bounds.getX() + getChartPainter().getStyleManager().getAxisTickMarkLength(), yOffset);
          g.drawLine((int) (bounds.getX() + bounds.getWidth() - 2), yOffset, (int) (bounds.getX() + bounds.getWidth() - 2) - getChartPainter().getStyleManager().getAxisTickMarkLength(), yOffset);

        }
      }

      // vertical
      if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar
          && (getChartPainter().getStyleManager().isPlotGridLinesVisible() || getChartPainter().getStyleManager().isPlotTicksMarksVisible())) {

        List<Integer> xAxisTickLocations = getChartPainter().getAxisPair().getxAxis().getAxisTick().getTickLocations();
        for (int i = 0; i < xAxisTickLocations.size(); i++) {

          int tickLocation = xAxisTickLocations.get(i);
          int xOffset = (int) (bounds.getX() + tickLocation - 1);

          // draw lines
          if (getChartPainter().getStyleManager().isPlotGridLinesVisible()) {
            g.setColor(getChartPainter().getStyleManager().getPlotGridLinesColor());
            g.setStroke(getChartPainter().getStyleManager().getPlotGridLinesStroke());

            g.drawLine(xOffset, (int) (bounds.getY()), xOffset, (int) (bounds.getY() + bounds.getHeight() - 1));
          }
          // tick marks
          if (getChartPainter().getStyleManager().isPlotTicksMarksVisible()) {

            g.setColor(getChartPainter().getStyleManager().getAxisTickMarksColor());
            g.setStroke(getChartPainter().getStyleManager().getAxisTickMarksStroke());

            g.drawLine(xOffset, (int) (bounds.getY()), xOffset, (int) (bounds.getY()) + getChartPainter().getStyleManager().getAxisTickMarkLength());
            g.drawLine(xOffset, (int) (bounds.getY() + bounds.getHeight() - 1), xOffset, (int) (bounds.getY() + bounds.getHeight() - 1) - getChartPainter().getStyleManager().getAxisTickMarkLength());

          }
        }
      }
    }
  }

  @Override
  public ChartPainter getChartPainter() {

    return plot.getChartPainter();
  }

}
