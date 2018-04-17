package org.knowm.xchart.style;

import java.awt.*;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.ToolTipType;
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

  private static final Font BASE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 10);

  // Chart Style ///////////////////////////////

  @Override
  public Font getBaseFont() {

    return BASE_FONT;
  }

  @Override
  public Color getChartBackgroundColor() {

    return ChartColor.getAWTColor(ChartColor.WHITE);
  }

  @Override
  public Color getChartFontColor() {

    return ChartColor.getAWTColor(ChartColor.BLACK);
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

    return ChartColor.getAWTColor(ChartColor.WHITE);
  }

  @Override
  public Color getChartTitleBoxBorderColor() {

    return ChartColor.getAWTColor(ChartColor.WHITE);
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

    return ChartColor.getAWTColor(ChartColor.WHITE);
  }

  @Override
  public Color getLegendBorderColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
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

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
  }

  @Override
  public Stroke getAxisTickMarksStroke() {

    return new BasicStroke(1.0f);
  }

  @Override
  public Color getAxisTickLabelsColor() {

    return ChartColor.getAWTColor(ChartColor.BLACK);
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

    return ChartColor.getAWTColor(ChartColor.WHITE);
  }

  @Override
  public Color getPlotBorderColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
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

    return ChartColor.getAWTColor(ChartColor.GREY);
  }

  @Override
  public Stroke getPlotGridLinesStroke() {

    return new BasicStroke(
        1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] {3.0f, 5.0f}, 0.0f);
  }

  @Override
  public double getPlotContentSize() {

    return .92;
  }

  @Override
  public int getPlotMargin() {

    return 4;
  }
  // Tool Tips ///////////////////////////////

  @Override
  public boolean isToolTipsEnabled() {

    return false;
  }

  @Override
  public ToolTipType getToolTipType() {

    return ToolTipType.xAndYLabels;
  }

  @Override
  public Font getToolTipFont() {

    return BASE_FONT;
  }

  @Override
  public Color getToolTipBackgroundColor() {

    return ChartColor.getAWTColor(ChartColor.WHITE);
  }

  @Override
  public Color getToolTipBorderColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
  }

  @Override
  public Color getToolTipHighlightColor() {

    return ChartColor.getAWTColor(ChartColor.LIGHT_GREY);
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
  public double getAnnotationDistance() {

    return .67;
  }

  @Override
  public AnnotationType getAnnotationType() {

    return AnnotationType.Percentage;
  }

  @Override
  public boolean isDrawAllAnnotations() {

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

    return getAnnotationFont();
  }

  // Line, Scatter, Area Charts ///////////////////////////////

  @Override
  public int getMarkerSize() {

    return 8;
  }

  // Error Bars ///////////////////////////////

  @Override
  public Color getErrorBarsColor() {

    return ChartColor.getAWTColor(ChartColor.BLACK);
  }

  @Override
  public boolean isErrorBarsColorSeriesColor() {

    return false;
  }

  // Annotations ///////////////////////////////

  /** Pie font, size 12. */
  @Override
  public Font getAnnotationFont() {

    return getPieFont().deriveFont(12f);
  }
}
