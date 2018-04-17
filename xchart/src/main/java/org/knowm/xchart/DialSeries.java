package org.knowm.xchart;

import java.awt.*;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.markers.Marker;

/**
 * A Series containing Radar data to be plotted on a Chart
 *
 * @author timmolter
 */
public class DialSeries extends Series {

  /** Line Style */
  private BasicStroke stroke;

  /** Line Color */
  private Color lineColor;

  /** Line Width */
  private float lineWidth;

  /** Marker */
  private Marker marker;

  /** Marker Color */
  private Color markerColor;

  private double value;
  private String annotation;

  /**
   * @param annotation Adds custom annotation for series. If annotation is null, it is automatically
   *     calculated.
   */
  public DialSeries(String name, double value, String annotation) {

    super(name);
    this.value = value;
    this.annotation = annotation;
  }

  public double getValue() {

    return value;
  }

  public void setValue(double value) {

    this.value = value;
  }

  public String getAnnotation() {

    return annotation;
  }

  public BasicStroke getLineStyle() {

    return stroke;
  }

  /**
   * Set the line style of the series
   *
   * @param basicStroke
   */
  public DialSeries setLineStyle(BasicStroke basicStroke) {

    stroke = basicStroke;
    if (this.lineWidth > 0.0f) {
      stroke =
          new BasicStroke(
              lineWidth,
              this.stroke.getEndCap(),
              this.stroke.getLineJoin(),
              this.stroke.getMiterLimit(),
              this.stroke.getDashArray(),
              this.stroke.getDashPhase());
    }
    return this;
  }

  public Color getLineColor() {

    return lineColor;
  }

  /**
   * Set the line color of the series
   *
   * @param color
   */
  public DialSeries setLineColor(java.awt.Color color) {

    this.lineColor = color;
    return this;
  }

  public float getLineWidth() {

    return lineWidth;
  }

  /**
   * Set the line width of the series
   *
   * @param lineWidth
   */
  public DialSeries setLineWidth(float lineWidth) {

    this.lineWidth = lineWidth;
    return this;
  }

  public Marker getMarker() {

    return marker;
  }

  /**
   * Sets the marker for the series
   *
   * @param marker
   */
  public DialSeries setMarker(Marker marker) {

    this.marker = marker;
    return this;
  }

  public Color getMarkerColor() {

    return markerColor;
  }

  /**
   * Sets the marker color for the series
   *
   * @param color
   */
  public DialSeries setMarkerColor(java.awt.Color color) {

    this.markerColor = color;
    return this;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    // Dial charts don't have a legend
    return null;
  }
}
