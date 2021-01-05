package org.knowm.xchart.style.colors;

import java.awt.Color;

public class FontColorDetector {

  private static final int BRIGHTNESS_THRESHOLD = 130;
  private static final double RED_FACTOR = .241;
  private static final double GREEN_FACTOR = .587;
  private static final double BLUE_FACTOR = .114;

  public static Color getAutomaticFontColor(
      Color backgroundColor, Color darkForegroundColor, Color lightForegroundColor) {
    double backgroundColorPerceivedBrightness =
        Math.sqrt(
            Math.pow(backgroundColor.getRed(), 2) * RED_FACTOR
                + Math.pow(backgroundColor.getGreen(), 2) * GREEN_FACTOR
                + Math.pow(backgroundColor.getBlue(), 2) * BLUE_FACTOR);
    return backgroundColorPerceivedBrightness < BRIGHTNESS_THRESHOLD
        ? lightForegroundColor
        : darkForegroundColor;
  }
}
