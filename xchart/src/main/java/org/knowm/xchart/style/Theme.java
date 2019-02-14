package org.knowm.xchart.style;

import java.awt.*;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.ToolTipType;
import org.knowm.xchart.style.colors.SeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

/** @author timmolter */
public interface Theme extends SeriesMarkers, SeriesLines, SeriesColors {

  // Chart Style ///////////////////////////////

  Font getBaseFont();

  Color getChartBackgroundColor();

  Color getChartFontColor();

  int getChartPadding();

  // Chart Title ///////////////////////////////

  Font getChartTitleFont();

  boolean isChartTitleVisible();

  boolean isChartTitleBoxVisible();

  Color getChartTitleBoxBackgroundColor();

  Color getChartTitleBoxBorderColor();

  int getChartTitlePadding();

  // Chart Legend ///////////////////////////////

  Font getLegendFont();

  boolean isLegendVisible();

  Color getLegendBackgroundColor();

  Color getLegendBorderColor();

  int getLegendPadding();

  int getLegendSeriesLineLength();

  LegendPosition getLegendPosition();

  // Chart Axes ///////////////////////////////

  boolean isXAxisTitleVisible();

  boolean isYAxisTitleVisible();

  Font getAxisTitleFont();

  boolean isXAxisTicksVisible();

  boolean isYAxisTicksVisible();

  Font getAxisTickLabelsFont();

  int getAxisTickMarkLength();

  int getAxisTickPadding();

  Color getAxisTickMarksColor();

  Stroke getAxisTickMarksStroke();

  Color getAxisTickLabelsColor();

  boolean isAxisTicksLineVisible();

  boolean isAxisTicksMarksVisible();

  int getAxisTitlePadding();

  int getXAxisTickMarkSpacingHint();

  int getYAxisTickMarkSpacingHint();

  // Chart Plot Area ///////////////////////////////

  boolean isPlotGridLinesVisible();

  boolean isPlotGridVerticalLinesVisible();

  boolean isPlotGridHorizontalLinesVisible();

  Color getPlotBackgroundColor();

  Color getPlotBorderColor();

  boolean isPlotBorderVisible();

  Color getPlotGridLinesColor();

  Stroke getPlotGridLinesStroke();

  boolean isPlotTicksMarksVisible();

  double getPlotContentSize();

  int getPlotMargin();

  // ToolTips ///////////////////////////////

  boolean isToolTipsEnabled();

  ToolTipType getToolTipType();

  Font getToolTipFont();

  Color getToolTipBackgroundColor();

  Color getToolTipBorderColor();

  Color getToolTipHighlightColor();

  // Bar Charts ///////////////////////////////

  double getAvailableSpaceFill();

  boolean isOverlapped();

  // Pie Charts ///////////////////////////////

  boolean isCircular();

  double getStartAngleInDegrees();

  Font getPieFont();

  double getAnnotationDistance();

  AnnotationType getAnnotationType();

  boolean isDrawAllAnnotations();

  double getDonutThickness();

  boolean isSumVisible();

  Font getSumFont();

  // Line, Scatter, Area Charts ///////////////////////////////

  int getMarkerSize();

  // Error Bars ///////////////////////////////

  Color getErrorBarsColor();

  boolean isErrorBarsColorSeriesColor();

  // Annotations ///////////////////////////////

  Font getAnnotationFont();
}
