/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.demo.charts.theme;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

import org.knowm.xchart.style.AbstractBaseTheme;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.lines.XChartSeriesLines;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.XChartSeriesMarkers;

/**
 * @author timmolter
 */
public class MyCustomTheme extends AbstractBaseTheme {

  // Chart Style ///////////////////////////////

  @Override
  public Font getBaseFont() {

	return new Font(Font.SERIF, Font.PLAIN, 10);
  }

  @Override
  public Color getChartBackgroundColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
  }

  @Override
  public Color getChartFontColor() {

    return ChartColor.getAWTColor(ChartColor.DARK_GREY);
  }

  @Override
  public int getChartPadding() {

    return 12;
  }

  @Override
  public Color[] getSeriesColors() {

    return new MyCustomSeriesColors().getSeriesColors();
  }

  @Override
  public Marker[] getSeriesMarkers() {

    return new XChartSeriesMarkers().getSeriesMarkers();
  }

  @Override
  public BasicStroke[] getSeriesLines() {

    return new XChartSeriesLines().getSeriesLines();
  }

  // Chart Title ///////////////////////////////

  @Override
  public Font getChartTitleFont() {

	return getBaseFont().deriveFont(Font.BOLD).deriveFont(18f);
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

  // Chart Legend ///////////////////////////////

  // Chart Axes ///////////////////////////////

  @Override
  public Stroke getAxisTickMarksStroke() {

    return new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[]{3.0f, 0.0f}, 0.0f);
  }

  // Chart Plot Area ///////////////////////////////

  @Override
  public boolean isPlotTicksMarksVisible() {

    return false;
  }

  @Override
  public Stroke getPlotGridLinesStroke() {

    return new BasicStroke(0.25f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[]{3.0f, 3.0f}, 0.0f);
  }

  // Category Charts ///////////////////////////////

  // Pie Charts ///////////////////////////////

  // Line, Scatter, Area Charts ///////////////////////////////

  @Override
  public int getMarkerSize() {

    return 16;
  }

  // Error Bars ///////////////////////////////

  // Annotations ///////////////////////////////

}
