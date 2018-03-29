package org.knowm.xchart.style.colors;

import java.awt.*;

/** @author timmolter */
public class GGPlot2SeriesColors implements SeriesColors {

  public static final Color RED = new Color(248, 118, 109, 255);
  public static final Color YELLOW_GREEN = new Color(163, 165, 0, 255);
  public static final Color GREEN = new Color(0, 191, 125, 255);
  public static final Color BLUE = new Color(0, 176, 246, 255);
  public static final Color PURPLE = new Color(231, 107, 243, 255);

  private final Color[] seriesColors;

  /** Constructor */
  public GGPlot2SeriesColors() {

    seriesColors = new Color[] {RED, YELLOW_GREEN, GREEN, BLUE, PURPLE};
  }

  @Override
  public Color[] getSeriesColors() {

    return seriesColors;
  }
}
