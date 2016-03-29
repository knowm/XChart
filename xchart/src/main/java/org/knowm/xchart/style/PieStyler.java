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

import java.awt.Font;

import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;

/**
 * @author timmolter
 */
public class PieStyler extends Styler {

  private PieSeriesRenderStyle chartPieSeriesRenderStyle;

  public enum AnnotationType {

    Percentage, Label, LabelAndPercentage
  }

  private boolean isCircular;
  private double startAngleInDegrees;
  private Font annotationFont;
  private double annotationDistance;
  private AnnotationType annotationType;

  /**
   * Constructor
   */
  public PieStyler() {

    this.setAllStyles();
    super.setAllStyles();
  }

  @Override
  protected void setAllStyles() {

    chartPieSeriesRenderStyle = PieSeriesRenderStyle.Pie; // set default to pie, donut may be a future one
    isCircular = theme.isCircular();
    annotationFont = theme.getPieFont();
    annotationDistance = theme.getAnnotationDistance();
    annotationType = theme.getAnnotationType();
  }

  public PieSeriesRenderStyle getDefaultSeriesRenderStyle() {

    return chartPieSeriesRenderStyle;
  }

  /**
   * Sets the default series render style for the chart (line, scatter, area, etc.) You can override the series render style individually on each Series object.
   *
   * @param chartPieSeriesRenderStyle
   */
  public PieStyler setDefaultSeriesRenderStyle(PieSeriesRenderStyle chartPieSeriesRenderStyle) {

    this.chartPieSeriesRenderStyle = chartPieSeriesRenderStyle;
    return this;
  }

  public boolean isCircular() {

    return isCircular;
  }

  /**
   * Sets whether or not the pie chart is forced to be circular. Otherwise it's shape is oval, matching the containing plot.
   *
   * @param isCircular
   */
  public PieStyler setCircular(boolean isCircular) {

    this.isCircular = isCircular;
    return this;
  }

  public double getStartAngleInDegrees() {

    return startAngleInDegrees;
  }

  /**
   * Sets the start angle in degrees. Zero degrees is straight up.
   *
   * @param startAngleInDegrees
   */
  public PieStyler setStartAngleInDegrees(double startAngleInDegrees) {

    this.startAngleInDegrees = startAngleInDegrees;
    return this;
  }

  public Font getAnnotationFont() {

    return annotationFont;
  }

  /**
   * Sets the font used on the Pie Chart's annotations
   *
   * @param pieFont
   */
  public PieStyler setAnnotationFont(Font pieFont) {

    this.annotationFont = pieFont;
    return this;
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

  public AnnotationType getAnnotationType() {

    return annotationType;
  }

  /**
   * Sets the Pie chart's annotation type
   *
   * @param annotationType
   */
  public PieStyler setAnnotationType(AnnotationType annotationType) {

    this.annotationType = annotationType;
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
