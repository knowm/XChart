package org.knowm.xchart;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.chartpart.Annotation;
import org.knowm.xchart.internal.chartpart.Chart;

public class AnnotationText extends Annotation {

  // style
  private Color fontColor;
  private Font textFont;

  private String text;

  // internal
  private double startx;
  private double starty;

  /**
   * Constructor
   *
   * @param text
   * @param x
   * @param y
   * @param isValueInScreenSpace
   */
  public AnnotationText(String text, double x, double y, boolean isValueInScreenSpace) {

    this.text = text;
    this.x = x;
    this.y = y;
    this.isValueInScreenSpace = isValueInScreenSpace;
  }

  public void init(Chart chart) {

    super.init(chart);

    // TODO move these to an annotation styler
    if (fontColor == null) {
      fontColor = chart.getStyler().getChartFontColor();
    }
    if (textFont == null) {
      textFont = chart.getStyler().getLegendFont();
    }
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    if (!isVisible) {
      return;
    }
    // TODO implment this correctly
    bounds = g.getClipBounds();

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g.setColor(fontColor);
    g.setFont(textFont);

    FontRenderContext frc = g.getFontRenderContext();
    TextLayout tl = new TextLayout(text, textFont, frc);
    Shape shape = tl.getOutline(null);

    Rectangle2D textBounds = shape.getBounds2D();
    calculatePosition(textBounds);

    AffineTransform orig = g.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(startx, starty);
    g.transform(at);
    g.fill(shape);
    g.setTransform(orig);

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
  }

  protected void calculatePosition(Rectangle2D textBounds) {

    if (isValueInScreenSpace) {
      startx = x;
      starty = y;
    } else {
      startx = getXAxisSreenValue(x) - textBounds.getWidth() / 2;
      starty = getYAxisSreenValue(y) + textBounds.getHeight() / 2;
    }
  }

  public void setFontColor(Color fontColor) {
    this.fontColor = fontColor;
  }

  public void setTextFont(Font textFont) {
    this.textFont = textFont;
  }

  public void setText(String text) {
    this.text = text;
  }
}
