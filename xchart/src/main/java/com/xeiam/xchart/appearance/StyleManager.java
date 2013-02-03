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
package com.xeiam.xchart.appearance;

import java.awt.Color;

/**
 * @author timmolter
 */
public class StyleManager {

  private Color backgroundColor;
  public Color bordersColor;
  public Color fontColor;

  /**
   * Constructor
   */
  public StyleManager() {

    backgroundColor = ChartColor.getAWTColor(ChartColor.GREY);
    bordersColor = ChartColor.getAWTColor(ChartColor.DARK_GREY);
    fontColor = ChartColor.getAWTColor(ChartColor.BLACK);
  }

  /**
   * Set the chart background color - the part around the edge of the chart
   * 
   * @param color
   */
  public void setBackgroundColor(Color backgroundColor) {

    this.backgroundColor = backgroundColor;
  }

  /**
   * Sets the color of the plot border, legend border, tick marks, and error bars
   * 
   * @param color
   */
  public void setBordersColor(Color bordersColor) {

    this.bordersColor = bordersColor;
  }

  /**
   * Set the chart font color
   * 
   * @param color
   */
  public void setFontColor(Color fontColor) {

    this.fontColor = fontColor;
  }

  public Color getBackgroundColor() {

    return backgroundColor;
  }

  public Color getBordersColor() {

    return bordersColor;
  }

  public Color getFontColor() {

    return fontColor;
  }

}
