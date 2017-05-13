package org.knowm.xchart.style.label;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.knowm.xchart.style.Styler;

// TODO move this out of Style

/**
 * Data labels can be put on all labels or configured to popup like a tooltip from a mouse over.
 */
public class DataLabeller implements MouseMotionListener {

  /**
   * What to dispolay in the data label
   */
  public enum DataLabelContent {
    xLabels, yLabels, xAndYLabels
  }

  // for pop up
  private final ArrayList<DataPoint> dataPointList = new ArrayList<DataPoint>();
  private DataPoint popupDataPoint;

  // edge detection
  private final static int MARGIN = 5;
  private double leftEdge;
  private double rightEdge;
  private double topEdge;
  private double bottomEdge;

  // should they be enabled or not?
  private boolean isDataLabelsEnabled = true;

  // (either they are all rendered permanently or on via tool tip)
  private boolean isDataLabelsAsToolTips = true;

  private DataLabelContent dataLabelContent = DataLabelContent.xAndYLabels;

  // should prevent overlapping labels
  private boolean preventDataLabelOverlap = false;

  // currently drawn labels
  private final ArrayList<Shape> shapeList = new ArrayList<Shape>();

  private final Styler styler;

  private Color popupHighlightColor;
  private Color backgroundColor;
  private Color borderColor;
  private Color textColor;
  private Font textFont;
//  DecimalFormat twoPlaces;

  /**
   * Constructor
   *
   * @param styler
   */
  public DataLabeller(Styler styler) {

    this.styler = styler;

    // initStyle
    // TODO create specific styles in styler for some of these
//    popupHighlightColor = styler.getChartTitleBoxBackgroundColor();
    popupHighlightColor = Color.PINK;
    backgroundColor = styler.getLegendBackgroundColor(); // Color.WHITE;
    borderColor = styler.getLegendBorderColor(); // Color.BLACK;
    textColor = styler.getChartFontColor();
    textFont = styler.getAnnotationsFont();

    popupDataPoint = null;
  }

  @Override
  public void mouseDragged(MouseEvent e) {

  }

  @Override
  public void mouseMoved(MouseEvent e) {

    if (!isDataLabelsEnabled || !isDataLabelsAsToolTips) { // don't draw anything or all labels aready drawn
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
      if (popupDataPoint != null) {
        if (popupDataPoint.equals(newPoint)) {
          return;
        }
      }
      popupDataPoint = newPoint;
//      System.out.println("e.getComponent() = " + e.getComponent()); // should be XChartPanel
      e.getComponent().repaint(); // repaint the entire XChartPanel
    }
    // remove the popup shape
    else if (popupDataPoint != null) {
      popupDataPoint = null;
      e.getComponent().repaint(); // repaint the entire XChartPanel
    }
  }

  public void prepare(Graphics2D g) {

    if (!isDataLabelsEnabled) {
      return;
    }
    // clear lists
    dataPointList.clear();
    shapeList.clear();

    Rectangle clipBounds = g.getClipBounds();

    leftEdge = clipBounds.getX() + MARGIN;
    rightEdge = clipBounds.getMaxX() - MARGIN * 2;

    topEdge = clipBounds.getY() + MARGIN;
    bottomEdge = clipBounds.getMaxY() - MARGIN * 2;
  }

  public void paint(Graphics2D g) {

    // abort of data labels is not enabled
    if (!isDataLabelsEnabled) {
      return;
    }

    if (isDataLabelsAsToolTips) {
      if (popupDataPoint != null) { // popupDataPoint was created in mouse move, need to render it
        paintDataLabel(g, popupDataPoint);
      }
    } else { // data labels should be permanently shown for all points

      for (DataPoint dataPoint : dataPointList) {
        paintDataLabel(g, dataPoint);
      }
    }
  }

  /**
   * Adds a data (xValue, yValue) with coordinates (xOffset, yOffset). This point will be highlighted with a circle centering (xOffset, yOffset)
   */
  public void addData(double xOffset, double yOffset, String xValue, String yValue) {

    String label = getLabel(xValue, yValue);

    addData(xOffset, yOffset, label);
  }

  /**
   * Adds a data with label with coordinates (xOffset, yOffset). This point will be highlighted with a circle centering (xOffset, yOffset)
   */
  public void addData(double xOffset, double yOffset, String label) {

    DataPoint dp = new DataPoint(xOffset, yOffset, MARGIN, label);
    dataPointList.add(dp);
  }

  /**
   * Adds a data (xValue, yValue) with geometry defined with shape. This point will be highlighted using the shape
   */
  public void addData(Shape shape, double xOffset, double yOffset, double width, String xValue, String yValue) {

    String label = getLabel(xValue, yValue);
    addData(shape, xOffset, yOffset, width, label);
  }

  public void addData(Shape shape, double xOffset, double yOffset, double width, String label) {

    DataPoint dp = new DataPoint(shape, xOffset, yOffset, width, label);
    dataPointList.add(dp);
  }

  private String getLabel(String xValue, String yValue) {

    switch (dataLabelContent) {
      case xAndYLabels:
        return "(" + xValue + ", " + yValue + ")";
      case xLabels:
        return xValue;
      case yLabels:
        return yValue;

      default:
        break;
    }
    return null;
  }

  private void paintDataLabel(Graphics2D g, DataPoint dataPoint) {

    TextLayout textLayout = new TextLayout(dataPoint.label, textFont, new FontRenderContext(null, true, false));
    Rectangle2D annotationRectangle = textLayout.getBounds();

    // double x = dataPoint.x + MARGIN;
    double x = dataPoint.x + dataPoint.w / 2 - annotationRectangle.getWidth() / 2 - MARGIN;
    double y = dataPoint.y - 3 * MARGIN - annotationRectangle.getHeight();

    double w = annotationRectangle.getWidth() + 2 * MARGIN;
    double h = annotationRectangle.getHeight() + 2 * MARGIN;
    double halfHeight = h / 2;

    // not the box with label, but the shape
    if (isDataLabelsAsToolTips) {
      // highlight shape for popup
      g.setColor(popupHighlightColor);
      g.fill(dataPoint.shape);
    }

    // the label in a box
    x = Math.max(x, leftEdge);
    x = Math.min(x, rightEdge - w);
    y = Math.max(y, topEdge);
    y = Math.min(y, bottomEdge - h);
    Rectangle2D rectangle = new Rectangle2D.Double(x, y, w, h);

    boolean shouldDrawBox = true;
    if (preventDataLabelOverlap) {
      for (Shape shape : shapeList) {
        if (shape.intersects(rectangle)) {
          shouldDrawBox = false;
          break;
        }
      }
      if (shouldDrawBox) {
        shapeList.add(rectangle);
      }
    }
    if (shouldDrawBox) {

      // fill background
      g.setColor(backgroundColor);
//      g.setColor(Color.blue);
      g.fill(rectangle);

      // draw outline
      g.setColor(borderColor);
      g.draw(rectangle);

      // draw text label
      Shape shape = textLayout.getOutline(null);
      g.setColor(textColor);
      g.setFont(textFont);
      AffineTransform orig = g.getTransform();
      AffineTransform at = new AffineTransform();
      // at.translate(annotationX, annotationY);
      at.translate(x + MARGIN, y + MARGIN + halfHeight);
      g.transform(at);
      g.fill(shape);
      g.setTransform(orig);

    }
  }

  public MouseMotionListener getMouseMotionListener() {

    return this;
  }

  public boolean isDataLabelsEnabled() {

    return isDataLabelsEnabled;
  }

  public void setDataLabelsEnabled(boolean dataLabelsEnabled) {

    this.isDataLabelsEnabled = dataLabelsEnabled;
  }

  public boolean isDataLabelsAsToolTips() {

    return isDataLabelsAsToolTips;
  }

  public void setDataLabelsAsToolTips(boolean dataLabelsAsToolTips) {

    this.isDataLabelsAsToolTips = dataLabelsAsToolTips;
  }

  public DataLabelContent getDataLabelContent() {

    return dataLabelContent;
  }

  public void setDataLabelContent(DataLabelContent dataLabelContent) {

    this.dataLabelContent = dataLabelContent;
  }

  public boolean isPreventDataLabelOverlap() {

    return preventDataLabelOverlap;
  }

  public void setPreventDataLabelOverlap(boolean preventDataLabelOverlap) {

    this.preventDataLabelOverlap = preventDataLabelOverlap;
  }

  protected static class DataPoint {

    private final String label;

    // used for popup detection & popup highlight
    private final Shape shape;

    // label center coordinates
    private final double x;
    private final double y;

    // width of data point (used for bar charts)
    double w;

    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param margin
     * @param label
     */
    public DataPoint(double x, double y, int margin, String label) {

      double halfSize = margin * 1.5;
      double markerSize = margin * 3;

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
    public DataPoint(Shape shape, double x, double y, double width, String label) {

      this.x = x;
      this.y = y;
      this.w = width;
      this.shape = shape;
      this.label = label;
    }

  }

}
