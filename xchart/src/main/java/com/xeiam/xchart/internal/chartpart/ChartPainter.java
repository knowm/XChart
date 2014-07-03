/**
 * Copyright 2011 - 2014 Xeiam LLC.
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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import com.xeiam.xchart.Series;
import com.xeiam.xchart.StyleManager;

/**
 * @author timmolter
 */
public class ChartPainter {

  private int width;
  private int height;

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
  public ChartPainter(int width, int height) {

    this.width = width;
    this.height = height;

    styleManager = new StyleManager();

    chartLegend = new Legend(this);
    axisPair = new AxisPair(this);
    plot = new Plot(this);
    chartTitle = new ChartTitle(this);
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

    for (Series series : getAxisPair().getSeriesMap().values()) {
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
    if (axisPair.getSeriesMap().isEmpty()) {
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

  /**
   * Gets the Chart's style manager, which can be used to customize the Chart's appearance
   *
   * @return the style manager
   */
  public StyleManager getStyleManager() {

    return styleManager;
  }
}
