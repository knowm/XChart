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

import org.knowm.xchart.Series_XY.ChartXYSeriesRenderStyle;
import org.knowm.xchart.style.Styler_AxesChart;
import org.knowm.xchart.style.Theme_;

/**
 * @author timmolter
 */
public class Styler_XY extends Styler_AxesChart {

  private ChartXYSeriesRenderStyle chartXYSeriesRenderStyle;

  /**
   * Constructor
   */
  public Styler_XY() {

    this.setAllStyles();
    super.setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    chartXYSeriesRenderStyle = ChartXYSeriesRenderStyle.Line; // set default to line
  }

  public ChartXYSeriesRenderStyle getDefaultSeriesRenderStyle() {

    return chartXYSeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (line, scatter, area, etc.) You can override the series render style individually on each Series object.
   *
   * @param chartXYSeriesRenderStyle
   */
  public void setDefaultSeriesRenderStyle(ChartXYSeriesRenderStyle chartXYSeriesRenderStyle) {

    this.chartXYSeriesRenderStyle = chartXYSeriesRenderStyle;
  }

  /**
   * Set the theme the styler should use
   *
   * @param theme
   */
  protected void setTheme(Theme_ theme) {

    this.theme = theme;
    super.setAllStyles();
  }

  public Theme_ getTheme() {

    return theme;
  }

}
