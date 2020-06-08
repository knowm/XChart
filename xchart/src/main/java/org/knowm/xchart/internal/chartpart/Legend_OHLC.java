package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.knowm.xchart.OHLCSeries;
import org.knowm.xchart.OHLCSeries.OHLCSeriesRenderStyle;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.style.OHLCStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;

/** @author arthurmcgibbon */
public class Legend_OHLC<ST extends OHLCStyler, S extends OHLCSeries> extends Legend_<ST, S> {

  private final ST axesChartStyler;

  /**
   * Constructor
   *
   * @param chart
   */
  public Legend_OHLC(Chart<ST, S> chart) {

    super(chart);
    axesChartStyler = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    // Draw legend content inside legend box
    double startx = xOffset + chart.getStyler().getLegendPadding();
    double starty = yOffset + chart.getStyler().getLegendPadding();

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {

      if (!series.isShowInLegend()) {
        continue;
      }
      if (!series.isEnabled()) {
        continue;
      }

      Map<String, Rectangle2D> seriesTextBounds = getSeriesTextBounds(series);
      float legendEntryHeight =
          getLegendEntryHeight(seriesTextBounds, axesChartStyler.getMarkerSize());

      if (series.getOhlcSeriesRenderStyle() != OHLCSeriesRenderStyle.Line) {

        Shape rectSmall =
            new Rectangle2D.Double(
                startx,
                starty + legendEntryHeight / 2.0 - BOX_SIZE / 2,
                chart.getStyler().getLegendSeriesLineLength(),
                BOX_SIZE);
        if (series.getLineColor() == null) {
          g.setColor(series.getUpColor());
        } else {
          g.setColor(series.getLineColor());
        }
        g.fill(rectSmall);
      }

      // paint line
      if (series.getOhlcSeriesRenderStyle() == OHLCSeriesRenderStyle.Line
          && series.getLegendRenderType() == LegendRenderType.Line
          && series.getLineStyle() != SeriesLines.NONE) {
        g.setColor(series.getLineColor());
        g.setStroke(series.getLineStyle());
        Shape line =
            new Line2D.Double(
                startx,
                starty + legendEntryHeight / 2.0,
                startx + chart.getStyler().getLegendSeriesLineLength(),
                starty + legendEntryHeight / 2.0);
        g.draw(line);
      }

      // paint marker
      if (series.getOhlcSeriesRenderStyle() == OHLCSeriesRenderStyle.Line
          && series.getMarker() != null) {
        g.setColor(series.getMarkerColor());
        series
            .getMarker()
            .paint(
                g,
                startx + chart.getStyler().getLegendSeriesLineLength() / 2.0,
                starty + legendEntryHeight / 2.0,
                axesChartStyler.getMarkerSize());
      }

      // paint series text
      double x =
          startx
              + chart.getStyler().getLegendSeriesLineLength()
              + chart.getStyler().getLegendPadding();
      paintSeriesText(g, seriesTextBounds, axesChartStyler.getMarkerSize(), x, starty);

      if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
        starty += legendEntryHeight + chart.getStyler().getLegendPadding();
      } else {
        int markerWidth = chart.getStyler().getLegendSeriesLineLength();
        float legendEntryWidth = getLegendEntryWidth(seriesTextBounds, markerWidth);
        startx += legendEntryWidth + chart.getStyler().getLegendPadding();
      }
    }
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  @Override
  public double getSeriesLegendRenderGraphicHeight(S series) {

    return (series.getLegendRenderType() == LegendRenderType.Box
            || series.getLegendRenderType() == LegendRenderType.BoxNoOutline)
        ? BOX_SIZE
        : axesChartStyler.getMarkerSize();
  }
}
