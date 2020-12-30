package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.knowm.xchart.internal.chartpart.Annotation;
import org.knowm.xchart.internal.chartpart.Chart;

public class AnnotationImage extends Annotation {

  // internal
  private BufferedImage image;
  protected double x;
  protected double y;

  /**
   * Constructor
   *
   * @param image
   * @param x
   * @param y
   * @param isValueInScreenSpace
   */
  public AnnotationImage(BufferedImage image, double x, double y, boolean isValueInScreenSpace) {

    this.image = image;
    this.x = x;
    this.y = y;
    this.isValueInScreenSpace = isValueInScreenSpace;
  }

  public void init(Chart chart) {

    super.init(chart);
  }

  @Override
  public void paint(Graphics2D g) {

    if (!isVisible) {
      return;
    }

    int xOffset;
    int yOffset;

    if (isValueInScreenSpace) {
      xOffset = (int) x - image.getWidth() / 2;
      yOffset = chart.getHeight() - (int) y - image.getWidth() / 2;
    } else {
      xOffset = (int) (getXAxisSreenValue(x) + 0.5) - image.getWidth() / 2;
      yOffset = (int) (getYAxisSreenValue(y) + 0.5) - image.getHeight() / 2;
    }
    g.drawImage(image, xOffset, yOffset, null);

    bounds = new Rectangle2D.Double(xOffset, yOffset, image.getWidth(), image.getHeight());
  }

  public void setImage(BufferedImage image) {
    this.image = image;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
  }
}
