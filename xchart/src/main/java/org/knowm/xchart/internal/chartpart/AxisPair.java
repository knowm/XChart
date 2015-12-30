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
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.Series;
import org.knowm.xchart.StyleManager.ChartType;
import org.knowm.xchart.internal.chartpart.Axis.AxisType;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyleCycler;

/**
 * @author timmolter
 */
public class AxisPair implements ChartPart {

  /** parent */
  private final ChartPainter chartPainter;

  private Map<String, Series> seriesMap = new LinkedHashMap<String, Series>();

  private Axis xAxis;
  private Axis yAxis;

  private SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler = new SeriesColorMarkerLineStyleCycler();

  /**
   * Constructor
   *
   * @param the parent chartPainter
   */
  public AxisPair(ChartPainter chartPainter) {

    this.chartPainter = chartPainter;

    // add axes
    xAxis = new Axis(this, Axis.Direction.X);
    yAxis = new Axis(this, Axis.Direction.Y);
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
      if (! seriesMap.isEmpty() && getChartPainter().getStyleManager().getChartType() == ChartType.Category) {
        Collection<?> existingSeries = seriesMap.values().iterator().next().getXData();
        if (! existingSeries.equals(xData)) {
          throw new IllegalArgumentException("Category charts must have matching x values, values do not match a previous series");
        }
      }
      // inspect the series to see what kind of data it contains (Number, Date or String)
      Iterator<?> itr = xData.iterator();
      Object dataPoint = itr.next();
      if (dataPoint instanceof Number) {
        xAxis.setAxisType(AxisType.Number);
      }
      else if (dataPoint instanceof Date) {
        xAxis.setAxisType(AxisType.Date);
      }
      else if (dataPoint instanceof String) {
        if (getChartPainter().getStyleManager().getChartType() != ChartType.Bar && 
              getChartPainter().getStyleManager().getChartType() != ChartType.Category) {
          throw new RuntimeException("X-Axis data types of String can only be used for Bar and Category Charts!!!");
        }
        xAxis.setAxisType(AxisType.String);
      }
      else {
        throw new RuntimeException("Series data must be either Number, Date or String type!!!");
      }
      yAxis.setAxisType(AxisType.Number);
      series = new Series(seriesName, xData, xAxis.getAxisType(), yData, yAxis.getAxisType(), errorBars, seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle());
    }
    else { // generate xData
      if (getChartPainter().getStyleManager().getChartType() == ChartType.Category) {
        throw new IllegalStateException("Category charts must have x data catagories for every series");
      }
      List<Double> generatedXData = new ArrayList<Double>();
      for (int i = 1; i < yData.size() + 1; i++) {
        generatedXData.add((double) i);
      }
      xAxis.setAxisType(AxisType.Number);
      yAxis.setAxisType(AxisType.Number);
      series = new Series(seriesName, generatedXData, xAxis.getAxisType(), yData, yAxis.getAxisType(), errorBars, seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle());
    }

    // set series type based on chart type, but only if it's not explicitly set on the series yet.
    switch (chartPainter.getStyleManager().getChartType()) {
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

  @Override
  public void paint(Graphics2D g) {

    yAxis.paint(g);
    xAxis.paint(g);
  }

  @Override
  public Rectangle getBounds() {

    return null; // should never be called
  }

  @Override
  public ChartPainter getChartPainter() {

    return chartPainter;
  }

  // Getters /////////////////////////////////////////////////

  public Map<String, Series> getSeriesMap() {

    return seriesMap;
  }

  public Axis getXAxis() {

    return xAxis;
  }

  public Axis getYAxis() {

    return yAxis;
  }

}
