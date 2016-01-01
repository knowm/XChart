/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
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
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.Series;
import org.knowm.xchart.StyleManager;
import org.knowm.xchart.internal.chartpart.Axis.AxisType;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;

/**
 * @author timmolter
 */
public class ChartInternal {

  private int width;
  private int height;

  private Map<String, Series> seriesMap = new LinkedHashMap<String, Series>();
  private SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler = new SeriesColorMarkerLineStyleCycler();

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
  public ChartInternal(int width, int height) {

    this.width = width;
    this.height = height;

    styleManager = new StyleManager();

    chartLegend = new Legend(this);
    axisPair = new AxisPair(this);
    plot = new Plot(this);
    chartTitle = new ChartTitle(this);
  }

  /**
   * @param seriesName
   * @param xData
   * @param yData
   * @param errorBars
   * @return Series
   */
  public Series addSeries(String seriesName, List<?> xData, List<? extends Number> yData, List<? extends Number> errorBars) {

    // Sanity checks
    if (seriesName == null) {
      throw new IllegalArgumentException("Series Name cannot be null!!!");
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
    // Sanity check
    if (errorBars != null && errorBars.size() != yData.size()) {
      throw new IllegalArgumentException("errorbars and Y-Axis sizes are not the same!!!");
    }

    Series series = null;
    if (xData != null) {

      // Sanity check
      if (xData.size() != yData.size()) {
        throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
      }
      // inspect the series to see what kind of data it contains (Number, Date or String)
      Iterator<?> itr = xData.iterator();
      Object dataPoint = itr.next();
      if (dataPoint instanceof Number) {
        axisPair.getXAxis().setAxisType(AxisType.Number);
      }
      else if (dataPoint instanceof Date) {
        axisPair.getXAxis().setAxisType(AxisType.Date);
      }
      else if (dataPoint instanceof String) {
        axisPair.getXAxis().setAxisType(AxisType.String);
      }
      else {
        throw new IllegalArgumentException("Series data must be either Number, Date or String type!!!");
      }
      axisPair.getYAxis().setAxisType(AxisType.Number);
      series = new Series(seriesName, xData, axisPair.getXAxis().getAxisType(), yData, axisPair.getYAxis().getAxisType(), errorBars, seriesColorMarkerLineStyleCycler
          .getNextSeriesColorMarkerLineStyle());
    }
    else { // generate xData
      List<Double> generatedXData = new ArrayList<Double>();
      for (int i = 1; i < yData.size() + 1; i++) {
        generatedXData.add((double) i);
      }
      axisPair.getXAxis().setAxisType(AxisType.Number);
      axisPair.getYAxis().setAxisType(AxisType.Number);
      series = new Series(seriesName, generatedXData, axisPair.getXAxis().getAxisType(), yData, axisPair.getYAxis().getAxisType(), errorBars, seriesColorMarkerLineStyleCycler
          .getNextSeriesColorMarkerLineStyle());
    }

    // set series type based on chart type, but only if it's not explicitly set on the series yet.
    switch (getStyleManager().getChartType()) {
    case Line:
      if (series.getSeriesType() == null) {
        series.setSeriesType(Series.SeriesType.Line);
      }
      break;
    case Area:
      if (series.getSeriesType() == null) {
        series.setSeriesType(Series.SeriesType.Area);
      }
      break;
    case Scatter:
      if (series.getSeriesType() == null) {
        series.setSeriesType(Series.SeriesType.Scatter);
      }
      break;
    case Bar:
      if (series.getSeriesType() == null) {
        series.setSeriesType(Series.SeriesType.Bar);
      }
      break;
    default:
      if (series.getSeriesType() == null) {
        series.setSeriesType(Series.SeriesType.Line);
      }
      break;
    }

    if (seriesMap.keySet().contains(seriesName)) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< has already been used. Use unique names for each series!!!");
    }

    seriesMap.put(seriesName, series);

    return series;
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

    // calc axis min and max
    axisPair.getXAxis().resetMinMax();
    axisPair.getYAxis().resetMinMax();

    for (Series series : getSeriesMap().values()) {
      // add min/max to axis
      // System.out.println(series.getxMin());
      // System.out.println(series.getxMax());
      // System.out.println(series.getyMin());
      // System.out.println(series.getyMax());
      // System.out.println("****");
      axisPair.getXAxis().addMinMax(series.getXMin(), series.getXMax());
      axisPair.getYAxis().addMinMax(series.getYMin(), series.getYMax());
    }

    // Sanity checks
    if (getSeriesMap().isEmpty()) {
      throw new RuntimeException("No series defined for Chart!!!");
    }
    if (getStyleManager().isXAxisLogarithmic() && axisPair.getXAxis().getMin() <= 0.0) {
      throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic X-Axis!!!");
    }
    if (getStyleManager().isYAxisLogarithmic() && axisPair.getYAxis().getMin() <= 0.0) {
      // System.out.println(axisPair.getyAxis().getMin());
      throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic Y-Axis!!!");
    }

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // global rendering hint
    g.setColor(styleManager.getChartBackgroundColor());
    Shape rect = new Rectangle2D.Double(0, 0, width, height);
    g.fill(rect);

    // now that we added all the series, we can calculate the legend size
    chartLegend.determineLegendBoxSize();

    axisPair.paint(g);
    plot.paint(g);
    chartTitle.paint(g);
    chartLegend.paint(g);

    g.dispose();
  }

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

  public Map<String, Series> getSeriesMap() {

    return seriesMap;
  }

  /**
   * Gets the Chart's style manager, which can be used to customize the Chart's appearance
   *
   * @return the style manager
   */
  public StyleManager getStyleManager() {

    return styleManager;
  }
}
