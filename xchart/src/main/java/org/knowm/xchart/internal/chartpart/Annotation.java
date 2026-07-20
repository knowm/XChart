package org.knowm.xchart.internal.chartpart;

import java.awt.geom.Rectangle2D;
import org.knowm.xchart.style.Styler;

public abstract class Annotation implements ChartPart {

  protected boolean isVisible = true;
  protected boolean isValueInScreenSpace;

  /**
   * The Y-Axis group this annotation's chart-space Y value is resolved against. Defaults to the
   * primary group, {@code 0}, which matches the behavior before groups were supported.
   */
  protected int yAxisGroup = 0;

  protected AxesChart<?, ?> chart;
  protected Styler styler;
  protected Rectangle2D bounds;

  public Annotation(boolean isValueInScreenSpace) {
    this.isValueInScreenSpace = isValueInScreenSpace;
  }

  public void init(AxesChart<?, ?> chart) {

    this.chart = chart;
    this.styler = chart.getStyler();
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  public Annotation setVisible(boolean visible) {

    isVisible = visible;
    return this;
  }

  /**
   * Sets the Y-Axis group whose scale this annotation's chart-space Y value is resolved against.
   * Use this when the annotation belongs to a series placed on a secondary axis via {@code
   * series.setYAxisGroup(int)}. Has no effect on annotations in screen space.
   *
   * @param yAxisGroup the Y-Axis group index; falls back to the primary axis if the group holds no
   *     series
   * @return this annotation, for chaining
   */
  public Annotation setYAxisGroup(int yAxisGroup) {

    this.yAxisGroup = yAxisGroup;
    return this;
  }

  public int getYAxisGroup() {

    return yAxisGroup;
  }

  protected int getXAxisScreenValue(double chartSpaceValue) {
    return (int) chart.getXAxis().getScreenValue(chartSpaceValue);
  }

  protected int getYAxisScreenValue(double chartSpaceValue) {
    return (int) getYAxisForGroup().getScreenValue(chartSpaceValue);
  }

  /**
   * Returns the Y-Axis for the configured group, or the primary Y-Axis when that group has no
   * series and therefore no axis of its own.
   */
  private Axis_Y<?, ?> getYAxisForGroup() {

    Axis_Y<?, ?> yAxis = chart.getYAxis(yAxisGroup);
    return yAxis == null ? chart.getYAxis() : yAxis;
  }

  protected int getXAxisScreenValueForMax() {
    return (int) chart.getPlot().plotSurface.getBounds().getMaxX();
  }

  protected int getXAxisScreenValueForMin() {
    return (int) chart.getPlot().plotSurface.getBounds().getMinX();
  }

  protected int getYAxisScreenValueForMax() {
    return (int) chart.getPlot().plotSurface.getBounds().getMaxY();
  }

  protected int getYAxisScreenValueForMin() {
    return (int) chart.getPlot().plotSurface.getBounds().getMinY();
  }
}
