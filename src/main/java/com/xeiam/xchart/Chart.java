/**
 * Copyright 2011-2012 Xeiam LLC.
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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import com.xeiam.xchart.internal.chartpart.AxisPair;
import com.xeiam.xchart.internal.chartpart.ChartTitle;
import com.xeiam.xchart.internal.chartpart.Legend;
import com.xeiam.xchart.internal.chartpart.Plot;

/**
 * An XChart Chart
 * 
 * @author timmolter
 */
public class Chart {

  public int width;
  public int height;
  private Color backgroundColor;
  public Color bordersColor;
  public Color fontColor;

  public final static int CHART_PADDING = 10;

  public ChartTitle chartTitle = new ChartTitle(this);
  public Legend chartLegend = new Legend(this);
  public AxisPair axisPair = new AxisPair(this);
  protected Plot plot = new Plot(this);

  /**
   * Constructor
   * 
   * @param width
   * @param height
   */
  public Chart(int width, int height) {

    this.width = width;
    this.height = height;
    backgroundColor = ChartColor.getAWTColor(ChartColor.GREY);
    bordersColor = ChartColor.getAWTColor(ChartColor.DARK_GREY);
    fontColor = ChartColor.getAWTColor(ChartColor.BLACK);
  }

  /**
   * @param g
   */
  protected void paint(Graphics2D g, int width, int height) {

    this.width = width;
    this.height = height;

    paint(g);
  }

  /**
   * @param g
   */
  protected void paint(Graphics2D g) {

    // Sanity check
    if (axisPair.seriesMap.isEmpty()) {
      throw new RuntimeException("No series defined for Chart!!!");
    }

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // global rendering hint
    g.setColor(backgroundColor);
    g.fillRect(0, 0, width, height);

    chartTitle.paint(g);
    chartLegend.paint(g);
    axisPair.paint(g);
    plot.paint(g);

    g.dispose();

    // reset static Ids
    SeriesColor.resetId();
    SeriesLineStyle.resetId();
    SeriesMarker.resetId();

  }

  // PUBLIC SETTERS

  /**
   * @param seriesName
   * @param xData
   * @param yData
   */
  public Series addDateSeries(String seriesName, Collection<Date> xData, Collection<Number> yData) {

    return axisPair.addSeries(seriesName, xData, yData, null);
  }

  public Series addDateSeries(String seriesName, Collection<Date> xData, Collection<Number> yData, Collection<Number> errorBars) {

    return axisPair.addSeries(seriesName, xData, yData, errorBars);
  }

  /**
   * Add series data as Collection<Number>
   * 
   * @param seriesName
   * @param xData Collection<Number>
   * @param yData Collection<Number>
   * @return
   */
  public Series addSeries(String seriesName, Collection<Number> xData, Collection<Number> yData) {

    return axisPair.addSeries(seriesName, xData, yData, null);
  }

  public Series addSeries(String seriesName, Collection<Number> xData, Collection<Number> yData, Collection<Number> errorBars) {

    return axisPair.addSeries(seriesName, xData, yData, errorBars);
  }

  /**
   * Convenience Method - Add series data as double arrays
   * 
   * @param seriesName
   * @param xData double[]
   * @param yData double[]
   * @return
   */
  public Series addSeries(String seriesName, double[] xData, double[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Convenience Method - Add series data as double arrays with errorbars
   * 
   * @param seriesName
   * @param xData
   * @param yData
   * @param errorBars
   * @return
   */
  public Series addSeries(String seriesName, double[] xData, double[] yData, double[] errorBars) {

    Collection<Number> xDataNumber = null;
    if (xData != null) {
      xDataNumber = new ArrayList<Number>();
      for (double d : xData) {
        xDataNumber.add(new Double(d));
      }
    }
    Collection<Number> yDataNumber = new ArrayList<Number>();
    for (double d : yData) {
      yDataNumber.add(new Double(d));
    }
    Collection<Number> errorBarDataNumber = null;
    if (errorBars != null) {
      errorBarDataNumber = new ArrayList<Number>();
      for (double d : errorBars) {
        errorBarDataNumber.add(new Double(d));
      }
    }

    return axisPair.addSeries(seriesName, xDataNumber, yDataNumber, errorBarDataNumber);
  }

  public void setTitle(String title) {

    this.chartTitle.setText(title);
  }

  public void setXAxisTitle(String title) {

    this.axisPair.xAxis.setAxisTitle(title);
  }

  public void setYAxisTitle(String title) {

    this.axisPair.yAxis.setAxisTitle(title);
  }

  // ChartPart visibility ////////////////////////////////

  public void setTitleVisible(boolean isVisible) {

    this.chartTitle.setVisible(isVisible);
  }

  public void setAxisTitlesVisible(boolean isVisible) {

    this.axisPair.xAxis.getAxisTitle().setVisible(isVisible);
    this.axisPair.yAxis.getAxisTitle().setVisible(isVisible);
  }

  public void setXAxisTitleVisible(boolean isVisible) {

    this.axisPair.xAxis.getAxisTitle().setVisible(isVisible);
  }

  public void setYAxisTitleVisible(boolean isVisible) {

    this.axisPair.yAxis.getAxisTitle().setVisible(isVisible);
  }

  public void setLegendVisible(boolean isVisible) {

    this.chartLegend.setVisible(isVisible);
  }

  public void setAxisTicksVisible(boolean isVisible) {

    this.axisPair.xAxis.axisTick.setVisible(isVisible);
    this.axisPair.yAxis.axisTick.setVisible(isVisible);
  }

  public void setXAxisTicksVisible(boolean isVisible) {

    this.axisPair.xAxis.axisTick.setVisible(isVisible);
  }

  public void setYAxisTicksVisible(boolean isVisible) {

    this.axisPair.yAxis.axisTick.setVisible(isVisible);
  }

  public void setGridlinesVisible(boolean isVisible) {

    this.plot.plotSurface.setVisible(isVisible);
  }

  public void setBackgroundColor(Color color) {

    this.backgroundColor = color;
  }

  public void setForegroundColor(Color color) {

    this.plot.plotSurface.setForegroundColor(color);
  }

  public void setGridLinesColor(Color color) {

    this.plot.plotSurface.setGridLinesColor(color);
  }

  public void setLegendBackgroundColor(Color color) {

    this.chartLegend.backgroundColor = color;
  }

  /**
   * Sets the color of the plot border, legend border, tick marks, and error bars
   * 
   * @param color
   */
  public void setLinesColor(Color color) {

    this.bordersColor = color;
  }

  public void setFontColor(Color color) {

    this.fontColor = color;
  }

  public void setTitleFont(Font font) {

    this.chartTitle.font = font;
  }

  public void setLegendFont(Font font) {

    this.chartLegend.font = font;
  }

  public void setAxisLabelsFont(Font font) {

    this.axisPair.xAxis.axisTitle.font = font;
    this.axisPair.yAxis.axisTitle.font = font;
  }

  public void setTickLabelsFont(Font font) {

    this.axisPair.xAxis.axisTick.axisTickLabels.font = font;
    this.axisPair.yAxis.axisTick.axisTickLabels.font = font;
  }

  /**
   * @param pattern - the pattern describing the date and time format
   */
  public void setDateFormatter(String pattern) {

    this.axisPair.xAxis.axisTick.datePattern = pattern;
  }

  /**
   * @param pattern - the pattern describing the decimal format
   */
  public void setDecmialFormatter(String pattern) {

    this.axisPair.xAxis.axisTick.normalDecimalPattern = pattern;
    this.axisPair.yAxis.axisTick.normalDecimalPattern = pattern;
  }

  /**
   * @param pattern - the pattern describing the scientific notation format
   */
  public void setDecmialScientificFormatter(String pattern) {

    this.axisPair.xAxis.axisTick.scientificDecimalPattern = pattern;
    this.axisPair.yAxis.axisTick.scientificDecimalPattern = pattern;
  }

  /**
   * @param locale - the locale to use when drawing the chart
   */
  public void setLocale(Locale locale) {

    this.axisPair.xAxis.axisTick.locale = locale;
    this.axisPair.yAxis.axisTick.locale = locale;
  }

}
