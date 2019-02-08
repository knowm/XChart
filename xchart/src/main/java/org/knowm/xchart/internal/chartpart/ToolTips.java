package org.knowm.xchart.internal.chartpart;

import static org.knowm.xchart.internal.chartpart.ChartPart.SOLID_STROKE;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.style.Styler;

/** Data labels can be put on all labels or configured to popup like a tooltip from a mouse over. */
public class ToolTips implements MouseMotionListener {

  // edge detection
  private static final int MARGIN = 5;
  // for pop up
  private final List<DataPoint> dataPointList = new ArrayList<DataPoint>();
  private final Styler styler;
  private DataPoint dataPoint;
  private double leftEdge;
  private double rightEdge;
  private double topEdge;
  private double bottomEdge;
  /**
   * Constructor
   *
   * @param styler
   */
  ToolTips(Styler styler) {

    this.styler = styler;
    this.dataPoint = null;
  }

  @Override
  public void mouseDragged(MouseEvent e) {

    // ignore
  }

  @Override
  public void mouseMoved(MouseEvent e) {

    if (!styler.isToolTipsEnabled()) { // don't draw anything or all labels aready drawn
      return;
    }

    DataPoint newPoint = null;
    int x = e.getX();
    int y = e.getY();
    for (DataPoint dataPoint : dataPointList) {
      if (dataPoint.shape.contains(x, y)) {
        newPoint = dataPoint;
        break;
      }
    }

    if (newPoint != null) {

      // if the existing shown data point is already shown, abort
      if (dataPoint != null) {
        if (dataPoint.equals(newPoint)) {
          return;
        }
      }
      dataPoint = newPoint;
      e.getComponent().repaint(); // repaint the entire XChartPanel
    }
    // remove the popup shape
    else if (dataPoint != null) {
      dataPoint = null;
      e.getComponent().repaint(); // repaint the entire XChartPanel
    }
  }

  void prepare(Graphics2D g) {

    if (!styler.isToolTipsEnabled()) {
      return;
    }
    // clear lists
    dataPointList.clear();

    Rectangle clipBounds = g.getClipBounds();

    leftEdge = clipBounds.getX() + MARGIN;
    rightEdge = clipBounds.getMaxX() - MARGIN * 2;

    topEdge = clipBounds.getY() + MARGIN;
    bottomEdge = clipBounds.getMaxY() - MARGIN * 2;
  }

  public void paint(Graphics2D g) {

    // abort of data labels is not enabled
    if (!styler.isToolTipsEnabled()) {
      return;
    }

    if (styler.isToolTipsAlwaysVisible()) {
      for (DataPoint dataPoint : dataPointList) {
        paintToolTip(g, dataPoint);
      }
    }

    if (dataPoint != null) { // dataPoint was created in mouse move, need to render it
      paintToolTip(g, dataPoint);
    }
  }

  /**
   * Adds a data (xValue, yValue) with coordinates (xOffset, yOffset). This point will be
   * highlighted with a circle centering (xOffset, yOffset)
   */
  void addData(double xOffset, double yOffset, String xValue, String yValue) {

    String label = getLabel(xValue, yValue);

    addData(xOffset, yOffset, label);
  }

  /**
   * Adds a data with label with coordinates (xOffset, yOffset). This point will be highlighted with
   * a circle centering (xOffset, yOffset)
   */
  public void addData(double xOffset, double yOffset, String label) {

    DataPoint dp = new DataPoint(xOffset, yOffset, label);
    dataPointList.add(dp);
  }

  /**
   * Adds a data (xValue, yValue) with geometry defined with shape. This point will be highlighted
   * using the shape
   */
  void addData(
      Shape shape, double xOffset, double yOffset, double width, String xValue, String yValue) {

    String label = getLabel(xValue, yValue);
    addData(shape, xOffset, yOffset, width, label);
  }

  void addData(Shape shape, double xOffset, double yOffset, double width, String label) {

    DataPoint dp = new DataPoint(shape, xOffset, yOffset, width, label);
    dataPointList.add(dp);
  }

  private String getLabel(String xValue, String yValue) {

    switch (styler.getToolTipType()) {
      case xAndYLabels:
        return "(" + xValue + ", " + yValue + ")";
      case xLabels:
        return xValue;
      case yLabels:
        return yValue;
      default:
        break;
    }
    return "";
  }

  private void paintToolTip(Graphics2D g, DataPoint dataPoint) {

    TextLayout textLayout =
        new TextLayout(
            dataPoint.label, styler.getToolTipFont(), new FontRenderContext(null, true, false));
    Rectangle2D annotationRectangle = textLayout.getBounds();

    double x = dataPoint.x + dataPoint.w / 2 - annotationRectangle.getWidth() / 2 - MARGIN;
    double y = dataPoint.y - 3 * MARGIN - annotationRectangle.getHeight();

    double w = annotationRectangle.getWidth() + 2 * MARGIN;
    double h = annotationRectangle.getHeight() + 2 * MARGIN;
    double halfHeight = h / 2;

    if (dataPoint == this.dataPoint) {
      // not the box with label, but the shape
      // highlight shape for popup
      g.setColor(styler.getToolTipHighlightColor());
      g.fill(dataPoint.shape);
    }

    // the label in a box
    x = Math.max(x, leftEdge);
    x = Math.min(x, rightEdge - w);
    y = Math.max(y, topEdge);
    y = Math.min(y, bottomEdge - h);
    Rectangle2D rectangle = new Rectangle2D.Double(x, y, w, h);

    // fill background
    g.setColor(styler.getToolTipBackgroundColor());
    g.fill(rectangle);

    // draw outline
    g.setStroke(SOLID_STROKE);
    g.setColor(styler.getToolTipBorderColor());
    g.draw(rectangle);

    // draw text label
    Shape shape = textLayout.getOutline(null);
    g.setColor(styler.getChartFontColor());
    g.setFont(styler.getToolTipFont());
    AffineTransform orig = g.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(x + MARGIN - 1, y + MARGIN - 1 + halfHeight);
    g.transform(at);
    g.fill(shape);
    g.setTransform(orig);
  }

  public MouseMotionListener getMouseMotionListener() {

    return this;
  }

  static class DataPoint {

    // width of data point (used for bar charts)
    final double w;
    private final String label;
    // used for popup detection & popup highlight
    private final Shape shape;
    // label center coordinates
    private final double x;
    private final double y;

    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param label
     */
    DataPoint(double x, double y, String label) {

      double halfSize = MARGIN * 1.5;
      double markerSize = MARGIN * 3;

      this.shape = new Ellipse2D.Double(x - halfSize, y - halfSize, markerSize, markerSize);

      this.x = x;
      this.y = y;
      this.w = 0;
      this.label = label;
    }

    /**
     * Constructor
     *
     * @param shape
     * @param x
     * @param y
     * @param width
     * @param label
     */
    DataPoint(Shape shape, double x, double y, double width, String label) {

      this.x = x;
      this.y = y;
      this.w = width;
      this.shape = shape;
      this.label = label;
    }
  }
}
