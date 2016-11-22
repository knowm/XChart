package org.knowm.xchart.graphics;

import org.knowm.xchart.Font;
import org.knowm.xchart.font.TextLayout;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public interface Graphics {
  void setColor(Color color);
  void setStroke(Stroke stroke);
  void setFont(Font font);
  void setClip(Shape clip);

  TextLayout getTextLayout(String text, Font font);
  RenderContext getRenderContext();

  AffineTransform getTransform();
  void setTransform(AffineTransform transform);
  void transform(AffineTransform transform);

  void fill(Shape shape);
  void draw(Shape shape);
  void drawLine(int x1, int y1, int x2, int y2);
}
