package org.knowm.xchart.style.lines;

import java.awt.*;

/** @author timmolter */
public class GGPlot2SeriesLines implements SeriesLines {

  private final BasicStroke[] seriesLines;

  /** Constructor */
  public GGPlot2SeriesLines() {

    seriesLines = new BasicStroke[] {SOLID, DOT_DOT, DASH_DASH};
  }

  @Override
  public BasicStroke[] getSeriesLines() {

    return seriesLines;
  }
}
