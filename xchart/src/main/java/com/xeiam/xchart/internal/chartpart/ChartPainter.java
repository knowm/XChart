/**
 * Copyright (C) 2013 Xeiam LLC http://xeiam.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.math.BigDecimal;

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
   * @param chart
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

    // Sanity checks
    if (axisPair.getSeriesMap().isEmpty()) {
      throw new RuntimeException("No series defined for Chart!!!");
    }
    if (getStyleManager().isXAxisLogarithmic() && axisPair.getxAxis().getMin().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Series data cannot be less or equal to zero for a logarithmic X-Axis!!!");
    }
    if (getStyleManager().isYAxisLogarithmic() && axisPair.getyAxis().getMin().compareTo(BigDecimal.ZERO) <= 0) {
      // System.out.println(axisPair.getyAxis().getMin());
      throw new IllegalArgumentException("Series data cannot be less or equal to zero for a logarithmic Y-Axis!!!");
    }

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // global rendering hint
    g.setColor(styleManager.getChartBackgroundColor());
    g.fillRect(0, 0, width, height);

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
