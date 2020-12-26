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

  // button position
  protected CardinalPosition position = CardinalPosition.InsideN;

  protected double xOffset = 0;
  protected double yOffset = 0;

  // internal
  private Shape buttonRect;
  private boolean mouseOver;

  /**
   * Constructor
   *
   * @param text
   */
  public ChartButton(String text) {

    this.text = text;
  }

  protected EventListenerList listenerList = new EventListenerList();

  public void addActionListener(ActionListener l) {

    listenerList.add(ActionListener.class, l);
  }

  public void removeActionListener(ActionListener l) {

    listenerList.remove(ActionListener.class, l);
  }

  protected void fireActionPerformed() {

    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (action == null) {
        action = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
      }

      ((ActionListener) listeners[i + 1]).actionPerformed(action);
    }
  }

  // TODO get rid of init methods
  public void init(XChartPanel<XYChart> chartPanel) {

    this.xChartPanel = chartPanel;
    chart = chartPanel.getChart();
    if (fontColor == null) {
      fontColor = chart.getStyler().getChartFontColor();
    }
    if (textFont == null) {
      textFont = chart.getStyler().getLegendFont();
    }
    if (borderColor == null) {
      borderColor = chart.getStyler().getLegendBorderColor();
    }
    chartPanel.addMouseListener(this);
    chartPanel.addMouseMotionListener(this);
    chart.addPlotPart(this);
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

  @Override
  public void mouseMoved(MouseEvent e) {

    if (!visible) {
      return;
    }
    if (buttonRect == null) {
      return;
    }
    if (buttonRect.contains(e.getX(), e.getY())) {
      mouseOver = true;
      repaint();
    } else {
      if (mouseOver) {
        mouseOver = false;
        repaint();
      }
    }
  }

  protected void repaint() {

    xChartPanel.invalidate();
    xChartPanel.repaint();
  }

  protected void calculatePosition(Rectangle2D textBounds) {

    double textHeight = textBounds.getHeight();
    double textWidth = textBounds.getWidth();
    double widthAdjustment = textWidth + margin * 3;
    double heightAdjustment = textHeight + margin * 3;

    double boundsWidth = bounds.getWidth();
    double boundsHeight = bounds.getHeight();

    if (position != null) {
      switch (position) {
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

        default:
          break;
      }
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

  public Color getColor() {

    return color;
  }

  public void setColor(Color color) {

    this.color = color;
  }

  public Color getHoverColor() {

    return hoverColor;
  }

  public void setHoverColor(Color hoverColor) {

    this.hoverColor = hoverColor;
  }

  public String getText() {

    return text;
  }

  public void setText(String text) {

    this.text = text;
  }

  public boolean isVisible() {

    return visible;
  }

  public void setVisible(boolean visible) {

    this.visible = visible;
    if (!visible) {
      mouseOver = false;
    }
  }

  Color getFontColor() {

    return fontColor;
  }

  void setFontColor(Color fontColor) {

    this.fontColor = fontColor;
  }

  Font getTextFont() {

    return textFont;
  }

  void setTextFont(Font textFont) {

    this.textFont = textFont;
  }

  Color getBorderColor() {

    return borderColor;
  }

  void setBorderColor(Color borderColor) {

    this.borderColor = borderColor;
  }

  int getMargin() {

    return margin;
  }

  void setMargin(int margin) {

    this.margin = margin;
  }

  ActionEvent getAction() {

    return action;
  }

  void setAction(ActionEvent action) {

    this.action = action;
  }

  double getxOffset() {

    return xOffset;
  }

  void setxOffset(double xOffset) {

    this.xOffset = xOffset;
  }

  double getyOffset() {

    return yOffset;
  }

  void setyOffset(double yOffset) {

    this.yOffset = yOffset;
  }

  void setPosition(CardinalPosition position) {
    this.position = position;
  }
}
