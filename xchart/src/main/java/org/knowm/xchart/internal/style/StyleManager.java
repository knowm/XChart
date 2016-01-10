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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import org.knowm.xchart.internal.style.markers.Marker;

/**
 * The StyleManager is used to manage all things related to styling of the vast number of Chart components
 *
 * @author timmolter
 */
public abstract class StyleManager {

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
  protected Theme theme = new XChartTheme();

  // Chart Style ///////////////////////////////
  private Color chartBackgroundColor;
  private Color chartFontColor;
  private int chartPadding;
  private Color[] seriesColors;
  private BasicStroke[] seriesLines;
  private Marker[] seriesMarkers;

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

  // Chart Plot Area ///////////////////////////////
  private Color plotBackgroundColor;
  private Color plotBorderColor;
  private boolean isPlotBorderVisible;

  protected void setAllStyles() {

    // Chart Style ///////////////////////////////
    chartBackgroundColor = theme.getChartBackgroundColor();
    chartFontColor = theme.getChartFontColor();
    chartPadding = theme.getChartPadding();
    seriesColors = theme.getSeriesColors();
    seriesLines = theme.getSeriesLines();
    seriesMarkers = theme.getSeriesMarkers();

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

    // Chart Plot Area ///////////////////////////////
    plotBackgroundColor = theme.getPlotBackgroundColor();
    plotBorderColor = theme.getPlotBorderColor();
    isPlotBorderVisible = theme.isPlotBorderVisible();
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

  public Color[] getSeriesColors() {

    return seriesColors;
  }

  public void setSeriesColors(Color[] seriesColors) {

    this.seriesColors = seriesColors;
  }

  public BasicStroke[] getSeriesLines() {

    return seriesLines;
  }

  public void setSeriesLines(BasicStroke[] seriesLines) {

    this.seriesLines = seriesLines;
  }

  public Marker[] getSeriesMarkers() {

    return seriesMarkers;
  }

  public void setSeriesMarkers(Marker[] seriesMarkers) {

    this.seriesMarkers = seriesMarkers;
  }

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

  // Chart Plot ///////////////////////////////

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

}
