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
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;

/**
 * A button that can be used on the chart for whatever function. For example the ChartZoom class
 * uses this to reset the zoom function. When it is clicked it fires its actionPerformed action and
 * whoever is listening to it can react to it.
 */
// TODO tie this to the styler properties
public class ChartButton extends MouseAdapter implements ChartPart {

  private final Chart chart;
  private final Styler styler;
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

    this.text = text;

    chart = xyChart;
    styler = chart.getStyler();

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
  
  private static final int BOUND_LIMIT = 30;

  @Override
  public void paint(Graphics2D graphic) {

    if (!visible) {
      return;
    }

    bounds = graphic.getClipBounds();

    double boundsWidth = bounds.getWidth();
    if (boundsWidth < BOUND_LIMIT) {
      return;
    }

    Object oldHint = graphic.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    graphic.setColor(styler.getChartButtonFontColor());
    graphic.setFont(styler.getChartButtonFont());

    FontRenderContext fontRenderContext = graphic.getFontRenderContext();
    TextLayout textLayout = new TextLayout(text, styler.getChartButtonFont(), fontRenderContext);
    Shape shape = textLayout.getOutline(null);

    Rectangle2D textBounds = shape.getBounds2D();
    calculatePosition(textBounds);
    double textHeight = textBounds.getHeight();
    double textWidth = textBounds.getWidth();
    
    double finalTextWidth = textWidth + styler.getChartButtonMargin() * 2;
    double finalTextHeight = textHeight + styler.getChartButtonMargin() * 2;
    
    buttonRect =
        new Rectangle2D.Double(
            xOffset,
            yOffset,
            finalTextWidth,
            finalTextHeight);
    graphic.setColor(styler.getChartButtonBackgroundColor());
    graphic.fill(buttonRect);
    graphic.setStroke(SOLID_STROKE);
    graphic.setColor(styler.getChartButtonBorderColor());
    graphic.draw(buttonRect);

    double startx = xOffset + styler.getChartButtonMargin();
    double starty = yOffset + styler.getChartButtonMargin();

    graphic.setColor(styler.getChartButtonFontColor());

    AffineTransform orig = graphic.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(startx, starty + textHeight);
    graphic.transform(at);
    graphic.fill(shape);
    graphic.setTransform(orig);

    graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  private void calculatePosition(Rectangle2D textBounds) {

    double textHeight = textBounds.getHeight();
    double textWidth = textBounds.getWidth();
    double widthAdjustment = textWidth + styler.getChartButtonMargin() * 3;
    double heightAdjustment = textHeight + styler.getChartButtonMargin() * 3;

    double boundsWidth = bounds.getWidth();
    double boundsHeight = bounds.getHeight();
    
    double adjustmentedWidth = boundsWidth - widthAdjustment;
    double adjustmentedHeight = boundsHeight - heightAdjustment;
    
    switch (styler.getChartButtonPosition()) {
      case InsideNW:
        xOffset = bounds.getX() + styler.getChartButtonMargin();
        yOffset = bounds.getY() + styler.getChartButtonMargin();
        break;
      case InsideNE:
        xOffset = bounds.getX() + adjustmentedWidth;
        yOffset = bounds.getY() + styler.getChartButtonMargin();
        break;
      case InsideSE:
        xOffset = bounds.getX() + adjustmentedWidth;
        yOffset = bounds.getY() + adjustmentedHeight;
        break;
      case InsideSW:
        xOffset = bounds.getX() + styler.getChartButtonMargin();
        yOffset = bounds.getY() + adjustmentedHeight;
        break;
      case InsideN:
        xOffset = bounds.getX() + boundsWidth / 2 - textWidth / 2 - styler.getChartButtonMargin();
        yOffset = bounds.getY() + styler.getChartButtonMargin();
        break;
      case InsideS:
        xOffset = bounds.getX() + boundsWidth / 2 - textWidth / 2 - styler.getChartButtonMargin();
        yOffset = bounds.getY() + adjustmentedHeight;
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
