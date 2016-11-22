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
package org.knowm.xchart.style;

import org.knowm.xchart.Font;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.SeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.Color;
import java.awt.Stroke;

/**
 * @author timmolter
 */
public abstract class Theme implements SeriesMarkers, SeriesLines, SeriesColors {

  // Chart Style ///////////////////////////////

  public abstract Color getChartBackgroundColor();

  public abstract Color getChartFontColor();

  public abstract int getChartPadding();

  // Chart Title ///////////////////////////////

  public abstract Font getChartTitleFont();

  public abstract boolean isChartTitleVisible();

  public abstract boolean isChartTitleBoxVisible();

  public abstract Color getChartTitleBoxBackgroundColor();

  public abstract Color getChartTitleBoxBorderColor();

  public abstract int getChartTitlePadding();

  // Chart Legend ///////////////////////////////

  public abstract Font getLegendFont();

  public abstract boolean isLegendVisible();

  public abstract Color getLegendBackgroundColor();

  public abstract Color getLegendBorderColor();

  public abstract int getLegendPadding();

  public abstract int getLegendSeriesLineLength();

  public abstract LegendPosition getLegendPosition();

  // Chart Axes ///////////////////////////////

  public abstract boolean isXAxisTitleVisible();

  public abstract boolean isYAxisTitleVisible();

  public abstract Font getAxisTitleFont();

  public abstract boolean isXAxisTicksVisible();

  public abstract boolean isYAxisTicksVisible();

  public abstract Font getAxisTickLabelsFont();

  public abstract int getAxisTickMarkLength();

  public abstract int getAxisTickPadding();

  public abstract Color getAxisTickMarksColor();

  public abstract Stroke getAxisTickMarksStroke();

  public abstract Color getAxisTickLabelsColor();

  public abstract boolean isAxisTicksLineVisible();

  public abstract boolean isAxisTicksMarksVisible();

  public abstract int getAxisTitlePadding();

  public abstract int getXAxisTickMarkSpacingHint();

  public abstract int getYAxisTickMarkSpacingHint();

  // Chart Plot Area ///////////////////////////////

  public abstract boolean isPlotGridLinesVisible();

  public abstract boolean isPlotGridVerticalLinesVisible();

  public abstract boolean isPlotGridHorizontalLinesVisible();

  public abstract Color getPlotBackgroundColor();

  public abstract Color getPlotBorderColor();

  public abstract boolean isPlotBorderVisible();

  public abstract Color getPlotGridLinesColor();

  public abstract Stroke getPlotGridLinesStroke();

  public abstract boolean isPlotTicksMarksVisible();

  public abstract double getPlotContentSize();

  public abstract int getPlotMargin();

  // Bar Charts ///////////////////////////////

  public abstract double getAvailableSpaceFill();

  public abstract boolean isOverlapped();

  // Pie Charts ///////////////////////////////

  public abstract boolean isCircular();

  public abstract double getStartAngleInDegrees();

  public abstract Font getPieFont();

  public abstract double getAnnotationDistance();

  public abstract AnnotationType getAnnotationType();

  public abstract boolean isDrawAllAnnotations();

  public abstract double getDonutThickness();

  // Line, Scatter, Area Charts ///////////////////////////////

  public abstract int getMarkerSize();

  public abstract boolean showMarkers();

  // Error Bars ///////////////////////////////

  public abstract Color getErrorBarsColor();

  public abstract boolean isErrorBarsColorSeriesColor();

  // Annotations ///////////////////////////////

  public abstract Font getAnnotationFont();

}
