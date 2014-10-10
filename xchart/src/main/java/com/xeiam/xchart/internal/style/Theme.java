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
package com.xeiam.xchart.internal.style;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

import com.xeiam.xchart.StyleManager.LegendPosition;

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

  public boolean isChartTitleBoxVisible();

  public Color getChartTitleBoxBackgroundColor();

  public Color getChartTitleBoxBorderColor();

  public int getChartTitlePadding();

  // Chart Legend ///////////////////////////////

  public Font getLegendFont();

  public boolean isLegendVisible();

  public Color getLegendBackgroundColor();

  public Color getLegendBorderColor();

  public int getLegendPadding();

  public int getLegendSeriesLineLength();

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

  public boolean isAxisTicksMarksVisible();

  public int getAxisTitlePadding();

  public int getPlotPadding();

  public int getXAxisTickMarkSpacingHint();

  public int getYAxisTickMarkSpacingHint();

  // Chart Plot Area ///////////////////////////////

  public boolean isPlotGridLinesVisible();

  public Color getPlotBackgroundColor();

  public Color getPlotBorderColor();

  public boolean isPlotBorderVisible();

  public Color getPlotGridLinesColor();

  public Stroke getPlotGridLinesStroke();

  public boolean isPlotTicksMarksVisible();

  // Bar Charts ///////////////////////////////

  public double getBarWidthPercentage();

  public boolean isBarsOverlapped();

  public boolean isBarFilled();

  // Line, Scatter, Area Charts ///////////////////////////////

  public int getMarkerSize();

  // Error Bars ///////////////////////////////

  public Color getErrorBarsColor();

}
