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
import java.awt.Stroke;

import com.xeiam.xchart.style.StyleManager.LegendPosition;

/**
 * @author timmolter
 */
public interface Theme {

  // Chart Style ///////////////////////////////

  public Color getChartBackgroundColor();

  public Color getChartFontColor();

  public int getChartPadding();

  // Chart Title ///////////////////////////////

  public Font getChartTitleFont();

  public boolean isChartTitleVisible();

  public Color getChartTitleBackgroundColor();

  public Color getChartTitleBorderColor();

  public int getChartTitlePadding();

  // Chart Legend ///////////////////////////////

  public Font getLegendFont();

  public boolean isLegendVisible();

  public Color getLegendBackgroundColor();

  public Color getLegendBorderColor();

  public int getLegendPadding();

  public LegendPosition getLegendPosition();

  // Chart Axes ///////////////////////////////

  public boolean isXAxisTitleVisible();

  public boolean isYAxisTitleVisible();

  public Font getAxisTitleFont();

  public boolean isXAxisTicksVisible();

  public boolean isYAxisTicksVisible();

  public Font getAxisTickLabelsFont();

  public int getAxisTickMarkLength();

  public int getAxisTickPadding();

  public Color getAxisTickMarksColor();

  public Stroke getAxisTickMarksStroke();

  public Color getAxisTickLabelsColor();

  public boolean isAxisTicksLineVisible();

  public int getAxisTitlePadding();

  public int getPlotPadding();

  // Chart Plot Area ///////////////////////////////

  public boolean isPlotGridLinesVisible();

  public Color getPlotBackgroundColor();

  public Color getPlotBorderColor();

  public boolean isPlotBorderVisible();

  public Color getPlotGridLinesColor();

  public Stroke getPlotGridLinesStroke();

  // Error Bars ///////////////////////////////

  public Color getErrorBarsColor();

}
