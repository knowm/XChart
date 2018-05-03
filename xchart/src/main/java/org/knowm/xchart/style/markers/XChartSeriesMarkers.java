package org.knowm.xchart.style.markers;

/** @author timmolter */
public class XChartSeriesMarkers implements SeriesMarkers {

  private final Marker[] seriesMarkers;

  /** Constructor */
  public XChartSeriesMarkers() {

    seriesMarkers =
        new Marker[] {CIRCLE, DIAMOND, SQUARE, TRIANGLE_DOWN, TRIANGLE_UP, CROSS, PLUS, RECTANGLE};
  }

  @Override
  public Marker[] getSeriesMarkers() {

    return seriesMarkers;
  }
}
