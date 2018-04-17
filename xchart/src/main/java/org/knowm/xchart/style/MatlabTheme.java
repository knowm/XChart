package org.knowm.xchart.style;

import java.awt.*;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.colors.MatlabSeriesColors;
import org.knowm.xchart.style.lines.MatlabSeriesLines;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.MatlabSeriesMarkers;

/** @author timmolter */
public class MatlabTheme extends AbstractBaseTheme {

  // Chart Style ///////////////////////////////

  // SeriesMarkers, SeriesLines, SeriesColors ///////////////////////////////

  @Override
  public Marker[] getSeriesMarkers() {

    return new MatlabSeriesMarkers().getSeriesMarkers();
  }

  @Override
  public BasicStroke[] getSeriesLines() {

    return new MatlabSeriesLines().getSeriesLines();
  }

  @Override
  public Color[] getSeriesColors() {

    return new MatlabSeriesColors().getSeriesColors();
  }

  // Chart Title ///////////////////////////////

  @Override
  public boolean isChartTitleBoxVisible() {

    return false;
  }

  // Chart Legend ///////////////////////////////

  @Override
  public Color getLegendBorderColor() {

    return ChartColor.getAWTColor(ChartColor.BLACK);
  }

  // Chart Axes ///////////////////////////////

  @Override
  public Font getAxisTitleFont() {

    return getBaseFont().deriveFont(12f);
  }

  @Override
  public int getAxisTickMarkLength() {

    return 5;
  }

  @Override
  public Color getAxisTickMarksColor() {

    return ChartColor.getAWTColor(ChartColor.BLACK);
  }

  @Override
  public Stroke getAxisTickMarksStroke() {

    return new BasicStroke(.5f);
  }

  @Override
  public boolean isAxisTicksLineVisible() {

    return false;
  }

  @Override
  public boolean isAxisTicksMarksVisible() {

    return false;
  }

  // Chart Plot Area ///////////////////////////////

  @Override
  public Color getPlotBorderColor() {

    return ChartColor.getAWTColor(ChartColor.BLACK);
  }

  @Override
  public Color getPlotGridLinesColor() {

    return ChartColor.getAWTColor(ChartColor.BLACK);
  }

  @Override
  public Stroke getPlotGridLinesStroke() {

    return new BasicStroke(
        .5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[] {1f, 3.0f}, 0.0f);
  }

  @Override
  public int getPlotMargin() {

    return 3;
  }

  // Tool Tips ///////////////////////////////

  @Override
  public Color getToolTipBackgroundColor() {

    return new Color(255, 255, 220);
  }

  @Override
  public Color getToolTipBorderColor() {

    return ChartColor.getAWTColor(ChartColor.BLACK);
  }

  @Override
  public Color getToolTipHighlightColor() {

    return ChartColor.getAWTColor(ChartColor.BLACK);
  }

  // Category Charts ///////////////////////////////

  // Pie Charts ///////////////////////////////

  @Override
  public AnnotationType getAnnotationType() {

    return AnnotationType.Label;
  }

  // Line, Scatter, Area Charts ///////////////////////////////

  // Error Bars ///////////////////////////////

  // Annotations ///////////////////////////////

}
