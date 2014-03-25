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

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.internal.chartpart.ChartPainter;
import com.xeiam.xchart.internal.style.Theme;

/**
 * An XChart Chart
 * 
 * @author timmolter
 */
public class Chart {

  private final ChartPainter chartPainter;

  /**
   * Constructor
   * 
   * @param width
   * @param height
   */
  public Chart(int width, int height) {

    chartPainter = new ChartPainter(width, height);
  }

  /**
   * Constructor
   * 
   * @param width
   * @param height
   * @param chartTheme
   */
  public Chart(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   * 
   * @param width
   * @param height
   * @param theme instance of Theme class
   */
  public Chart(int width, int height, Theme theme) {

    chartPainter = new ChartPainter(width, height);
    chartPainter.getStyleManager().setTheme(theme);
  }

  /**
   * Constructor
   * 
   * @param chartBuilder
   */
  public Chart(ChartBuilder chartBuilder) {

    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
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

    chartPainter.paint(g, width, height);
  }

  /**
   * @param g
   */
  public void paint(Graphics2D g) {

    chartPainter.paint(g);
  }

  /**
   * Add a series to the chart using Collections
   * 
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public Series addSeries(String seriesName, Collection<?> xData, Collection<? extends Number> yData) {

    return chartPainter.getAxisPair().addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a Number series to the chart using Collections with error bars
   * 
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public Series addSeries(String seriesName, Collection<?> xData, Collection<? extends Number> yData, Collection<? extends Number> errorBars) {

    return chartPainter.getAxisPair().addSeries(seriesName, xData, yData, errorBars);
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

    List<Double> xDataNumber = null;
    if (xData != null) {
      xDataNumber = new ArrayList<Double>();
      for (double d : xData) {
        xDataNumber.add(new Double(d));
      }
    }
    List<Double> yDataNumber = new ArrayList<Double>();
    for (double d : yData) {
      yDataNumber.add(new Double(d));
    }
    List<Double> errorBarDataNumber = null;
    if (errorBars != null) {
      errorBarDataNumber = new ArrayList<Double>();
      for (double d : errorBars) {
        errorBarDataNumber.add(new Double(d));
      }
    }

    return chartPainter.getAxisPair().addSeries(seriesName, xDataNumber, yDataNumber, errorBarDataNumber);
  }

  /**
   * Add a series to the chart using int arrays
   * 
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public Series addSeries(String seriesName, int[] xData, int[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series to the chart using int arrays with error bars
   * 
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public Series addSeries(String seriesName, int[] xData, int[] yData, int[] errorBars) {

    List<Double> xDataNumber = null;
    if (xData != null) {
      xDataNumber = new ArrayList<Double>();
      for (int d : xData) {
        xDataNumber.add(new Double(d));
      }
    }
    List<Double> yDataNumber = new ArrayList<Double>();
    for (int d : yData) {
      yDataNumber.add(new Double(d));
    }
    List<Double> errorBarDataNumber = null;
    if (errorBars != null) {
      errorBarDataNumber = new ArrayList<Double>();
      for (int d : errorBars) {
        errorBarDataNumber.add(new Double(d));
      }
    }

    return chartPainter.getAxisPair().addSeries(seriesName, xDataNumber, yDataNumber, errorBarDataNumber);
  }

  /**
   * Set the chart title
   * 
   * @param title
   */
  public void setChartTitle(String title) {

    chartPainter.getChartTitle().setText(title);
  }

  /**
   * Set the x-axis title
   * 
   * @param title
   */
  public void setXAxisTitle(String title) {

    chartPainter.getAxisPair().getXAxis().getAxisTitle().setText(title);
  }

  /**
   * Set the y-axis title
   * 
   * @param title
   */
  public void setYAxisTitle(String title) {

    chartPainter.getAxisPair().getYAxis().getAxisTitle().setText(title);
  }

  /**
   * Gets the Chart's style manager, which can be used to customize the Chart's appearance
   * 
   * @return the style manager
   */
  public StyleManager getStyleManager() {

    return chartPainter.getStyleManager();
  }

  public int getWidth() {

    return chartPainter.getWidth();
  }

  public int getHeight() {

    return chartPainter.getHeight();
  }

  public Map<String, Series> getSeriesMap() {

    return chartPainter.getAxisPair().getSeriesMap();
  }

}
