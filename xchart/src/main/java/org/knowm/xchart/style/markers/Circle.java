package org.knowm.xchart.style.markers;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/** @author timmolter */
public class Circle extends Marker {

  @Override
  public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {

    g.setStroke(stroke);
    double halfSize = (double) markerSize / 2;
    Shape circle =
        new Ellipse2D.Double(xOffset - halfSize, yOffset - halfSize, markerSize, markerSize);
    g.fill(circle);
  }
}
