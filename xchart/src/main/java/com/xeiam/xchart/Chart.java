/**
 * Copyright 2011 - 2013 Xeiam LLC.
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
import java.util.Date;
import java.util.Map;

import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.internal.chartpart.ChartPainter;
import com.xeiam.xchart.internal.style.GGPlot2Theme;
import com.xeiam.xchart.internal.style.MatlabTheme;
import com.xeiam.xchart.internal.style.XChartTheme;

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

    chartPainter = new ChartPainter(width, height);

    if (chartTheme == ChartTheme.XChart) {
      chartPainter.getStyleManager().setTheme(new XChartTheme());
    }
    else if (chartTheme == ChartTheme.GGPlot2) {
      chartPainter.getStyleManager().setTheme(new GGPlot2Theme());
    }
    else if (chartTheme == ChartTheme.Matlab) {
      chartPainter.getStyleManager().setTheme(new MatlabTheme());
    }
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
   * Add a Category series to the chart
   * 
   * @param seriesName
   * @param xData
   * @param yData
   * @return
   */
  public Series addCategorySeries(String seriesName, Collection<String> xData, Collection<Number> yData) {

    return chartPainter.getAxisPair().addSeries(seriesName, xData, yData, null);
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

    return chartPainter.getAxisPair().addSeries(seriesName, xData, yData, null);
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

    return chartPainter.getAxisPair().addSeries(seriesName, xData, yData, errorBars);
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

    return chartPainter.getAxisPair().addSeries(seriesName, xData, yData, null);
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

    chartPainter.getAxisPair().getxAxis().getAxisTitle().setText(title);
  }

  /**
   * Set the y-axis title
   * 
   * @param title
   */
  public void setYAxisTitle(String title) {

    chartPainter.getAxisPair().getyAxis().getAxisTitle().setText(title);
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
