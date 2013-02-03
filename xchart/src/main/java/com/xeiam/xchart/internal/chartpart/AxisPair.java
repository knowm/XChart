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
package com.xeiam.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.interfaces.IChartPart;
import com.xeiam.xchart.internal.misc.SeriesColorMarkerLineStyleCycler;
import com.xeiam.xchart.style.Series;

/**
 * @author timmolter
 */
public class AxisPair implements IChartPart {

  /** parent */
  protected Chart chart;

  public Map<Integer, Series> seriesMap = new LinkedHashMap<Integer, Series>();

  private int seriesCount;

  public Axis xAxis;
  public Axis yAxis;

  public SeriesColorMarkerLineStyleCycler seriesColorMarkerLineStyleCycler = new SeriesColorMarkerLineStyleCycler();

  /**
   * Constructor
   * 
   * @param the parent chart
   */
  public AxisPair(Chart chart) {

    this.chart = chart;

    // add axes
    xAxis = new Axis(this, Axis.Direction.X);
    yAxis = new Axis(this, Axis.Direction.Y);
  }

  /**
   * @param <T>
   * @param xData
   * @param yData
   */
  public <T> Series addSeries(String seriesName, Collection<T> xData, Collection<Number> yData, Collection<Number> errorBars) {

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

    Series series = null;
    if (xData != null) {
      // Check if xAxis series contains Number or Date data
      Iterator<?> itr = xData.iterator();
      Object dataPoint = itr.next();
      if (dataPoint instanceof Number) {
        xAxis.setAxisType(AxisType.NUMBER);
      } else if (dataPoint instanceof Date) {
        xAxis.setAxisType(AxisType.DATE);
      }
      yAxis.setAxisType(AxisType.NUMBER);
      series = new Series(seriesName, xData, xAxis.axisType, yData, yAxis.axisType, errorBars, seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle());
    } else { // generate xData
      Collection<Number> generatedXData = new ArrayList<Number>();
      for (int i = 1; i < yData.size() + 1; i++) {
        generatedXData.add(i);
      }
      xAxis.setAxisType(AxisType.NUMBER);
      yAxis.setAxisType(AxisType.NUMBER);
      series = new Series(seriesName, generatedXData, xAxis.axisType, yData, yAxis.axisType, errorBars, seriesColorMarkerLineStyleCycler.getNextSeriesColorMarkerLineStyle());
    }

    // Sanity check
    if (xData != null && xData.size() != yData.size()) {
      throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!! ");
    }
    if (errorBars != null && errorBars.size() != yData.size()) {
      throw new IllegalArgumentException("errorbars and Y-Axis sizes are not the same!!! ");
    }

    seriesMap.put(seriesCount++, series);

    // add min/max to axis
    xAxis.addMinMax(series.xMin, series.xMax);
    yAxis.addMinMax(series.yMin, series.yMax);

    return series;
  }

  protected Rectangle getChartTitleBounds() {

    return chart.chartTitle.getBounds();
  }

  protected Rectangle getChartLegendBounds() {

    return chart.chartLegend.getBounds();
  }

  /**
   * Gets the percentage of working space allowed for tick marks
   * 
   * @param workingSpace
   * @return
   */
  protected static int getTickSpace(int workingSpace) {

    return (int) (workingSpace * 0.95);
  }

  /**
   * Gets the offset for the beginning of the tick marks
   * 
   * @param workingSpace
   * @param tickSpace
   * @return
   */
  protected static int getTickStartOffset(int workingSpace, int tickSpace) {

    int marginSpace = workingSpace - tickSpace;
    return (int) (marginSpace / 2.0);
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

}
