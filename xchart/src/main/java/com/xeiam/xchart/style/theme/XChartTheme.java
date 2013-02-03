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
package com.xeiam.xchart.style.theme;

import java.awt.Color;
import java.awt.Font;

import com.xeiam.xchart.style.ChartColor;

/**
 * @author timmolter
 */
public class XChartTheme implements Theme {

  @Override
  public Color getChartBackgroundColor() {

    return ChartColor.getAWTColor(ChartColor.GREY);
  }

  @Override
  public Color getChartBordersColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
  }

  @Override
  public Color getChartFontColor() {

    return ChartColor.getAWTColor(ChartColor.BLACK);
  }

  @Override
  public Font getChartTitleFont() {

    return new Font(Font.SANS_SERIF, Font.BOLD, 14);
  }

}
