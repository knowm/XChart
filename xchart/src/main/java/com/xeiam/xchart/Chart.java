/**
 * Copyright 2011-2013 Xeiam LLC.
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
   * @param width
   * @param height
   */
  public void paint(Graphics2D g, int width, int height) {

    this.width = width;
    this.height = height;

    paint(g);
  }

  /**
   * @param g
   */
  public void paint(Graphics2D g) {

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
  }

  // PUBLIC SETTERS

  /**
   * Add a Date series to the chart
   * 
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public Series addDateSeries(String seriesName, Collection<Date> xData, Collection<Number> yData) {

    return axisPair.addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a Date series to the chart with error bars
   * 
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public Series addDateSeries(String seriesName, Collection<Date> xData, Collection<Number> yData, Collection<Number> errorBars) {

    return axisPair.addSeries(seriesName, xData, yData, errorBars);
  }

  /**
   * Add a Number series to the chart using Collection<Number>
   * 
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public Series addSeries(String seriesName, Collection<Number> xData, Collection<Number> yData) {

    return axisPair.addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a Number series to the chart using Collection<Number> with error bars
   * 
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public Series addSeries(String seriesName, Collection<Number> xData, Collection<Number> yData, Collection<Number> errorBars) {

    return axisPair.addSeries(seriesName, xData, yData, errorBars);
  }

  /**
   * Add a series to the chart using double arrays
   * 
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public Series addSeries(String seriesName, double[] xData, double[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series to the chart using double arrays with error bars
   * 
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
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

  /**
   * Set the chart title
   * 
   * @param title
   */
  public void setTitle(String title) {

    this.chartTitle.setText(title);
  }

  /**
   * Set the x-axis title
   * 
   * @param title
   */
  public void setXAxisTitle(String title) {

    this.axisPair.xAxis.axisTitle.setText(title);
  }

  /**
   * Set the y-axis title
   * 
   * @param title
   */
  public void setYAxisTitle(String title) {

    this.axisPair.yAxis.axisTitle.setText(title);
  }

  // ChartPart visibility ////////////////////////////////

  /**
   * Set the chart title visibility
   * 
   * @param isVisible
   */
  public void setTitleVisible(boolean isVisible) {

    this.chartTitle.setVisible(isVisible);
  }

  /**
   * Set the x- and y-axis titles visibility
   * 
   * @param isVisible
   */
  public void setAxisTitlesVisible(boolean isVisible) {

    this.axisPair.xAxis.getAxisTitle().setVisible(isVisible);
    this.axisPair.yAxis.getAxisTitle().setVisible(isVisible);
  }

  /**
   * Set the x-axis title visibility
   * 
   * @param isVisible
   */
  public void setXAxisTitleVisible(boolean isVisible) {

    this.axisPair.xAxis.getAxisTitle().setVisible(isVisible);
  }

  /**
   * Set the y-axis title visibility
   * 
   * @param isVisible
   */
  public void setYAxisTitleVisible(boolean isVisible) {

    this.axisPair.yAxis.getAxisTitle().setVisible(isVisible);
  }

  /**
   * Set the chart legend visibility
   * 
   * @param isVisible
   */
  public void setLegendVisible(boolean isVisible) {

    this.chartLegend.setVisible(isVisible);
  }

  /**
   * Set the x- and y-axis tick marks and labels visibility
   * 
   * @param isVisible
   */
  public void setAxisTicksVisible(boolean isVisible) {

    this.axisPair.xAxis.axisTick.setVisible(isVisible);
    this.axisPair.yAxis.axisTick.setVisible(isVisible);
  }

  /**
   * Set the x-axis tick marks and labels visibility
   * 
   * @param isVisible
   */
  public void setXAxisTicksVisible(boolean isVisible) {

    this.axisPair.xAxis.axisTick.setVisible(isVisible);
  }

  /**
   * Set the y-axis tick marks and labels visibility
   * 
   * @param isVisible
   */
  public void setYAxisTicksVisible(boolean isVisible) {

    this.axisPair.yAxis.axisTick.setVisible(isVisible);
  }

  /**
   * Set the chart grid lines visibility
   * 
   * @param isVisible
   */
  public void setGridlinesVisible(boolean isVisible) {

    this.plot.plotSurface.setVisible(isVisible);
  }

  /**
   * Set the chart background color - the part around the edge of the chart
   * 
   * @param color
   */
  public void setBackgroundColor(Color color) {

    this.backgroundColor = color;
  }

  /**
   * Set the chart foreground color - the part the series are drawn on
   * 
   * @param color
   */
  public void setForegroundColor(Color color) {

    this.plot.plotSurface.setForegroundColor(color);
  }

  /**
   * Set the chart grid lines color
   * 
   * @param color
   */
  public void setGridLinesColor(Color color) {

    this.plot.plotSurface.setGridLinesColor(color);
  }

  /**
   * Set the chart legend color
   * 
   * @param color
   */
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

  /**
   * Set the chart font color
   * 
   * @param color
   */
  public void setFontColor(Color color) {

    this.fontColor = color;
  }

  /**
   * Set the chart title font
   * 
   * @param font
   */
  public void setTitleFont(Font font) {

    this.chartTitle.font = font;
  }

  /**
   * Set the chart legend font
   * 
   * @param font
   */
  public void setLegendFont(Font font) {

    this.chartLegend.font = font;
  }

  /**
   * Set the x- and y-axis title font
   * 
   * @param font
   */
  public void setAxisTitleFont(Font font) {

    this.axisPair.xAxis.axisTitle.font = font;
    this.axisPair.yAxis.axisTitle.font = font;
  }

  /**
   * Set the x- and y-axis tick label font
   * 
   * @param font
   */
  public void setTickLabelFont(Font font) {

    this.axisPair.xAxis.axisTick.axisTickLabels.font = font;
    this.axisPair.yAxis.axisTick.axisTickLabels.font = font;
  }

  /**
   * Set the String formatter for Data x-axis
   * 
   * @param pattern - the pattern describing the date and time format
   */
  public void setDateFormatter(String pattern) {

    this.axisPair.xAxis.axisTick.datePattern = pattern;
  }

  /**
   * Set the decimal formatter for all tick labels
   * 
   * @param pattern - the pattern describing the decimal format
   */
  public void setDecmialFormatter(String pattern) {

    this.axisPair.xAxis.axisTick.normalDecimalPattern = pattern;
    this.axisPair.yAxis.axisTick.normalDecimalPattern = pattern;
  }

  /**
   * Set the scientific notation formatter for all tick labels
   * 
   * @param pattern - the pattern describing the scientific notation format
   */
  public void setDecmialScientificFormatter(String pattern) {

    this.axisPair.xAxis.axisTick.scientificDecimalPattern = pattern;
    this.axisPair.yAxis.axisTick.scientificDecimalPattern = pattern;
  }

  /**
   * Set the locale to use for rendering the chart
   * 
   * @param locale - the locale to use when drawing the chart
   */
  public void setLocale(Locale locale) {

    this.axisPair.xAxis.axisTick.locale = locale;
    this.axisPair.yAxis.axisTick.locale = locale;
  }

}
