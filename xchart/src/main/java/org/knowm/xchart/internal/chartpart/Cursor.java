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

/**
 * Cursor movement to display matching point data information.
 *
 * @author Mr14huashao
 */
public class Cursor extends MouseAdapter implements ChartPart {

  private static final int LINE_SPACING = 5;

  private static final int MOUSE_SPACING = 15;

  private final List<DataPoint> dataPointList = new ArrayList<>();
  private final List<DataPoint> matchingDataPointList = new ArrayList<>();

  private final Chart chart;
  private final XYStyler styler;

  private Map<String, Series> seriesMap;

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
  public Cursor(Chart chart) {

    this.chart = chart;
    this.styler = (XYStyler) chart.getStyler();
    PlotContent_XY plotContent_xy = (PlotContent_XY) (chart.plot.plotContent);
    plotContent_xy.setCursor(this);

    // clear lists
    dataPointList.clear();

    this.seriesMap = chart.getSeriesMap();
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

    boolean isMouseOut = false;
    if (!chart.plot.plotContent.getBounds().contains(mouseX, mouseY)) {
      isMouseOut = true;
    }
    return isMouseOut;
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
        chart.plot.plotContent.getBounds().getY(),
        dataPoint.x,
        chart.plot.plotContent.getBounds().getY() + chart.plot.plotContent.getBounds().getHeight());
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
        > chart.plot.plotContent.getBounds().getX()
            + chart.plot.plotContent.getBounds().getWidth()) {
      startX = mouseX - backgroundWidth - MOUSE_SPACING;
    }

    if (mouseY + MOUSE_SPACING + backgroundHeight
        > chart.plot.plotContent.getBounds().getY()
            + chart.plot.plotContent.getBounds().getHeight()) {
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

  void addData(double xOffset, double yOffset, String xValue, String yValue, String seriesName) {

    DataPoint dataPoint = new DataPoint(xOffset, yOffset, xValue, yValue, seriesName);
    dataPointList.add(dataPoint);
  }

  /** One DataPoint per series, keep the DataPoint closest to mouseX */
  private void calculateMatchingDataPoints() {

    List<DataPoint> dataPoints = new ArrayList<>();
    for (DataPoint dataPoint : dataPointList) {
      if (dataPoint.shape.contains(mouseX, dataPoint.shape.getBounds().getCenterY())
          && chart.plot.plotContent.getBounds().getY() < mouseY
          && chart.plot.plotContent.getBounds().getY()
                  + chart.plot.plotContent.getBounds().getHeight()
              > mouseY) {
        dataPoints.add(dataPoint);
      }
    }

    if (dataPoints.size() > 0) {
      Map<String, DataPoint> map = new HashMap<>();
      String seriesName = "";
      for (DataPoint dataPoint : dataPoints) {
        seriesName = dataPoint.seriesName;
        if (map.containsKey(seriesName)) {
          if (Math.abs(dataPoint.x - mouseX) < Math.abs(map.get(seriesName).x - mouseX)) {
            map.put(seriesName, dataPoint);
          }
        } else {
          map.put(seriesName, dataPoint);
        }
      }
      matchingDataPointList.clear();
      matchingDataPointList.addAll(map.values());
    }
  }

  private class DataPoint {

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
