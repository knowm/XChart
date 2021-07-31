package org.knowm.xchart.style.markers;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Oval extends Marker {

  @Override
  public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {
    g.setStroke(stroke);
    markerSize = (int) Math.ceil(markerSize * 1.25);
    final double halfSize = markerSize / 2.0;

    Shape circle =
        new Ellipse2D.Double(xOffset - (halfSize / 2), yOffset - halfSize, halfSize, markerSize);
    g.fill(circle);
  }
}
