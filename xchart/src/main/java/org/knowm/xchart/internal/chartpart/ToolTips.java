package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.knowm.xchart.style.BoxStyler;
import org.knowm.xchart.style.OHLCStyler;
import org.knowm.xchart.style.Styler;

// TODO make the background color the same color as the series??
/**
 * Tooltips can be put on all data points or configured to popup like a tooltip from a mouse over.
 */
public class ToolTips extends MouseAdapter implements ChartPart {

  private static final int MARGIN = 5;
  private static final int MOUSE_MARGIN = 20;

  private final Chart chart;
  private final Styler styler;

  // The tool tips and currently shown Tooltip
  private final List<ToolTip> toolTipList = new ArrayList<>();
  private ToolTip tooltip = null;

  /**
   * Constructor
   *
   * @param chart
   */
  public ToolTips(Chart chart) {

    this.chart = chart;
    this.styler = chart.getStyler();
    chart.plot.plotContent.setToolTips(this);
  }

  ////////////////////////////////////////////
  // MouseAdapter method /////////////////////
  ////////////////////////////////////////////

  @Override
  public void mouseMoved(MouseEvent e) {

    boolean isRepaint = false;

    ToolTip newPoint = getSelectedTooltip(e.getX(), e.getY());

    if (newPoint != null) {

      // if the existing shown data point is already shown, abort
      if (tooltip != null) {
        if (tooltip.equals(newPoint)) {
          isRepaint = false; // Don't need to repaint again as it's already showing
        }
      }
      tooltip = newPoint;
      isRepaint = true;

    }
    // remove the popup shape
    else if (tooltip != null) {
      tooltip = null;
      isRepaint = true;
    }

    if (isRepaint) {
      //      xChartPanel.invalidate();
      //      xChartPanel.repaint();
      e.getComponent().repaint();
    }
  }

  private ToolTip getSelectedTooltip(int x, int y) {

    // find the datapoint based on the mouse location
    ToolTip newPoint = null;
    for (ToolTip tooltip : toolTipList) {
      if (tooltip.shape.contains(x, y)) {
        newPoint = tooltip;
        break;
      }
    }
    //    System.out.println("newPoint = " + newPoint);
    return newPoint;
  }

  ////////////////////////////////////////////
  // ChartPart methods ///////////////////////
  ////////////////////////////////////////////

  @Override
  public Rectangle2D getBounds() {
    return null;
  }

  @Override
  public void paint(Graphics2D g) {

    if (styler.isToolTipsAlwaysVisible()) {
      for (ToolTip tooltip : toolTipList) {
        paintToolTip(g, tooltip);
      }
    }

    // TODO need this null check??
    if (tooltip != null) { // dataPoint was created in mouse move, need to render it
      // TODO See OHLC04. The line series are rendering as multi-line. Can we just define the
      // tooltip during creation and if it's multiline, paint it
      // as multiline??
      if (styler instanceof BoxStyler || styler instanceof OHLCStyler) {
        paintMultiLineToolTip(g);
      } else {
        paintToolTip(g, tooltip);
      }
    }
  }
  ////////////////////////////////////////////////
  /// PAINTING //////////////////////////////////
  ///////////////////////////////////////////////

  private void paintToolTip(Graphics2D g, ToolTip tooltip) {

    TextLayout textLayout =
        new TextLayout(
            tooltip.label, styler.getToolTipFont(), new FontRenderContext(null, true, false));
    Rectangle2D annotationRectangle = textLayout.getBounds();

    double w = annotationRectangle.getWidth() + 2 * MARGIN;
    double h = annotationRectangle.getHeight() + 2 * MARGIN;
    double halfHeight = h / 2;

    // TODO is this needed??
    if (tooltip == this.tooltip) {
      // not the box with label, but the shape
      // highlight shape for popup
      g.setColor(styler.getToolTipHighlightColor());
      g.fill(tooltip.shape);
    }

    //    System.out.println("paintToolTip");

    int leftEdge = (int) chart.plot.plotContent.getBounds().getX();
    int rightEdge =
        (int)
            (chart.plot.plotContent.getBounds().getX()
                + chart.plot.plotContent.getBounds().getWidth());
    int topEdge = (int) chart.plot.plotContent.getBounds().getY();
    int bottomEdge =
        (int)
            (chart.plot.plotContent.getBounds().getY()
                + chart.plot.plotContent.getBounds().getHeight());

    double x = tooltip.x + tooltip.w / 2 - annotationRectangle.getWidth() / 2 - MARGIN;
    double y = tooltip.y - 3 * MARGIN - annotationRectangle.getHeight();

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

  private void paintMultiLineToolTip(Graphics2D g) {

    String[] texts = tooltip.label.split(System.lineSeparator());
    List<TextLayout> list = new ArrayList<>();
    TextLayout textLayout = null;
    Rectangle2D bounds = null;
    double backgroundHeight = MARGIN;
    double backgroundWidth = 0;
    for (String text : texts) {
      textLayout =
          new TextLayout(text, styler.getToolTipFont(), new FontRenderContext(null, true, false));
      bounds = textLayout.getBounds();
      bounds.getHeight();
      if (backgroundWidth < bounds.getWidth()) {
        backgroundWidth = bounds.getWidth();
      }
      backgroundHeight += styler.getToolTipFont().getSize() + MARGIN;
      list.add(textLayout);
    }

    //    System.out.println("paintMultiLineToolTip");

    Rectangle clipBounds = g.getClipBounds();
    double startX = tooltip.x;
    double startY = tooltip.y;
    if (tooltip.x + MOUSE_MARGIN + backgroundWidth > clipBounds.getX() + clipBounds.getWidth()) {
      startX = tooltip.x - backgroundWidth - MOUSE_MARGIN;
    }

    if (tooltip.y + MOUSE_MARGIN + backgroundHeight > clipBounds.getY() + clipBounds.getHeight()) {
      startY = tooltip.y - backgroundHeight - MOUSE_MARGIN;
    }

    g.setColor(styler.getToolTipBackgroundColor());
    g.fillRect(
        (int) startX + MOUSE_MARGIN,
        (int) startY + MOUSE_MARGIN,
        (int) (backgroundWidth) + 2 * MARGIN,
        (int) (backgroundHeight));

    AffineTransform orig = g.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(
        startX + MOUSE_MARGIN + MARGIN,
        startY + textLayout.getBounds().getHeight() + MOUSE_MARGIN + MARGIN);
    g.transform(at);
    // TODO make a fontcolor for tooltips in styler
    g.setColor(styler.getChartFontColor());
    g.setFont(styler.getToolTipFont());
    for (TextLayout t : list) {
      g.fill(t.getOutline(null));
      at = new AffineTransform();
      at.translate(0, styler.getToolTipFont().getSize() + MARGIN);
      g.transform(at);
    }
    g.setTransform(orig);
  }

  // Adding Tooltips ////////////////////////////

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
  void addData(double xOffset, double yOffset, String label) {

    ToolTip toolTip = new ToolTip(xOffset, yOffset, label);
    toolTipList.add(toolTip);
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

    ToolTip toolTip = new ToolTip(shape, xOffset, yOffset, width, label);
    toolTipList.add(toolTip);
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

  public void clearData() {
    toolTipList.clear();
  }

  static class ToolTip {

    // width of data point (used for bar charts)
    // TODO possibly delete this
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
    ToolTip(double x, double y, String label) {

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
    ToolTip(Shape shape, double x, double y, double width, String label) {

      this.x = x;
      this.y = y;
      this.w = width;
      this.shape = shape;
      this.label = label;
    }

    @Override
    public String toString() {
      return "DataPoint{"
          + "w="
          + w
          + ", label='"
          + label
          + '\''
          + ", shape="
          + shape
          + ", x="
          + x
          + ", y="
          + y
          + '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ToolTip tooltip = (ToolTip) o;
      return label.equals(tooltip.label) && shape.equals(tooltip.shape);
    }

    @Override
    public int hashCode() {
      return Objects.hash(label, shape);
    }
  }
}
