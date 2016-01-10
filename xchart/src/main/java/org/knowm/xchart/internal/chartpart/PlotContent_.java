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

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.style.StyleManagerAxesChart;

/**
 * @author timmolter
 */
public abstract class PlotContent_<SM extends StyleManagerAxesChart, S extends Series> implements ChartPart {

  protected final Chart<SM, S> chart;

  protected final Stroke errorBarStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

  /**
   * Constructor
   *
   * @param chart - The Chart
   */
  protected PlotContent_(Chart<SM, S> chart) {

    this.chart = chart;
  }

  @Override
  public Rectangle2D getBounds() {

    return chart.getPlot().getBounds();
  }

}
