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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.colors.GGPlot2SeriesColors;
import org.knowm.xchart.style.lines.GGPlot2SeriesLines;
import org.knowm.xchart.style.markers.GGPlot2SeriesMarkers;
import org.knowm.xchart.style.markers.Marker;

/**
 * @author timmolter
 */
public class GGPlot2Theme implements Theme {

  // Chart Style ///////////////////////////////

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

    return new Font(Font.SANS_SERIF, Font.PLAIN, 14);
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

    return ChartColor.getAWTColor(ChartColor.GREY);
  }

  @Override
  public Color getChartTitleBoxBorderColor() {

    return ChartColor.getAWTColor(ChartColor.GREY);
  }

  @Override
  public int getChartTitlePadding() {

    return 5;
  }

  // Chart Legend ///////////////////////////////

  @Override
  public Font getLegendFont() {

    return new Font(Font.SANS_SERIF, Font.PLAIN, 14);
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

    return ChartColor.getAWTColor(ChartColor.WHITE);
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

    return new Font(Font.SANS_SERIF, Font.PLAIN, 14);
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

    return new Font(Font.SANS_SERIF, Font.BOLD, 13);
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
  public int getPlotMargin() {

    return 0;
  }

  @Override
  public boolean isAxisTicksLineVisible() {

    return false;
  }

  @Override
  public boolean isAxisTicksMarksVisible() {

    return true;
  }

  @Override
  public Color getAxisTickMarksColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);

  }

  @Override
  public Stroke getAxisTickMarksStroke() {

    return new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 0.0f }, 0.0f);
  }

  @Override
  public Color getAxisTickLabelsColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
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

    return new BasicStroke(.75f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 0.0f }, 0.0f);
  }

  @Override
  public double getPlotContentSize() {

    return .92;
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

  @Override
  public Font getPieFont() {

    return new Font(Font.SANS_SERIF, Font.PLAIN, 15);
  }

  @Override
  public double getAnnotationDistance() {

    return .67;
  }

  @Override
  public AnnotationType getAnnotationType() {

    return AnnotationType.LabelAndPercentage;
  }

  @Override
  public boolean isDrawAllAnnotations() {

    return false;
  }

  @Override
  public double getDonutThickness() {

    return .25;
  }

  // Line, Scatter, Area Charts ///////////////////////////////

  @Override
  public int getMarkerSize() {

    return 8;
  }

  @Override
  public boolean showMarkers() {

    return true;
  }

  // Error Bars ///////////////////////////////

  @Override
  public Color getErrorBarsColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
  }

  @Override
  public boolean isErrorBarsColorSeriesColor() {

    return false;
  }

  // Annotations ///////////////////////////////

  @Override
  public Font getAnnotationFont() {

    return new Font(Font.SANS_SERIF, Font.PLAIN, 12);

  }

}
