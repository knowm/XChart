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
package org.knowm.xchart.internal.style;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author timmolter
 */
public abstract class Styler_AxesChart extends Styler {

  // Chart Axes ///////////////////////////////
  private boolean xAxisTitleVisible;
  private boolean yAxisTitleVisible;
  private Font axisTitleFont;
  private boolean xAxisTicksVisible;
  private boolean yAxisTicksVisible;
  private Font axisTickLabelsFont;
  private int axisTickMarkLength;
  private int axisTickPadding;
  private Color axisTickMarksColor;
  private Stroke axisTickMarksStroke;
  private Color axisTickLabelsColor;
  private boolean isAxisTicksLineVisible;
  private boolean isAxisTicksMarksVisible;
  private int plotMargin;
  private int axisTitlePadding;
  private int xAxisTickMarkSpacingHint;
  private int yAxisTickMarkSpacingHint;
  private boolean isXAxisLogarithmic;
  private boolean isYAxisLogarithmic;
  private Double xAxisMin;
  private Double xAxisMax;
  private Double yAxisMin;
  private Double yAxisMax;
  private TextAlignment xAxisLabelAlignment = TextAlignment.Centre;
  private TextAlignment yAxisLabelAlignment = TextAlignment.Left;
  private int xAxisLabelRotation = 0;

  // Chart Plot Area ///////////////////////////////
  private boolean isPlotGridHorizontalLinesVisible;
  private boolean isPlotGridVerticalLinesVisible;
  private boolean isPlotTicksMarksVisible;
  private Color plotGridLinesColor;
  private Stroke plotGridLinesStroke;

  // Bar Charts ///////////////////////////////
  private double barWidthPercentage;
  private boolean isBarsOverlapped;
  private boolean isBarFilled;

  // Line, Scatter, Area Charts ///////////////////////////////
  private int markerSize;

  // Error Bars ///////////////////////////////
  private Color errorBarsColor;
  private boolean isErrorBarsColorSeriesColor;

  // Formatting ////////////////////////////////
  private Locale locale;
  private TimeZone timezone;
  private String datePattern;
  private String decimalPattern;
  private String xAxisDecimalPattern;
  private String yAxisDecimalPattern;

  @Override
  protected void setAllStyles() {

    super.setAllStyles();

    // axes
    xAxisTitleVisible = theme.isXAxisTitleVisible();
    yAxisTitleVisible = theme.isYAxisTitleVisible();
    axisTitleFont = theme.getAxisTitleFont();
    xAxisTicksVisible = theme.isXAxisTicksVisible();
    yAxisTicksVisible = theme.isYAxisTicksVisible();
    axisTickLabelsFont = theme.getAxisTickLabelsFont();
    axisTickMarkLength = theme.getAxisTickMarkLength();
    axisTickPadding = theme.getAxisTickPadding();
    axisTickMarksColor = theme.getAxisTickMarksColor();
    axisTickMarksStroke = theme.getAxisTickMarksStroke();
    axisTickLabelsColor = theme.getAxisTickLabelsColor();
    isAxisTicksLineVisible = theme.isAxisTicksLineVisible();
    isAxisTicksMarksVisible = theme.isAxisTicksMarksVisible();
    plotMargin = theme.getPlotMargin();
    axisTitlePadding = theme.getAxisTitlePadding();
    xAxisTickMarkSpacingHint = theme.getXAxisTickMarkSpacingHint();
    yAxisTickMarkSpacingHint = theme.getYAxisTickMarkSpacingHint();
    isXAxisLogarithmic = false;
    isYAxisLogarithmic = false;
    xAxisMin = null;
    xAxisMax = null;
    yAxisMin = null;
    yAxisMax = null;

    // Chart Plot Area ///////////////////////////////
    isPlotGridVerticalLinesVisible = theme.isPlotGridVerticalLinesVisible();
    isPlotGridHorizontalLinesVisible = theme.isPlotGridHorizontalLinesVisible();
    isPlotTicksMarksVisible = theme.isPlotTicksMarksVisible();
    plotGridLinesColor = theme.getPlotGridLinesColor();
    plotGridLinesStroke = theme.getPlotGridLinesStroke();

    // Bar Charts ///////////////////////////////
    barWidthPercentage = theme.getBarWidthPercentage();
    isBarsOverlapped = theme.isBarsOverlapped();
    isBarFilled = theme.isBarFilled();

    // Line, Scatter, Area Charts ///////////////////////////////
    markerSize = theme.getMarkerSize();

    // Error Bars ///////////////////////////////
    errorBarsColor = theme.getErrorBarsColor();
    isErrorBarsColorSeriesColor = theme.isErrorBarsColorSeriesColor();

    // Formatting ////////////////////////////////
    locale = Locale.getDefault();
    timezone = TimeZone.getDefault();
    datePattern = null; // if not null, this override pattern will be used
    decimalPattern = null;
    xAxisDecimalPattern = null;
    yAxisDecimalPattern = null;
  }

  // Chart Axes ///////////////////////////////

  /**
   * Set the x-axis title visibility
   *
   * @param isVisible
   */
  public void setXAxisTitleVisible(boolean xAxisTitleVisible) {

    this.xAxisTitleVisible = xAxisTitleVisible;
  }

  public boolean isXAxisTitleVisible() {

    return xAxisTitleVisible;
  }

  /**
   * Set the y-axis title visibility
   *
   * @param isVisible
   */

  public void setYAxisTitleVisible(boolean yAxisTitleVisible) {

    this.yAxisTitleVisible = yAxisTitleVisible;
  }

  public boolean isYAxisTitleVisible() {

    return yAxisTitleVisible;
  }

  /**
   * Set the x- and y-axis titles visibility
   *
   * @param isVisible
   */

  public void setAxisTitlesVisible(boolean isVisible) {

    this.xAxisTitleVisible = isVisible;
    this.yAxisTitleVisible = isVisible;
  }

  /**
   * Set the x- and y-axis title font
   *
   * @param axisTitleFont
   */

  public void setAxisTitleFont(Font axisTitleFont) {

    this.axisTitleFont = axisTitleFont;
  }

  public Font getAxisTitleFont() {

    return axisTitleFont;
  }

  /**
   * Set the x-axis tick marks and labels visibility
   *
   * @param isVisible
   */

  public void setXAxisTicksVisible(boolean xAxisTicksVisible) {

    this.xAxisTicksVisible = xAxisTicksVisible;
  }

  public boolean isXAxisTicksVisible() {

    return xAxisTicksVisible;
  }

  /**
   * Set the y-axis tick marks and labels visibility
   *
   * @param isVisible
   */

  public void setYAxisTicksVisible(boolean yAxisTicksVisible) {

    this.yAxisTicksVisible = yAxisTicksVisible;
  }

  public boolean isYAxisTicksVisible() {

    return yAxisTicksVisible;
  }

  /**
   * Set the x- and y-axis tick marks and labels visibility
   *
   * @param isVisible
   */

  public void setAxisTicksVisible(boolean isVisible) {

    this.xAxisTicksVisible = isVisible;
    this.yAxisTicksVisible = isVisible;
  }

  /**
   * Set the x- and y-axis tick label font
   *
   * @param foxAxisTicksFontnt
   */

  public void setAxisTickLabelsFont(Font axisTicksFont) {

    this.axisTickLabelsFont = axisTicksFont;
  }

  public Font getAxisTickLabelsFont() {

    return axisTickLabelsFont;
  }

  /**
   * set the axis tick mark length
   *
   * @param axisTickMarkLength
   */

  public void setAxisTickMarkLength(int axisTickMarkLength) {

    this.axisTickMarkLength = axisTickMarkLength;
  }

  public int getAxisTickMarkLength() {

    return axisTickMarkLength;
  }

  /**
   * sets the padding between the tick labels and the tick marks
   *
   * @param axisTickPadding
   */
  public void setAxisTickPadding(int axisTickPadding) {

    this.axisTickPadding = axisTickPadding;
  }

  public int getAxisTickPadding() {

    return axisTickPadding;
  }

  /**
   * sets the axis tick mark color
   *
   * @param axisTickColor
   */

  public void setAxisTickMarksColor(Color axisTickColor) {

    this.axisTickMarksColor = axisTickColor;
  }

  public Color getAxisTickMarksColor() {

    return axisTickMarksColor;
  }

  /**
   * sets the axis tick marks Stroke
   *
   * @param axisTickMarksStroke
   */

  public void setAxisTickMarksStroke(Stroke axisTickMarksStroke) {

    this.axisTickMarksStroke = axisTickMarksStroke;
  }

  public Stroke getAxisTickMarksStroke() {

    return axisTickMarksStroke;
  }

  /**
   * sets the axis tick label color
   *
   * @param axisTickLabelsColor
   */

  public void setAxisTickLabelsColor(Color axisTickLabelsColor) {

    this.axisTickLabelsColor = axisTickLabelsColor;
  }

  public Color getAxisTickLabelsColor() {

    return axisTickLabelsColor;
  }

  /**
   * sets the visibility of the line parallel to the plot edges that go along with the tick marks
   *
   * @param isAxisTicksLineVisible
   */

  public void setAxisTicksLineVisible(boolean isAxisTicksLineVisible) {

    this.isAxisTicksLineVisible = isAxisTicksLineVisible;
  }

  public boolean isAxisTicksLineVisible() {

    return isAxisTicksLineVisible;
  }

  /**
   * sets the visibility of the tick marks
   *
   * @param isAxisTicksMarksVisible
   */

  public void setAxisTicksMarksVisible(boolean isAxisTicksMarksVisible) {

    this.isAxisTicksMarksVisible = isAxisTicksMarksVisible;
  }

  public boolean isAxisTicksMarksVisible() {

    return isAxisTicksMarksVisible;
  }

  /**
   * sets the margin around the plot area
   *
   * @param plotMargin
   */

  public void setPlotMargin(int plotMargin) {

    this.plotMargin = plotMargin;
  }

  public int getPlotMargin() {

    return plotMargin;
  }

  /**
   * sets the padding between the axis title and the tick labels
   *
   * @param axisTitlePadding
   */

  public void setAxisTitlePadding(int axisTitlePadding) {

    this.axisTitlePadding = axisTitlePadding;
  }

  public int getAxisTitlePadding() {

    return axisTitlePadding;
  }

  /**
   * set the spacing between tick marks for the X-Axis
   *
   * @param xAxisTickMarkSpacingHint
   */

  public void setXAxisTickMarkSpacingHint(int xAxisTickMarkSpacingHint) {

    this.xAxisTickMarkSpacingHint = xAxisTickMarkSpacingHint;
  }

  public int getXAxisTickMarkSpacingHint() {

    return xAxisTickMarkSpacingHint;
  }

  /**
   * set the spacing between tick marks for the Y-Axis
   *
   * @param xAxisTickMarkSpacingHint
   */

  public void setYAxisTickMarkSpacingHint(int yAxisTickMarkSpacingHint) {

    this.yAxisTickMarkSpacingHint = yAxisTickMarkSpacingHint;
  }

  public int getYAxisTickMarkSpacingHint() {

    return yAxisTickMarkSpacingHint;
  }

  /**
   * sets the X-Axis to be rendered with a logarithmic scale or not
   *
   * @param isxAxisLogarithmic
   */

  public void setXAxisLogarithmic(boolean isXAxisLogarithmic) {

    this.isXAxisLogarithmic = isXAxisLogarithmic;
  }

  public boolean isXAxisLogarithmic() {

    return isXAxisLogarithmic;
  }

  /**
   * sets the Y-Axis to be rendered with a logarithmic scale or not
   *
   * @param isyAxisLogarithmic
   */

  public void setYAxisLogarithmic(boolean isYAxisLogarithmic) {

    this.isYAxisLogarithmic = isYAxisLogarithmic;
  }

  public boolean isYAxisLogarithmic() {

    return isYAxisLogarithmic;
  }

  public void setXAxisMin(double xAxisMin) {

    this.xAxisMin = xAxisMin;
  }

  public Double getXAxisMin() {

    return xAxisMin;
  }

  public void setXAxisMax(double xAxisMax) {

    this.xAxisMax = xAxisMax;
  }

  public Double getXAxisMax() {

    return xAxisMax;
  }

  public void setYAxisMin(double yAxisMin) {

    this.yAxisMin = yAxisMin;
  }

  public Double getYAxisMin() {

    return yAxisMin;
  }

  public void setYAxisMax(double yAxisMax) {

    this.yAxisMax = yAxisMax;
  }

  public Double getYAxisMax() {

    return yAxisMax;
  }

  public TextAlignment getXAxisLabelAlignment() {

    return xAxisLabelAlignment;
  }

  public void setXAxisLabelAlignment(TextAlignment xAxisLabelAlignment) {

    this.xAxisLabelAlignment = xAxisLabelAlignment;
  }

  public TextAlignment getYAxisLabelAlignment() {

    return yAxisLabelAlignment;
  }

  public void setYAxisLabelAlignment(TextAlignment yAxisLabelAlignment) {

    this.yAxisLabelAlignment = yAxisLabelAlignment;
  }

  public int getXAxisLabelRotation() {

    return xAxisLabelRotation;
  }

  public void setXAxisLabelRotation(int xAxisLabelRotation) {

    this.xAxisLabelRotation = xAxisLabelRotation;
  }

  // Chart Plot Area ///////////////////////////////

  /**
   * sets the visibility of the gridlines on the plot area
   *
   * @param isPlotGridLinesVisible
   */

  public void setPlotGridLinesVisible(boolean isPlotGridLinesVisible) {

    this.isPlotGridHorizontalLinesVisible = isPlotGridLinesVisible;
    this.isPlotGridVerticalLinesVisible = isPlotGridLinesVisible;
  }

  public boolean isPlotGridLinesVisible() {

    return isPlotGridHorizontalLinesVisible && isPlotGridVerticalLinesVisible;
  }

  /**
   * sets the visibility of the horizontal gridlines on the plot area
   *
   * @param isPlotGridLinesVisible
   */

  public void setPlotGridHorizontalLinesVisible(boolean isPlotGridHorizontalLinesVisible) {

    this.isPlotGridHorizontalLinesVisible = isPlotGridHorizontalLinesVisible;
  }

  public boolean isPlotGridHorizontalLinesVisible() {

    return isPlotGridHorizontalLinesVisible;
  }

  /**
   * sets the visibility of the vertical gridlines on the plot area
   *
   * @param isPlotGridLinesVisible
   */

  public void setPlotGridVerticalLinesVisible(boolean isPlotGridVerticalLinesVisible) {

    this.isPlotGridVerticalLinesVisible = isPlotGridVerticalLinesVisible;
  }

  public boolean isPlotGridVerticalLinesVisible() {

    return isPlotGridVerticalLinesVisible;
  }

  /**
   * sets the visibility of the ticks marks inside the plot area
   *
   * @param isPlotTicksMarksVisible
   */

  public void setPlotTicksMarksVisible(boolean isPlotTicksMarksVisible) {

    this.isPlotTicksMarksVisible = isPlotTicksMarksVisible;
  }

  public boolean isPlotTicksMarksVisible() {

    return isPlotTicksMarksVisible;
  }

  /**
   * set the plot area's grid lines color
   *
   * @param plotGridLinesColor
   */

  public void setPlotGridLinesColor(Color plotGridLinesColor) {

    this.plotGridLinesColor = plotGridLinesColor;
  }

  public Color getPlotGridLinesColor() {

    return plotGridLinesColor;
  }

  /**
   * set the plot area's grid lines Stroke
   *
   * @param plotGridLinesStroke
   */

  public void setPlotGridLinesStroke(Stroke plotGridLinesStroke) {

    this.plotGridLinesStroke = plotGridLinesStroke;
  }

  public Stroke getPlotGridLinesStroke() {

    return plotGridLinesStroke;
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
   * set whether or no bars are filled with a solid color or empty.
   *
   * @param isBarFilled
   */

  public void setBarFilled(boolean isBarFilled) {

    this.isBarFilled = isBarFilled;
  }

  public boolean isBarFilled() {

    return isBarFilled;
  }

  // Line, Scatter, Area Charts ///////////////////////////////

  /**
   * Sets the size of the markers in pixels
   *
   * @param markerSize
   */

  public void setMarkerSize(int markerSize) {

    this.markerSize = markerSize;
  }

  public int getMarkerSize() {

    return markerSize;
  }

  // Error Bars ///////////////////////////////

  /**
   * Sets the color of the error bars
   *
   * @param errorBarsColor
   */

  public void setErrorBarsColor(Color errorBarsColor) {

    this.errorBarsColor = errorBarsColor;
  }

  public Color getErrorBarsColor() {

    return errorBarsColor;
  }

  /**
   * Set true if the the error bar color should match the series color
   *
   * @return
   */

  public void setErrorBarsColorSeriesColor(boolean isErrorBarsColorSeriesColor) {

    this.isErrorBarsColorSeriesColor = isErrorBarsColorSeriesColor;
  }

  public boolean isErrorBarsColorSeriesColor() {

    return isErrorBarsColorSeriesColor;
  }

  // Formatting ////////////////////////////////

  /**
   * Set the locale to use for rendering the chart
   *
   * @param locale - the locale to use when formatting Strings and dates for the axis tick labels
   */

  public void setLocale(Locale locale) {

    this.locale = locale;
  }

  public Locale getLocale() {

    return locale;
  }

  /**
   * Set the timezone to use for formatting Date axis tick labels
   *
   * @param timezone the timezone to use when formatting date data
   */

  public void setTimezone(TimeZone timezone) {

    this.timezone = timezone;
  }

  public TimeZone getTimezone() {

    return timezone;
  }

  /**
   * Set the String formatter for Data x-axis
   *
   * @param pattern - the pattern describing the date and time format
   */

  public void setDatePattern(String datePattern) {

    this.datePattern = datePattern;
  }

  public String getDatePattern() {

    return datePattern;
  }

  /**
   * Set the decimal formatter for all tick labels
   *
   * @param pattern - the pattern describing the decimal format
   */

  public void setDecimalPattern(String decimalPattern) {

    this.decimalPattern = decimalPattern;
  }

  public String getDecimalPattern() {

    return decimalPattern;
  }

  public String getXAxisDecimalPattern() {

    return xAxisDecimalPattern;
  }

  /**
   * Set the decimal formatting pattern for the X-Axis
   *
   * @param xAxisDecimalPattern
   */

  public void setXAxisDecimalPattern(String xAxisDecimalPattern) {

    this.xAxisDecimalPattern = xAxisDecimalPattern;
  }

  public String getYAxisDecimalPattern() {

    return yAxisDecimalPattern;
  }

  /**
   * Set the decimal formatting pattern for the Y-Axis
   *
   * @param yAxisDecimalPattern
   */

  public void setYAxisDecimalPattern(String yAxisDecimalPattern) {

    this.yAxisDecimalPattern = yAxisDecimalPattern;
  }

}
