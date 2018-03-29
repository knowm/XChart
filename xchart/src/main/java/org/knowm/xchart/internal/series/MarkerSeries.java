package org.knowm.xchart.internal.series;

import java.awt.Color;
import org.knowm.xchart.style.markers.Marker;

/**
 * A Series containing X and Y data to be plotted on a Chart with X and Y Axes, contains series
 * markers and error bars.
 *
 * @author timmolter
 */
public abstract class MarkerSeries extends AxesChartSeries {

  /** Marker */
  private Marker marker;

  /** Marker Color */
  private Color markerColor;

  /**
   * Constructor
   *
   * @param name
   * @param xAxisDataType
   */
  protected MarkerSeries(String name, DataType xAxisDataType) {

    super(name, xAxisDataType);
  }

  /**
   * Sets the marker for the series
   *
   * @param marker
   */
  public MarkerSeries setMarker(Marker marker) {

    this.marker = marker;
    return this;
  }

  /**
   * Sets the marker color for the series
   *
   * @param color
   */
  public MarkerSeries setMarkerColor(java.awt.Color color) {

    this.markerColor = color;
    return this;
  }

  public Marker getMarker() {

    return marker;
  }

  public Color getMarkerColor() {

    return markerColor;
  }
}
