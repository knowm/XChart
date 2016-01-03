/**
 * Copyright 2015 Knowm Inc. (http://knowm.org) and contributors.
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
package org.knowm.xchart.internal.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.knowm.xchart.ChartColor;
import org.knowm.xchart.SeriesColor;
import org.knowm.xchart.SeriesLineStyle;
import org.knowm.xchart.SeriesMarker;
import org.knowm.xchart.StyleManager.LegendPosition;

/**
 * @author timmolter
 */
public class XChartTheme implements Theme {

  /** BLUE */
  public static SeriesColor BLUE = new SeriesColor(0, 55, 255, 180);

  /** ORANGE */
  public static SeriesColor ORANGE = new SeriesColor(255, 172, 0, 180);

  /** PURPLE */
  public static SeriesColor PURPLE = new SeriesColor(128, 0, 255, 180);

  /** GREEN */
  public static SeriesColor GREEN = new SeriesColor(0, 205, 0, 180);

  /** RED */
  public static SeriesColor RED = new SeriesColor(205, 0, 0, 180);

  /** YELLOW */
  public static SeriesColor YELLOW = new SeriesColor(255, 215, 0, 180);

  /** MAGENTA */
  public static SeriesColor MAGENTA = new SeriesColor(255, 0, 255, 180);

  /** PINK */
  public static SeriesColor PINK = new SeriesColor(255, 166, 201, 180);

  /** LIGHT_GREY */
  public static SeriesColor LIGHT_GREY = new SeriesColor(207, 207, 207, 180);

  /** CYAN */
  public static SeriesColor CYAN = new SeriesColor(0, 255, 255, 180);

  /** BROWN */
  public static SeriesColor BROWN = new SeriesColor(102, 56, 10, 180);

  /** BLACK */
  public static SeriesColor BLACK = new SeriesColor(0, 0, 0, 180);

  // Chart Style ///////////////////////////////

  @Override
  public Color getChartBackgroundColor() {

    return ChartColor.getAWTColor(ChartColor.GREY);
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
  public SeriesColorMarkerLineStyleCycler getSeriesColorMarkerLineStyleCycler() {

    return new XChartSeriesColorMarkerLineStyleCycler();
  }

  // Chart Title ///////////////////////////////

  @Override
  public Font getChartTitleFont() {

    return new Font(Font.SANS_SERIF, Font.BOLD, 14);
  }

  @Override
  public boolean isChartTitleVisible() {

    return false;
  }

  @Override
  public boolean isChartTitleBoxVisible() {

    return false;
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

    return new Font(Font.SANS_SERIF, Font.PLAIN, 11);
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

    return new Font(Font.SANS_SERIF, Font.BOLD, 12);
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

    return new Font(Font.SANS_SERIF, Font.BOLD, 12);
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

    return new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 0.0f }, 0.0f);
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
  public int getPlotPadding() {

    return 4;
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

    return false;
  }

  @Override
  public Color getPlotGridLinesColor() {

    return ChartColor.getAWTColor(ChartColor.GREY);
  }

  @Override
  public Stroke getPlotGridLinesStroke() {

    return new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] { 3.0f, 3.0f }, 0.0f);
  }

  // Bar Charts ///////////////////////////////

  @Override
  public double getBarWidthPercentage() {

    return 0.9;
  }

  @Override
  public boolean isBarsOverlapped() {

    return false;
  }

  @Override
  public boolean isBarFilled() {

    return true;
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

  public class XChartSeriesColorMarkerLineStyleCycler extends SeriesColorMarkerLineStyleCycler {

    @Override
    public List<SeriesColor> getSeriesColorList() {

      // 1. Color
      List<SeriesColor> seriesColorMap = new ArrayList<SeriesColor>();
      seriesColorMap.add(BLUE);
      seriesColorMap.add(ORANGE);
      seriesColorMap.add(PURPLE);
      seriesColorMap.add(GREEN);
      seriesColorMap.add(RED);
      seriesColorMap.add(YELLOW);
      seriesColorMap.add(MAGENTA);
      seriesColorMap.add(PINK);
      seriesColorMap.add(LIGHT_GREY);
      seriesColorMap.add(CYAN);
      seriesColorMap.add(BROWN);
      seriesColorMap.add(BLACK);

      return seriesColorMap;
    }

    @Override
    public List<SeriesMarker> getSeriesMarkerList() {

      // 2. Marker
      List<SeriesMarker> seriesMarkerList = new ArrayList<SeriesMarker>();
      for (SeriesMarker seriesMarker : EnumSet.allOf(SeriesMarker.class)) {
        if (seriesMarker.getIndex() >= 0) { // skip SeriesMarker.NONE
          seriesMarkerList.add(seriesMarker);
        }
      }
      return seriesMarkerList;
    }

    @Override
    public List<SeriesLineStyle> getLineStyleList() {

      // 3. Stroke
      List<SeriesLineStyle> seriesLineStyleList = new ArrayList<SeriesLineStyle>();
      for (SeriesLineStyle seriesLineStyle : EnumSet.allOf(SeriesLineStyle.class)) {
        if (seriesLineStyle.getIndex() >= 0) { // skip SeriesLineStyle.NONE
          seriesLineStyleList.add(seriesLineStyle);
        }
      }
      return seriesLineStyleList;
    }

  }

}
