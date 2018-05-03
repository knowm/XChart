package org.knowm.xchart.style.markers;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Rectangle extends Marker {

  @Override
  public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {
    g.setStroke(stroke);
    double halfSize = (double) markerSize / 2;

    Shape square =
        new Rectangle2D.Double(xOffset - (halfSize / 2), yOffset - halfSize, halfSize, markerSize);
    g.fill(square);
  }
}
