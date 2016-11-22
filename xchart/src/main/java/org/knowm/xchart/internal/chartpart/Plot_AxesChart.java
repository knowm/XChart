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

import org.knowm.xchart.XYSeries;
import org.knowm.xchart.graphics.Graphics;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

import java.awt.geom.Rectangle2D;

/**
 * @author timmolter
 */
public class Plot_AxesChart<ST extends Styler, S extends Series> extends Plot_ {

  AxesChartStyler stylerAxesChart;

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_AxesChart(Chart<AxesChartStyler, XYSeries> chart) {

    super(chart);
    stylerAxesChart = chart.getStyler();
    this.plotSurface = new PlotSurface_AxesChart<AxesChartStyler, XYSeries>(chart);
  }

  @Override
  public void paint(Graphics g) {

    // calculate bounds
    double xOffset = chart.getYAxis().getBounds(g.getRenderContext()).getX() + chart.getYAxis().getBounds(g.getRenderContext()).getWidth()

        + (stylerAxesChart.isYAxisTicksVisible() ? stylerAxesChart.getPlotMargin() : 0);

    double yOffset = chart.getYAxis().getBounds(g.getRenderContext()).getY();
    double width = chart.getXAxis().getBounds(g.getRenderContext()).getWidth();
    double height = chart.getYAxis().getBounds(g.getRenderContext()).getHeight();
    this.bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);

    super.paint(g);
  }
}
