package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.knowm.xchart.HorizontalBarSeries;
import org.knowm.xchart.style.HorizontalBarStyler;
import org.knowm.xchart.style.Styler;

public class Legend_HorizontalBar<ST extends Styler, S extends HorizontalBarSeries>
    extends Legend_<ST, S> {

  /**
   * Constructor
   *
   * @param chart
   */
  public Legend_HorizontalBar(Chart<ST, S> chart) {

    super(chart);
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
    boolean barFillGradient =
        chart.getStyler() instanceof HorizontalBarStyler
            && ((HorizontalBarStyler) chart.getStyler()).isBarFillGradientEnabled();
    // Match the bars: lighter at the right, or at the left when every bar grows leftward.
    boolean allNegative = barFillGradient && allNegativeXAxis();

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

      // paint inner box, with the bar gradient when enabled
      Shape rectSmall = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
      double left = startx;
      double right = startx + BOX_SIZE;
      BarGradient.fill(
          g,
          rectSmall,
          series.getFillColor(),
          barFillGradient,
          allNegative ? right : left,
          starty,
          allNegative ? left : right,
          starty);

      // paint series text
      double x = startx + BOX_SIZE + chart.getStyler().getLegendPadding();
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

    return BOX_SIZE;
  }

  private boolean allNegativeXAxis() {

    if (!(chart instanceof AxesChart)) {
      return false;
    }
    Axis_X<?, ?> xAxis = ((AxesChart<?, ?>) chart).getXAxis();
    return xAxis.getMin() < 0.0 && xAxis.getMax() < 0.0;
  }
}
