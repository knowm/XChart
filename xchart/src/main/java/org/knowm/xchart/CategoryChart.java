/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.internal.chartpart.Legend_Marker;
import org.knowm.xchart.internal.chartpart.Plot_Category;
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Theme;

/**
 * @author timmolter
 */
public class CategoryChart extends Chart<CategoryStyler, CategorySeries> {

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public CategoryChart(int width, int height) {
    this(width,height,false);
  }
  public CategoryChart(int width, int height, boolean paintJustXAxisTickLabels) {

    super(width, height, new CategoryStyler());
    axisPair = new AxisPair<CategoryStyler, CategorySeries>(this,paintJustXAxisTickLabels);
    plot = new Plot_Category<CategoryStyler, CategorySeries>(this);
    legend = new Legend_Marker<CategoryStyler, CategorySeries>(this);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public CategoryChart(int width, int height, Theme theme) {
    this(width, height, theme, false);
  }
  public CategoryChart(int width, int height, Theme theme, boolean paintJustXAxisTickLabels) {

    this(width, height,paintJustXAxisTickLabels);
    styler.setTheme(theme);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param chartTheme - pass in the desired ChartTheme enum
   */
  public CategoryChart(int width, int height, ChartTheme chartTheme) {
    this(width, height, chartTheme,false);
  }
  public CategoryChart(int width, int height, ChartTheme chartTheme, boolean paintJustXAxisTickLabels) {

    this(width, height, chartTheme.newInstance(chartTheme),paintJustXAxisTickLabels);
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public CategoryChart(CategoryChartBuilder chartBuilder) {
    this(chartBuilder,false);
  }
  public CategoryChart(CategoryChartBuilder chartBuilder,boolean paintJustXAxisTickLabels) {

    this(chartBuilder.width, chartBuilder.height, chartBuilder.chartTheme,paintJustXAxisTickLabels);
    setTitle(chartBuilder.title);
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
  }

  /**
   * Add a series for a Category type chart using using double arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(String seriesName, double[] xData, double[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a Category type chart using using double arrays with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(String seriesName, double[] xData, double[] yData, double[] errorBars) {

    return addSeries(seriesName, Utils.getNumberListFromDoubleArray(xData), Utils.getNumberListFromDoubleArray(yData), Utils.getNumberListFromDoubleArray(errorBars));
  }

  /**
   * Add a series for a Category type chart using using int arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(String seriesName, int[] xData, int[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a Category type chart using using int arrays with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param xData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(String seriesName, int[] xData, int[] yData, int[] errorBars) {

    return addSeries(seriesName, Utils.getNumberListFromIntArray(xData), Utils.getNumberListFromIntArray(yData), Utils.getNumberListFromIntArray(errorBars));
  }

  /**
   * Add a series for a Category type chart using Lists
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a Category type chart using Lists with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {

    // Sanity checks
    sanityCheck(seriesName, xData, yData, errorBars);

    CategorySeries series;
    if (xData != null) {

      // Sanity check
      if (xData.size() != yData.size()) {
        throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
      }

      series = new CategorySeries(seriesName, xData, yData, errorBars, getDataType(xData));
    } else { // generate xData
      series = new CategorySeries(seriesName, Utils.getGeneratedDataAsList(yData.size()), yData, errorBars, getDataType(xData));
    }

    seriesMap.put(seriesName, series);

    return series;
  }

  private DataType getDataType(List<?> data) {

    DataType axisType;

    Iterator<?> itr = data.iterator();
    Object dataPoint = itr.next();
    if (dataPoint instanceof Number) {
      axisType = DataType.Number;
    } else if (dataPoint instanceof Date) {
      axisType = DataType.Date;
    } else if (dataPoint instanceof String) {
      axisType = DataType.String;
    } else {
      throw new IllegalArgumentException("Series data must be either Number, Date or String type!!!");
    }
    return axisType;
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and error bar data
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers starting from
   * 1 and ending at the size of the new Y-Axis data list.
   * @param newYData
   * @param newErrorBarData - set null if there are no error bars
   * @return
   */
  public CategorySeries updateCategorySeries(String seriesName, List<?> newXData, List<? extends Number> newYData, List<? extends Number> newErrorBarData) {

    Map<String, CategorySeries> seriesMap = getSeriesMap();
    CategorySeries series = seriesMap.get(seriesName);
    if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
    if (newXData == null) {
      // generate X-Data
      List<Integer> generatedXData = new ArrayList<Integer>();
      for (int i = 1; i <= newYData.size(); i++) {
        generatedXData.add(i);
      }
      series.replaceData(generatedXData, newYData, newErrorBarData);
    } else {
      series.replaceData(newXData, newYData, newErrorBarData);
    }

    return series;
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and error bar data
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers starting from
   * 1 and ending at the size of the new Y-Axis data list.
   * @param newYData
   * @param newErrorBarData - set null if there are no error bars
   * @return
   */
  public CategorySeries updateCategorySeries(String seriesName, double[] newXData, double[] newYData, double[] newErrorBarData) {

    return updateCategorySeries(seriesName, Utils.getNumberListFromDoubleArray(newXData), Utils.getNumberListFromDoubleArray(newYData), Utils.getNumberListFromDoubleArray(newErrorBarData));
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

    // set the series render styles if they are not set. Legend and Plot need it.
    for (CategorySeries seriesCategory : getSeriesMap().values()) {
      CategorySeries.CategorySeriesRenderStyle seriesType = seriesCategory.getChartCategorySeriesRenderStyle(); // would be directly set
      if (seriesType == null) { // wasn't overridden, use default from Style Manager
        seriesCategory.setChartCategorySeriesRenderStyle(getStyler().getDefaultSeriesRenderStyle());
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
   * set the series color, marker and line style based on theme
   */
  private void setSeriesStyles() {

    SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler = new SeriesColorMarkerLineStyleCycler(getStyler().getSeriesColors(), getStyler().getSeriesMarkers(), getStyler()
        .getSeriesLines());
    for (CategorySeries series : getSeriesMap().values()) {

      SeriesColorMarkerLineStyle seriesColorMarkerLineStyle = seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle();

      if (series.getLineStyle() == null) { // wasn't set manually
        series.setLineStyle(seriesColorMarkerLineStyle.getStroke());
      }
      if (series.getLineColor() == null) { // wasn't set manually
        series.setLineColor(seriesColorMarkerLineStyle.getColor());
      }
      if (series.getFillColor() == null) { // wasn't set manually
        series.setFillColor(seriesColorMarkerLineStyle.getColor());
      }
      if (series.getMarker() == null) { // wasn't set manually
        series.setMarker(seriesColorMarkerLineStyle.getMarker());
      }
      if (series.getMarkerColor() == null) { // wasn't set manually
        series.setMarkerColor(seriesColorMarkerLineStyle.getColor());
      }
    }
  }
  
  /**
   *
   * Set custom X-Axis category labels
   *
   * @param customCategoryLabels Map containing category name -> label mappings
   */
  public void setCustomCategoryLabels(Map<Object, Object> customCategoryLabels) {

    // get the first series
    AxesChartSeriesCategory axesChartSeries = getSeriesMap().values().iterator().next();
    // get the first categories, could be Number Date or String
    List<?> categories = (List<?>) axesChartSeries.getXData();

    Map<Double, Object> axisTickValueLabelMap = new LinkedHashMap<Double, Object>();
    for (Entry<Object, Object> entry : customCategoryLabels.entrySet()) {
      int index = categories.indexOf(entry.getKey());
      if (index == -1) {
        throw new IllegalArgumentException("Could not find category index for " + entry.getKey());
      }
      axisTickValueLabelMap.put((double) index, entry.getValue());
    }

    setXAxisLabelOverrideMap(axisTickValueLabelMap);
  }

}
