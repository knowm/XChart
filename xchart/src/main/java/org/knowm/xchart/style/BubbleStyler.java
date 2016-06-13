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
package org.knowm.xchart.style;

import org.knowm.xchart.BubbleSeries.BubbleSeriesRenderStyle;

/**
 * @author timmolter
 */
public class BubbleStyler extends AxesChartStyler {

  private BubbleSeriesRenderStyle bubbleChartSeriesRenderStyle;

  /**
   * Constructor
   */
  public BubbleStyler() {

    this.setAllStyles();
    super.setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    bubbleChartSeriesRenderStyle = BubbleSeriesRenderStyle.Round; // set default to Round
  }

  public BubbleSeriesRenderStyle getDefaultSeriesRenderStyle() {

    return bubbleChartSeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (Round is the only one for now) You can override the series render style individually on each Series object.
   *
   * @param chartXYSeriesRenderStyle
   */
  public BubbleStyler setDefaultSeriesRenderStyle(BubbleSeriesRenderStyle bubbleChartSeriesRenderStyle) {

    this.bubbleChartSeriesRenderStyle = bubbleChartSeriesRenderStyle;
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
