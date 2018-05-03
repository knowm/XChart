package org.knowm.xchart.style.markers;

import java.awt.*;

public class Trapezoid extends Marker {
  @Override
  public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {
    g.setStroke(stroke);
    double halfSize = (double) markerSize / 2;
    Polygon polygon = new Polygon();

    for (int i = 1; i <= 4; i++) {
      polygon.addPoint(
          (int) ((xOffset) + (markerSize * 0.75) * Math.sin(i * 2 * Math.PI / 5)),
          (int)
              ((yOffset + (markerSize * 0.25))
                  + (markerSize * 0.75) * Math.cos(i * 2 * Math.PI / 5)));
    }

    g.fillPolygon(polygon);
  }
}
