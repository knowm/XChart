package org.knowm.xchart.style.markers;

/**
 * @author timmolter
 * @author ekleinod
 */
public class BaseSeriesMarkers implements SeriesMarkers {

  private final Marker[] seriesMarkers;

  /** Constructor */
  public BaseSeriesMarkers() {

    seriesMarkers = new Marker[] {CIRCLE, SQUARE, DIAMOND, TRIANGLE_UP, TRIANGLE_DOWN, CROSS};
  }

  @Override
  public Marker[] getSeriesMarkers() {

    return seriesMarkers;
  }
}
