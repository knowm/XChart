package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.knowm.xchart.internal.chartpart.Annotation;
import org.knowm.xchart.internal.chartpart.Chart;

public class AnnotationWithXY extends Annotation {

	 protected double x;
	 protected double y;

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
  }
}
