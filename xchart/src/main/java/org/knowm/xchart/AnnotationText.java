package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.chartpart.Annotation;

public class AnnotationText extends Annotation {

  private String text;
  protected double x;
  protected double y;

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

  @Override
  public void paint(Graphics2D g) {

    if (!isVisible) {
      return;
    }

    Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g.setColor(styler.getAnnotationTextFontColor());
    g.setFont(styler.getAnnotationTextFont());

    FontRenderContext frc = g.getFontRenderContext();
    TextLayout tl = new TextLayout(text, styler.getAnnotationTextFont(), frc);
    Shape shape = tl.getOutline(null);

    Rectangle2D textBounds = shape.getBounds2D();

    double xOffset;
    double yOffset;

    if (isValueInScreenSpace) {
      xOffset = x - textBounds.getWidth() / 2;
      yOffset = chart.getHeight() - y + textBounds.getHeight() / 2;
    } else {
      xOffset = getXAxisScreenValue(x) - textBounds.getWidth() / 2;
      yOffset = getYAxisScreenValue(y) + textBounds.getHeight() / 2;
    }

    AffineTransform orig = g.getTransform();
    AffineTransform at = new AffineTransform();
    at.translate(xOffset, yOffset);
    g.transform(at);
    g.fill(shape);
    g.setTransform(orig);

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);

    bounds =
        new Rectangle2D.Double(xOffset, yOffset, textBounds.getWidth(), textBounds.getHeight());
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
  }
}
