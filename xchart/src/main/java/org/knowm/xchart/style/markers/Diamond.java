package org.knowm.xchart.style.markers;

import java.awt.*;
import java.awt.geom.Path2D;

/** @author timmolter */
public class Diamond extends Marker {

  @Override
  public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {

    g.setStroke(stroke);

    // Make a diamond
    double diamondHalfSize = (double) markerSize / 2 * 1.3;

    Path2D.Double path = new Path2D.Double();
    path.moveTo(xOffset - diamondHalfSize, yOffset);
    path.lineTo(xOffset, yOffset - diamondHalfSize);
    path.lineTo(xOffset + diamondHalfSize, yOffset);
    path.lineTo(xOffset, yOffset + diamondHalfSize);
    path.closePath();
    g.fill(path);
  }
}
