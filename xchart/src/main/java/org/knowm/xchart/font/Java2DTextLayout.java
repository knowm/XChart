package org.knowm.xchart.font;

import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Java2DTextLayout implements TextLayout {
  private final java.awt.font.TextLayout textLayout;

  public Java2DTextLayout(String text, java.awt.Font font, FontRenderContext frc) {
    this.textLayout = new java.awt.font.TextLayout(text, font, frc);
  }

  @Override
  public Rectangle2D getBounds() {
    return textLayout.getBounds();
  }

  @Override
  public Shape getOutline(AffineTransform transform) {
    return textLayout.getOutline(transform);
  }
}
