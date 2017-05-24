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
package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.text.Format;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/**
 * An XChart Chart
 *
 * @author timmolter
 */
public abstract class Chart<ST extends Styler, S extends Series> {

  public abstract void paint(Graphics2D g, int width, int height);

  protected final ST styler;

  /**
   * Meta Data
   */
  private int width;
  private int height;
  private String title = "";
  private String xAxisTitle = "";
  private HashMap<Integer, String> yAxisTitleMap = new HashMap<Integer, String>();

  /**
   * Chart Parts
   */
  protected AxisPair axisPair;
  final ToolTips toolTips; // ToolTip is hewre because AxisPair and Plot need access to it
  protected Plot_ plot;
  protected final ChartTitle chartTitle;
  protected Legend_ legend;

  protected final Map<String, S> seriesMap = new LinkedHashMap<String, S>();

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param styler
   */
  protected Chart(int width, int height, ST styler) {

    this.width = width;
    this.height = height;
    this.styler = styler;

    this.toolTips = new ToolTips(styler);

    this.chartTitle = new ChartTitle(this);
  }

  protected void paintBackground(Graphics2D g) {

    // paint chart main background
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // global rendering hint
    g.setColor(styler.getChartBackgroundColor());
    Shape rect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
    g.fill(rect);
  }

  /**
   * Gets the Chart's styler, which can be used to customize the Chart's appearance
   *
   * @return the styler
   */
  public ST getStyler() {

    return styler;
  }

  public S removeSeries(String seriesName) {

    return seriesMap.remove(seriesName);
  }

  public ToolTips getToolTips() {

    return toolTips;
  }

  /**
   * Meta Data Getters and Setters
   */
  public int getWidth() {

    return width;
  }

  protected void setWidth(int width) {

    this.width = width;
  }

  public int getHeight() {

    return height;
  }

  protected void setHeight(int height) {

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

  public String getYAxisTitle(int yIndex) {
    
	return yAxisTitleMap.get(yIndex);
  }

  public void setYAxisTitle(String yAxisTitle) {
    
	yAxisTitleMap.put(0, yAxisTitle);
  }
  
  public void setYAxisTitle(int yIndex, String yAxisTitle) {

	yAxisTitleMap.put(yIndex, yAxisTitle);
  }

  /**
   * Chart Parts Getters
   */

  ChartTitle getChartTitle() {

    return chartTitle;
  }

  Legend_ getLegend() {

    return legend;
  }

  Plot_ getPlot() {

    return plot;
  }

  Axis getXAxis() {

    return axisPair.getXAxis();
  }

  Axis getYAxis() {

    return axisPair.getYAxis();
  }
  
  Axis getYAxis(int yIndex) {
    
    return axisPair.getYAxis(yIndex);
  }

  AxisPair getAxisPair() {

    return axisPair;
  }

  public Map<String, S> getSeriesMap() {

    return seriesMap;
  }

  Format getXAxisFormat() {

    return axisPair.getXAxis().getAxisTickCalculator().getAxisFormat();
  }

  Format getYAxisFormat() {

    return axisPair.getYAxis().getAxisTickCalculator().getAxisFormat();
  }

}
