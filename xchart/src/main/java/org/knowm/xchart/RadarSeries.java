/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart;

import java.awt.BasicStroke;
import java.awt.Color;

import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.markers.Marker;

/**
 * A Series containing Radar data to be plotted on a Chart
 *
 */
public class RadarSeries extends Series {

  /**
   * Line Style
   */
  private BasicStroke stroke;

  /**
   * Line Color
   */
  private Color lineColor;

  /**
   * Line Width
   */
  private float lineWidth;

  /**
   * Marker
   */
  private Marker marker;

  /**
   * Marker Color
   */
  private Color markerColor;

  private double[] values;
  private String[] tooltipOverrides;

  /**
   * @param tooltipOverrides Adds custom tooltipOverrides for series. If tooltipOverrides is null, they are automatically generated.
   */
  public RadarSeries(String name, double[] values, String[] tooltipOverrides) {

    super(name);
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

  /**
   * Set the line style of the series
   *
   * @param basicStroke
   */
  public RadarSeries setLineStyle(BasicStroke basicStroke) {

    stroke = basicStroke;
    if (this.lineWidth > 0.0f) {
      stroke = new BasicStroke(lineWidth, this.stroke.getEndCap(), this.stroke.getLineJoin(), this.stroke.getMiterLimit(), this.stroke.getDashArray(), this.stroke.getDashPhase());
    }
    return this;
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

  /**
   * Set the line width of the series
   *
   * @param lineWidth
   */
  public RadarSeries setLineWidth(float lineWidth) {

    this.lineWidth = lineWidth;
    return this;
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

  /**
   * Sets the marker color for the series
   *
   * @param color
   */
  public RadarSeries setMarkerColor(java.awt.Color color) {

    this.markerColor = color;
    return this;
  }

  public BasicStroke getLineStyle() {

    return stroke;
  }

  public Color getLineColor() {

    return lineColor;
  }

  public float getLineWidth() {

    return lineWidth;
  }

  public Marker getMarker() {

    return marker;
  }

  public Color getMarkerColor() {

    return markerColor;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    // Radar charts are always rendered as a Box in the legend
    return null;
  }
}
