package org.knowm.xchart.style.colors;

import java.awt.*;

/** @author timmolter */
public class ColorBlindFriendlySeriesColors implements SeriesColors {

  // The color blind friendly palette
  private static final Color BLACK = new Color(0, 0, 0, 255);
  private static final Color ORANGE = new Color(230, 159, 0, 255);
  private static final Color SKY_BLUE = new Color(86, 180, 233, 255);
  private static final Color BLUISH_GREEN = new Color(0, 158, 115, 255);
  private static final Color YELLOW = new Color(240, 228, 66, 255);
  private static final Color BLUE = new Color(0, 114, 178, 255);
  private static final Color VERMILLION = new Color(213, 94, 0, 255);
  private static final Color REDDISH_PURPLE = new Color(204, 121, 167, 255);

  private final Color[] seriesColors;

  /** Constructor */
  public ColorBlindFriendlySeriesColors() {

    seriesColors =
        new Color[] {
          BLACK, ORANGE, SKY_BLUE, BLUISH_GREEN, YELLOW, BLUE, VERMILLION, REDDISH_PURPLE
        };
  }

  @Override
  public Color[] getSeriesColors() {

    return seriesColors;
  }
}
