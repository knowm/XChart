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

import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/**
 * @author timmolter
 */
public class Plot_<ST extends Styler, S extends Series> implements ChartPart {

  final Chart<ST, S> chart;
  Rectangle2D bounds;

  PlotSurface_ plotSurface;
  PlotContent_ plotContent;

  /**
   * Constructor
   *
   * @param chart
   */
  Plot_(Chart<ST, S> chart) {

    this.chart = chart;
  }

  @Override
  public void paint(Graphics2D g) {

    // g.setColor(Color.red);
    // g.draw(bounds);

    plotSurface.paint(g);
    if (chart.getSeriesMap().isEmpty()) {
      return;
    }
    plotContent.paint(g);
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }
}
