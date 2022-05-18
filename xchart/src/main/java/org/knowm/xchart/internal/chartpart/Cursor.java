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

  private final Map<String, Series> seriesMap;

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
  public void mouseMoved(MouseEvent mouseEvent) {

    //    // don't draw anything
    //    if (!styler.isCursorEnabled() || seriesMap == null) {
    //      return;
    //    }

    mouseX = mouseEvent.getX();
    mouseY = mouseEvent.getY();
    if (isMouseOutOfPlotContent()) {

      if (matchingDataPointList.size() > 0) {
        matchingDataPointList.clear();
        mouseEvent.getComponent().repaint();
      }
      return;
    }
    calculateMatchingDataPoints();
    mouseEvent.getComponent().repaint();
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
  public void paint(Graphics2D graphic) {

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

      paintVerticalLine(graphic, firstDataPoint);

      paintBackGround(graphic, xValueTextLayout);

      paintDataPointInfo(graphic xValueTextLayout);
    }
  }

  private void paintVerticalLine(Graphics2D graphic, DataPoint dataPoint) {

    BasicStroke stroke =
        new BasicStroke(styler.getCursorLineWidth(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    graphic.setStroke(stroke);
    graphic.setColor(styler.getCursorColor());
    Line2D.Double line = new Line2D.Double();
    line.setLine(
        dataPoint.x,
        chart.plot.plotContent.getBounds().getY(),
        dataPoint.x,
        chart.plot.plotContent.getBounds().getY() + chart.plot.plotContent.getBounds().getHeight());
    graphic.draw(line);
  }

  private void paintBackGround(Graphics2D graphic, TextLayout xValueTextLayout) {

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
    
    double mouseXPoint = mouseX + MOUSE_SPACING + backgroundWidth;
    double mouseYPoint = mouseY + MOUSE_SPACING + backgroundHeight;
    
    double mouseXBounde = chart.plot.plotContent.getBounds().getX() + chart.plot.plotContent.getBounds().getWidth();
    double mouseYBounde = chart.plot.plotContent.getBounds().getY() + chart.plot.plotContent.getBounds().getHeight();
    
    double width
    if (mouseXPoint > mouseXBounde) {
      startX = mouseX - backgroundWidth - MOUSE_SPACING;
    }

    if (mouseYPoint > mouseYBounde) {
      startY = mouseY - backgroundHeight - MOUSE_SPACING;
    }

    graphic.setColor(styler.getCursorBackgroundColor());
    graphic.fillRect(
        (int) startX + MOUSE_SPACING,
        (int) startY + MOUSE_SPACING,
        (int) (backgroundWidth),
        (int) (backgroundHeight));
  }

  private void paintDataPointInfo(Graphics2D graphic, TextLayout xValueTextLayout) {

    AffineTransform orig = graphic.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(
        startX + MOUSE_SPACING + LINE_SPACING, startY + textHeight + MOUSE_SPACING + LINE_SPACING);
    graphic.transform(at);
    graphic.setColor(styler.getCursorFontColor());
    graphic.fill(xValueTextLayout.getOutline(null));

    MarkerSeries series = null;
    TextLayout dataPointTextLayout = null;
    Shape circle = null;
    for (DataPoint dataPoint : matchingDataPointList) {
      at = new AffineTransform();
      at.translate(0, textHeight + LINE_SPACING);
      graphic.transform(at);
      series = (MarkerSeries) seriesMap.get(dataPoint.seriesName);
      if (series == null) {
        continue;
      }
      graphic.setColor(series.getMarkerColor());
      circle = new Ellipse2D.Double(0, -textHeight, textHeight, textHeight);
      graphic.fill(circle);

      at = new AffineTransform();
      at.translate(textHeight + LINE_SPACING, 0);
      graphic.transform(at);
      graphic.setColor(styler.getCursorFontColor());
      dataPointTextLayout =
          new TextLayout(
              dataPoint.seriesName + ": " + dataPoint.yValue,
              styler.getCursorFont(),
              new FontRenderContext(null, true, false));
      graphic.fill(dataPointTextLayout.getOutline(null));

      at = new AffineTransform();
      at.translate(-textHeight - LINE_SPACING, 0);
      graphic.transform(at);
    }
    graphic.setTransform(orig);
  }

  void addData(double xOffset, double yOffset, String xValue, String yValue, String seriesName) {

    DataPoint dataPoint = new DataPoint(xOffset, yOffset, xValue, yValue, seriesName);
    dataPointList.add(dataPoint);
  }

  /** One DataPoint per series, keep the DataPoint closest to mouseX */
  private void calculateMatchingDataPoints() {

    List<DataPoint> dataPoints = new ArrayList<>();
    for (DataPoint dataPoint : dataPointList) {
    	
      boolean isDatapointColseX = dataPoint.shape.contains(mouseX, dataPoint.shape.getBounds().getCenterY());
      boolean isYOverLowBound = chart.plot.plotContent.getBounds().getY() < mouseY;
      boolean isYUnderHighBound = chart.plot.plotContent.getBounds().getY() + chart.plot.plotContent.getBounds().getHeight() > mouseY;
      
      if (isDatapointColseX && isYOverLowBound && isYUnderHighBound) {
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

  private static class DataPoint {

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
