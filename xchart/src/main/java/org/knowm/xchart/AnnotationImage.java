package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.knowm.xchart.internal.chartpart.Annotation;
import org.knowm.xchart.internal.chartpart.AxesChart;

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
    super(isValueInScreenSpace);
    this.image = image;
    this.x = x;
    this.y = y;
  }

  public void init(AxesChart<?, ?> chart) {

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
      yOffset = chart.getHeight() - (int) y - image.getHeight() / 2;
    } else {
      xOffset = (int) (getXAxisScreenValue(x) + 0.5) - image.getWidth() / 2;
      yOffset = (int) (getYAxisScreenValue(y) + 0.5) - image.getHeight() / 2;
    }
    g.drawImage(image, xOffset, yOffset, null);

    bounds = new Rectangle2D.Double(xOffset, yOffset, image.getWidth(), image.getHeight());
  }

  public AnnotationImage setImage(BufferedImage image) {
    this.image = image;
    return this;
  }

  public AnnotationImage setX(double x) {
    this.x = x;
    return this;
  }

  public AnnotationImage setY(double y) {
    this.y = y;
    return this;
  }

  // Covariant overrides so base-class setters can appear anywhere in a chain

  @Override
  public AnnotationImage setYAxisGroup(int yAxisGroup) {

    super.setYAxisGroup(yAxisGroup);
    return this;
  }

  @Override
  public AnnotationImage setVisible(boolean visible) {

    super.setVisible(visible);
    return this;
  }
}
