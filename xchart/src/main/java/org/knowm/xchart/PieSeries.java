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
package org.knowm.xchart;

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;

/**
 * A Series containing Pie data to be plotted on a Chart
 *
 * @author timmolter
 */
public class PieSeries extends Series {

  public enum PieSeriesRenderStyle implements RenderableSeries {

    Pie(LegendRenderType.Box),

    Donut(LegendRenderType.Box);

    private final LegendRenderType legendRenderType;

    private PieSeriesRenderStyle(LegendRenderType legendRenderType) {
      this.legendRenderType = legendRenderType;
    }

    @Override
    public LegendRenderType getLegendRenderType() {

      return legendRenderType;
    }
  }

  private PieSeriesRenderStyle chartPieSeriesRenderStyle = null;

  private Number value;

  /**
   * Constructor
   *
   * @param name
   * @param value
   */
  public PieSeries(String name, Number value) {

    super(name);
    this.value = value;
  }

  /**
   * *This is an internal method which shouldn't be called from client code. Use PieChart.updatePieSeries instead!
   *
   * @param value
   */
  public void replaceData(Number value) {

    this.value = value;
  }

  public PieSeriesRenderStyle getChartPieSeriesRenderStyle() {

    return chartPieSeriesRenderStyle;
  }

  public PieSeries setChartPieSeriesRenderStyle(PieSeriesRenderStyle chartPieSeriesRenderStyle) {

    this.chartPieSeriesRenderStyle = chartPieSeriesRenderStyle;
    return this;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return chartPieSeriesRenderStyle.getLegendRenderType();
  }

  public Number getValue() {

    return value;
  }

  public void setValue(Number value) {

    this.value = value;
  }

}
