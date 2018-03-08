package org.knowm.xchart.style.markers;

import java.awt.*;

public class Pentagon extends Marker {
    @Override
    public void paint(Graphics2D g, double xOffset, double yOffset, int markerSize) {
        g.setStroke(stroke);
        double halfSize = (double) markerSize / 2;
        Polygon polygon = new Polygon();

        for (int i = 0; i < 5; i++) {
            polygon.addPoint((int) ((xOffset - halfSize) + markerSize * Math.cos(i * 2 * Math.PI / 5)),
                    (int) ((yOffset - halfSize) + markerSize * Math.sin(i * 2 * Math.PI / 5)));
        }

        g.fillPolygon(polygon);
    }
}
