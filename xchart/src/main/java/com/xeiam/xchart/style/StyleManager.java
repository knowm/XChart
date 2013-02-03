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
package com.xeiam.xchart.style;

import java.awt.Color;
import java.awt.Font;

import com.xeiam.xchart.style.theme.Theme;
import com.xeiam.xchart.style.theme.XChartTheme;

/**
 * @author timmolter
 */
public class StyleManager {

  /** the default Theme */
  private Theme theme = new XChartTheme();

  private Color chartBackgroundColor;
  public Color chartBordersColor;
  public Color chartFontColor;

  private Font chartTitleFont;

  /**
   * Constructor
   */
  public StyleManager() {

    setAllStyles();
  }

  private void setAllStyles() {

    chartBackgroundColor = theme.getChartBackgroundColor();
    chartBordersColor = theme.getChartBordersColor();
    chartFontColor = theme.getChartFontColor();
    chartTitleFont = theme.getChartTitleFont();
  }

  /**
   * Set the theme the style manager should use
   * 
   * @param theme
   */
  public void setTheme(Theme theme) {

    this.theme = theme;
    setAllStyles();
  }

  // Chart Style ///////////////////////////////

  /**
   * Set the chart background color - the part around the edge of the chart
   * 
   * @param color
   */
  public void setChartBackgroundColor(Color color) {

    this.chartBackgroundColor = color;
  }

  public Color getChartBackgroundColor() {

    return chartBackgroundColor;
  }

  /**
   * Sets the color of the plot border, legend border, tick marks, and error bars
   * 
   * @param color
   */
  public void setChartBordersColor(Color color) {

    this.chartBordersColor = color;
  }

  public Color getChartBordersColor() {

    return chartBordersColor;
  }

  /**
   * Set the chart font color
   * 
   * @param color
   */
  public void setChartFontColor(Color color) {

    this.chartFontColor = color;
  }

  public Color getChartFontColor() {

    return chartFontColor;
  }

  // Chart Title ///////////////////////////////

  /**
   * Set the chart title font
   * 
   * @param font
   */
  public void setTitleFont(Font font) {

    this.chartTitleFont = font;
  }

  public Font getChartTitleFont() {

    return chartTitleFont;
  }

  // Chart Legend ///////////////////////////////
  // Chart Title ///////////////////////////////

}
