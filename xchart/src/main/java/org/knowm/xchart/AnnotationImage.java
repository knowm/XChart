package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.knowm.xchart.internal.chartpart.Annotation;
import org.knowm.xchart.internal.chartpart.Chart;

public class AnnotationImage extends AnnotationWithXY {

  // internal
  private BufferedImage image;

  /**
   * Constructor
   *
   * @param image
   * @param x
   * @param y
   * @param isValueInScreenSpace
   */
  public AnnotationImage(BufferedImage image, double x, double y, boolean isValueInScreenSpace) {
    super(x, y, isValueInScreenSpace);
    this.image = image;
  }

  public void init(Chart chart) {

    super.init(chart);
  }

  @Override
  public void paint(Graphics2D graphic) {

    if (!isVisible) {
      return;
    }

    int xOffset;
    int yOffset;

    if (isValueInScreenSpace) {
      xOffset = (int) x - image.getWidth() / 2;
      yOffset = chart.getHeight() - (int) y - image.getWidth() / 2;
    } else {
      xOffset = (int) (getXAxisScreenValue(x) + 0.5) - image.getWidth() / 2;
      yOffset = (int) (getYAxisScreenValue(y) + 0.5) - image.getHeight() / 2;
    }
    graphic.drawImage(image, xOffset, yOffset, null);

    bounds = new Rectangle2D.Double(xOffset, yOffset, image.getWidth(), image.getHeight());
  }

  public void setImage(BufferedImage image) {
    this.image = image;
  }
}
