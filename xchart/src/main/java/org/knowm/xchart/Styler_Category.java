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

import org.knowm.xchart.Series_Category.ChartCategorySeriesRenderStyle;
import org.knowm.xchart.internal.style.Styler_AxesChart;
import org.knowm.xchart.internal.style.Theme_;

/**
 * @author timmolter
 */
public class Styler_Category extends Styler_AxesChart {

  private ChartCategorySeriesRenderStyle chartCategorySeriesRenderStyle;

  // Bar Charts ///////////////////////////////
  private double barWidthPercentage;
  private boolean isBarsOverlapped;

  /**
   * Constructor
   */
  public Styler_Category() {

    this.setAllStyles();
    super.setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    this.chartCategorySeriesRenderStyle = ChartCategorySeriesRenderStyle.Bar; // set default to bar

    // Bar Charts ///////////////////////////////
    barWidthPercentage = theme.getBarWidthPercentage();
    isBarsOverlapped = theme.isBarsOverlapped();
  }

  public ChartCategorySeriesRenderStyle getDefaultSeriesRenderStyle() {

    return chartCategorySeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (bar, stick, line, scatter, area, etc.) You can override the series render style individually on each Series object.
   *
   * @param chartCategorySeriesRenderStyle
   */
  public void setDefaultSeriesRenderStyle(ChartCategorySeriesRenderStyle chartCategorySeriesRenderStyle) {

    this.chartCategorySeriesRenderStyle = chartCategorySeriesRenderStyle;
  }

  // Bar Charts ///////////////////////////////

  /**
   * set the width of a single bar in a bar chart. full width is 100%, i.e. 1.0
   *
   * @param barWidthPercentage
   */
  public void setBarWidthPercentage(double barWidthPercentage) {

    this.barWidthPercentage = barWidthPercentage;
  }

  public double getBarWidthPercentage() {

    return barWidthPercentage;
  }

  /**
   * set whether or no bars are overlapped. Otherwise they are places side-by-side
   *
   * @param isBarsOverlapped
   */
  public void setBarsOverlapped(boolean isBarsOverlapped) {

    this.isBarsOverlapped = isBarsOverlapped;
  }

  public boolean isBarsOverlapped() {

    return isBarsOverlapped;
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
