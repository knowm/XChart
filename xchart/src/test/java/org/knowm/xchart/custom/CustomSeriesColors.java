package org.knowm.xchart.custom;

import java.awt.Color;
import org.knowm.xchart.style.colors.SeriesColors;

public class CustomSeriesColors implements SeriesColors {
  public static final Color GREEN = new Color(0, 205, 0, 180);
  public static final Color RED = new Color(205, 0, 0, 180);
  public static final Color BLACK = new Color(0, 0, 0, 180);

  private final Color[] seriesColors;

  public CustomSeriesColors() {
    seriesColors = new Color[] {GREEN, RED, BLACK};
  }

  @Override
  public Color[] getSeriesColors() {
    return seriesColors;
  }
}
