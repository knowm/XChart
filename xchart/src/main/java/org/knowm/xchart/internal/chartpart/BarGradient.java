package org.knowm.xchart.internal.chartpart;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;

/**
 * Shared two-color bar gradient used by the category and horizontal bar plots and their legends.
 * The gradient runs from the series fill at the bar base to {@link #endColor(Color)} at the value
 * end.
 */
final class BarGradient {

  /** Share of white blended into the fill to get the value-end color. */
  static final float END_COLOR_WHITE_BLEND = 0.4f;

  private static final String VECTOR_GRAPHICS_PACKAGE = "de.erichseifert.vectorgraphics2d";

  private BarGradient() {}

  /**
   * The value-end color: {@code fill} blended toward white, alpha kept. Unlike {@link
   * Color#brighter()}, this also lightens saturated and black fills such as {@link Color#RED} or
   * {@link Color#BLACK}. Only a white fill has no lighter end.
   */
  static Color endColor(Color fill) {

    return lerp(fill, Color.WHITE, END_COLOR_WHITE_BLEND);
  }

  /**
   * Color of the gradient at {@code fraction} of the way from the base (0) to the value end (1),
   * clamped to that range.
   */
  static Color colorAt(Color fill, double fraction) {

    double t = Math.max(0.0, Math.min(1.0, fraction));
    return lerp(fill, endColor(fill), t);
  }

  /** True when the bar grows toward more negative transformed values. */
  static boolean extendsNegative(int chartForm, double transformed) {

    if (chartForm > 0) {
      return false;
    }
    if (chartForm < 0) {
      return true;
    }
    return transformed < 0.0;
  }

  /**
   * Fills {@code shape} with {@code fill}, or with a non-cyclic gradient from {@code fill} at the
   * base to {@link #endColor(Color)} at the value end. Coincident endpoints keep the flat fill, as
   * do vector backends, whose writers fill from the last {@code setColor} and drop a {@link
   * GradientPaint}. The paint in effect before a gradient fill is restored.
   */
  static void fill(
      Graphics2D g,
      Shape shape,
      Color fill,
      boolean gradient,
      double baseX,
      double baseY,
      double valueX,
      double valueY) {

    if (gradient && fill != null && !isVectorGraphics(g)) {
      float x1 = (float) baseX;
      float y1 = (float) baseY;
      float x2 = (float) valueX;
      float y2 = (float) valueY;
      if (x1 != x2 || y1 != y2) {
        Paint previousPaint = g.getPaint();
        g.setPaint(new GradientPaint(x1, y1, fill, x2, y2, endColor(fill), false));
        g.fill(shape);
        g.setPaint(previousPaint);
        return;
      }
    }
    g.setColor(fill);
    g.fill(shape);
  }

  static double clamp(double value, double min, double max) {

    return Math.max(min, Math.min(max, value));
  }

  private static boolean isVectorGraphics(Graphics2D g) {

    return g.getClass().getName().startsWith(VECTOR_GRAPHICS_PACKAGE);
  }

  private static Color lerp(Color from, Color to, double t) {

    int r = (int) Math.round(from.getRed() + (to.getRed() - from.getRed()) * t);
    int g = (int) Math.round(from.getGreen() + (to.getGreen() - from.getGreen()) * t);
    int b = (int) Math.round(from.getBlue() + (to.getBlue() - from.getBlue()) * t);
    return new Color(r, g, b, from.getAlpha());
  }
}
