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

public class DataLabeller implements MouseMotionListener {

  public enum DataLabelContent {
    xLabels, yLabels, xAndYLabels
  }

  // for pop up
  private ArrayList<DataPoint> dataPointList;
  private DataPoint popupDataPoint;

  private int margin = 5;

  // edge detection
  private double leftEdge;
  private double rightEdge;
  private double topEdge;
  private double bottomEdge;

  private boolean shouldShowDataLabels = true;
  // (either they are all rendered permanently or on via pop up)
  private boolean dataLabelsAsToolTips = true;
  private DataLabelContent dataLabelContent = DataLabelContent.xAndYLabels;

  // dont draw overlapping labels
  private boolean preventOverlap = false;

  // currently drawn labels
  private ArrayList<Shape> shapeList;

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
    popupHighlightColor = styler.getChartTitleBoxBackgroundColor();
    backgroundColor = styler.getLegendBackgroundColor(); // Color.WHITE;
    borderColor = styler.getLegendBorderColor(); // Color.BLACK;
    textColor = styler.getChartFontColor();
    textFont = styler.getAnnotationsFont();

//    if (styler.getDecimalPattern() != null) {
//      twoPlaces = new DecimalFormat(styler.getDecimalPattern());
//    }

    //init
    dataPointList = new ArrayList<DataPoint>();
    shapeList = new ArrayList<Shape>();
//    twoPlaces = new DecimalFormat("#.#");
    popupDataPoint = null;
  }

  public void startPaint(Graphics2D g) {

    if (!shouldShowDataLabels) {
      return;
    }
//    if (textColor == null) {
//      initStyle();
//    }
    dataPointList.clear();
    shapeList.clear();

    Rectangle clipBounds = g.getClipBounds();

    leftEdge = clipBounds.getX() + margin;
    rightEdge = clipBounds.getMaxX() - margin * 2;

    topEdge = clipBounds.getY() + margin;
    bottomEdge = clipBounds.getMaxY() - margin * 2;
  }

  public void paint(Graphics2D g) {

    if (!shouldShowDataLabels) {
      return;
    }

    if (dataLabelsAsToolTips) {
      if (popupDataPoint != null) {
        paintDataLabel(g, popupDataPoint, true);
      }
      return;
    }

    for (DataPoint dataPoint : dataPointList) {
      paintDataLabel(g, dataPoint, false);
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

    DataPoint dp = new DataPoint(xOffset, yOffset, margin, label);
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

  public void paintDataLabel(Graphics2D g, DataPoint dataPoint, boolean isPopup) {

    TextLayout textLayout = new TextLayout(dataPoint.label, textFont, new FontRenderContext(null, true, false));
    Rectangle2D annotationRectangle = textLayout.getBounds();

    // double x = dataPoint.x + margin;
    double x = dataPoint.x + dataPoint.w / 2 - annotationRectangle.getWidth() / 2 - margin;
    double y = dataPoint.y - 3 * margin - annotationRectangle.getHeight();

    double w = annotationRectangle.getWidth() + 2 * margin;
    double h = annotationRectangle.getHeight() + 2 * margin;
    double halfHeight = h / 2;

    if (isPopup) {
      // highlight shape for popup
      g.setColor(popupHighlightColor);
      g.fill(dataPoint.shape);
    }

    x = Math.max(x, leftEdge);
    x = Math.min(x, rightEdge - w);
    y = Math.max(y, topEdge);
    y = Math.min(y, bottomEdge - h);

    Rectangle2D rectangle = new Rectangle2D.Double(x, y, w, h);
    // RoundRectangle2D rectangle = new RoundRectangle2D.Double(x + margin, y +
    // margin, w, h, margin * 2, margin);
    boolean draw = true;
    if (preventOverlap) {
      for (Shape shape : shapeList) {
        if (shape.intersects(rectangle)) {
          draw = false;
          break;
        }
      }
      if (draw) {
        shapeList.add(rectangle);
      }
    }
    if (draw) {
      // fill background
      g.setColor(backgroundColor);
      g.fill(rectangle);

      // draw outline
      g.setColor(borderColor);
      g.draw(rectangle);

      Shape shape = textLayout.getOutline(null);
      g.setColor(textColor);
      g.setFont(textFont);
      AffineTransform orig = g.getTransform();
      AffineTransform at = new AffineTransform();
      // at.translate(annotationX, annotationY);
      at.translate(x + margin, y + margin + halfHeight);
      g.transform(at);
      g.fill(shape);
      g.setTransform(orig);

      // Rectangle2D bounds = g.getFontMetrics().getStringBounds(dataPoint.label,
      // g);
      // g.drawString(dataPoint.label, (float) (x + margin * 2), (float) (y +
      // margin * 2 + halfHeight));

    }
  }

  public MouseMotionListener getMouseMotionListener() {

    return this;
  }

  @Override
  public void mouseDragged(MouseEvent e) {

  }

  @Override
  public void mouseMoved(MouseEvent e) {

    if (!shouldShowDataLabels || !dataLabelsAsToolTips) {
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
      if (popupDataPoint != null) {
        if (popupDataPoint.equals(newPoint)) {
          return;
        }
      }
      popupDataPoint = newPoint;
      e.getComponent().repaint();
    } else if (popupDataPoint != null) {
      popupDataPoint = null;
      e.getComponent().repaint();
    }
  }

  public boolean isShouldShowDataLabels() {

    return shouldShowDataLabels;
  }

  public void setShouldShowDataLabels(boolean shouldShowDataLabels) {

    this.shouldShowDataLabels = shouldShowDataLabels;
  }

  public boolean isDataLabelsAsToolTips() {

    return dataLabelsAsToolTips;
  }

  public void setDataLabelsAsToolTips(boolean dataLabelsAsToolTips) {

    this.dataLabelsAsToolTips = dataLabelsAsToolTips;
  }

  public DataLabelContent getDataLabelContent() {

    return dataLabelContent;
  }

  public void setDataLabelContent(DataLabelContent dataLabelContent) {

    this.dataLabelContent = dataLabelContent;
  }

  public boolean isPreventOverlap() {

    return preventOverlap;
  }

  public void setPreventOverlap(boolean preventOverlap) {

    this.preventOverlap = preventOverlap;
  }

  protected static class DataPoint {

    String label;

    // used for popup detection & popup highlight
    Shape shape;

    // label center coordinates
    double x;
    double y;

    // width of data point (used for bar charts)
    double w;

    public DataPoint(double x, double y, int margin, String label) {

      double halfSize = margin * 1.5;
      double markerSize = margin * 3;

      shape = new Ellipse2D.Double(x - halfSize, y - halfSize, markerSize, markerSize);

      this.x = x;
      this.y = y;
      this.w = 0;
      this.label = label;
    }

    public DataPoint(Shape shape, double x, double y, double width, String label) {
      this.x = x;
      this.y = y;
      this.w = width;
      this.shape = shape;
      this.label = label;
    }

  }

}
