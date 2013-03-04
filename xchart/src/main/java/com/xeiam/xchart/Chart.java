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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.xeiam.xchart.internal.chartpart.AxisPair;
import com.xeiam.xchart.internal.chartpart.ChartTitle;
import com.xeiam.xchart.internal.chartpart.Legend;
import com.xeiam.xchart.internal.chartpart.Plot;
import com.xeiam.xchart.internal.style.Theme;

/**
 * An XChart Chart
 * 
 * @author timmolter
 */
public class Chart {

  private int width;
  private int height;

  private final StyleManager styleManager;

  // Chart Parts
  private Legend chartLegend;
  private AxisPair axisPair;
  private Plot plot;
  private ChartTitle chartTitle;

  /**
   * Constructor
   * 
   * @param width
   * @param height
   */
  public Chart(int width, int height) {

    styleManager = new StyleManager();
    chartLegend = new Legend(this);
    axisPair = new AxisPair(this);
    plot = new Plot(this);
    chartTitle = new ChartTitle(this);
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
    getStyleManager().setChartTheme(chartBuilder.chartTheme);
    setChartTitle(chartBuilder.title);
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
    getStyleManager().setChartType(chartBuilder.chartType);
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

    // Sanity checks
    if (axisPair.getSeriesMap().isEmpty()) {
      throw new RuntimeException("No series defined for Chart!!!");
    }
    if (getStyleManager().isXAxisLogarithmic() && axisPair.getxAxis().getMin().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Series data cannot be less or equal to zero for a logarithmic X-Axis!!!");
    }
    if (getStyleManager().isYAxisLogarithmic() && axisPair.getyAxis().getMin().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Series data cannot be less or equal to zero for a logarithmic Y-Axis!!!");
    }

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // global rendering hint
    g.setColor(styleManager.getChartBackgroundColor());
    g.fillRect(0, 0, width, height);

    axisPair.paint(g);
    plot.paint(g);
    chartTitle.paint(g);
    chartLegend.paint(g);

    g.dispose();
  }

  // PUBLIC SETTERS

  /**
   * Add a Category series to the chart
   * 
   * @param seriesName
   * @param xData
   * @param yData
   * @return
   */
  public Series addCategorySeries(String seriesName, Collection<String> xData, Collection<Number> yData) {

    return axisPair.addSeries(seriesName, xData, yData, null);
  }

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
  public void setChartTitle(String title) {

    this.chartTitle.setText(title);
  }

  /**
   * Set the x-axis title
   * 
   * @param title
   */
  public void setXAxisTitle(String title) {

    this.axisPair.getxAxis().getAxisTitle().setText(title);
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
    this.axisPair.getyAxis().getAxisTitle().setText(title);
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

  // / Internal /////////////////////////////////////////

  /**
   * for internal usage
   * 
   * @return
   */
  public ChartTitle getChartTitle() {

    return chartTitle;
  }

  /**
   * for internal usage
   * 
   * @return
   */
  public Legend getChartLegend() {

    return chartLegend;
  }

  /**
   * for internal usage
   * 
   * @return
   */
  public AxisPair getAxisPair() {

    return axisPair;
  }

  /**
   * for internal usage
   * 
   * @return
   */
  public Plot getPlot() {

    return plot;
  }

  public int getWidth() {

    return width;
  }

  public int getHeight() {

    return height;
  }

}
