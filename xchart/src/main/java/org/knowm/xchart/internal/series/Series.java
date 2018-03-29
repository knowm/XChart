package org.knowm.xchart.internal.series;

import java.awt.*;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;

/**
 * A Series to be plotted on a Chart
 *
 * @author timmolter
 */
public abstract class Series {

  private final String name;
  private String label;
  private Color fillColor;
  private boolean showInLegend = true;
  private boolean isEnabled = true;
  private int yAxisGroup = 0;

  /**
   * Constructor
   *
   * @param name the name of the series
   */
  protected Series(String name) {

    if (name == null || name.length() < 1) {
      throw new IllegalArgumentException("Series name cannot be null or zero-length!!!");
    }
    this.name = name;
    this.label = name;
  }

  public abstract LegendRenderType getLegendRenderType();

  public Color getFillColor() {

    return fillColor;
  }

  public Series setFillColor(Color fillColor) {

    this.fillColor = fillColor;
    return this;
  }

  public String getName() {

    return name;
  }

  public String getLabel() {

    return label;
  }

  public Series setLabel(String label) {

    this.label = label;
    return this;
  }

  public boolean isShowInLegend() {

    return showInLegend;
  }

  public Series setShowInLegend(boolean showInLegend) {

    this.showInLegend = showInLegend;
    return this;
  }

  public boolean isEnabled() {

    return isEnabled;
  }

  public Series setEnabled(boolean isEnabled) {

    this.isEnabled = isEnabled;
    return this;
  }

  public int getYAxisGroup() {

    return yAxisGroup;
  }

  /**
   * Set the Y Axis Group the series should belong to
   *
   * @param yAxisGroup
   */
  public Series setYAxisGroup(int yAxisGroup) {

    this.yAxisGroup = yAxisGroup;
    return this;
  }

  public enum DataType {
    Number,
    Date,
    String
  }
}
