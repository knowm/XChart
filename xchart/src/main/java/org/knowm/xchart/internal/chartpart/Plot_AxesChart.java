/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

/**
 * @author timmolter
 */
public class Plot_AxesChart<ST extends Styler, S extends Series> extends Plot_ {

  /**
   * Constructor
   *
   * @param chart
   */
  Plot_AxesChart(Chart<AxesChartStyler, XYSeries> chart) {

    super(chart);
    this.plotSurface = new PlotSurface_AxesChart<AxesChartStyler, XYSeries>(chart);
  }

  @Override
  public void paint(Graphics2D g) {
    Rectangle2D yAxisBounds = chart.getAxisPair().getLeftYAxisBounds();
    Rectangle2D xAxisBounds = chart.getXAxis().getBounds();

    // calculate bounds
    double xOffset = xAxisBounds.getX();
    double yOffset = yAxisBounds.getY();
    double width = xAxisBounds.getWidth();
    double height = yAxisBounds.getHeight();
    this.bounds = new Rectangle2D.Double(xOffset, yOffset, width, height);

    super.paint(g);
  }
}
