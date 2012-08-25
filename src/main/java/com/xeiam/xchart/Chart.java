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
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;

import com.xeiam.xchart.series.Series;
import com.xeiam.xchart.series.SeriesColor;
import com.xeiam.xchart.series.SeriesLineStyle;
import com.xeiam.xchart.series.SeriesMarker;

/**
 * @author timmolter
 */
public class Chart {

  private int width;
  private int height;

  protected final static int CHART_PADDING = 10;

  private ChartTitle chartTitle = new ChartTitle(this);
  private ChartLegend chartLegend = new ChartLegend(this);
  private AxisPair axisPair = new AxisPair(this);
  private Plot plot = new Plot(this);

  /**
   * Constructor
   * 
   * @param pWidth
   * @param pHeight
   */
  public Chart(final int pWidth, final int pHeight) {

    width = pWidth;
    height = pHeight;
  }

  /**
   * @param g
   */
  public void paint(final Graphics2D g) {

    // Sanity check
    if (axisPair.getSeriesMap().isEmpty()) {
      throw new RuntimeException("No series defined for Chart!!!");
    }

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // global rendering hint
    g.setColor(ChartColor.getAWTColor(ChartColor.GREY));
    g.fillRect(0, 0, width, height);

    chartTitle.paint(g);
    chartLegend.paint(g);
    axisPair.paint(g);
    plot.paint(g);

    g.dispose();

    // reset static Ids
    SeriesColor.resetId();
    SeriesLineStyle.resetId();
    SeriesMarker.resetId();

  }

  // GETTERS & SETTERS

  public int getWidth() {

    return width;
  }

  public int getHeight() {

    return height;
  }

  protected ChartTitle getTitle() {

    return chartTitle;
  }

  protected ChartLegend getLegend() {

    return chartLegend;
  }

  protected AxisPair getAxisPair() {

    return axisPair;
  }

  protected Plot getPlot() {

    return plot;
  }

  // PUBLIC SETTERS

  /**
   * Add series data as Collection<Number>
   * 
   * @param seriesName
   * @param xData Collection<Number>
   * @param yData Collection<Number>
   * @return
   */
  public Series addSeries(String seriesName, Collection<Number> xData, Collection<Number> yData) {

    return axisPair.addSeries(seriesName, xData, yData, null);
  }

  public Series addSeries(String seriesName, Collection<Number> xData, Collection<Number> yData, Collection<Number> errorBars) {

    return axisPair.addSeries(seriesName, xData, yData, errorBars);
  }

  /**
   * Convenience Method - Add series data as double arrays
   * 
   * @param seriesName
   * @param xData double[]
   * @param yData double[]
   * @return
   */
  public Series addSeries(String seriesName, double[] xData, double[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Convenience Method - Add series data as double arrays with errorbars
   * 
   * @param seriesName
   * @param xData
   * @param yData
   * @param errorBars
   * @return
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

  public void setChartTitle(String title) {

    this.chartTitle.setText(title);
  }

  public void setChartTitleVisible(boolean isVisible) {

    this.chartTitle.setVisible(isVisible);
  }

  public void setXAxisTitle(String title) {

    this.axisPair.getXAxis().setAxisTitle(title);
  }

  public void setYAxisTitle(String title) {

    this.axisPair.getYAxis().setAxisTitle(title);
  }

  public void setAxisTitlesVisible(boolean isVisible) {

    this.axisPair.getXAxis().getAxisTitle().setVisible(isVisible);
    this.axisPair.getYAxis().getAxisTitle().setVisible(isVisible);
  }

  public void setChartLegendVisible(boolean isVisible) {

    this.chartLegend.setVisible(isVisible);
  }
}
