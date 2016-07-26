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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;

import org.knowm.xchart.internal.Series;
import org.knowm.xchart.style.Styler;

/**
 * An XChart Chart
 *
 * @author timmolter
 */
public abstract class Chart<ST extends Styler, S extends Series> {

  public abstract void paint(Graphics2D g, int width, int height);

  protected ST styler;

  /** Meta Data */
  private int width;
  private int height;
  private String title = "";
  private String xAxisTitle = "";
  private String yAxisTitle = "";

  /** Chart Parts */
  protected ChartTitle chartTitle;
  protected Legend_ legend;
  protected Plot_ plot;
  protected AxisPair axisPair;

  protected Map<String, S> seriesMap = new LinkedHashMap<String, S>();

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param styler
   */
  public Chart(int width, int height, ST styler) {

    this.width = width;
    this.height = height;
    this.styler = styler;

    this.chartTitle = new ChartTitle(this);
  }

  public void paintBackground(Graphics2D g) {

    // paint chart main background
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // global rendering hint
    g.setColor(styler.getChartBackgroundColor());
    Shape rect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
    g.fill(rect);
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

  protected Legend_ getLegend() {

    return legend;
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

  public S removeSeries(String seriesName) {

    return seriesMap.remove(seriesName);
  }

  /**
   * Gets the Chart's styler, which can be used to customize the Chart's appearance
   *
   * @return the styler
   */
  public ST getStyler() {

    return styler;
  }

}
