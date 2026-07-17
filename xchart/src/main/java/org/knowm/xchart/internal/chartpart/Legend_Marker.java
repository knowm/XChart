package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.MarkerSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;

public class Legend_Marker<ST extends Styler, S extends MarkerSeries> extends Legend_<ST, S> {

  private final ST axesChartStyler;

  /**
   * Constructor
   *
   * @param chart
   */
  public Legend_Marker(Chart<ST, S> chart) {

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

    // In a horizontal legend entries flow across shared rows (wrapping to a new row when a row
    // fills up - issue #577), and all entries in a row are vertically centered against a common row
    // height. Otherwise mixed render styles (e.g. a Bar's 20px box vs. a Line's smaller marker)
    // center against their own graphic height and sit at different baselines (issue #892). In a
    // vertical legend each entry gets its own row, so the per-entry height is the correct reference.
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
      float legendEntryHeight =
          getLegendEntryHeight(
              seriesTextBounds,
              ((series.getLegendRenderType() == LegendRenderType.Line
                      || series.getLegendRenderType() == LegendRenderType.Scatter)
                  ? axesChartStyler.getMarkerSize()
                  : BOX_SIZE));

      // In a horizontal layout, position this entry via the wrapping cursor and center its natural
      // block within the shared row by shifting its vertical origin. All per-element math below then
      // stays exactly as in the vertical layout, just drawn from startx/entryStarty. In a vertical
      // layout each entry has its own row, so the shift is zero.
      double entryAdvanceWidth = 0;
      if (isHorizontal) {
        entryAdvanceWidth =
            getLegendEntryWidth(seriesTextBounds, getLegendEntryMarkerWidth(series))
                + chart.getStyler().getLegendPadding();
        cursor.maybeWrap(entryAdvanceWidth);
        startx = cursor.x;
      }

      double entryStarty =
          isHorizontal ? cursor.y + (cursor.rowHeight - legendEntryHeight) / 2.0 : starty;

      // paint line and marker
      if (series.getLegendRenderType() == LegendRenderType.Line
          || series.getLegendRenderType() == LegendRenderType.Scatter) {

        // paint line
        if (series.getLegendRenderType() == LegendRenderType.Line
            && series.getLineStyle() != SeriesLines.NONE) {
          g.setColor(series.getLineColor());
          g.setStroke(series.getLineStyle());
          Shape line =
              new Line2D.Double(
                  startx,
                  entryStarty + legendEntryHeight / 2.0,
                  startx + chart.getStyler().getLegendSeriesLineLength(),
                  entryStarty + legendEntryHeight / 2.0);
          g.draw(line);
        }

        // paint marker
        if (series.getMarker() != null) {
          g.setColor(series.getMarkerColor());
          series
              .getMarker()
              .paint(
                  g,
                  startx + chart.getStyler().getLegendSeriesLineLength() / 2.0,
                  entryStarty + legendEntryHeight / 2.0,
                  axesChartStyler.getMarkerSize());
        }
      } else { // bar/pie type series

        double boxStarty = entryStarty;

        // paint inner box
        Shape rectSmall = new Rectangle2D.Double(startx, boxStarty, BOX_SIZE, BOX_SIZE);
        g.setColor(series.getFillColor());
        g.fill(rectSmall);

        // Draw outline
        if (series.getLegendRenderType() != LegendRenderType.BoxNoOutline) {

          // paint outer box
          g.setColor(series.getLineColor());

          // Only respect the existing stroke width up to BOX_OUTLINE_WIDTH, as the legend box is
          // very small.
          // Note the simplified conversion of line width from user space to device space.
          BasicStroke existingLineStyle = series.getLineStyle();
          BasicStroke newLineStyle =
              new BasicStroke(
                  Math.min(existingLineStyle.getLineWidth(), BOX_OUTLINE_WIDTH * 0.5f),
                  existingLineStyle.getEndCap(),
                  existingLineStyle.getLineJoin(),
                  existingLineStyle.getMiterLimit(),
                  existingLineStyle.getDashArray(),
                  existingLineStyle.getDashPhase());

          g.setPaint(series.getLineColor());
          g.setStroke(newLineStyle);

          Path2D.Double outlinePath = new Path2D.Double();

          double lineOffset = existingLineStyle.getLineWidth() * 0.5;
          outlinePath.moveTo(startx + lineOffset, boxStarty + lineOffset);
          outlinePath.lineTo(startx + lineOffset, boxStarty + BOX_SIZE - lineOffset);
          outlinePath.lineTo(startx + BOX_SIZE - lineOffset, boxStarty + BOX_SIZE - lineOffset);
          outlinePath.lineTo(startx + BOX_SIZE - lineOffset, boxStarty + lineOffset);
          outlinePath.closePath();

          g.draw(outlinePath);
        }
      }

      // paint series text
      if (series.getLegendRenderType() == LegendRenderType.Line
          || series.getLegendRenderType() == LegendRenderType.Scatter) {

        double x =
            startx
                + chart.getStyler().getLegendSeriesLineLength()
                + chart.getStyler().getLegendPadding();
        paintSeriesText(g, seriesTextBounds, axesChartStyler.getMarkerSize(), x, entryStarty);
      } else { // bar/pie type series

        double x = startx + BOX_SIZE + chart.getStyler().getLegendPadding();
        paintSeriesText(g, seriesTextBounds, BOX_SIZE, x, entryStarty);
      }

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

    return (series.getLegendRenderType() == LegendRenderType.Box
            || series.getLegendRenderType() == LegendRenderType.BoxNoOutline)
        ? BOX_SIZE
        : axesChartStyler.getMarkerSize();
  }
}
