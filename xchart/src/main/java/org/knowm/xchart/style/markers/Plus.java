package org.knowm.xchart.style.markers;

import java.awt.*;
import java.awt.geom.Path2D;

public class Plus extends Marker {

  @Override
  public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {

    g.setStroke(stroke);

    // Make a cross
    double halfSize = (double) markerSize / 2;

    Path2D.Double path = new Path2D.Double();
    path.moveTo(xOffset + halfSize, yOffset);
    path.lineTo(xOffset - halfSize, yOffset);
    path.moveTo(xOffset, yOffset + halfSize);
    path.lineTo(xOffset, yOffset - halfSize);
    g.draw(path);
  }
}
