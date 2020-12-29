package org.knowm.xchart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.chartpart.Annotation;

public class AnnotationLine extends Annotation {

  //  style
  // TODO move these to an annotation styler
  private Color color = new Color(114, 147, 203);
  private BasicStroke stroke = SOLID_STROKE;
  private final boolean isVertical;
  private double value;

  /**
   * Constructor
   *
   * @param value
   * @param isVertical
   * @param isValueInScreenSpace
   */
  public AnnotationLine(double value, boolean isVertical, boolean isValueInScreenSpace) {

    this.value = value;
    this.isVertical = isVertical;
    this.isValueInScreenSpace = isValueInScreenSpace;
  }

  @Override
  public Rectangle2D getBounds() {
    // TODO implment this correctly
    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    if (!isVisible) {
      return;
    }

    bounds = g.getClipBounds();
    int x1 = 0, x2 = 0, y1 = 0, y2 = 0;

    if (isVertical) {
      y1 = (int) bounds.getY();
      y2 = (int) (bounds.getY() + bounds.getHeight());
    } else {
      x1 = (int) bounds.getX();
      x2 = (int) (bounds.getX() + bounds.getWidth());
    }

    if (isValueInScreenSpace) {
      if (isVertical) {
        x1 = (int) value;
        x2 = x1;
      } else {
        y1 = (int) value;
        y2 = y1;
      }
    } else {
      if (isVertical) {
        x1 = (int) getXAxisSreenValue(value);
        x2 = x1;
      } else {
        y1 = (int) getYAxisSreenValue(value);
        y2 = y1;
      }
    }

    g.setStroke(stroke);
    g.setColor(color);
    g.drawLine(x1, y1, x2, y2);
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public void setStroke(BasicStroke stroke) {
    this.stroke = stroke;
  }

  public void setValue(double value) {
    this.value = value;
  }
}
