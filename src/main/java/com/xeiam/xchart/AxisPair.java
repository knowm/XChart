/**
 * Copyright 2011-2012 Xeiam LLC.
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
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.xeiam.xchart.Axis.AxisType;
import com.xeiam.xchart.interfaces.IChartPart;
import com.xeiam.xchart.series.Series;

/**
 * @author timmolter
 */
public class AxisPair implements IChartPart {

  /** the chart */
  private Chart chart;

  private Map<Integer, Series> seriesMap = new LinkedHashMap<Integer, Series>();

  int seriesCount = 0;

  Axis xAxis;
  Axis yAxis;

  /**
   * Constructor.
   * 
   * @param chart the chart
   */
  public AxisPair(Chart chart) {

    this.chart = chart;

    // add axes
    xAxis = new Axis(chart, this, Axis.Direction.X);
    yAxis = new Axis(chart, this, Axis.Direction.Y);
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
    if (yData.size() == 1 && Double.isNaN(yData.iterator().next().doubleValue())) {
      throw new IllegalArgumentException("Y-Axis data cannot contain a single NaN value!!!");
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
      series = new Series(seriesName, xData, xAxis.getAxisType(), yData, yAxis.getAxisType(), errorBars);
    } else { // generate xData
      Collection<Number> generatedXData = new ArrayList<Number>();
      for (int i = 1; i < yData.size(); i++) {
        generatedXData.add(i);
      }
      xAxis.setAxisType(AxisType.NUMBER);
      yAxis.setAxisType(AxisType.NUMBER);
      series = new Series(seriesName, generatedXData, xAxis.getAxisType(), yData, yAxis.getAxisType(), errorBars);
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
    xAxis.addMinMax(series.getxMin(), series.getxMax());
    yAxis.addMinMax(series.getyMin(), series.getyMax());

    return series;
  }

  protected Axis getXAxis() {

    return xAxis;
  }

  protected Axis getYAxis() {

    return yAxis;
  }

  protected Rectangle getChartTitleBounds() {

    return chart.getTitle().getBounds();
  }

  protected Rectangle getChartLegendBounds() {

    return chart.getLegend().getBounds();
  }

  protected Map<Integer, Series> getSeriesMap() {

    return seriesMap;
  }

  public static int getTickSpace(int workingSpace) {

    int tickSpace = (int) (workingSpace * 0.95);
    return tickSpace;
  }

  public static int getMargin(int workingSpace, int tickSpace) {

    int marginSpace = workingSpace - tickSpace;
    int margin = (int) (marginSpace / 2.0);
    return margin;
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
