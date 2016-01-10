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
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.style.StyleManager;

/**
 * An XChart Chart
 *
 * @author timmolter
 */
public abstract class Chart<SM extends StyleManager, S extends Series> implements ChartPart {

  public abstract void paint(Graphics2D g, int width, int height);

  protected SM styleManager;

  /** Meta Data */
  private int width;
  private int height;
  private String title;
  private String xAxisTitle;
  private String yAxisTitle;

  /** Chart Parts */
  protected ChartTitle chartTitle;
  protected Legend chartLegend;
  protected Plot_ plot;
  protected AxisPair axisPair;

  protected Map<String, S> seriesMap = new LinkedHashMap<String, S>();

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param styleManager
   */
  public Chart(int width, int height, SM styleManager) {

    this.width = width;
    this.height = height;
    this.styleManager = styleManager;

    this.chartTitle = new ChartTitle(this);
  }

  public List<Double> getNumberListFromDoubleArray(double[] data) {

    if (data == null) {
      return null;
    }

    List<Double> dataNumber = null;
    if (data != null) {
      dataNumber = new ArrayList<Double>();
      for (double d : data) {
        dataNumber.add(new Double(d));
      }
    }
    return dataNumber;
  }

  public List<Double> getNumberListFromIntArray(int[] data) {

    if (data == null) {
      return null;
    }

    List<Double> dataNumber = null;
    if (data != null) {
      dataNumber = new ArrayList<Double>();
      for (double d : data) {
        dataNumber.add(new Double(d));
      }
    }
    return dataNumber;
  }

  public List<Double> getGeneratedData(int length) {

    List<Double> generatedData = new ArrayList<Double>();
    for (int i = 1; i < length + 1; i++) {
      generatedData.add((double) i);
    }
    return generatedData;
  }

  /** Meta Data Getters and Setters */
  public int getWidth() {

    return width;
  }

  public void setWidth(int width) {

    this.width = width;
  }

  public int getHeight() {

    return height;
  }

  public void setHeight(int height) {

    this.height = height;
  }

  public String getTitle() {

    return title;
  }

  public void setTitle(String title) {

    this.title = title;
  }

  public String getXAxisTitle() {

    return xAxisTitle;
  }

  public void setXAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
  }

  public String getyYAxisTitle() {

    return yAxisTitle;
  }

  public void setYAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
  }

  /** Chart Parts Getters */

  protected ChartTitle getChartTitle() {

    return chartTitle;
  }

  protected Legend getChartLegend() {

    return chartLegend;
  }

  protected Plot_ getPlot() {

    return plot;
  }

  protected Axis getXAxis() {

    return axisPair.getXAxis();
  }

  protected Axis getYAxis() {

    return axisPair.getYAxis();
  }

  public AxisPair getAxisPair() {

    return axisPair;
  }

  public Map<String, S> getSeriesMap() {

    return seriesMap;
  }

  /**
   * Gets the Chart's style manager, which can be used to customize the Chart's appearance
   *
   * @return the style manager
   */
  public SM getStyleManager() {

    return styleManager;
  }

  @Override
  public Rectangle2D getBounds() {

    return new Rectangle2D.Double(0, 0, width, height);
  }
}
