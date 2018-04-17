package org.knowm.xchart.style.lines;

import java.awt.*;

/** @author timmolter */
public class XChartSeriesLines implements SeriesLines {

  private final BasicStroke[] seriesLines;

  /** Constructor */
  public XChartSeriesLines() {

    seriesLines = new BasicStroke[] {SOLID, DASH_DOT, DASH_DASH, DOT_DOT};
  }

  @Override
  public BasicStroke[] getSeriesLines() {

    return seriesLines;
  }
}
