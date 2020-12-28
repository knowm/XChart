package org.knowm.xchart.internal.chartpart;

import java.awt.Color;
import java.awt.Font;
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
import org.knowm.xchart.style.Styler.CardinalPosition;

/**
 * A button that can be used on the chart for whatever function. For example the ChartZoom class
 * uses this to reset the zoom function. When it is clicked it fires it's actionPerformed action and
 * whoever is listening to it can react to it.
 */
public class ChartButton extends MouseAdapter implements ChartPart {

  protected XChartPanel xChartPanel;
  protected Chart chart;
  protected Rectangle bounds;

  // properties
  protected Color color = new Color(114, 147, 203);
  protected Color hoverColor = new Color(57, 106, 177);
  protected String text;
  protected boolean visible = true;
  protected Color fontColor;
  protected Font textFont;
  protected Color borderColor;
  protected int margin = 6;
  protected ActionEvent action;
  private EventListenerList listenerList = new EventListenerList();

  // button position
  protected CardinalPosition cardinalPosition = CardinalPosition.InsideN;

  protected double xOffset = 0;
  protected double yOffset = 0;

  // internal
  private Shape buttonRect;
  private boolean mouseOver;

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
    if (fontColor == null) {
      fontColor = chart.getStyler().getChartFontColor();
    }
    if (textFont == null) {
      textFont = chart.getStyler().getLegendFont();
    }
    if (borderColor == null) {
      borderColor = chart.getStyler().getLegendBorderColor();
    }
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
    //    System.out.println("PAINT BUTTOM");

    bounds = g.getClipBounds();

    double boundsWidth = bounds.getWidth();
    if (boundsWidth < 30) {
      return;
    }

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g.setColor(fontColor);
    g.setFont(textFont);

    FontRenderContext frc = g.getFontRenderContext();
    TextLayout tl = new TextLayout(text, textFont, frc);
    Shape shape = tl.getOutline(null);

    Rectangle2D textBounds = shape.getBounds2D();
    calculatePosition(textBounds);
    double textHeight = textBounds.getHeight();
    double textWidth = textBounds.getWidth();

    buttonRect =
        new Rectangle2D.Double(xOffset, yOffset, textWidth + margin * 2, textHeight + margin * 2);
    if (mouseOver) {
      g.setColor(hoverColor);
    } else {
      g.setColor(color);
    }
    g.fill(buttonRect);
    g.setStroke(SOLID_STROKE);
    g.setColor(borderColor);
    g.draw(buttonRect);

    double startx = xOffset + margin;
    double starty = yOffset + margin;

    g.setColor(fontColor);

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
    double widthAdjustment = textWidth + margin * 3;
    double heightAdjustment = textHeight + margin * 3;

    double boundsWidth = bounds.getWidth();
    double boundsHeight = bounds.getHeight();

    if (cardinalPosition != null) {
      switch (cardinalPosition) {
        case InsideNW:
          xOffset = bounds.getX() + margin;
          yOffset = bounds.getY() + margin;
          break;
        case InsideNE:
          xOffset = bounds.getX() + boundsWidth - widthAdjustment;
          yOffset = bounds.getY() + margin;
          break;
        case InsideSE:
          xOffset = bounds.getX() + boundsWidth - widthAdjustment;
          yOffset = bounds.getY() + boundsHeight - heightAdjustment;
          break;
        case InsideSW:
          xOffset = bounds.getX() + margin;
          yOffset = bounds.getY() + boundsHeight - heightAdjustment;
          break;
        case InsideN:
          xOffset = bounds.getX() + boundsWidth / 2 - textWidth / 2 - margin;
          yOffset = bounds.getY() + margin;
          break;
        case InsideS:
          xOffset = bounds.getX() + boundsWidth / 2 - textWidth / 2 - margin;
          yOffset = bounds.getY() + boundsHeight - heightAdjustment;
          break;
        case OutsideE:
        case OutsideS:
          throw new IllegalArgumentException("Button cannot be placed outside of plot area!!!");
        default:
          break;
      }
    }
  }

  //   SETTERS

  public void setColor(Color color) {

    this.color = color;
  }

  public void setHoverColor(Color hoverColor) {

    this.hoverColor = hoverColor;
  }

  public void setText(String text) {

    this.text = text;
  }

  public void setVisible(boolean visible) {

    this.visible = visible;
    if (!visible) {
      mouseOver = false;
    }
  }

  void setFontColor(Color fontColor) {

    this.fontColor = fontColor;
  }

  void setTextFont(Font textFont) {

    this.textFont = textFont;
  }

  void setBorderColor(Color borderColor) {

    this.borderColor = borderColor;
  }

  void setMargin(int margin) {

    this.margin = margin;
  }

  void setCardinalPosition(CardinalPosition cardinalPosition) {
    this.cardinalPosition = cardinalPosition;
  }
}
