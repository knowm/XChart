package org.knowm.xchart.style.theme;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import org.knowm.xchart.style.PieStyler.LabelType;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.colors.SeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

/** @author timmolter */
public interface Theme extends SeriesMarkers, SeriesLines, SeriesColors {

  Font BASE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
  BasicStroke BASE_STROKE = new BasicStroke(1.0f);

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

  // Chart Plot Area ///////////////////////////////

  boolean isPlotGridLinesVisible();

  boolean isPlotGridVerticalLinesVisible();

  boolean isPlotGridHorizontalLinesVisible();

  Color getPlotBackgroundColor();

  Color getPlotBorderColor();

  boolean isPlotBorderVisible();

  Color getPlotGridLinesColor();

  BasicStroke getPlotGridLinesStroke();

  boolean isPlotTicksMarksVisible();

  default double getPlotContentSize() {
    return .92;
  }

  int getPlotMargin();

  // Chart Annotations ///////////////////////////////

  default Font getAnnotationTextPanelFont() {
    return new Font(Font.MONOSPACED, Font.PLAIN, 10);
  }

  default Color getAnnotationTextPanelFontColor() {
    return ChartColor.getAWTColor(ChartColor.BLACK);
  }

  default Color getAnnotationTextPanelBackgroundColor() {
    return ChartColor.getAWTColor(ChartColor.WHITE);
  }

  default Color getAnnotationTextPanelBorderColor() {
    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
  }

  default int getAnnotationTextPanelPadding() {
    return 10;
  }

  default Font getAnnotationTextFont() {
    return new Font(Font.MONOSPACED, Font.PLAIN, 10);
  }

  default Color getAnnotationTextFontColor() {
    return ChartColor.getAWTColor(ChartColor.BLACK);
  }

  default BasicStroke getAnnotationLineStroke() {
    return BASE_STROKE;
  }

  default Color getAnnotationLineColor() {
    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
  }

  // ToolTips ///////////////////////////////

  default boolean isToolTipsEnabled() {

    return false;
  }

  default Styler.ToolTipType getToolTipType() {

    return Styler.ToolTipType.xAndYLabels;
  }

  default Font getToolTipFont() {

    return BASE_FONT;
  }

  default Color getToolTipBackgroundColor() {

    return ChartColor.getAWTColor(ChartColor.WHITE);
  }

  default Color getToolTipBorderColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
  }

  default Color getToolTipHighlightColor() {

    return ChartColor.getAWTColor(ChartColor.LIGHT_GREY);
  }

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

  BasicStroke getAxisTickMarksStroke();

  Color getAxisTickLabelsColor();

  boolean isAxisTicksLineVisible();

  boolean isAxisTicksMarksVisible();

  int getAxisTitlePadding();

  int getXAxisTickMarkSpacingHint();

  int getYAxisTickMarkSpacingHint();

  // Cursor ///////////////////////////////

  boolean isCursorEnabled();

  Color getCursorColor();

  float getCursorSize();

  Font getCursorFont();

  Color getCursorFontColor();

  Color getCursorBackgroundColor();

  // Zoom /////////////////////////////////////

  default boolean isZoomEnabled() {
    return false;
  }

  // Bar Charts ///////////////////////////////

  double getAvailableSpaceFill();

  boolean isOverlapped();

  // Pie Charts ///////////////////////////////

  boolean isCircular();

  double getStartAngleInDegrees();

  Font getPieFont();

  double getLabelsDistance();

  LabelType getLabelType();

  boolean setForceAllLabelsVisible();

  double getDonutThickness();

  boolean isSumVisible();

  Font getSumFont();

  // Line, Scatter, Area Charts ///////////////////////////////

  int getMarkerSize();

  // Error Bars ///////////////////////////////

  Color getErrorBarsColor();

  boolean isErrorBarsColorSeriesColor();

  Color getAnnotationAutodetectDarkFontColor();

  Color getAnnotationAutodetectLightFontColor();

  boolean isAnnotationAutoColorDetectionEnabled();
}
