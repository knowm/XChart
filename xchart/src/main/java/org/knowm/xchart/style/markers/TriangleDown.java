package org.knowm.xchart.style.markers;

import java.awt.*;
import java.awt.geom.Path2D;

/** @author timmolter */
public class TriangleDown extends Marker {

  @Override
  public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {

    g.setStroke(stroke);
    double halfSize = (double) markerSize / 2;

    // Make a triangle
    Path2D.Double path = new Path2D.Double();
    path.moveTo(xOffset - halfSize, 1 + yOffset - halfSize);
    path.lineTo(xOffset, 1 + yOffset - halfSize + markerSize);
    path.lineTo(xOffset - halfSize + markerSize, 1 + yOffset - halfSize);
    path.closePath();
    g.fill(path);
  }
}
