package org.knowm.xchart.style.lines;

import java.awt.*;

/** @author timmolter */
public class MatlabSeriesLines implements SeriesLines {

  private final BasicStroke[] seriesLines;

  /** Constructor */
  public MatlabSeriesLines() {

    seriesLines = new BasicStroke[] {SOLID, DASH_DASH, DOT_DOT};
  }

  @Override
  public BasicStroke[] getSeriesLines() {

    return seriesLines;
  }
}
