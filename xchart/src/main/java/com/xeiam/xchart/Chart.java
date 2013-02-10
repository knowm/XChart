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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.xeiam.xchart.internal.chartpart.AxisPair;
import com.xeiam.xchart.internal.chartpart.ChartTitle;
import com.xeiam.xchart.internal.chartpart.Legend;
import com.xeiam.xchart.internal.chartpart.Plot;
import com.xeiam.xchart.style.Series;
import com.xeiam.xchart.style.StyleManager;
import com.xeiam.xchart.style.theme.Theme;

/**
 * An XChart Chart
 * 
 * @author timmolter
 */
public class Chart {

  public int width;
  public int height;

  private StyleManager styleManager = new StyleManager();

  // Chart Parts
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

  }

  /**
   * Constructor
   * 
   * @param chartBuilder
   */
  public Chart(ChartBuilder chartBuilder) {

    this(chartBuilder.width, chartBuilder.height);
    setTitle(chartBuilder.title);
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
    styleManager.setLegendVisible(chartBuilder.isLegendVisible);
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
    g.setColor(styleManager.getChartBackgroundColor());
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

    if (title == null || title.trim().equalsIgnoreCase("")) {
      styleManager.setxAxisTitleVisible(false);
    } else {
      styleManager.setxAxisTitleVisible(true);
    }
    this.axisPair.xAxis.axisTitle.setText(title);
  }

  /**
   * Set the y-axis title
   * 
   * @param title
   */
  public void setYAxisTitle(String title) {

    if (title == null || title.trim().equalsIgnoreCase("")) {
      styleManager.setyAxisTitleVisible(false);
    } else {
      styleManager.setyAxisTitleVisible(true);
    }
    this.axisPair.yAxis.axisTitle.setText(title);
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
   * @param locale - the locale to use when formatting Strings and dates for the axis tick labels
   */
  public void setLocale(Locale locale) {

    this.axisPair.xAxis.axisTick.locale = locale;
    this.axisPair.yAxis.axisTick.locale = locale;
  }

  /**
   * Set the timezone to use for formatting Date axis tick labels
   * 
   * @param timezone the timezone to use when formatting date data
   */
  public void setTimezone(TimeZone timezone) {

    this.axisPair.xAxis.axisTick.timezone = timezone;
    this.axisPair.yAxis.axisTick.timezone = timezone;
  }

  /**
   * Gets the Chart's style manager, which can be used to customize the Chart's appearance
   * 
   * @return the style manager
   */
  public StyleManager getStyleManager() {

    return styleManager;
  }

  /**
   * Set the theme the Chart's style manager should use
   * 
   * @param theme
   */
  public void setTheme(Theme theme) {

    styleManager.setTheme(theme);

  }

}
