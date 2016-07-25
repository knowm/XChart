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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Bubble;
import org.knowm.xchart.internal.chartpart.Plot_Bubble;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.BubbleStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Theme;

/**
 * @author timmolter
 */
public class BubbleChart extends Chart<BubbleStyler, BubbleSeries> {

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public BubbleChart(int width, int height) {

    super(width, height, new BubbleStyler());
    axisPair = new AxisPair(this);
    plot = new Plot_Bubble(this);
    legend = new Legend_Bubble(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public BubbleChart(int width, int height, Theme theme) {

    this(width, height);
    styler.setTheme(theme);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param chartTheme - pass in the desired ChartTheme enum
   */
  public BubbleChart(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public BubbleChart(BubbleChartBuilder chartBuilder) {

    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme);
    setTitle(chartBuilder.title);
  }

  /**
   * Add a series for a Bubble type chart using using double arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @param bubbleData the bubble data
   * @return A Series object that you can set properties on
   */
  public BubbleSeries addSeries(String seriesName, double[] xData, double[] yData, double[] bubbleData) {

    return addSeries(seriesName, Utils.getNumberListFromDoubleArray(xData), Utils.getNumberListFromDoubleArray(yData), Utils.getNumberListFromDoubleArray(bubbleData));
  }

  /**
   * Add a series for a Bubble type chart using using Lists
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @param bubbleData the bubble data
   * @return A Series object that you can set properties on
   * @return
   */
  public BubbleSeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> bubbleData) {

    // Sanity checks
    sanityCheck(seriesName, xData, yData, bubbleData);

    BubbleSeries series = null;
    if (xData != null) {

      // Sanity check
      if (xData.size() != yData.size()) {
        throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
      }

      series = new BubbleSeries(seriesName, xData, yData, bubbleData);
    }
    else { // generate xData
      series = new BubbleSeries(seriesName, Utils.getGeneratedData(yData.size()), yData, bubbleData);
    }

    seriesMap.put(seriesName, series);

    return series;
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and bubble data
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers starting from
   *          1 and ending at the size of the new Y-Axis data list.
   * @param newYData
   * @param newBubbleData - set null if there are no error bars
   * @return
   */
  public BubbleSeries updateBubbleSeries(String seriesName, List<?> newXData, List<? extends Number> newYData, List<? extends Number> newBubbleData) {

    Map<String, BubbleSeries> seriesMap = getSeriesMap();
    BubbleSeries series = seriesMap.get(seriesName);
    if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
    if (newXData == null) {
      // generate X-Data
      List<Integer> generatedXData = new ArrayList<Integer>();
      for (int i = 1; i <= newYData.size(); i++) {
        generatedXData.add(i);
      }
      series.replaceData(generatedXData, newYData, newBubbleData);
    }
    else {
      series.replaceData(newXData, newYData, newBubbleData);
    }

    return series;
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and bubble data
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers starting from
   *          1 and ending at the size of the new Y-Axis data list.
   * @param newYData
   * @param newBubbleData - set null if there are no error bars
   * @return
   */
  public BubbleSeries updateXYSeries(String seriesName, double[] newXData, double[] newYData, double[] newBubbleData) {

    return updateBubbleSeries(seriesName, Utils.getNumberListFromDoubleArray(newXData), Utils.getNumberListFromDoubleArray(newYData), Utils.getNumberListFromDoubleArray(newBubbleData));
  }
  ///////////////////////////////////////////////////
  // Internal Members and Methods ///////////////////
  ///////////////////////////////////////////////////

  private void sanityCheck(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> bubbleData) {

    if (seriesMap.keySet().contains(seriesName)) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< has already been used. Use unique names for each series!!!");
    }
    if (yData == null) {
      throw new IllegalArgumentException("Y-Axis data cannot be null!!! >" + seriesName);
    }
    if (yData.size() == 0) {
      throw new IllegalArgumentException("Y-Axis data cannot be empty!!! >" + seriesName);
    }
    if (xData != null && xData.size() == 0) {
      throw new IllegalArgumentException("X-Axis data cannot be empty!!! >" + seriesName);
    }
    if (bubbleData.size() != yData.size()) {
      throw new IllegalArgumentException("Bubble Data and Y-Axis sizes are not the same!!! >" + seriesName);
    }
  }

  @Override
  public void paint(Graphics2D g, int width, int height) {

    setWidth(width);
    setHeight(height);

    // set the series types if they are not set. Legend and Plot need it.
    for (BubbleSeries bubbleSeries : getSeriesMap().values()) {
      BubbleSeries.BubbleSeriesRenderStyle seriesType = bubbleSeries.getBubbleSeriesRenderStyle(); // would be directly set
      if (seriesType == null) { // wasn't overridden, use default from Style Manager
        bubbleSeries.setBubbleSeriesRenderStyle(getStyler().getDefaultSeriesRenderStyle());
      }
    }
    setSeriesStyles();

    paintBackground(g);

    axisPair.paint(g);
    plot.paint(g);
    chartTitle.paint(g);
    legend.paint(g);
  }

  /**
   * set the series color based on theme
   */
  public void setSeriesStyles() {

    SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler = new SeriesColorMarkerLineStyleCycler(getStyler().getSeriesColors(), getStyler().getSeriesMarkers(), getStyler()
        .getSeriesLines());
    for (BubbleSeries series : getSeriesMap().values()) {

      SeriesColorMarkerLineStyle seriesColorMarkerLineStyle = seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();

      /** Line Style */
      BasicStroke stroke;

      /** Line Color */
      Color lineColor;

      /** Line Width */
      float lineWidth = -1.0f;
      if (series.getLineStyle() == null) { // wasn't set manually
        series.setLineStyle(seriesColorMarkerLineStyle.getStroke());
      }
      if (series.getLineColor() == null) { // wasn't set manually
        series.setLineColor(seriesColorMarkerLineStyle.getColor());
      }
      if (series.getFillColor() == null) { // wasn't set manually
        series.setFillColor(seriesColorMarkerLineStyle.getColor());
      }
    }
  }

}
