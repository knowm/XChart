package org.knowm.xchart.style.lines;

import java.awt.*;

/**
 * @author timmolter
 * @author ekleinod
 */
public class BaseSeriesLines implements SeriesLines {

  private final BasicStroke[] seriesLines;

  /** Constructor */
  public BaseSeriesLines() {

    seriesLines = new BasicStroke[] {SOLID, DOT_DOT, DASH_DASH, DASH_DOT};
  }

  @Override
  public BasicStroke[] getSeriesLines() {

    return seriesLines;
  }
}
