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

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author timmolter
 */
public abstract class AxesChartStyler extends Styler {

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
    this.xAxisTitleVisible = theme.isXAxisTitleVisible();
    this.yAxisTitleVisible = theme.isYAxisTitleVisible();
    this.axisTitleFont = theme.getAxisTitleFont();
    this.xAxisTicksVisible = theme.isXAxisTicksVisible();
    this.yAxisTicksVisible = theme.isYAxisTicksVisible();
    this.axisTickLabelsFont = theme.getAxisTickLabelsFont();
    this.axisTickMarkLength = theme.getAxisTickMarkLength();
    this.axisTickPadding = theme.getAxisTickPadding();
    this.axisTickMarksColor = theme.getAxisTickMarksColor();
    this.axisTickMarksStroke = theme.getAxisTickMarksStroke();
    this.axisTickLabelsColor = theme.getAxisTickLabelsColor();
    this.isAxisTicksLineVisible = theme.isAxisTicksLineVisible();
    this.isAxisTicksMarksVisible = theme.isAxisTicksMarksVisible();
    this.plotMargin = theme.getPlotMargin();
    this.axisTitlePadding = theme.getAxisTitlePadding();
    this.xAxisTickMarkSpacingHint = theme.getXAxisTickMarkSpacingHint();
    this.yAxisTickMarkSpacingHint = theme.getYAxisTickMarkSpacingHint();
    this.isXAxisLogarithmic = false;
    this.isYAxisLogarithmic = false;
    this.xAxisMin = null;
    this.xAxisMax = null;
    this.yAxisMin = null;
    this.yAxisMax = null;

    // Chart Plot Area ///////////////////////////////
    this.isPlotGridVerticalLinesVisible = theme.isPlotGridVerticalLinesVisible();
    this.isPlotGridHorizontalLinesVisible = theme.isPlotGridHorizontalLinesVisible();
    this.isPlotTicksMarksVisible = theme.isPlotTicksMarksVisible();
    this.plotGridLinesColor = theme.getPlotGridLinesColor();
    this.plotGridLinesStroke = theme.getPlotGridLinesStroke();

    // Line, Scatter, Area Charts ///////////////////////////////
    this.markerSize = theme.getMarkerSize();

    // Error Bars ///////////////////////////////
    this.errorBarsColor = theme.getErrorBarsColor();
    this.isErrorBarsColorSeriesColor = theme.isErrorBarsColorSeriesColor();

    // Formatting ////////////////////////////////
    this.locale = Locale.getDefault();
    this.timezone = TimeZone.getDefault();
    this.datePattern = null; // if not null, this override pattern will be used
    this.decimalPattern = null;
    this.xAxisDecimalPattern = null;
    this.yAxisDecimalPattern = null;

    // Annotations ////////////////////////////////
    this.hasAnnotations = false;
  }

  // Chart Axes ///////////////////////////////

  /**
   * Set the x-axis title visibility
   *
   * @param isVisible
   */
  public AxesChartStyler setXAxisTitleVisible(boolean xAxisTitleVisible) {

    this.xAxisTitleVisible = xAxisTitleVisible;
    return this;
  }

  public boolean isXAxisTitleVisible() {

    return xAxisTitleVisible;
  }

  /**
   * Set the y-axis title visibility
   *
   * @param isVisible
   */
  public AxesChartStyler setYAxisTitleVisible(boolean yAxisTitleVisible) {

    this.yAxisTitleVisible = yAxisTitleVisible;
    return this;
  }

  public boolean isYAxisTitleVisible() {

    return yAxisTitleVisible;
  }

  /**
   * Set the x- and y-axis titles visibility
   *
   * @param isVisible
   */
  public AxesChartStyler setAxisTitlesVisible(boolean isVisible) {

    this.xAxisTitleVisible = isVisible;
    this.yAxisTitleVisible = isVisible;
    return this;
  }

  /**
   * Set the x- and y-axis title font
   *
   * @param axisTitleFont
   */
  public AxesChartStyler setAxisTitleFont(Font axisTitleFont) {

    this.axisTitleFont = axisTitleFont;
    return this;
  }

  public Font getAxisTitleFont() {

    return axisTitleFont;
  }

  /**
   * Set the x-axis tick marks and labels visibility
   *
   * @param isVisible
   */
  public AxesChartStyler setXAxisTicksVisible(boolean xAxisTicksVisible) {

    this.xAxisTicksVisible = xAxisTicksVisible;
    return this;
  }

  public boolean isXAxisTicksVisible() {

    return xAxisTicksVisible;
  }

  /**
   * Set the y-axis tick marks and labels visibility
   *
   * @param isVisible
   */
  public AxesChartStyler setYAxisTicksVisible(boolean yAxisTicksVisible) {

    this.yAxisTicksVisible = yAxisTicksVisible;
    return this;
  }

  public boolean isYAxisTicksVisible() {

    return yAxisTicksVisible;
  }

  /**
   * Set the x- and y-axis tick marks and labels visibility
   *
   * @param isVisible
   */
  public AxesChartStyler setAxisTicksVisible(boolean isVisible) {

    this.xAxisTicksVisible = isVisible;
    this.yAxisTicksVisible = isVisible;
    return this;
  }

  /**
   * Set the x- and y-axis tick label font
   *
   * @param axisTicksFont
   */
  public AxesChartStyler setAxisTickLabelsFont(Font axisTicksFont) {

    this.axisTickLabelsFont = axisTicksFont;
    return this;
  }

  public Font getAxisTickLabelsFont() {

    return axisTickLabelsFont;
  }

  /**
   * Set the axis tick mark length
   *
   * @param axisTickMarkLength
   */
  public AxesChartStyler setAxisTickMarkLength(int axisTickMarkLength) {

    this.axisTickMarkLength = axisTickMarkLength;
    return this;
  }

  public int getAxisTickMarkLength() {

    return axisTickMarkLength;
  }

  /**
   * sets the padding between the tick labels and the tick marks
   *
   * @param axisTickPadding
   */
  public AxesChartStyler setAxisTickPadding(int axisTickPadding) {

    this.axisTickPadding = axisTickPadding;
    return this;
  }

  public int getAxisTickPadding() {

    return axisTickPadding;
  }

  /**
   * sets the axis tick mark color
   *
   * @param axisTickColor
   */
  public AxesChartStyler setAxisTickMarksColor(Color axisTickColor) {

    this.axisTickMarksColor = axisTickColor;
    return this;
  }

  public Color getAxisTickMarksColor() {

    return axisTickMarksColor;
  }

  /**
   * sets the axis tick marks Stroke
   *
   * @param axisTickMarksStroke
   */
  public AxesChartStyler setAxisTickMarksStroke(Stroke axisTickMarksStroke) {

    this.axisTickMarksStroke = axisTickMarksStroke;
    return this;
  }

  public Stroke getAxisTickMarksStroke() {

    return axisTickMarksStroke;
  }

  /**
   * sets the axis tick label color
   *
   * @param axisTickLabelsColor
   */
  public AxesChartStyler setAxisTickLabelsColor(Color axisTickLabelsColor) {

    this.axisTickLabelsColor = axisTickLabelsColor;
    return this;
  }

  public Color getAxisTickLabelsColor() {

    return axisTickLabelsColor;
  }

  /**
   * sets the visibility of the line parallel to the plot edges that go along with the tick marks
   *
   * @param isAxisTicksLineVisible
   */
  public AxesChartStyler setAxisTicksLineVisible(boolean isAxisTicksLineVisible) {

    this.isAxisTicksLineVisible = isAxisTicksLineVisible;
    return this;
  }

  public boolean isAxisTicksLineVisible() {

    return isAxisTicksLineVisible;
  }

  /**
   * sets the visibility of the tick marks
   *
   * @param isAxisTicksMarksVisible
   */
  public AxesChartStyler setAxisTicksMarksVisible(boolean isAxisTicksMarksVisible) {

    this.isAxisTicksMarksVisible = isAxisTicksMarksVisible;
    return this;
  }

  public boolean isAxisTicksMarksVisible() {

    return isAxisTicksMarksVisible;
  }

  /**
   * sets the margin around the plot area
   *
   * @param plotMargin
   */
  public AxesChartStyler setPlotMargin(int plotMargin) {

    this.plotMargin = plotMargin;
    return this;
  }

  public int getPlotMargin() {

    return plotMargin;
  }

  /**
   * sets the padding between the axis title and the tick labels
   *
   * @param axisTitlePadding
   */
  public AxesChartStyler setAxisTitlePadding(int axisTitlePadding) {

    this.axisTitlePadding = axisTitlePadding;
    return this;
  }

  public int getAxisTitlePadding() {

    return axisTitlePadding;
  }

  /**
   * set the spacing between tick marks for the X-Axis
   *
   * @param xAxisTickMarkSpacingHint
   */
  public AxesChartStyler setXAxisTickMarkSpacingHint(int xAxisTickMarkSpacingHint) {

    this.xAxisTickMarkSpacingHint = xAxisTickMarkSpacingHint;
    return this;
  }

  public int getXAxisTickMarkSpacingHint() {

    return xAxisTickMarkSpacingHint;
  }

  /**
   * set the spacing between tick marks for the Y-Axis
   *
   * @param xAxisTickMarkSpacingHint
   */
  public AxesChartStyler setYAxisTickMarkSpacingHint(int yAxisTickMarkSpacingHint) {

    this.yAxisTickMarkSpacingHint = yAxisTickMarkSpacingHint;
    return this;
  }

  public int getYAxisTickMarkSpacingHint() {

    return yAxisTickMarkSpacingHint;
  }

  /**
   * sets the X-Axis to be rendered with a logarithmic scale or not
   *
   * @param isxAxisLogarithmic
   */
  public AxesChartStyler setXAxisLogarithmic(boolean isXAxisLogarithmic) {

    this.isXAxisLogarithmic = isXAxisLogarithmic;
    return this;
  }

  public boolean isXAxisLogarithmic() {

    return isXAxisLogarithmic;
  }

  /**
   * sets the Y-Axis to be rendered with a logarithmic scale or not
   *
   * @param isyAxisLogarithmic
   */
  public AxesChartStyler setYAxisLogarithmic(boolean isYAxisLogarithmic) {

    this.isYAxisLogarithmic = isYAxisLogarithmic;
    return this;
  }

  public boolean isYAxisLogarithmic() {

    return isYAxisLogarithmic;
  }

  public AxesChartStyler setXAxisMin(Double xAxisMin) {

    this.xAxisMin = xAxisMin;
    return this;
  }

  public Double getXAxisMin() {

    return xAxisMin;
  }

  public AxesChartStyler setXAxisMax(Double xAxisMax) {

    this.xAxisMax = xAxisMax;
    return this;
  }

  public Double getXAxisMax() {

    return xAxisMax;
  }

  public AxesChartStyler setYAxisMin(Double yAxisMin) {

    this.yAxisMin = yAxisMin;
    return this;
  }

  public Double getYAxisMin() {

    return yAxisMin;
  }

  public AxesChartStyler setYAxisMax(Double yAxisMax) {

    this.yAxisMax = yAxisMax;
    return this;
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

  public AxesChartStyler setYAxisLabelAlignment(TextAlignment yAxisLabelAlignment) {

    this.yAxisLabelAlignment = yAxisLabelAlignment;
    return this;
  }

  public int getXAxisLabelRotation() {

    return xAxisLabelRotation;
  }

  public AxesChartStyler setXAxisLabelRotation(int xAxisLabelRotation) {

    this.xAxisLabelRotation = xAxisLabelRotation;
    return this;
  }

  // Chart Plot Area ///////////////////////////////

  /**
   * sets the visibility of the gridlines inside the plot area
   *
   * @param isPlotGridLinesVisible
   */
  public AxesChartStyler setPlotGridLinesVisible(boolean isPlotGridLinesVisible) {

    this.isPlotGridHorizontalLinesVisible = isPlotGridLinesVisible;
    this.isPlotGridVerticalLinesVisible = isPlotGridLinesVisible;
    return this;
  }

  public boolean isPlotGridLinesVisible() {

    return isPlotGridHorizontalLinesVisible && isPlotGridVerticalLinesVisible;
  }

  /**
   * sets the visibility of the horizontal gridlines on the plot area
   *
   * @param isPlotGridLinesVisible
   */
  public AxesChartStyler setPlotGridHorizontalLinesVisible(boolean isPlotGridHorizontalLinesVisible) {

    this.isPlotGridHorizontalLinesVisible = isPlotGridHorizontalLinesVisible;
    return this;
  }

  public boolean isPlotGridHorizontalLinesVisible() {

    return isPlotGridHorizontalLinesVisible;
  }

  /**
   * sets the visibility of the vertical gridlines on the plot area
   *
   * @param isPlotGridLinesVisible
   */
  public AxesChartStyler setPlotGridVerticalLinesVisible(boolean isPlotGridVerticalLinesVisible) {

    this.isPlotGridVerticalLinesVisible = isPlotGridVerticalLinesVisible;
    return this;
  }

  public boolean isPlotGridVerticalLinesVisible() {

    return isPlotGridVerticalLinesVisible;
  }

  /**
   * sets the visibility of the ticks marks inside the plot area
   *
   * @param isPlotTicksMarksVisible
   */
  public AxesChartStyler setPlotTicksMarksVisible(boolean isPlotTicksMarksVisible) {

    this.isPlotTicksMarksVisible = isPlotTicksMarksVisible;
    return this;
  }

  public boolean isPlotTicksMarksVisible() {

    return isPlotTicksMarksVisible;
  }

  /**
   * set the plot area's grid lines color
   *
   * @param plotGridLinesColor
   */
  public AxesChartStyler setPlotGridLinesColor(Color plotGridLinesColor) {

    this.plotGridLinesColor = plotGridLinesColor;
    return this;
  }

  public Color getPlotGridLinesColor() {

    return plotGridLinesColor;
  }

  /**
   * set the plot area's grid lines Stroke
   *
   * @param plotGridLinesStroke
   */
  public AxesChartStyler setPlotGridLinesStroke(Stroke plotGridLinesStroke) {

    this.plotGridLinesStroke = plotGridLinesStroke;
    return this;
  }

  public Stroke getPlotGridLinesStroke() {

    return plotGridLinesStroke;
  }

  // Line, Scatter, Area Charts ///////////////////////////////

  /**
   * Sets the size of the markers in pixels
   *
   * @param markerSize
   */
  public AxesChartStyler setMarkerSize(int markerSize) {

    this.markerSize = markerSize;
    return this;
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
  public AxesChartStyler setErrorBarsColor(Color errorBarsColor) {

    this.errorBarsColor = errorBarsColor;
    return this;
  }

  public Color getErrorBarsColor() {

    return errorBarsColor;
  }

  /**
   * Set true if the the error bar color should match the series color
   *
   * @return
   */
  public AxesChartStyler setErrorBarsColorSeriesColor(boolean isErrorBarsColorSeriesColor) {

    this.isErrorBarsColorSeriesColor = isErrorBarsColorSeriesColor;
    return this;
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
  public AxesChartStyler setLocale(Locale locale) {

    this.locale = locale;
    return this;
  }

  public Locale getLocale() {

    return locale;
  }

  /**
   * Set the timezone to use for formatting Date axis tick labels
   *
   * @param timezone the timezone to use when formatting date data
   */
  public AxesChartStyler setTimezone(TimeZone timezone) {

    this.timezone = timezone;
    return this;
  }

  public TimeZone getTimezone() {

    return timezone;
  }

  /**
   * Set the String formatter for Data x-axis
   *
   * @param pattern - the pattern describing the date and time format
   */
  public AxesChartStyler setDatePattern(String datePattern) {

    this.datePattern = datePattern;
    return this;
  }

  public String getDatePattern() {

    return datePattern;
  }

  /**
   * Set the decimal formatter for all tick labels
   *
   * @param pattern - the pattern describing the decimal format
   */
  public AxesChartStyler setDecimalPattern(String decimalPattern) {

    this.decimalPattern = decimalPattern;
    return this;
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
  public AxesChartStyler setXAxisDecimalPattern(String xAxisDecimalPattern) {

    this.xAxisDecimalPattern = xAxisDecimalPattern;
    return this;
  }

  public String getYAxisDecimalPattern() {

    return yAxisDecimalPattern;
  }

  /**
   * Set the decimal formatting pattern for the Y-Axis
   *
   * @param yAxisDecimalPattern
   */
  public AxesChartStyler setYAxisDecimalPattern(String yAxisDecimalPattern) {

    this.yAxisDecimalPattern = yAxisDecimalPattern;
    return this;
  }

}
