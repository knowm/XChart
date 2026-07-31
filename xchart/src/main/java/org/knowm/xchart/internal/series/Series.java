package org.knowm.xchart.internal.series;

import java.awt.*;
import org.knowm.xchart.ToolTipGenerator;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;

/** A Series to be plotted on a Chart */
public abstract class Series {

  private final String name;
  // TODO rename this displayName??
  private String label;
  private Color fillColor;
  private boolean showInLegend = true;
  private boolean isEnabled = true;

  /** generates custom tooltip text for this series' data points; null means default labels */
  private ToolTipGenerator toolTipGenerator;

  // TODO there is not always a y-axis group (pie chart for example) move this to an axis series
  // tyoe??
  private int yAxisGroup = 0;

  /** the yAxis decimalPattern */
  private String yAxisDecimalPattern;

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

  public String getYAxisDecimalPattern() {

    return yAxisDecimalPattern;
  }

  public Series setYAxisDecimalPattern(String yAxisDecimalPattern) {

    this.yAxisDecimalPattern = yAxisDecimalPattern;
    return this;
  }

  public ToolTipGenerator getToolTipGenerator() {

    return toolTipGenerator;
  }

  /**
   * Set a generator that produces custom tooltip text for this series' data points, replacing the
   * default label built from the formatted axis values. The generator is called once per rendered
   * data point on every repaint; returning {@code null} for a data point falls back to the default
   * label. Requires tooltips to be enabled on the chart's styler.
   *
   * @param toolTipGenerator the generator, or {@code null} to restore the default labels
   */
  public Series setToolTipGenerator(ToolTipGenerator toolTipGenerator) {

    this.toolTipGenerator = toolTipGenerator;
    return this;
  }

  public enum DataType {
    Number,
    Date,
    String
  }
}
