package org.knowm.xchart.demo.charts.theme;

import java.awt.Color;
import org.knowm.xchart.style.colors.SeriesColors;

/** @author timmolter */
public class MyCustomSeriesColors implements SeriesColors {

  public static final Color GREEN = new Color(0, 205, 0, 180);
  public static final Color RED = new Color(205, 0, 0, 180);
  public static final Color BLACK = new Color(0, 0, 0, 180);

  private final Color[] seriesColors;

  /** Constructor */
  public MyCustomSeriesColors() {

    seriesColors = new Color[] {GREEN, RED, BLACK};
  }

  @Override
  public Color[] getSeriesColors() {

    return seriesColors;
  }
}
