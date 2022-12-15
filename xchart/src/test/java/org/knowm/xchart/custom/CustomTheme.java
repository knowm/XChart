package org.knowm.xchart.custom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.lines.XChartSeriesLines;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.XChartSeriesMarkers;
import org.knowm.xchart.style.theme.AbstractBaseTheme;

public class CustomTheme extends AbstractBaseTheme {
    @Override
    public Font getBaseFont() {
        return new Font(Font.SERIF, Font.PLAIN, 10);
    }

    @Override
    public Color getChartBackgroundColor() {
        return ChartColor.DARK_GREY.getColor();
    }

    @Override
    public Color getChartFontColor() {
        return ChartColor.DARK_GREY.getColor();
    }

    @Override
    public int getChartPadding() {
        return 12;
    }

    @Override
    public Color[] getSeriesColors() {
        return new CustomSeriesColors().getSeriesColors();
    }

    @Override
    public Marker[] getSeriesMarkers() {
        return new XChartSeriesMarkers().getSeriesMarkers();
    }

    @Override
    public BasicStroke[] getSeriesLines() {
        return new XChartSeriesLines().getSeriesLines();
    }

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
        return ChartColor.GREY.getColor();
    }

    @Override
    public Color getChartTitleBoxBorderColor() {
        return ChartColor.GREY.getColor();
    }

    @Override
    public BasicStroke getAxisTickMarksStroke() {
        return new BasicStroke(
                1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] {3.0f, 0.0f}, 0.0f);
    }

    @Override
    public boolean isPlotTicksMarksVisible() {
        return false;
    }

    @Override
    public BasicStroke getPlotGridLinesStroke() {
        return new BasicStroke(
                0.25f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f, new float[] {3.0f, 3.0f}, 0.0f);
    }

    @Override
    public int getMarkerSize() {
        return 16;
    }
}
