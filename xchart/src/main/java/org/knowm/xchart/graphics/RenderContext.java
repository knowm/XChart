package org.knowm.xchart.graphics;

import org.knowm.xchart.Font;
import org.knowm.xchart.font.TextLayout;

public interface RenderContext {
  TextLayout getTextLayout(String text, Font font);
}
