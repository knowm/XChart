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
import java.awt.geom.Rectangle2D;

import org.knowm.xchart.StyleManager.ChartType;
import org.knowm.xchart.internal.chartpart.ChartInternal.ChartInternalType;

/**
 * @author timmolter
 */
public class Plot implements ChartPart {

  /** parent */
  private final ChartInternal chartInternal;

  /** the bounds */
  private Rectangle2D bounds;

  private PlotSurface plotSurface;

  private PlotContent plotContent;

  /**
   * Constructor
   *
   * @param chartInternal
   */
  public Plot(ChartInternal chartInternal) {

    this.chartInternal = chartInternal;
    this.plotSurface = new PlotSurface(this);
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    bounds = new Rectangle2D.Double();

    // calculate bounds
    double xOffset = chartInternal.getAxisPair().getYAxis().getBounds().getX()

        + chartInternal.getAxisPair().getYAxis().getBounds().getWidth()

        + (chartInternal.getStyleManager().isYAxisTicksVisible() ? (chartInternal.getStyleManager().getPlotPadding()) : 0)

    ;

    double yOffset = chartInternal.getAxisPair().getYAxis().getBounds().getY();
    double width = chartInternal.getAxisPair().getXAxis().getBounds().getWidth();
    double height = chartInternal.getAxisPair().getYAxis().getBounds().getHeight();
    bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);
    // g.setColor(Color.green);
    // g.draw(bounds);

    plotSurface.paint(g);

    if (getChartInternal().getChartInternalType() == ChartInternalType.Category) {
      if (getChartInternal().getStyleManager().getChartType() == ChartType.Bar) {
        this.plotContent = new PlotContentCategoricalChart_Bar(this);
      }

      else {
        this.plotContent = new PlotContentCategoricalChart_Line_Area_Scatter(this);
      }
    }
    else if (getChartInternal().getChartInternalType() == ChartInternalType.Pie) {
      this.plotContent = new PlotContentCategoricalChart_Pie(this);
    }
    else {
      this.plotContent = new PlotContentNumericalChart(this);
    }
    plotContent.paint(g);

  }

  @Override
  public ChartInternal getChartInternal() {

    return chartInternal;
  }
}
