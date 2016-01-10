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

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.style.StyleManager;

/**
 * @author timmolter
 */
public class Plot_<SM extends StyleManager, S extends Series> implements ChartPart {

  protected final Chart<SM, S> chart;
  protected Rectangle2D bounds;

  protected PlotSurface plotSurface;
  protected PlotContent_ plotContent;

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_(Chart<SM, S> chart) {

    this.chart = chart;
  }

  @Override
  public void paint(Graphics2D g) {

    // g.setColor(Color.green);
    // g.draw(bounds);

    plotSurface.paint(g);
    plotContent.paint(g);

    // if (chart.getChartType() == Chart.ChartType.Category) {
    // if (((StyleManagerCategory) chart.getStyleManager()).getChartCategorySeriesType() == ChartCategorySeriesType.Bar) {
    // this.plotContent = new PlotContentCategoricalChart_Bar(this);
    // }
    //
    // else {
    // this.plotContent = new PlotContentCategoricalChart_Line_Area_Scatter(this);
    // }
    // }
    // else if (chart.getChartType() == Chart.ChartType.Pie) {
    // this.plotContent = new PlotContentCategoricalChart_Pie(this);
    // }
    // else {
    // this.plotContent = new PlotContentXYChart(chart);
    // }
    // plotContent.paint(g);

  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }
}
