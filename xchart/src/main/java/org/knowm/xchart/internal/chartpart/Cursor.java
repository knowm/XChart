package org.knowm.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.internal.series.MarkerSeries;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.XYStyler;

/** Cursor movement to display matching point data information. */
public class Cursor extends MouseAdapter implements ChartPart {

  private static final int LINE_SPACING = 5;

  private static final int MOUSE_SPACING = 15;

  // package-private so tests in this package can assert it doesn't grow across repaints
  final List<DataPoint> dataPointList = new ArrayList<>();
  // package-private so tests in this package can assert on what the cursor label shows
  final List<DataPoint> matchingDataPointList = new ArrayList<>();

  private final XYStyler styler;

  private final Map<String, Series> seriesMap;

  private Rectangle2D plotBounds = null;

  private double mouseX;
  private double mouseY;
  private double startX;
  private double startY;
  private double textHeight;

  /**
   * Constructor
   *
   * @param chart
   */
  public Cursor(Chart<?, ?> chart) {

    this.styler = (XYStyler) chart.getStyler();

    // clear lists
    dataPointList.clear();

    @SuppressWarnings("unchecked")
    Map<String, Series> tmp = (Map<String, Series>) (Map<?, ?>) chart.getSeriesMap();
    this.seriesMap = tmp;
  }

  @Override
  public void mouseMoved(MouseEvent e) {

    //    // don't draw anything
    //    if (!styler.isCursorEnabled() || seriesMap == null) {
    //      return;
    //    }

    mouseX = e.getX();
    mouseY = e.getY();
    if (isMouseOutOfPlotContent()) {

      if (matchingDataPointList.size() > 0) {
        matchingDataPointList.clear();
        e.getComponent().repaint();
      }
      return;
    }
    calculateMatchingDataPoints();
    e.getComponent().repaint();
  }

  private boolean isMouseOutOfPlotContent() {

    if (plotBounds == null) {
      return true;
    }
    return !plotBounds.contains(mouseX, mouseY);
  }

  @Override
  public Rectangle2D getBounds() {
    return null;
  }

  @Override
  public void paint(Graphics2D g) {

    //    if (!styler.isCursorEnabled()) {
    //      return;
    //    }

    if (matchingDataPointList.size() > 0) {
      DataPoint firstDataPoint = matchingDataPointList.get(0);

      TextLayout xValueTextLayout =
          new TextLayout(
              firstDataPoint.xValue,
              styler.getCursorFont(),
              new FontRenderContext(null, true, false));
      textHeight = xValueTextLayout.getBounds().getHeight();

      paintVerticalLine(g, firstDataPoint);

      paintBackGround(g, xValueTextLayout);

      paintDataPointInfo(g, xValueTextLayout);
    }
  }

  private void paintVerticalLine(Graphics2D g, DataPoint dataPoint) {

    BasicStroke stroke =
        new BasicStroke(styler.getCursorLineWidth(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    g.setStroke(stroke);
    g.setColor(styler.getCursorColor());
    Line2D.Double line = new Line2D.Double();
    line.setLine(
        dataPoint.x,
        plotBounds.getY(),
        dataPoint.x,
        plotBounds.getY() + plotBounds.getHeight());
    g.draw(line);
  }

  private void paintBackGround(Graphics2D g, TextLayout xValueTextLayout) {

    double maxLinewidth = xValueTextLayout.getBounds().getWidth();
    TextLayout dataPointTextLayout = null;
    Rectangle2D dataPointRectangle = null;
    for (DataPoint dataPoint : matchingDataPointList) {
      dataPointTextLayout =
          new TextLayout(
              dataPoint.seriesName + ": " + dataPoint.yValue,
              styler.getCursorFont(),
              new FontRenderContext(null, true, false));
      dataPointRectangle = dataPointTextLayout.getBounds();
      if (maxLinewidth < dataPointRectangle.getWidth()) {
        maxLinewidth = dataPointRectangle.getWidth();
      }
    }

    double backgroundWidth = styler.getCursorFont().getSize() + maxLinewidth + 3 * LINE_SPACING;
    double backgroundHeight =
        textHeight * (1 + matchingDataPointList.size())
            + (2 + matchingDataPointList.size()) * LINE_SPACING;

    startX = mouseX;
    startY = mouseY;
    if (mouseX + MOUSE_SPACING + backgroundWidth
        > plotBounds.getX() + plotBounds.getWidth()) {
      startX = mouseX - backgroundWidth - MOUSE_SPACING;
    }

    if (mouseY + MOUSE_SPACING + backgroundHeight
        > plotBounds.getY() + plotBounds.getHeight()) {
      startY = mouseY - backgroundHeight - MOUSE_SPACING;
    }

    g.setColor(styler.getCursorBackgroundColor());
    g.fillRect(
        (int) startX + MOUSE_SPACING,
        (int) startY + MOUSE_SPACING,
        (int) (backgroundWidth),
        (int) (backgroundHeight));
  }

  private void paintDataPointInfo(Graphics2D g, TextLayout xValueTextLayout) {

    AffineTransform orig = g.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(
        startX + MOUSE_SPACING + LINE_SPACING, startY + textHeight + MOUSE_SPACING + LINE_SPACING);
    g.transform(at);
    g.setColor(styler.getCursorFontColor());
    g.fill(xValueTextLayout.getOutline(null));

    MarkerSeries series = null;
    TextLayout dataPointTextLayout = null;
    Shape circle = null;
    for (DataPoint dataPoint : matchingDataPointList) {
      at = new AffineTransform();
      at.translate(0, textHeight + LINE_SPACING);
      g.transform(at);
      series = (MarkerSeries) seriesMap.get(dataPoint.seriesName);
      if (series == null) {
        continue;
      }
      g.setColor(series.getMarkerColor());
      circle = new Ellipse2D.Double(0, -textHeight, textHeight, textHeight);
      g.fill(circle);

      at = new AffineTransform();
      at.translate(textHeight + LINE_SPACING, 0);
      g.transform(at);
      g.setColor(styler.getCursorFontColor());
      dataPointTextLayout =
          new TextLayout(
              dataPoint.seriesName + ": " + dataPoint.yValue,
              styler.getCursorFont(),
              new FontRenderContext(null, true, false));
      g.fill(dataPointTextLayout.getOutline(null));

      at = new AffineTransform();
      at.translate(-textHeight - LINE_SPACING, 0);
      g.transform(at);
    }
    g.setTransform(orig);
  }

  public void setData(PlotInteractionData data) {

    dataPointList.clear();
    if (data == null) {
      plotBounds = null;
      return;
    }
    plotBounds = data.getPlotBounds();
    for (PlotInteractionData.CursorData cd : data.getCursorDataList()) {
      dataPointList.add(new DataPoint(cd.x, cd.y, cd.xValue, cd.yValue, cd.seriesName));
    }
    // Refresh matching points with the current mouse position so the cursor label
    // updates on every repaint, not only on the next mouseMoved event (fixes #805).
    if (!isMouseOutOfPlotContent()) {
      calculateMatchingDataPoints();
    }
  }

  /**
   * One entry per series in matchingDataPointList. When multiple points of the same series fall
   * under the cursor, the closest one's X position is used and all their Y values are combined into
   * a comma-separated string (fixes #805).
   */
  private void calculateMatchingDataPoints() {

    List<DataPoint> dataPoints = new ArrayList<>();
    for (DataPoint dataPoint : dataPointList) {
      if (dataPoint.shape.contains(mouseX, dataPoint.shape.getBounds().getCenterY())
          && plotBounds.getY() < mouseY
          && plotBounds.getY() + plotBounds.getHeight() > mouseY) {
        dataPoints.add(dataPoint);
      }
    }

    if (dataPoints.size() > 0) {
      Map<String, DataPoint> closestMap = new HashMap<>();
      Map<String, List<String>> yValuesMap = new HashMap<>();
      for (DataPoint dataPoint : dataPoints) {
        String seriesName = dataPoint.seriesName;
        yValuesMap.computeIfAbsent(seriesName, k -> new ArrayList<>()).add(dataPoint.yValue);
        if (closestMap.containsKey(seriesName)) {
          if (Math.abs(dataPoint.x - mouseX) < Math.abs(closestMap.get(seriesName).x - mouseX)) {
            closestMap.put(seriesName, dataPoint);
          }
        } else {
          closestMap.put(seriesName, dataPoint);
        }
      }
      matchingDataPointList.clear();
      for (Map.Entry<String, DataPoint> entry : closestMap.entrySet()) {
        DataPoint closest = entry.getValue();
        List<String> yVals = yValuesMap.get(entry.getKey());
        if (yVals.size() > 1) {
          String combinedY = String.join(", ", yVals);
          matchingDataPointList.add(
              new DataPoint(closest.x, closest.y, closest.xValue, combinedY, closest.seriesName));
        } else {
          matchingDataPointList.add(closest);
        }
      }
    }
  }

  static class DataPoint {

    // edge detection
    private static final int MARGIN = 5;

    // Used to determine the point that the mouse has passed vertically
    final Shape shape;
    final double x;
    final double y;
    final String xValue;
    final String yValue;
    final String seriesName;

    public DataPoint(double x, double y, String xValue, String yValue, String seriesName) {

      double halfSize = MARGIN * 1.5;
      double markerSize = MARGIN * 3;

      this.x = x;
      this.y = y;
      this.shape =
          new Ellipse2D.Double(this.x - halfSize, this.y - halfSize, markerSize, markerSize);

      this.xValue = xValue;
      this.yValue = yValue;
      this.seriesName = seriesName;
    }
  }
}
