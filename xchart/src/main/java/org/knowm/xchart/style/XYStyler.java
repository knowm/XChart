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
package org.knowm.xchart.style;

import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;

/**
 * @author timmolter
 */
public class XYStyler extends AxesChartStyler {

  private XYSeriesRenderStyle xySeriesRenderStyle;

  /**
   * Constructor
   */
  public XYStyler() {

    this.setAllStyles();
    super.setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    xySeriesRenderStyle = XYSeriesRenderStyle.Line; // set default to line
  }

  public XYSeriesRenderStyle getDefaultSeriesRenderStyle() {

    return xySeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (line, scatter, area, etc.) You can override the series render style individually on each Series object.
   *
   * @param xySeriesRenderStyle
   */
  public XYStyler setDefaultSeriesRenderStyle(XYSeriesRenderStyle xySeriesRenderStyle) {

    this.xySeriesRenderStyle = xySeriesRenderStyle;
    return this;
  }

  /**
   * Set the theme the styler should use
   *
   * @param theme
   */
  public void setTheme(Theme theme) {

    this.theme = theme;
    super.setAllStyles();
  }

  public Theme getTheme() {

    return theme;
  }
}
