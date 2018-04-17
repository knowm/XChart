package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/** @author timmolter */
public class Legend_Pie<ST extends Styler, S extends Series> extends Legend_<ST, S> {

  /**
   * Constructor
   *
   * @param chart
   */
  public Legend_Pie(Chart<ST, S> chart) {

    super(chart);
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
      float legendEntryHeight = getLegendEntryHeight(seriesTextBounds, BOX_SIZE);

      // paint little box
      Shape rectSmall = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
      g.setColor(series.getFillColor());
      g.fill(rectSmall);

      // paint series text
      final double x = startx + BOX_SIZE + chart.getStyler().getLegendPadding();
      paintSeriesText(g, seriesTextBounds, BOX_SIZE, x, starty);

      if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
        starty += legendEntryHeight + chart.getStyler().getLegendPadding();
      } else {
        int markerWidth = BOX_SIZE;
        if (series.getLegendRenderType() == RenderableSeries.LegendRenderType.Line) {
          markerWidth = chart.getStyler().getLegendSeriesLineLength();
        }
        float legendEntryWidth = getLegendEntryWidth(seriesTextBounds, markerWidth);
        startx += legendEntryWidth + chart.getStyler().getLegendPadding();
      }
    }

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  @Override
  public double getSeriesLegendRenderGraphicHeight(S series) {

    return BOX_SIZE;
  }
}
