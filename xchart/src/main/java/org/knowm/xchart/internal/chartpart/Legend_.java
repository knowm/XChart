package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/** @author timmolter */
public abstract class Legend_<ST extends Styler, S extends Series> implements ChartPart {

  static final int BOX_SIZE = 20;
  static final int BOX_OUTLINE_WIDTH = 5;
  private static final int LEGEND_MARGIN = 6;
  private static final int MULTI_LINE_SPACE = 3;
  final Chart<ST, S> chart;
  double xOffset = 0;
  double yOffset = 0;
  private Rectangle2D bounds;

  /**
   * Constructor
   *
   * @param chart
   */
  Legend_(Chart<ST, S> chart) {

    this.chart = chart;
  }

  protected abstract double getSeriesLegendRenderGraphicHeight(S series);

  protected abstract void doPaint(Graphics2D g);

  @Override
  public void paint(Graphics2D g) {

    if (!chart.getStyler().isLegendVisible()) {
      return;
    }

    if (chart.getSeriesMap().isEmpty()) {
      return;
    }

    // if the area to draw a chart on is so small, don't even bother
    if (chart.getPlot().getBounds().getWidth() < 30) {
      return;
    }

    // We call get bounds hint because sometimes the Axis object needs it to know it's bounds (if
    // Legend is outside Plot). If it's null, we just need to calulate it before painting, because
    // the paint
    // methods needs the bounds.
    // if (bounds == null) { // No other part asked for the bounds yet. Probably because it's an
    // "inside" legend location
    if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
      bounds =
          getBoundsHintVertical(); // Actually, the only information contained in this bounds is the
      // width and height.
    } else {
      bounds =
          getBoundsHintHorizontal(); // Actually, the only information contained in this bounds is
      // the width and height.
    }

    // legend draw position
    double height = bounds.getHeight();

    switch (chart.getStyler().getLegendPosition()) {
      case OutsideE:
        xOffset = chart.getWidth() - bounds.getWidth() - LEGEND_MARGIN;
        yOffset =
            chart.getPlot().getBounds().getY()
                + (chart.getPlot().getBounds().getHeight() - bounds.getHeight()) / 2.0;
        break;
      case InsideNW:
        xOffset = chart.getPlot().getBounds().getX() + LEGEND_MARGIN;
        yOffset = chart.getPlot().getBounds().getY() + LEGEND_MARGIN;
        break;
      case InsideNE:
        xOffset =
            chart.getPlot().getBounds().getX()
                + chart.getPlot().getBounds().getWidth()
                - bounds.getWidth()
                - LEGEND_MARGIN;
        yOffset = chart.getPlot().getBounds().getY() + LEGEND_MARGIN;
        break;
      case InsideSE:
        xOffset =
            chart.getPlot().getBounds().getX()
                + chart.getPlot().getBounds().getWidth()
                - bounds.getWidth()
                - LEGEND_MARGIN;
        yOffset =
            chart.getPlot().getBounds().getY()
                + chart.getPlot().getBounds().getHeight()
                - bounds.getHeight()
                - LEGEND_MARGIN;
        break;
      case InsideSW:
        xOffset = chart.getPlot().getBounds().getX() + LEGEND_MARGIN;
        yOffset =
            chart.getPlot().getBounds().getY()
                + chart.getPlot().getBounds().getHeight()
                - bounds.getHeight()
                - LEGEND_MARGIN;
        break;
      case InsideN:
        xOffset =
            chart.getPlot().getBounds().getX()
                + (chart.getPlot().getBounds().getWidth() - bounds.getWidth()) / 2
                + LEGEND_MARGIN;
        yOffset = chart.getPlot().getBounds().getY() + LEGEND_MARGIN;
        break;
      case InsideS:
        xOffset =
            chart.getPlot().getBounds().getX()
                + (chart.getPlot().getBounds().getWidth() - bounds.getWidth()) / 2
                + LEGEND_MARGIN;
        yOffset =
            chart.getPlot().getBounds().getY()
                + chart.getPlot().getBounds().getHeight()
                - bounds.getHeight()
                - LEGEND_MARGIN;
        break;
      case OutsideS:
        xOffset =
            chart.getPlot().getBounds().getX()
                + (chart.getPlot().getBounds().getWidth() - bounds.getWidth()) / 2.0;
        yOffset = chart.getHeight() - bounds.getHeight() - LEGEND_MARGIN;
        break;

      default:
        break;
    }

    // draw legend box background and border
    Shape rect = new Rectangle2D.Double(xOffset, yOffset, bounds.getWidth(), height);
    g.setColor(chart.getStyler().getLegendBackgroundColor());
    g.fill(rect);
    g.setStroke(SOLID_STROKE);
    g.setColor(chart.getStyler().getLegendBorderColor());
    g.draw(rect);

    doPaint(g);

    // bounds
    // bounds = new Rectangle2D.Double(xOffset, yOffset, bounds.getWidth(), bounds.getHeight());
    // g.setColor(Color.blue);
    // g.draw(bounds);
  }

  /** determine the width and height of the chart legend */
  private Rectangle2D getBoundsHintVertical() {

    if (!chart.getStyler().isLegendVisible()) {
      return new Rectangle2D
          .Double(); // Constructs a new Rectangle2D, initialized to location (0, 0) and size (0,
      // 0).
    }

    boolean containsBox = false;

    // determine legend text content max width
    double legendTextContentMaxWidth = 0;

    // determine total legend content height
    double legendContentHeight = 0;

    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {

      if (!series.isShowInLegend()) {
        continue;
      }
      if (!series.isEnabled()) {
        continue;
      }

      Map<String, Rectangle2D> seriesTextBounds = getSeriesTextBounds(series);

      double legendEntryHeight = 0; // could be multi-line
      for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
        legendEntryHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
        legendTextContentMaxWidth =
            Math.max(legendTextContentMaxWidth, entry.getValue().getWidth());
      }

      legendEntryHeight -= MULTI_LINE_SPACE; // subtract away the bottom MULTI_LINE_SPACE
      legendEntryHeight = Math.max(legendEntryHeight, (getSeriesLegendRenderGraphicHeight(series)));

      legendContentHeight += legendEntryHeight + chart.getStyler().getLegendPadding();

      if (series.getLegendRenderType() == LegendRenderType.Box) {
        containsBox = true;
      }
    }

    // determine legend content width
    double legendContentWidth;
    if (!containsBox) {
      legendContentWidth =
          chart.getStyler().getLegendSeriesLineLength()
              + chart.getStyler().getLegendPadding()
              + legendTextContentMaxWidth;
    } else {
      legendContentWidth =
          BOX_SIZE + chart.getStyler().getLegendPadding() + legendTextContentMaxWidth;
    }

    // Legend Box
    double width = legendContentWidth + 2 * chart.getStyler().getLegendPadding();
    double height = legendContentHeight + chart.getStyler().getLegendPadding();

    return new Rectangle2D.Double(0, 0, width, height); // 0 indicates not sure yet.
  }

  /** determine the width and height of the chart legend with horizontal layout */
  private Rectangle2D getBoundsHintHorizontal() {

    if (!chart.getStyler().isLegendVisible()) {
      return new Rectangle2D
          .Double(); // Constructs a new Rectangle2D, initialized to location (0, 0) and size (0,
      // 0).
    }

    // determine legend text content max height
    double legendTextContentMaxHeight = 0;

    // determine total legend content width
    double legendContentWidth = 0;

    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {

      if (!series.isShowInLegend()) {
        continue;
      }
      if (!series.isEnabled()) {
        continue;
      }

      Map<String, Rectangle2D> seriesTextBounds = getSeriesTextBounds(series);

      double legendEntryHeight = 0; // could be multi-line
      double legendEntryMaxWidth = 0; // could be multi-line
      for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
        legendEntryHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
        legendEntryMaxWidth = Math.max(legendEntryMaxWidth, entry.getValue().getWidth());
      }

      legendEntryHeight -= MULTI_LINE_SPACE; // subtract away the bottom MULTI_LINE_SPACE
      legendTextContentMaxHeight =
          Math.max(legendEntryHeight, getSeriesLegendRenderGraphicHeight(series));

      legendContentWidth += legendEntryMaxWidth + chart.getStyler().getLegendPadding();

      if (series.getLegendRenderType() == LegendRenderType.Line) {
        legendContentWidth =
            chart.getStyler().getLegendSeriesLineLength()
                + chart.getStyler().getLegendPadding()
                + legendContentWidth;
      } else {
        legendContentWidth = BOX_SIZE + chart.getStyler().getLegendPadding() + legendContentWidth;
      }
    }

    // Legend Box
    double width = legendContentWidth + chart.getStyler().getLegendPadding();
    double height = legendTextContentMaxHeight + chart.getStyler().getLegendPadding() * 2;

    return new Rectangle2D.Double(0, 0, width, height); // 0 indicates not sure yet.
  }

  /**
   * Normally each legend entry just has one line of text, but it can be made multi-line by adding
   * "\\n". This method returns a Map for each single legend entry, which is normally just a Map
   * with one single entry.
   *
   * @param series
   * @return
   */
  Map<String, Rectangle2D> getSeriesTextBounds(S series) {

    // FontMetrics fontMetrics = g.getFontMetrics(getChartPainter().getstyler().getLegendFont());
    // float fontDescent = fontMetrics.getDescent();

    String lines[] = series.getName().split("\\n");
    Map<String, Rectangle2D> seriesTextBounds =
        new LinkedHashMap<String, Rectangle2D>(lines.length);
    for (String line : lines) {
      TextLayout textLayout =
          new TextLayout(
              line, chart.getStyler().getLegendFont(), new FontRenderContext(null, true, false));
      Shape shape = textLayout.getOutline(null);
      Rectangle2D bounds = shape.getBounds2D();
      // System.out.println(tl.getAscent());
      // System.out.println(tl.getDescent());
      // System.out.println(tl.getBounds());
      // seriesTextBounds.put(line, new Rectangle2D.Double(bounds.getX(), bounds.getY(),
      // bounds.getWidth(), bounds.getHeight() - tl.getDescent()));
      // seriesTextBounds.put(line, new Rectangle2D.Double(bounds.getX(), bounds.getY(),
      // bounds.getWidth(), tl.getAscent()));
      seriesTextBounds.put(line, bounds);
    }
    return seriesTextBounds;
  }

  float getLegendEntryHeight(Map<String, Rectangle2D> seriesTextBounds, int markerSize) {

    float legendEntryHeight = 0;
    for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
      legendEntryHeight += entry.getValue().getHeight() + MULTI_LINE_SPACE;
    }
    legendEntryHeight -= MULTI_LINE_SPACE;

    legendEntryHeight = Math.max(legendEntryHeight, markerSize);

    return legendEntryHeight;
  }

  float getLegendEntryWidth(Map<String, Rectangle2D> seriesTextBounds, int markerSize) {

    float legendEntryWidth = 0;
    for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {
      legendEntryWidth = Math.max(legendEntryWidth, (float) entry.getValue().getWidth());
    }

    return legendEntryWidth + markerSize + chart.getStyler().getLegendPadding();
  }

  void paintSeriesText(
      Graphics2D g,
      Map<String, Rectangle2D> seriesTextBounds,
      int markerSize,
      double x,
      double starty) {

    g.setColor(chart.getStyler().getChartFontColor());
    g.setFont(chart.getStyler().getLegendFont());

    double multiLineOffset = 0.0;

    for (Map.Entry<String, Rectangle2D> entry : seriesTextBounds.entrySet()) {

      double height = entry.getValue().getHeight();
      double centerOffsetY = (Math.max(markerSize, height) - height) / 2.0;

      FontRenderContext frc = g.getFontRenderContext();
      TextLayout tl = new TextLayout(entry.getKey(), chart.getStyler().getLegendFont(), frc);
      Shape shape = tl.getOutline(null);
      AffineTransform orig = g.getTransform();
      AffineTransform at = new AffineTransform();
      at.translate(x, starty + height + centerOffsetY + multiLineOffset);
      g.transform(at);
      g.fill(shape);
      g.setTransform(orig);

      // // debug box
      // Rectangle2D boundsTemp = new Rectangle2D.Double(x, starty + centerOffsetY,
      // entry.getValue().getWidth(), height);
      // g.setColor(Color.blue);
      // g.draw(boundsTemp);
      multiLineOffset += height + MULTI_LINE_SPACE;
    }
  }

  @Override
  public Rectangle2D getBounds() {

    if (chart.getStyler().getLegendLayout() == Styler.LegendLayout.Vertical) {
      return getBoundsHintVertical(); // Actually, the only information contained in this bounds is
      // the width and height.
    } else {
      return getBoundsHintHorizontal(); // Actually, the only information contained in this bounds
      // is the width and height.
    }
  }
}
