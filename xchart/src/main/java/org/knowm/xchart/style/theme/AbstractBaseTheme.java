package org.knowm.xchart.style.theme;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import org.knowm.xchart.style.PieStyler.LabelType;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.BaseSeriesColors;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.lines.BaseSeriesLines;
import org.knowm.xchart.style.markers.BaseSeriesMarkers;
import org.knowm.xchart.style.markers.Marker;

/**
 * @author timmolter
 * @author ekleinod
 */
public abstract class AbstractBaseTheme implements Theme {

  // Chart Style ///////////////////////////////

  @Override
  public Font getBaseFont() {

    return BASE_FONT;
  }

  @Override
  public Color getChartBackgroundColor() {

    return ChartColor.WHITE.getColor();
  }

  @Override
  public Color getChartFontColor() {

    return ChartColor.BLACK.getColor();
  }

  @Override
  public int getChartPadding() {

    return 10;
  }

  // SeriesMarkers, SeriesLines, SeriesColors ///////////////////////////////

  @Override
  public Color[] getSeriesColors() {

    return new BaseSeriesColors().getSeriesColors();
  }

  @Override
  public Marker[] getSeriesMarkers() {

    return new BaseSeriesMarkers().getSeriesMarkers();
  }

  @Override
  public BasicStroke[] getSeriesLines() {

    return new BaseSeriesLines().getSeriesLines();
  }

  // Chart Title ///////////////////////////////

  /** Base font, bold, size 14. */
  @Override
  public Font getChartTitleFont() {

    return getBaseFont().deriveFont(Font.BOLD).deriveFont(14f);
  }

  @Override
  public boolean isChartTitleVisible() {

    return true;
  }

  @Override
  public boolean isChartTitleBoxVisible() {

    return true;
  }

  @Override
  public Color getChartTitleBoxBackgroundColor() {

    return ChartColor.WHITE.getColor();
  }

  @Override
  public Color getChartTitleBoxBorderColor() {

    return ChartColor.WHITE.getColor();
  }

  @Override
  public int getChartTitlePadding() {

    return 5;
  }

  // Chart Legend ///////////////////////////////

  @Override
  public Font getLegendFont() {

    return getBaseFont().deriveFont(11f);
  }

  @Override
  public boolean isLegendVisible() {

    return true;
  }

  @Override
  public Color getLegendBackgroundColor() {

    return ChartColor.WHITE.getColor();
  }

  @Override
  public Color getLegendBorderColor() {

    return ChartColor.DARK_GREY.getColor();
  }

  @Override
  public int getLegendPadding() {

    return 10;
  }

  @Override
  public int getLegendSeriesLineLength() {

    return 24;
  }

  @Override
  public LegendPosition getLegendPosition() {

    return LegendPosition.OutsideE;
  }

  // Chart Axes ///////////////////////////////

  @Override
  public boolean isXAxisTitleVisible() {

    return true;
  }

  @Override
  public boolean isYAxisTitleVisible() {

    return true;
  }

  @Override
  public Font getAxisTitleFont() {

    return getBaseFont().deriveFont(Font.BOLD).deriveFont(12f);
  }

  @Override
  public boolean isXAxisTicksVisible() {

    return true;
  }

  @Override
  public boolean isYAxisTicksVisible() {

    return true;
  }

  @Override
  public Font getAxisTickLabelsFont() {

    return getAxisTitleFont();
  }

  @Override
  public int getAxisTickMarkLength() {

    return 3;
  }

  @Override
  public int getAxisTickPadding() {

    return 4;
  }

  @Override
  public Color getAxisTickMarksColor() {

    return ChartColor.DARK_GREY.getColor();
  }

  @Override
  public BasicStroke getAxisTickMarksStroke() {

    return BASE_STROKE;
  }

  @Override
  public Color getAxisTickLabelsColor() {

    return ChartColor.BLACK.getColor();
  }

  @Override
  public boolean isAxisTicksLineVisible() {

    return true;
  }

  @Override
  public boolean isAxisTicksMarksVisible() {

    return true;
  }

  @Override
  public int getAxisTitlePadding() {

    return 10;
  }

  @Override
  public int getXAxisTickMarkSpacingHint() {

    return 74;
  }

  @Override
  public int getYAxisTickMarkSpacingHint() {

    return 44;
  }

  // Chart Plot Area ///////////////////////////////

  @Override
  public boolean isPlotGridLinesVisible() {

    return true;
  }

  @Override
  public boolean isPlotGridVerticalLinesVisible() {

    return true;
  }

  @Override
  public boolean isPlotGridHorizontalLinesVisible() {

    return true;
  }

  @Override
  public Color getPlotBackgroundColor() {

    return ChartColor.WHITE.getColor();
  }

  @Override
  public Color getPlotBorderColor() {

    return ChartColor.DARK_GREY.getColor();
  }

  @Override
  public boolean isPlotBorderVisible() {

    return true;
  }

  @Override
  public boolean isPlotTicksMarksVisible() {

    return true;
  }

  @Override
  public Color getPlotGridLinesColor() {

    return ChartColor.GREY.getColor();
  }

  @Override
  public BasicStroke getPlotGridLinesStroke() {

    return new BasicStroke(
        1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] {3.0f, 5.0f}, 0.0f);
  }

  @Override
  public int getPlotMargin() {

    return 4;
  }

  // Cursor ///////////////////////////////

  @Override
  public boolean isCursorEnabled() {

    return false;
  }

  @Override
  public Color getCursorColor() {

    return Color.BLACK;
  }

  @Override
  public float getCursorSize() {

    return 1;
  }

  @Override
  public Font getCursorFont() {

    return new Font(Font.SANS_SERIF, Font.PLAIN, 16);
  }

  @Override
  public Color getCursorFontColor() {

    return Color.WHITE;
  }

  @Override
  public Color getCursorBackgroundColor() {

    return Color.GRAY;
  }

  // Category Charts ///////////////////////////////

  @Override
  public double getAvailableSpaceFill() {

    return 0.9;
  }

  @Override
  public boolean isOverlapped() {

    return false;
  }

  // Pie Charts ///////////////////////////////

  @Override
  public boolean isCircular() {

    return true;
  }

  @Override
  public double getStartAngleInDegrees() {

    return 0;
  }

  /** Base font, size 15. */
  @Override
  public Font getPieFont() {

    return getBaseFont().deriveFont(15f);
  }

  @Override
  public double getLabelsDistance() {

    return .67;
  }

  @Override
  public LabelType getLabelType() {

    return LabelType.Percentage;
  }

  @Override
  public boolean setForceAllLabelsVisible() {

    return false;
  }

  @Override
  public double getDonutThickness() {

    return .33;
  }

  @Override
  public boolean isSumVisible() {

    return false;
  }

  @Override
  public Font getSumFont() {

    return getBaseFont().deriveFont(15f);
  }

  // Line, Scatter, Area Charts ///////////////////////////////

  @Override
  public int getMarkerSize() {

    return 8;
  }

  // Error Bars ///////////////////////////////

  @Override
  public Color getErrorBarsColor() {

    return ChartColor.BLACK.getColor();
  }

  @Override
  public boolean isErrorBarsColorSeriesColor() {

    return false;
  }

  @Override
  public Color getAnnotationAutodetectDarkFontColor() {
    return Color.BLACK;
  }

  @Override
  public Color getAnnotationAutodetectLightFontColor() {
    return Color.WHITE;
  }

  @Override
  public boolean isAnnotationAutoColorDetectionEnabled() {
    return true;
  }
}
