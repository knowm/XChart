package org.knowm.xchart.style.markers;

/** @author timmolter */
public class GGPlot2SeriesMarkers implements SeriesMarkers {

  private final Marker[] seriesMarkers;

  /** Constructor */
  public GGPlot2SeriesMarkers() {

    seriesMarkers = new Marker[] {CIRCLE, DIAMOND};
  }

  @Override
  public Marker[] getSeriesMarkers() {

    return seriesMarkers;
  }
}
