/**
 * Copyright 2011 - 2014 Xeiam LLC.
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
package com.xeiam.xchart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.Locale;
import java.util.TimeZone;

import com.xeiam.xchart.internal.style.GGPlot2Theme;
import com.xeiam.xchart.internal.style.MatlabTheme;
import com.xeiam.xchart.internal.style.Theme;
import com.xeiam.xchart.internal.style.XChartTheme;

/**
 * The StyleManager is used to manage all things related to styling of the vast number of Chart components
 *
 * @author timmolter
 */
public class StyleManager {

  /**
   * Note: For Area Charts, the X-Axis data must be in ascending order.
   */
  public enum ChartType {

    Line, Scatter, Area, Bar
  }

  public enum LegendPosition {

    OutsideE, InsideNW, InsideNE, InsideSE, InsideSW, InsideN
  }

  public enum ChartTheme {

    XChart, GGPlot2, Matlab;

    public Theme newInstance(ChartTheme chartTheme) {

      switch (chartTheme) {
      case GGPlot2:
        return new GGPlot2Theme();

      case Matlab:
        return new MatlabTheme();

      case XChart:
      default:
        return new XChartTheme();
      }
    }
  }

  public enum TextAlignment {
    Left, Centre, Right;
  }

  /** the default Theme */
  private Theme theme = new XChartTheme();

  // Chart Style ///////////////////////////////
  private ChartType chartType;
  private Color chartBackgroundColor;
  public Color chartFontColor;
  private int chartPadding;

  // Chart Title ///////////////////////////////
  private Font chartTitleFont;
  private boolean isChartTitleVisible;
  private boolean isChartTitleBoxVisible;
  private Color chartTitleBoxBackgroundColor;
  private Color chartTitleBoxBorderColor;
  private int chartTitlePadding;

  // Chart Legend ///////////////////////////////
  private boolean isLegendVisible;
  private Color legendBackgroundColor;
  private Color legendBorderColor;
  private Font legendFont;
  private int legendPadding;
  private int legendSeriesLineLength;
  private LegendPosition legendPosition;

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
  private int plotPadding;
  private int axisTitlePadding;
  private int xAxisTickMarkSpacingHint;
  private int yAxisTickMarkSpacingHint;
  private boolean isXAxisLogarithmic;
  private boolean isYAxisLogarithmic;
  private Double xAxisMin;
  private Double xAxisMax;
  private Double yAxisMin;
  private Double yAxisMax;
  private double axisTickSpacePercentage;
  private TextAlignment xAxisLabelAlignment = TextAlignment.Centre;
  private TextAlignment yAxisLabelAlignment = TextAlignment.Left;

  // Chart Plot Area ///////////////////////////////
  private boolean isPlotGridLinesVisible;
  private Color plotBackgroundColor;
  private Color plotBorderColor;
  private boolean isPlotBorderVisible;
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

  // Formatting ////////////////////////////////
  private Locale locale;
  private TimeZone timezone;
  private String datePattern;
  private String decimalPattern;
  private String xAxisDecimalPattern;
  private String yAxisDecimalPattern;

  /**
   * Constructor
   */
  public StyleManager() {

    setAllStyles();
  }

  private void setAllStyles() {

    // Chart Style ///////////////////////////////
    chartType = ChartType.Line;
    chartBackgroundColor = theme.getChartBackgroundColor();
    chartFontColor = theme.getChartFontColor();
    chartPadding = theme.getChartPadding();

    // Chart Title ///////////////////////////////
    chartTitleFont = theme.getChartTitleFont();
    isChartTitleVisible = theme.isChartTitleVisible();
    isChartTitleBoxVisible = theme.isChartTitleBoxVisible();
    chartTitleBoxBackgroundColor = theme.getChartTitleBoxBackgroundColor();
    chartTitleBoxBorderColor = theme.getChartTitleBoxBorderColor();
    chartTitlePadding = theme.getChartTitlePadding();

    // legend
    isLegendVisible = theme.isLegendVisible();
    legendBackgroundColor = theme.getLegendBackgroundColor();
    legendBorderColor = theme.getLegendBorderColor();
    legendFont = theme.getLegendFont();
    legendPadding = theme.getLegendPadding();
    legendSeriesLineLength = theme.getLegendSeriesLineLength();
    legendPosition = theme.getLegendPosition();

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
    plotPadding = theme.getPlotPadding();
    axisTitlePadding = theme.getAxisTitlePadding();
    xAxisTickMarkSpacingHint = theme.getXAxisTickMarkSpacingHint();
    yAxisTickMarkSpacingHint = theme.getYAxisTickMarkSpacingHint();
    isXAxisLogarithmic = false;
    isYAxisLogarithmic = false;
    xAxisMin = null;
    xAxisMax = null;
    yAxisMin = null;
    yAxisMax = null;
    axisTickSpacePercentage = .95;

    // Chart Plot Area ///////////////////////////////
    isPlotGridLinesVisible = theme.isPlotGridLinesVisible();
    plotBackgroundColor = theme.getPlotBackgroundColor();
    plotBorderColor = theme.getPlotBorderColor();
    isPlotBorderVisible = theme.isPlotBorderVisible();
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

    // Formatting ////////////////////////////////
    locale = Locale.getDefault();
    timezone = TimeZone.getDefault();
    datePattern = null; // if not null, this override pattern will be used
    decimalPattern = null;
    xAxisDecimalPattern = null;
    yAxisDecimalPattern = null;
  }

  /**
   * Set the theme the style manager should use
   *
   * @param theme
   */
  protected void setTheme(Theme theme) {

    this.theme = theme;
    setAllStyles();
  }

  public Theme getTheme() {

    return theme;
  }

  // Chart Style ///////////////////////////////

  /**
   * sets the Chart Type
   *
   * @param chartType
   */
  public void setChartType(ChartType chartType) {

    this.chartType = chartType;
  }

  public ChartType getChartType() {

    return chartType;
  }

  /**
   * Set the chart background color - the part around the edge of the chart
   *
   * @param color
   */
  public void setChartBackgroundColor(Color color) {

    this.chartBackgroundColor = color;
  }

  public Color getChartBackgroundColor() {

    return chartBackgroundColor;
  }

  /**
   * Set the chart font color. includes: Chart title, axes label, legend
   *
   * @param color
   */
  public void setChartFontColor(Color color) {

    this.chartFontColor = color;
  }

  public Color getChartFontColor() {

    return chartFontColor;
  }

  /**
   * Set the chart padding
   *
   * @param chartPadding
   */
  public void setChartPadding(int chartPadding) {

    this.chartPadding = chartPadding;
  }

  public int getChartPadding() {

    return chartPadding;
  }

  // Chart Title ///////////////////////////////

  /**
   * Set the chart title font
   *
   * @param font
   */
  public void setChartTitleFont(Font chartTitleFont) {

    this.chartTitleFont = chartTitleFont;
  }

  public Font getChartTitleFont() {

    return chartTitleFont;
  }

  /**
   * Set the chart title visibility
   *
   * @param isChartTitleVisible
   */
  public void setChartTitleVisible(boolean isChartTitleVisible) {

    this.isChartTitleVisible = isChartTitleVisible;
  }

  public boolean isChartTitleVisible() {

    return isChartTitleVisible;
  }

  /**
   * Set the chart title box visibility
   *
   * @param isChartTitleBoxVisible
   */
  public void setChartTitleBoxVisible(boolean isChartTitleBoxVisible) {

    this.isChartTitleBoxVisible = isChartTitleBoxVisible;
  }

  public boolean isChartTitleBoxVisible() {

    return isChartTitleBoxVisible;
  }

  /**
   * set the chart title box background color
   *
   * @param chartTitleBoxBackgroundColor
   */
  public void setChartTitleBoxBackgroundColor(Color chartTitleBoxBackgroundColor) {

    this.chartTitleBoxBackgroundColor = chartTitleBoxBackgroundColor;
  }

  public Color getChartTitleBoxBackgroundColor() {

    return chartTitleBoxBackgroundColor;
  }

  /**
   * set the chart title box border color
   *
   * @param chartTitleBoxBorderColor
   */
  public void setChartTitleBoxBorderColor(Color chartTitleBoxBorderColor) {

    this.chartTitleBoxBorderColor = chartTitleBoxBorderColor;
  }

  public Color getChartTitleBoxBorderColor() {

    return chartTitleBoxBorderColor;
  }

  /**
   * set the chart title padding; the space between the chart title and the plot area
   *
   * @param chartTitlePadding
   */
  public void setChartTitlePadding(int chartTitlePadding) {

    this.chartTitlePadding = chartTitlePadding;
  }

  public int getChartTitlePadding() {

    return chartTitlePadding;
  }

  // Chart Legend ///////////////////////////////

  /**
   * Set the chart legend background color
   *
   * @param color
   */
  public void setLegendBackgroundColor(Color color) {

    this.legendBackgroundColor = color;
  }

  public Color getLegendBackgroundColor() {

    return legendBackgroundColor;
  }

  /**
   * Set the chart legend border color
   *
   * @return
   */
  public Color getLegendBorderColor() {

    return legendBorderColor;
  }

  public void setLegendBorderColor(Color legendBorderColor) {

    this.legendBorderColor = legendBorderColor;
  }

  /**
   * Set the chart legend font
   *
   * @param font
   */
  public void setLegendFont(Font font) {

    this.legendFont = font;
  }

  public Font getLegendFont() {

    return legendFont;
  }

  /**
   * Set the chart legend visibility
   *
   * @param isLegendVisible
   */
  public void setLegendVisible(boolean isLegendVisible) {

    this.isLegendVisible = isLegendVisible;
  }

  public boolean isLegendVisible() {

    return isLegendVisible;
  }

  /**
   * Set the chart legend padding
   *
   * @param legendPadding
   */
  public void setLegendPadding(int legendPadding) {

    this.legendPadding = legendPadding;
  }

  public int getLegendPadding() {

    return legendPadding;
  }

  /**
   * Set the chart legend series line length
   *
   * @param legendPadding
   */
  public void setLegendSeriesLineLength(int legendSeriesLineLength) {

    if (legendSeriesLineLength < 0) {
      this.legendSeriesLineLength = 0;
    }
    else {
      this.legendSeriesLineLength = legendSeriesLineLength;
    }
  }

  public int getLegendSeriesLineLength() {

    return legendSeriesLineLength;
  }

  /**
   * sets the legend position
   *
   * @param legendPosition
   */
  public void setLegendPosition(LegendPosition legendPosition) {

    this.legendPosition = legendPosition;
  }

  public LegendPosition getLegendPosition() {

    return legendPosition;
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
   * sets the padding between the tick marks and the plot area
   *
   * @param plotPadding
   */
  public void setPlotPadding(int plotPadding) {

    this.plotPadding = plotPadding;
  }

  public int getPlotPadding() {

    return plotPadding;
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

  public void setAxisTickSpacePercentage(double axisTickSpacePercentage) {

    this.axisTickSpacePercentage = axisTickSpacePercentage;
  }

  public double getAxisTickSpacePercentage() {

    return axisTickSpacePercentage;
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

  // Chart Plot Area ///////////////////////////////

  /**
   * sets the visibility of the gridlines on the plot area
   *
   * @param isPlotGridLinesVisible
   */
  public void setPlotGridLinesVisible(boolean isPlotGridLinesVisible) {

    this.isPlotGridLinesVisible = isPlotGridLinesVisible;
  }

  public boolean isPlotGridLinesVisible() {

    return isPlotGridLinesVisible;
  }

  /**
   * set the plot area's background color
   *
   * @param plotBackgroundColor
   */
  public void setPlotBackgroundColor(Color plotBackgroundColor) {

    this.plotBackgroundColor = plotBackgroundColor;
  }

  public Color getPlotBackgroundColor() {

    return plotBackgroundColor;
  }

  /**
   * set the plot area's border color
   *
   * @param plotBorderColor
   */
  public void setPlotBorderColor(Color plotBorderColor) {

    this.plotBorderColor = plotBorderColor;
  }

  public Color getPlotBorderColor() {

    return plotBorderColor;
  }

  /**
   * sets the visibility of the border around the plot area
   *
   * @param isPlotBorderVisible
   */
  public void setPlotBorderVisible(boolean isPlotBorderVisible) {

    this.isPlotBorderVisible = isPlotBorderVisible;
  }

  public boolean isPlotBorderVisible() {

    return isPlotBorderVisible;
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
