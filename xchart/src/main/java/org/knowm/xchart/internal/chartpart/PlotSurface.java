/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
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
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Draws the plot background, the plot border and the horizontal and vertical grid lines
 *
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
  public Rectangle2D getBounds() {

    return plot.getBounds();
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle2D bounds = plot.getBounds();

    // paint plot background
    Shape rect = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    g.setColor(getChartPainter().getStyleManager().getPlotBackgroundColor());
    g.fill(rect);

    // paint plot border
    if (getChartPainter().getStyleManager().isPlotBorderVisible()) {
      g.setColor(getChartPainter().getStyleManager().getPlotBorderColor());
      // g.setStroke(getChartPainter().getStyleManager().getAxisTickMarksStroke());
      g.draw(rect);
    }

    // paint grid lines and/or inner plot ticks

    // horizontal
    if (getChartPainter().getStyleManager().isPlotGridHorizontalLinesVisible() || getChartPainter().getStyleManager().isPlotTicksMarksVisible()) {
      List<Double> yAxisTickLocations = getChartPainter().getAxisPair().getYAxis().getAxisTickCalculator().getTickLocations();
      for (int i = 0; i < yAxisTickLocations.size(); i++) {

        double yOffset = bounds.getY() + bounds.getHeight() - yAxisTickLocations.get(i);

        if (yOffset > bounds.getY() && yOffset < bounds.getY() + bounds.getHeight()) {

          // draw lines
          if (getChartPainter().getStyleManager().isPlotGridHorizontalLinesVisible()) {

            g.setColor(getChartPainter().getStyleManager().getPlotGridLinesColor());
            g.setStroke(getChartPainter().getStyleManager().getPlotGridLinesStroke());
            Shape line = new Line2D.Double(bounds.getX(), yOffset, bounds.getX() + bounds.getWidth(), yOffset);
            g.draw(line);
          }

          // tick marks
          if (getChartPainter().getStyleManager().isPlotTicksMarksVisible()) {

            g.setColor(getChartPainter().getStyleManager().getAxisTickMarksColor());
            g.setStroke(getChartPainter().getStyleManager().getAxisTickMarksStroke());
            Shape line = new Line2D.Double(bounds.getX(), yOffset, bounds.getX() + getChartPainter().getStyleManager().getAxisTickMarkLength(), yOffset);
            g.draw(line);
            line = new Line2D.Double(bounds.getX() + bounds.getWidth(), yOffset, bounds.getX() + bounds.getWidth() - getChartPainter().getStyleManager().getAxisTickMarkLength(), yOffset);
            g.draw(line);
          }
        }
      }
    }

    // vertical
    if (getChartPainter().getStyleManager().isPlotGridVerticalLinesVisible() || getChartPainter().getStyleManager().isPlotTicksMarksVisible()) {

      List<Double> xAxisTickLocations = getChartPainter().getAxisPair().getXAxis().getAxisTickCalculator().getTickLocations();
      for (int i = 0; i < xAxisTickLocations.size(); i++) {

        double tickLocation = xAxisTickLocations.get(i);
        double xOffset = bounds.getX() + tickLocation;

        if (xOffset > bounds.getX() && xOffset < bounds.getX() + bounds.getWidth()) {

          // draw lines
          if (getChartPainter().getStyleManager().isPlotGridVerticalLinesVisible()) {
            g.setColor(getChartPainter().getStyleManager().getPlotGridLinesColor());
            g.setStroke(getChartPainter().getStyleManager().getPlotGridLinesStroke());

            Shape line = new Line2D.Double(xOffset, bounds.getY(), xOffset, bounds.getY() + bounds.getHeight());
            g.draw(line);
          }
          // tick marks
          if (getChartPainter().getStyleManager().isPlotTicksMarksVisible()) {

            g.setColor(getChartPainter().getStyleManager().getAxisTickMarksColor());
            g.setStroke(getChartPainter().getStyleManager().getAxisTickMarksStroke());

            Shape line = new Line2D.Double(xOffset, bounds.getY(), xOffset, bounds.getY() + getChartPainter().getStyleManager().getAxisTickMarkLength());
            g.draw(line);
            line = new Line2D.Double(xOffset, bounds.getY() + bounds.getHeight(), xOffset, bounds.getY() + bounds.getHeight() - getChartPainter().getStyleManager().getAxisTickMarkLength());
            g.draw(line);
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
