package org.knowm.xchart.cursors;

import java.awt.*;
import java.awt.geom.Line2D.Double;

public class Cross extends Cursor {
   public void paint(Graphics2D g, double xOffset, double yOffset, int cursorSize, double width, double height) {
      if(dataPoint == null) return;
      if(dataPoint.getX() == xOffset) {
         final BasicStroke stroke = new BasicStroke(cursorSize, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
         g.setStroke(stroke);
         g.draw(new Double(0, yOffset, width, yOffset));
         g.draw(new Double(xOffset, 0, xOffset, height));
      }
   }
}
