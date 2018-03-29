package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.MarkerSeries;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;

/** @author timmolter */
public class Legend_Marker<ST extends AxesChartStyler, S extends MarkerSeries>
    extends Legend_<ST, S> {

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
          getLegendEntryHeight(
              seriesTextBounds,
              ((series.getLegendRenderType() == LegendRenderType.Line
                      || series.getLegendRenderType() == LegendRenderType.Scatter)
                  ? axesChartStyler.getMarkerSize()
                  : BOX_SIZE));

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
                  starty + legendEntryHeight / 2.0,
                  startx + chart.getStyler().getLegendSeriesLineLength(),
                  starty + legendEntryHeight / 2.0);
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
                  starty + legendEntryHeight / 2.0,
                  axesChartStyler.getMarkerSize());
        }
      } else { // bar/pie type series

        // paint inner box
        Shape rectSmall = new Rectangle2D.Double(startx, starty, BOX_SIZE, BOX_SIZE);
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
                  existingLineStyle.getLineWidth() > BOX_OUTLINE_WIDTH * 0.5f
                      ? BOX_OUTLINE_WIDTH * 0.5f
                      : existingLineStyle.getLineWidth(),
                  existingLineStyle.getEndCap(),
                  existingLineStyle.getLineJoin(),
                  existingLineStyle.getMiterLimit(),
                  existingLineStyle.getDashArray(),
                  existingLineStyle.getDashPhase());

          g.setPaint(series.getLineColor());
          g.setStroke(newLineStyle);

          Path2D.Double outlinePath = new Path2D.Double();

          double lineOffset = existingLineStyle.getLineWidth() * 0.5;
          outlinePath.moveTo(startx + lineOffset, starty + lineOffset);
          outlinePath.lineTo(startx + lineOffset, starty + BOX_SIZE - lineOffset);
          outlinePath.lineTo(startx + BOX_SIZE - lineOffset, starty + BOX_SIZE - lineOffset);
          outlinePath.lineTo(startx + BOX_SIZE - lineOffset, starty + lineOffset);
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
        paintSeriesText(g, seriesTextBounds, axesChartStyler.getMarkerSize(), x, starty);
      } else { // bar/pie type series

        double x = startx + BOX_SIZE + chart.getStyler().getLegendPadding();
        paintSeriesText(g, seriesTextBounds, BOX_SIZE, x, starty);
      }

      if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
        starty += legendEntryHeight + chart.getStyler().getLegendPadding();
      } else {
        int markerWidth = BOX_SIZE;
        if (series.getLegendRenderType() == LegendRenderType.Line) {
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

    return (series.getLegendRenderType() == LegendRenderType.Box
            || series.getLegendRenderType() == LegendRenderType.BoxNoOutline)
        ? BOX_SIZE
        : axesChartStyler.getMarkerSize();
  }
}
