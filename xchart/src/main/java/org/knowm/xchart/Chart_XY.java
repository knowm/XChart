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
package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.knowm.xchart.internal.chartpart.Axis.AxisDataType;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.LegendAxesChart;
import org.knowm.xchart.internal.chartpart.Plot_XY;
import org.knowm.xchart.internal.style.Theme;
import org.knowm.xchart.internal.style.StyleManager.ChartTheme;

/**
 * @author timmolter
 */
public class Chart_XY extends Chart<StyleManagerXY, Series_XY> {

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public Chart_XY(int width, int height) {

    super(width, height, new StyleManagerXY());
    axisPair = new AxisPair(this);
    plot = new Plot_XY(this);
    chartLegend = new LegendAxesChart(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public Chart_XY(int width, int height, Theme theme) {

    this(width, height);
    styleManager.setTheme(theme);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param chartTheme - pass in the desired ChartTheme enum
   */
  public Chart_XY(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public Chart_XY(ChartBuilderXY chartBuilder) {

    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
    setTitle(chartBuilder.title);
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
  }

  /**
   * Add a series for a X-Y type chart using Lists
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public Series_XY addSeries(String seriesName, List<?> xData, List<? extends Number> yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a X-Y type chart using using double arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public Series_XY addSeries(String seriesName, double[] xData, double[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a X-Y type chart using using double arrays with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public Series_XY addSeries(String seriesName, double[] xData, double[] yData, double[] errorBars) {

    return addSeries(seriesName, getNumberListFromDoubleArray(xData), getNumberListFromDoubleArray(yData), getNumberListFromDoubleArray(errorBars));
  }

  /**
   * Add a series for a X-Y type chart using using int arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public Series_XY addSeries(String seriesName, int[] xData, int[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a X-Y type chart using using int arrays with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public Series_XY addSeries(String seriesName, int[] xData, int[] yData, int[] errorBars) {

    return addSeries(seriesName, getNumberListFromIntArray(xData), getNumberListFromIntArray(yData), getNumberListFromIntArray(errorBars));
  }

  /**
   * Add a series for a X-Y type chart using Lists with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public Series_XY addSeries(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {

    // Sanity checks
    sanityCheck(seriesName, xData, yData, errorBars);

    Series_XY series = null;
    if (xData != null) {

      // Sanity check
      if (xData.size() != yData.size()) {
        throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
      }

      // inspect the series to see what kind of data it contains (Number, Date)

      series = new Series_XY(seriesName, xData, yData, errorBars, styleManager.getSeriesColorMarkerLineStyleCycler().getNextSeriesColorMarkerLineStyle());
    }
    else { // generate xData
      series = new Series_XY(seriesName, getGeneratedData(yData.size()), yData, errorBars, styleManager.getSeriesColorMarkerLineStyleCycler().getNextSeriesColorMarkerLineStyle());
    }

    seriesMap.put(seriesName, series);

    getXAxis().setAxisType(series.getxAxisType());
    getYAxis().setAxisType(AxisDataType.Number);

    return series;
  }

  ///////////////////////////////////////////////////
  // Internal Members and Methods ///////////////////
  ///////////////////////////////////////////////////

  private void sanityCheck(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {

    if (seriesMap.keySet().contains(seriesName)) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< has already been used. Use unique names for each series!!!");
    }
    if (yData == null) {
      throw new IllegalArgumentException("Y-Axis data cannot be null!!!");
    }
    if (yData.size() == 0) {
      throw new IllegalArgumentException("Y-Axis data cannot be empty!!!");
    }
    if (xData != null && xData.size() == 0) {
      throw new IllegalArgumentException("X-Axis data cannot be empty!!!");
    }
    if (errorBars != null && errorBars.size() != yData.size()) {
      throw new IllegalArgumentException("Error bars and Y-Axis sizes are not the same!!!");
    }
  }

  @Override
  public void paint(Graphics2D g, int width, int height) {

    setWidth(width);
    setHeight(height);
    paint(g);
  }

  @Override
  public void paint(Graphics2D g) {

    // Sanity checks
    if (getSeriesMap().isEmpty()) {
      throw new RuntimeException("No series defined for Chart!!!");
    }

    // set the series render styles if they are not set. Legend and Plot need it.
    for (Series_XY seriesXY : getSeriesMap().values()) {
      Series_XY.ChartXYSeriesRenderStyle seriesType = seriesXY.getChartXYSeriesRenderStyle(); // would be directly set
      if (seriesType == null) { // wasn't overridden, use default from Style Manager
        seriesXY.setChartXYSeriesRenderStyle(getStyleManager().getChartXYSeriesRenderStyle());
      }
    }

    // paint chart main background
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // global rendering hint
    g.setColor(styleManager.getChartBackgroundColor());
    Shape rect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
    g.fill(rect);

    axisPair.paint(g);
    plot.paint(g);
    chartTitle.paint(g);
    chartLegend.paint(g);

    g.dispose();
  }

}
