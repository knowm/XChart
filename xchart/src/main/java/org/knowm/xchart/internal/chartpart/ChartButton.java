package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.event.EventListenerList;

import org.knowm.xchart.ChartButtonConfig;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler;

/**
 * A button that can be used on the chart for whatever function. For example the ChartZoom class
 * uses this to reset the zoom function. When it is clicked it fires its actionPerformed action and
 * whoever is listening to it can react to it.
 */
public class ChartButton extends MouseAdapter implements ChartPart {

  private final Styler styler;
  private final ChartButtonConfig chartButtonConfig;
  private Rectangle bounds;

  // properties

  protected String text;
  boolean visible = true;

  private ActionEvent action;
  private final EventListenerList listenerList = new EventListenerList();

  protected double xOffset = 0;
  protected double yOffset = 0;

  // internal
  private Shape buttonRect;

  /**
   * Constructor
   *
   * @param xyChart
   * @param xChartPanel
   * @param text
   */
  public ChartButton(XYChart xyChart, XChartPanel<XYChart> xChartPanel, String text) {

    this(xyChart, (XChartPanel<?>) xChartPanel, text);
  }

  /**
   * Constructor
   *
   * @param chart
   * @param xChartPanel
   * @param text
   */
  public ChartButton(
      Chart<? extends AxesChartStyler, ?> chart, XChartPanel<?> xChartPanel, String text) {

    this.text = text;

    styler = chart.getStyler();
    chartButtonConfig = xChartPanel.getChartButtonConfig();

    xChartPanel.addMouseListener(this);
    xChartPanel.addMouseMotionListener(this);
  }

  public void addActionListener(ActionListener l) {

    listenerList.add(ActionListener.class, l);
  }

  public void removeActionListener(ActionListener l) {

    listenerList.remove(ActionListener.class, l);
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void mouseClicked(MouseEvent e) {

    if (!visible) {
      return;
    }

    if (buttonRect == null) {
      return;
    }
    if (buttonRect.contains(e.getX(), e.getY())) {
      fireActionPerformed();
    }
  }

  /** Notify listeners that this button was clicked or interacted with in some way */
  private void fireActionPerformed() {

    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (action == null) {
        action = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
      }

      ((ActionListener) listeners[i + 1]).actionPerformed(action);
    }
  }

  @Override
  public void paint(Graphics2D g) {

    if (!visible) {
      return;
    }

    bounds = g.getClipBounds();

    double boundsWidth = bounds.getWidth();
    if (boundsWidth < 30) {
      return;
    }

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        styler.getTextAntiAlias()
            ? RenderingHints.VALUE_ANTIALIAS_ON
            : RenderingHints.VALUE_ANTIALIAS_OFF);

    g.setColor(chartButtonConfig.getFontColor());
    g.setFont(chartButtonConfig.getFont());

    FontRenderContext frc = g.getFontRenderContext();
    TextLayout tl = new TextLayout(text, chartButtonConfig.getFont(), frc);
    Shape shape = tl.getOutline(null);

    Rectangle2D textBounds = shape.getBounds2D();
    calculatePosition(textBounds);
    double textHeight = textBounds.getHeight();
    double textWidth = textBounds.getWidth();

    buttonRect =
        new Rectangle2D.Double(
            xOffset,
            yOffset,
            textWidth + chartButtonConfig.getMargin() * 2,
            textHeight + chartButtonConfig.getMargin() * 2);
    g.setColor(chartButtonConfig.getBackgroundColor());
    g.fill(buttonRect);
    g.setStroke(SOLID_STROKE);
    g.setColor(chartButtonConfig.getBorderColor());
    g.draw(buttonRect);

    double startx = xOffset + chartButtonConfig.getMargin();
    double starty = yOffset + chartButtonConfig.getMargin();

    g.setColor(chartButtonConfig.getFontColor());

    AffineTransform orig = g.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(startx, starty + textHeight);
    g.transform(at);
    g.fill(shape);
    g.setTransform(orig);

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  private void calculatePosition(Rectangle2D textBounds) {

    double textHeight = textBounds.getHeight();
    double textWidth = textBounds.getWidth();
    double widthAdjustment = textWidth + chartButtonConfig.getMargin() * 3;
    double heightAdjustment = textHeight + chartButtonConfig.getMargin() * 3;

    double boundsWidth = bounds.getWidth();
    double boundsHeight = bounds.getHeight();

    switch (chartButtonConfig.getPosition()) {
      case InsideNW:
        xOffset = bounds.getX() + chartButtonConfig.getMargin();
        yOffset = bounds.getY() + chartButtonConfig.getMargin();
        break;
      case InsideNE:
        xOffset = bounds.getX() + boundsWidth - widthAdjustment;
        yOffset = bounds.getY() + chartButtonConfig.getMargin();
        break;
      case InsideSE:
        xOffset = bounds.getX() + boundsWidth - widthAdjustment;
        yOffset = bounds.getY() + boundsHeight - heightAdjustment;
        break;
      case InsideSW:
        xOffset = bounds.getX() + chartButtonConfig.getMargin();
        yOffset = bounds.getY() + boundsHeight - heightAdjustment;
        break;
      case InsideN:
        xOffset =
            bounds.getX() + boundsWidth / 2 - textWidth / 2 - chartButtonConfig.getMargin();
        yOffset = bounds.getY() + chartButtonConfig.getMargin();
        break;
      case InsideS:
        xOffset =
            bounds.getX() + boundsWidth / 2 - textWidth / 2 - chartButtonConfig.getMargin();
        yOffset = bounds.getY() + boundsHeight - heightAdjustment;
        break;
      default:
        break;
    }
  }

  //   SETTERS

  void setText(String text) {

    this.text = text;
  }

  void setVisible(boolean visible) {

    this.visible = visible;
  }
}
