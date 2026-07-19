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

public abstract class Legend_<ST extends Styler, S extends Series> implements ChartPart {

  static final int BOX_SIZE = 20;
  static final int BOX_OUTLINE_WIDTH = 5;
  private static final int LEGEND_MARGIN = 6;
  private static final int MULTI_LINE_SPACE = 3;
  private static final double MAX_LEGEND_TEXT_WIDTH_RATIO = 0.45;
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

    // An OutsideS legend is centered on the plot, whose center sits right of the image center (the
    // left y-axis consumes horizontal space). A wide (wrapped) horizontal legend can therefore
    // still run off the right image edge even though every row fits within
    // getHorizontalLegendMaxRowWidth(). Clamp the box so it always stays fully within the image
    // (issue #577). For a legend narrower than the image this only nudges it left when it would
    // otherwise be cut off; a normal centered legend is left untouched.
    if (chart.getStyler().getLegendPosition() == Styler.LegendPosition.OutsideS) {
      xOffset = Math.min(xOffset, chart.getWidth() - bounds.getWidth() - LEGEND_MARGIN);
      xOffset = Math.max(xOffset, LEGEND_MARGIN);
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

    // All rows in a wrapping horizontal legend share the same (tallest) row height so entries line
    // up regardless of series order (issue #892).
    double rowHeight = computeHorizontalRowHeight();

    // Entries flow left-to-right and wrap to a new row once the current row would exceed the
    // available width, so a legend with many (or long-named) series no longer spills past the image
    // edge (issue #577). getBoundsHintHorizontal() and the subclass doPaint() methods walk the same
    // series in the same order using the same per-entry advance width, so they wrap at identical
    // points and the reported box matches what is painted.
    double maxRowWidth = getHorizontalLegendMaxRowWidth();

    double currentRowWidth = 0;
    double widestRow = 0;
    int rowCount = 1;

    Map<String, S> map = chart.getSeriesMap();
    for (S series : map.values()) {

      if (!series.isShowInLegend()) {
        continue;
      }
      if (!series.isEnabled()) {
        continue;
      }

      double entryAdvanceWidth = getHorizontalLegendEntryAdvanceWidth(series);

      // Wrap to the next row when this entry would overflow the current one (but never wrap an
      // empty row, so a single over-wide entry still gets its own row).
      if (currentRowWidth > 0 && currentRowWidth + entryAdvanceWidth > maxRowWidth) {
        widestRow = Math.max(widestRow, currentRowWidth);
        rowCount++;
        currentRowWidth = 0;
      }
      currentRowWidth += entryAdvanceWidth;
    }
    widestRow = Math.max(widestRow, currentRowWidth);

    // Legend Box. For a single row this reduces to the previous formula
    // (widestRow + padding wide, rowHeight + 2*padding tall).
    double width = widestRow + chart.getStyler().getLegendPadding();
    double height =
        rowCount * rowHeight
            + (rowCount - 1) * chart.getStyler().getLegendPadding()
            + chart.getStyler().getLegendPadding() * 2;

    return new Rectangle2D.Double(0, 0, width, height); // 0 indicates not sure yet.
  }

  /**
   * The tallest legend entry across all shown series (text or graphic, whichever is taller). Every
   * row in a horizontal legend uses this so entries share a common baseline (issue #892).
   */
  double computeHorizontalRowHeight() {

    double rowHeight = 0;
    for (S series : chart.getSeriesMap().values()) {
      if (!series.isShowInLegend() || !series.isEnabled()) {
        continue;
      }
      rowHeight =
          Math.max(
              rowHeight,
              getLegendEntryHeight(
                  getSeriesTextBounds(series), (int) getSeriesLegendRenderGraphicHeight(series)));
    }
    return rowHeight;
  }

  /**
   * The horizontal distance a single legend entry consumes, including the trailing padding that
   * separates it from the next entry. Used by both bounds calculation and painting so they wrap at
   * exactly the same points.
   */
  double getHorizontalLegendEntryAdvanceWidth(S series) {

    return getLegendEntryWidth(getSeriesTextBounds(series), getLegendEntryMarkerWidth(series))
        + chart.getStyler().getLegendPadding();
  }

  /**
   * The width of the legend graphic (line/marker or box) preceding an entry's text. Line and
   * Scatter entries reserve the series-line length (their text is painted at that offset, with the
   * marker centered within it); box-style entries reserve {@link #BOX_SIZE}. Subclasses whose
   * graphic isn't sized by render type (e.g. OHLC) override this.
   */
  int getLegendEntryMarkerWidth(S series) {

    return (series.getLegendRenderType() == LegendRenderType.Line
            || series.getLegendRenderType() == LegendRenderType.Scatter)
        ? chart.getStyler().getLegendSeriesLineLength()
        : BOX_SIZE;
  }

  /**
   * The maximum width one row of a horizontal legend may occupy before wrapping. Keyed off the
   * chart width (minus margin and padding) so the centered OutsideS legend box never extends past
   * the image edge (issue #577).
   */
  double getHorizontalLegendMaxRowWidth() {

    return chart.getWidth() - 2.0 * LEGEND_MARGIN - 2.0 * chart.getStyler().getLegendPadding();
  }

  /**
   * A left-to-right pen for laying out a wrapping horizontal legend. Subclass painters advance it
   * per entry and read {@link #x}/{@link #y} as the current entry's origin.
   */
  final class HorizontalCursor {

    final double leftOrigin;
    final double rowHeight;
    private final double maxRowWidth;
    double x;
    double y;

    HorizontalCursor(double startx, double starty) {
      this.leftOrigin = startx;
      this.x = startx;
      this.y = starty;
      this.rowHeight = computeHorizontalRowHeight();
      this.maxRowWidth = getHorizontalLegendMaxRowWidth();
    }

    /** Wrap to the next row if placing an entry of the given advance width would overflow. */
    void maybeWrap(double entryAdvanceWidth) {
      if (x > leftOrigin && (x - leftOrigin) + entryAdvanceWidth > maxRowWidth) {
        x = leftOrigin;
        y += rowHeight + chart.getStyler().getLegendPadding();
      }
    }

    /** Move past an entry of the given advance width. */
    void advance(double entryAdvanceWidth) {
      x += entryAdvanceWidth;
    }
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

    double maxTextWidth = chart.getWidth() * MAX_LEGEND_TEXT_WIDTH_RATIO;
    FontRenderContext frc = new FontRenderContext(null, true, false);
    String lines[] = series.getLabel().split("\\n");
    Map<String, Rectangle2D> seriesTextBounds =
        new LinkedHashMap<String, Rectangle2D>(lines.length);
    for (String line : lines) {
      Rectangle2D bounds = TexRenderer.getBounds(line, chart.getStyler().getLegendFont());
      if (!TexRenderer.isTeX(line) && bounds.getWidth() > maxTextWidth) {
        line = truncateLabel(line, frc, maxTextWidth);
        bounds = TexRenderer.getBounds(line, chart.getStyler().getLegendFont());
      }
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

      String label = entry.getKey();
      double height = entry.getValue().getHeight();
      double centerOffsetY = (Math.max(markerSize, height) - height) / 2.0;

      if (TexRenderer.isTeX(label)) {
        TexRenderer.render(
            g,
            label,
            x,
            starty + centerOffsetY + multiLineOffset,
            chart.getStyler().getLegendFont(),
            chart.getStyler().getChartFontColor());
      } else {
        FontRenderContext frc = g.getFontRenderContext();
        TextLayout tl = new TextLayout(label, chart.getStyler().getLegendFont(), frc);
        Shape shape = tl.getOutline(null);
        AffineTransform orig = g.getTransform();
        AffineTransform at = new AffineTransform();
        at.translate(x, starty + height + centerOffsetY + multiLineOffset);
        g.transform(at);
        g.fill(shape);
        g.setTransform(orig);
      }

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

  private String truncateLabel(String text, FontRenderContext frc, double maxWidth) {
    String ellipsis = "…";
    StringBuilder sb = new StringBuilder(text);
    while (sb.length() > 0) {
      TextLayout tl =
          new TextLayout(
              sb.toString() + ellipsis, chart.getStyler().getLegendFont(), frc);
      if (tl.getOutline(null).getBounds2D().getWidth() <= maxWidth) {
        return sb.toString() + ellipsis;
      }
      sb.deleteCharAt(sb.length() - 1);
    }
    return ellipsis;
  }
}
