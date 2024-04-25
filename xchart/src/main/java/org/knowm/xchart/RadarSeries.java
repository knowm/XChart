package org.knowm.xchart;

import java.awt.BasicStroke;
import java.awt.Color;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.MarkerSeries;
import org.knowm.xchart.style.markers.Marker;

/** A Series containing Radar data to be plotted on a Chart */
public class RadarSeries extends MarkerSeries {

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

  private double[] values;
  private String[] tooltipOverrides;

  // TODO refactor tooltips override
  /**
   * @param tooltipOverrides Adds custom tooltipOverrides for series. If tooltipOverrides is null,
   *     they are automatically generated.
   */
  public RadarSeries(String name, double[] values, String[] tooltipOverrides) {

    super(name, DataType.Number);
    this.values = values;
    this.tooltipOverrides = tooltipOverrides;
  }

  public double[] getValues() {

    return values;
  }

  public void setValues(double[] values) {

    this.values = values;
  }

  public String[] getTooltipOverrides() {

    return tooltipOverrides;
  }

  @Override
  protected void calculateMinMax() {}

  public BasicStroke getLineStyle() {

    return stroke;
  }

  /**
   * Set the line style of the series
   *
   * @param basicStroke
   */
  public RadarSeries setLineStyle(BasicStroke basicStroke) {

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
  public RadarSeries setLineColor(java.awt.Color color) {

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
  public RadarSeries setLineWidth(float lineWidth) {

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
  public RadarSeries setMarker(Marker marker) {

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
  public RadarSeries setMarkerColor(java.awt.Color color) {

    this.markerColor = color;
    return this;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return LegendRenderType.Line;
  }

  public void setTooltipOverrides(String[] tooltipOverrides) {

    this.tooltipOverrides = tooltipOverrides;
  }
}
