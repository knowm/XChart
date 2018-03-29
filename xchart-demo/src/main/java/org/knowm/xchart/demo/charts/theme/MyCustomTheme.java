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

/** @author timmolter */
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

    return new BasicStroke(
        1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] {3.0f, 0.0f}, 0.0f);
  }

  // Chart Plot Area ///////////////////////////////

  @Override
  public boolean isPlotTicksMarksVisible() {

    return false;
  }

  @Override
  public Stroke getPlotGridLinesStroke() {

    return new BasicStroke(
        0.25f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] {3.0f, 3.0f}, 0.0f);
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
