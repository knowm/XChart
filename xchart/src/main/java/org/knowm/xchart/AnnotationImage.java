package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.chartpart.Annotation;
import org.knowm.xchart.internal.chartpart.Chart;

public class AnnotationImage extends Annotation {

  // internal
  private Image image;
  protected double x;
  protected double y;

  // internal
  private int startx;
  private int starty;

  /**
   * Constructor
   *
   * @param image
   * @param x
   * @param y
   * @param isValueInScreenSpace
   */
  public AnnotationImage(Image image, double x, double y, boolean isValueInScreenSpace) {

    this.image = image;
    this.x = x;
    this.y = y;
    this.isValueInScreenSpace = isValueInScreenSpace;
  }

  public void init(Chart chart) {

    super.init(chart);
  }

  @Override
  public Rectangle2D getBounds() {

    // TODO implement this correctly
    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    if (!isVisible) {
      return;
    }
    bounds = g.getClipBounds();

    calculatePosition();
    g.drawImage(image, startx, starty, null);
  }

  private void calculatePosition() {

    if (isValueInScreenSpace) {
      startx = (int) x;
      starty = (int) y;
    } else {
      startx = (int) (getXAxisSreenValue(x) + 0.5) - image.getWidth(null) / 2;
      starty = (int) (getYAxisSreenValue(y) + 0.5) - image.getHeight(null) / 2;
    }
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
  }
}
