package org.knowm.xchart.style.markers;

/** @author timmolter */
public class MatlabSeriesMarkers implements SeriesMarkers {

  private final Marker[] seriesMarkers;

  /** Constructor */
  public MatlabSeriesMarkers() {

    seriesMarkers = new Marker[] {CIRCLE, SQUARE, DIAMOND};
  }

  @Override
  public Marker[] getSeriesMarkers() {

    return seriesMarkers;
  }
}
