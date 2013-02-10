/**
 * Copyright (C) 2013 Xeiam LLC http://xeiam.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchart.style;

import java.awt.Color;
import java.awt.Font;

import com.xeiam.xchart.style.theme.Theme;
import com.xeiam.xchart.style.theme.XChartTheme;

/**
 * @author timmolter
 */
public class StyleManager {

  /** the default Theme */
  private Theme theme = new XChartTheme();

  // Chart Style ///////////////////////////////
  private Color chartBackgroundColor;
  public Color chartBordersColor;
  public Color chartFontColor;
  private int chartPadding;

  // Chart Title ///////////////////////////////
  private Font chartTitleFont;
  private boolean isChartTitleVisible;

  // Chart Legend ///////////////////////////////
  private boolean isLegendVisible;
  private Color legendBackgroundColor;
  private Font legendFont;
  private int legendPadding;

  // Chart Axes ///////////////////////////////
  private boolean xAxisTitleVisible;
  private boolean yAxisTitleVisible;
  private Font axisTitleFont;
  private boolean xAxisTicksVisible;
  private boolean yAxisTicksVisible;
  private Font axisTicksFont;
  private int axisTickMarkLength;
  private int axisTickPadding;
  private boolean isAxisTicksLineVisible;
  private int plotPadding;
  private int axisTitlePadding;

  // Chart Plot Area ///////////////////////////////
  private boolean isPlotGridLinesVisible;

  /**
   * Constructor
   */
  public StyleManager() {

    setAllStyles();
  }

  private void setAllStyles() {

    // Chart Style ///////////////////////////////
    chartBackgroundColor = theme.getChartBackgroundColor();
    chartBordersColor = theme.getChartBordersColor();
    chartFontColor = theme.getChartFontColor();
    chartPadding = theme.getChartPadding();

    // Chart Title ///////////////////////////////
    chartTitleFont = theme.getChartTitleFont();
    isChartTitleVisible = theme.isChartTitleVisible();

    // legend
    isLegendVisible = theme.isLegendVisible();
    legendBackgroundColor = theme.getLegendBackgroundColor();
    legendFont = theme.getLegendFont();
    legendPadding = theme.getLegendPadding();

    // axes
    xAxisTitleVisible = theme.isXAxisTitleVisible();
    yAxisTitleVisible = theme.isYAxisTitleVisible();
    axisTitleFont = theme.getAxisTitleFont();
    xAxisTicksVisible = theme.isXAxisTicksVisible();
    yAxisTicksVisible = theme.isYAxisTicksVisible();
    axisTicksFont = theme.getAxisTicksFont();
    axisTickMarkLength = theme.getAxisTickMarkLength();
    axisTickPadding = theme.getAxisTickPadding();
    isAxisTicksLineVisible = theme.isAxisTicksLineVisible();
    plotPadding = theme.getPlotPadding();
    axisTitlePadding = theme.getAxisTitlePadding();

    // Chart Plot Area ///////////////////////////////
    isPlotGridLinesVisible = theme.isPlotGridLinesVisible();
  }

  /**
   * Set the theme the style manager should use
   * 
   * @param theme
   */
  public void setTheme(Theme theme) {

    this.theme = theme;
    setAllStyles();
  }

  // Chart Style ///////////////////////////////

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
   * Sets the color of the plot border, legend border, tick marks, and error bars
   * 
   * @param color
   */
  public void setChartBordersColor(Color color) {

    this.chartBordersColor = color;
  }

  public Color getChartBordersColor() {

    return chartBordersColor;
  }

  /**
   * Set the chart font color
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
   * @param isVisible
   */
  public void setChartTitleVisible(boolean isChartTitleVisible) {

    this.isChartTitleVisible = isChartTitleVisible;
  }

  public boolean isChartTitleVisible() {

    return isChartTitleVisible;
  }

  // Chart Legend ///////////////////////////////

  /**
   * Set the chart legend color
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

  // Chart Axes ///////////////////////////////

  /**
   * Set the x-axis title visibility
   * 
   * @param isVisible
   */
  public void setxAxisTitleVisible(boolean xAxisTitleVisible) {

    this.xAxisTitleVisible = xAxisTitleVisible;
  }

  public boolean isxAxisTitleVisible() {

    return xAxisTitleVisible;
  }

  /**
   * Set the y-axis title visibility
   * 
   * @param isVisible
   */
  public void setyAxisTitleVisible(boolean yAxisTitleVisible) {

    this.yAxisTitleVisible = yAxisTitleVisible;
  }

  public boolean isyAxisTitleVisible() {

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

  public void setxAxisTicksVisible(boolean xAxisTicksVisible) {

    this.xAxisTicksVisible = xAxisTicksVisible;
  }

  public boolean isxAxisTicksVisible() {

    return xAxisTicksVisible;
  }

  /**
   * Set the y-axis tick marks and labels visibility
   * 
   * @param isVisible
   */

  public void setyAxisTicksVisible(boolean yAxisTicksVisible) {

    this.yAxisTicksVisible = yAxisTicksVisible;
  }

  public boolean isyAxisTicksVisible() {

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
  public void setAxisTicksFont(Font axisTicksFont) {

    this.axisTicksFont = axisTicksFont;
  }

  public Font getAxisTicksFont() {

    return axisTicksFont;
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
   * the padding between the tick labels and the tick marks
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
   * sets the pading between the tick marks and the plot area
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
}
