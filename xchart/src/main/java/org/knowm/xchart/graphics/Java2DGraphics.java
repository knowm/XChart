package org.knowm.xchart.graphics;

import org.knowm.xchart.Font;
import org.knowm.xchart.font.FontFactory;
import org.knowm.xchart.font.Java2DFontFactory;
import org.knowm.xchart.font.TextLayout;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public class Java2DGraphics implements Graphics {
  private final Graphics2D g;
  private final FontFactory<java.awt.Font> fontFactory = new Java2DFontFactory();

  public Java2DGraphics(Graphics2D g2d) {
    this.g = g2d;
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // global rendering hint
  }

  @Override
  public void setColor(Color color) {
    g.setColor(color);
  }

  @Override
  public void setStroke(Stroke stroke) {
    g.setStroke(stroke);
  }

  @Override
  public void fill(Shape shape) {
    g.fill(shape);
  }

  @Override
  public void draw(Shape shape) {
    g.draw(shape);
  }

  @Override
  public AffineTransform getTransform() {
    return g.getTransform();
  }

  @Override
  public void setTransform(AffineTransform transform) {
    g.setTransform(transform);
  }

  @Override
  public void transform(AffineTransform transform) {
    g.transform(transform);
  }

  @Override
  public void setFont(Font font) {
    g.setFont(fontFactory.get(font));
  }

  @Override
  public void drawLine(int x1, int y1, int x2, int y2) {
    g.drawLine(x1, y1, x2, y2);
  }

  @Override
  public void setClip(Shape clip) {
    g.setClip(clip);
  }

  @Override
  public RenderContext getRenderContext() {
    return new Java2DRenderContext(fontFactory);
  }

  @Override
  public TextLayout getTextLayout(String text, Font font) {
    return getRenderContext().getTextLayout(text, font);
  }
}
