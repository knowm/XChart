package org.knowm.xchart.font;

import org.knowm.xchart.Font;

public class Java2DFontFactory implements FontFactory<java.awt.Font> {
  @Override
  public java.awt.Font get(Font font) {
    return new java.awt.Font(font.getName(), font.getStyle(), font.getSize());
  }
}
