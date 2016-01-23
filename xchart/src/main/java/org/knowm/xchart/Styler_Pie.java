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

import java.awt.Font;

import org.knowm.xchart.Series_Pie.ChartPieSeriesRenderStyle;
import org.knowm.xchart.internal.style.Styler;
import org.knowm.xchart.internal.style.Theme_;

/**
 * @author timmolter
 */
public class Styler_Pie extends Styler {

  private ChartPieSeriesRenderStyle chartPieSeriesRenderStyle;

  private double pieSize;
  private boolean isCircular;
  private double startAngleInDegrees;
  private Font annotationFont;
  private double annotationDistance;

  /**
   * Constructor
   */
  public Styler_Pie() {

    this.setAllStyles();
    super.setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    chartPieSeriesRenderStyle = ChartPieSeriesRenderStyle.Pie; // set default to pie, donut may be a future one
    pieSize = theme.getPieSize();
    isCircular = theme.isCircular();
    annotationFont = theme.getPieFont();
    annotationDistance = theme.getAnnotationDistance();
  }

  public ChartPieSeriesRenderStyle getChartPieSeriesRenderStyle() {

    return chartPieSeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (line, scatter, area, etc.) You can override the series render style individually on each Series object.
   *
   * @param chartPieSeriesRenderStyle
   */
  public void setChartPieSeriesRenderStyle(ChartPieSeriesRenderStyle chartPieSeriesRenderStyle) {

    this.chartPieSeriesRenderStyle = chartPieSeriesRenderStyle;
  }

  public double getPieSize() {

    return pieSize;
  }

  /**
   * Sets the amount of space that the pie chart fills. Full fill is 100%, i.e. 1.0
   *
   * @param pieSize
   */
  public void setPieSize(double pieSize) {

    this.pieSize = pieSize;
  }

  public boolean isCircular() {

    return isCircular;
  }

  /**
   * Sets whether or not the pie chart is forced to be circular. Otherwise it's shape is oval, matching the containing plot.
   *
   * @param isCircular
   */
  public void setCircular(boolean isCircular) {

    this.isCircular = isCircular;
  }

  public double getStartAngleInDegrees() {

    return startAngleInDegrees;
  }

  /**
   * Sets the start angle in degrees. Zero degrees is straight up.
   *
   * @param startAngleInDegrees
   */
  public void setStartAngleInDegrees(double startAngleInDegrees) {

    this.startAngleInDegrees = startAngleInDegrees;
  }

  public Font getAnnotationFont() {

    return annotationFont;
  }

  /**
   * Sets the font used on the Pie Chart's annotations
   *
   * @param pieFont
   */
  public void setAnnotationFont(Font pieFont) {

    this.annotationFont = pieFont;
  }

  public double getAnnotationDistance() {

    return annotationDistance;
  }

  /**
   * Sets the distance of the pie chart's annotation where 0 is the center, 1 is at the edge and greater than 1 is outside of the pie chart.
   *
   * @param annotationDistance
   */
  public void setAnnotationDistance(double annotationDistance) {

    this.annotationDistance = annotationDistance;
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
