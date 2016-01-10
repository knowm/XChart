/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
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

import org.knowm.xchart.Series_XY;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.style.StyleManager;
import org.knowm.xchart.internal.style.StyleManagerAxesChart;

/**
 * Draws the plot background, the plot border and the horizontal and vertical grid lines
 *
 * @author timmolter
 */
public class PlotSurfaceAxesChart<SM extends StyleManager, S extends Series> extends PlotSurface {

  private final StyleManagerAxesChart styleManagerAxesChart;

  /**
   * Constructor
   *
   * @param chart
   */
  protected PlotSurfaceAxesChart(Chart<StyleManagerAxesChart, Series_XY> chart) {

    super(chart);
    this.styleManagerAxesChart = chart.getStyleManager();
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle2D bounds = getBounds();

    // paint plot background
    Shape rect = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    g.setColor(styleManagerAxesChart.getPlotBackgroundColor());
    g.fill(rect);

    // paint plot border
    if (styleManagerAxesChart.isPlotBorderVisible()) {
      g.setColor(styleManagerAxesChart.getPlotBorderColor());
      // g.setStroke(getChartPainter().getStyleManager().getAxisTickMarksStroke());
      g.draw(rect);
    }

    // paint grid lines and/or inner plot ticks

    // horizontal
    if (styleManagerAxesChart.isPlotGridHorizontalLinesVisible() || styleManagerAxesChart.isPlotTicksMarksVisible()) {

      List<Double> yAxisTickLocations = chart.getYAxis().getAxisTickCalculator().getTickLocations();
      for (int i = 0; i < yAxisTickLocations.size(); i++) {

        double yOffset = bounds.getY() + bounds.getHeight() - yAxisTickLocations.get(i);

        if (yOffset > bounds.getY() && yOffset < bounds.getY() + bounds.getHeight()) {

          // draw lines
          if (styleManagerAxesChart.isPlotGridHorizontalLinesVisible()) {

            g.setColor(styleManagerAxesChart.getPlotGridLinesColor());
            g.setStroke(styleManagerAxesChart.getPlotGridLinesStroke());
            Shape line = new Line2D.Double(bounds.getX(), yOffset, bounds.getX() + bounds.getWidth(), yOffset);
            g.draw(line);
          }

          // tick marks
          if (styleManagerAxesChart.isPlotTicksMarksVisible()) {

            g.setColor(styleManagerAxesChart.getAxisTickMarksColor());
            g.setStroke(styleManagerAxesChart.getAxisTickMarksStroke());
            Shape line = new Line2D.Double(bounds.getX(), yOffset, bounds.getX() + styleManagerAxesChart.getAxisTickMarkLength(), yOffset);
            g.draw(line);
            line = new Line2D.Double(bounds.getX() + bounds.getWidth(), yOffset, bounds.getX() + bounds.getWidth() - styleManagerAxesChart.getAxisTickMarkLength(), yOffset);
            g.draw(line);
          }
        }
      }
    }

    // vertical

    if ((styleManagerAxesChart.isPlotGridVerticalLinesVisible() || styleManagerAxesChart.isPlotTicksMarksVisible())) {

      List<Double> xAxisTickLocations = chart.getXAxis().getAxisTickCalculator().getTickLocations();
      for (int i = 0; i < xAxisTickLocations.size(); i++) {

        double tickLocation = xAxisTickLocations.get(i);
        double xOffset = bounds.getX() + tickLocation;

        if (xOffset > bounds.getX() && xOffset < bounds.getX() + bounds.getWidth()) {

          // draw lines
          if (styleManagerAxesChart.isPlotGridVerticalLinesVisible()) {
            g.setColor(styleManagerAxesChart.getPlotGridLinesColor());
            g.setStroke(styleManagerAxesChart.getPlotGridLinesStroke());

            Shape line = new Line2D.Double(xOffset, bounds.getY(), xOffset, bounds.getY() + bounds.getHeight());
            g.draw(line);
          }
          // tick marks
          if (styleManagerAxesChart.isPlotTicksMarksVisible()) {

            g.setColor(styleManagerAxesChart.getAxisTickMarksColor());
            g.setStroke(styleManagerAxesChart.getAxisTickMarksStroke());

            Shape line = new Line2D.Double(xOffset, bounds.getY(), xOffset, bounds.getY() + styleManagerAxesChart.getAxisTickMarkLength());
            g.draw(line);
            line = new Line2D.Double(xOffset, bounds.getY() + bounds.getHeight(), xOffset, bounds.getY() + bounds.getHeight() - styleManagerAxesChart.getAxisTickMarkLength());
            g.draw(line);
          }
        }
      }
    }
  }

}
