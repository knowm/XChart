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
import java.awt.geom.Rectangle2D;

import org.knowm.xchart.Series_Pie;
import org.knowm.xchart.StyleManagerPie;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.style.StyleManager;
import org.knowm.xchart.internal.style.StyleManager.LegendPosition;

/**
 * @author timmolter
 */
public class Plot_Pie<SM extends StyleManager, S extends Series> extends Plot_ {

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_Pie(Chart<StyleManagerPie, Series_Pie> chart) {

    super(chart);
    this.plotContent = new PlotContent_Pie<StyleManagerPie, Series_Pie>(chart);
    this.plotSurface = new PlotSurfacePie<StyleManagerPie, Series_Pie>(chart);
  }

  @Override
  public void paint(Graphics2D g) {

    // calculate bounds
    double xOffset = chart.getStyleManager().getChartPadding();

    double yOffset = chart.getChartTitle().getBounds().getY() + chart.getStyleManager().getChartPadding();

    double width =

        chart.getWidth()

            - (chart.getStyleManager().getLegendPosition() == LegendPosition.OutsideE ? chart.getChartLegend().getBounds().getWidth() : 0)

            - 2 * chart.getStyleManager().getChartPadding()

            - (chart.getStyleManager().getLegendPosition() == LegendPosition.OutsideE && chart.getStyleManager().isLegendVisible() ? chart.getStyleManager().getChartPadding() : 0);

    double height = chart.getHeight() - chart.getChartTitle().getBounds().getHeight() - 2 * chart.getStyleManager().getChartPadding();

    bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);

    super.paint(g);
  }
}
