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
package org.knowm.xchart;

import org.knowm.xchart.internal.ChartBuilder;

/**
 * @author timmolter
 */
public class OHLCChartBuilder extends ChartBuilder<OHLCChartBuilder, OHLCChart> {

  String xAxisTitle = "";
  String yAxisTitle = "";

  public OHLCChartBuilder() {

  }

  public OHLCChartBuilder xAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
    return this;
  }

  public OHLCChartBuilder yAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
    return this;
  }

  /**
   * return fully built XYChart
   *
   * @return a XYChart
   */
  @Override
  public OHLCChart build() {

    return new OHLCChart(this);
  }
}
