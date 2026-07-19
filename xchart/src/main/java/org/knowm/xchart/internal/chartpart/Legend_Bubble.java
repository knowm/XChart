package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

public class Legend_Bubble<ST extends AxesChartStyler, S extends AxesChartSeries>
    extends Legend_<ST, S> {

  private final ST axesChartStyler;

  /**
   * Constructor
   *
   * @param chart
   */
  public Legend_Bubble(Chart<ST, S> chart) {

    super(chart);
    axesChartStyler = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    // Draw legend content inside legend box
    double startx = xOffset + chart.getStyler().getLegendPadding();
    double starty = yOffset + chart.getStyler().getLegendPadding();

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        chart.getStyler().getAntiAlias()
            ? RenderingHints.VALUE_ANTIALIAS_ON
            : RenderingHints.VALUE_ANTIALIAS_OFF);

    Map<String, S> map = chart.getSeriesMap();

    // In a horizontal legend, entries flow across shared rows and wrap to a new row when a row
    // fills up (issue #577); in a vertical legend each entry gets its own row.
    boolean isHorizontal = chart.getStyler().getLegendLayout() == Styler.LegendLayout.Horizontal;
    HorizontalCursor cursor = isHorizontal ? new HorizontalCursor(startx, starty) : null;

    for (S series : map.values()) {

      if (!series.isShowInLegend()) {
        continue;
      }
      if (!series.isEnabled()) {
        continue;
      }

      Map<String, Rectangle2D> seriesTextBounds = getSeriesTextBounds(series);
      float legendEntryHeight = getLegendEntryHeight(seriesTextBounds, BOX_SIZE);

      double entryAdvanceWidth = 0;
      if (isHorizontal) {
        entryAdvanceWidth =
            getLegendEntryWidth(seriesTextBounds, getLegendEntryMarkerWidth(series))
                + chart.getStyler().getLegendPadding();
        cursor.maybeWrap(entryAdvanceWidth);
        startx = cursor.x;
        starty = cursor.y;
      }

      // paint little circle
      Shape rectSmall = new Ellipse2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
      g.setColor(series.getFillColor());
      g.fill(rectSmall);
      g.setStroke(series.getLineStyle());
      g.setColor(series.getLineColor());
      g.draw(rectSmall);

      // paint series text
      final double x = startx + BOX_SIZE + chart.getStyler().getLegendPadding();
      paintSeriesText(g, seriesTextBounds, BOX_SIZE, x, starty);

      if (isHorizontal) {
        cursor.advance(entryAdvanceWidth);
      } else {
        starty += legendEntryHeight + chart.getStyler().getLegendPadding();
      }
    }
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  @Override
  public double getSeriesLegendRenderGraphicHeight(S series) {

    return series.getLegendRenderType() == LegendRenderType.Box
        ? BOX_SIZE
        : axesChartStyler.getMarkerSize();
  }
}
