package org.knowm.xchart.style.colors;

import java.awt.*;

/** @author timmolter */
public class MatlabSeriesColors implements SeriesColors {

  public static final Color BLUE = new Color(0, 0, 255, 255);
  public static final Color GREEN = new Color(0, 128, 0, 255);
  public static final Color RED = new Color(255, 0, 0, 255);
  public static final Color TURQUOISE = new Color(0, 191, 191, 255);
  public static final Color MAGENTA = new Color(191, 0, 191, 255);
  public static final Color YELLOW = new Color(191, 191, 0, 255);
  public static final Color DARK_GREY = new Color(64, 64, 64, 255);

  private final Color[] seriesColors;

  /** Constructor */
  public MatlabSeriesColors() {

    seriesColors = new Color[] {BLUE, GREEN, RED, TURQUOISE, MAGENTA, YELLOW, DARK_GREY};
  }

  @Override
  public Color[] getSeriesColors() {

    return seriesColors;
  }
}
