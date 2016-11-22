package org.knowm.xchart.graphics;

import org.knowm.xchart.Font;
import org.knowm.xchart.font.FontFactory;
import org.knowm.xchart.font.Java2DTextLayout;
import org.knowm.xchart.font.TextLayout;

import java.awt.font.FontRenderContext;

public class Java2DRenderContext implements RenderContext {
  private final FontFactory<java.awt.Font> fontFactory;

  Java2DRenderContext(FontFactory<java.awt.Font> fontFactory) {
    this.fontFactory = fontFactory;
  }

  @Override
  public TextLayout getTextLayout(String text, Font font) {
    return new Java2DTextLayout(text, fontFactory.get(font), new FontRenderContext(null, true, false));
  }
}
