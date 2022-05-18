package org.knowm.xchart.style.markers;

import java.awt.*;

public class Trapezoid extends Marker {
  private static final int NumberOfPoints = 4;

@Override
  public void paint(Graphics2D graphic, double xOffset, double yOffset, int markerSize) {
    graphic.setStroke(stroke);
    double halfSize = (double) markerSize / 2;
    Polygon polygon = new Polygon();

    for (int i = 1; i <= NumberOfPoints; i++) {
      polygon.addPoint(calculateAddedX(xOffset, markerSize, i), calculateAddedY(yOffset, markerSize, i));
    }

    graphic.fillPolygon(polygon);
  }

	public int calculateAddedY(double yOffset, int markerSize, int i) {
		return (int) ((yOffset + (markerSize * 0.25))
		          + (markerSize * 0.75) * Math.cos(i * 2 * Math.PI / 5));
	}

	public int calculateAddedX(double xOffset, int markerSize, int i) {
		return (int) ((xOffset) + (markerSize * 0.75) * Math.sin(i * 2 * Math.PI / 5));
	}
}
