package org.knowm.xchart.font;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public interface TextLayout {
  Rectangle2D getBounds();
  Shape getOutline(AffineTransform transform);
}
