package org.knowm.xchart.style;

import java.awt.*;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.colors.GGPlot2SeriesColors;
import org.knowm.xchart.style.lines.GGPlot2SeriesLines;
import org.knowm.xchart.style.markers.GGPlot2SeriesMarkers;
import org.knowm.xchart.style.markers.Marker;

/** @author timmolter */
public class GGPlot2Theme extends AbstractBaseTheme {

  // Chart Style ///////////////////////////////

  // SeriesMarkers, SeriesLines, SeriesColors ///////////////////////////////

  @Override
  public Marker[] getSeriesMarkers() {

    return new GGPlot2SeriesMarkers().getSeriesMarkers();
  }

  @Override
  public BasicStroke[] getSeriesLines() {

    return new GGPlot2SeriesLines().getSeriesLines();
  }

  @Override
  public Color[] getSeriesColors() {

    return new GGPlot2SeriesColors().getSeriesColors();
  }

  // Chart Title ///////////////////////////////

  @Override
  public Font getChartTitleFont() {

    return getBaseFont().deriveFont(14f);
  }

  @Override
  public Color getChartTitleBoxBackgroundColor() {

    return ChartColor.getAWTColor(ChartColor.GREY);
  }

  @Override
  public Color getChartTitleBoxBorderColor() {

    return ChartColor.getAWTColor(ChartColor.GREY);
  }

  // Chart Legend ///////////////////////////////

  @Override
  public Font getLegendFont() {

    return getBaseFont().deriveFont(14f);
  }

  @Override
  public Color getLegendBorderColor() {

    return ChartColor.getAWTColor(ChartColor.WHITE);
  }

  // Chart Axes ///////////////////////////////

  @Override
  public Font getAxisTitleFont() {

    return getBaseFont().deriveFont(14f);
  }

  @Override
  public Font getAxisTickLabelsFont() {

    return getBaseFont().deriveFont(Font.BOLD).deriveFont(13f);
  }

  @Override
  public int getAxisTickMarkLength() {

    return 8;
  }

  @Override
  public int getAxisTickPadding() {

    return 5;
  }

  @Override
  public Stroke getAxisTickMarksStroke() {

    return new BasicStroke(1.5f);
  }

  @Override
  public Color getAxisTickLabelsColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
  }

  @Override
  public boolean isAxisTicksLineVisible() {

    return false;
  }

  // Chart Plot Area ///////////////////////////////

  @Override
  public Color getPlotBackgroundColor() {

    return ChartColor.getAWTColor(ChartColor.LIGHT_GREY);
  }

  @Override
  public Color getPlotBorderColor() {

    return ChartColor.getAWTColor(ChartColor.WHITE);
  }

  @Override
  public boolean isPlotBorderVisible() {

    return false;
  }

  @Override
  public boolean isPlotTicksMarksVisible() {

    return false;
  }

  @Override
  public Color getPlotGridLinesColor() {

    return ChartColor.getAWTColor(ChartColor.WHITE);
  }

  @Override
  public Stroke getPlotGridLinesStroke() {

    return new BasicStroke(1.0f);
  }

  @Override
  public int getPlotMargin() {

    return 0;
  }

  // Tool Tips ///////////////////////////////

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

  // Pie Charts ///////////////////////////////

  @Override
  public AnnotationType getAnnotationType() {

    return AnnotationType.LabelAndPercentage;
  }

  @Override
  public double getDonutThickness() {

    return .25;
  }

  // Line, Scatter, Area Charts ///////////////////////////////

  // Error Bars ///////////////////////////////

  @Override
  public Color getErrorBarsColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
  }

  // Annotations ///////////////////////////////

}
