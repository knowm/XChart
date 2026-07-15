package org.knowm.xchart;

import java.awt.Shape;
import java.util.Objects;

/**
 * An immutable snapshot of a single rendered data point that the mouse is interacting with. An
 * instance is delivered to a {@link DataPointListener} when the mouse hovers over, exits, or clicks
 * the data point's rendered shape (the bar rectangle, marker ellipse, pie slice, etc.).
 *
 * <p>Not every chart type reports every field. {@link #getSeriesName()} may be {@code null} and
 * {@link #getDataPointIndex()} may be {@code -1} for chart types that don't yet propagate series
 * identity. {@link #getLabel()} is populated for single-label tooltips, whereas {@link #getXValue()}
 * / {@link #getYValue()} are populated for x/y-pair tooltips; typically one or the other is set.
 */
public class ChartDataPoint {

  private final String seriesName;
  private final int dataPointIndex;
  private final String label;
  private final String xValue;
  private final String yValue;
  private final double screenX;
  private final double screenY;
  private final Shape shape;

  public ChartDataPoint(
      String seriesName,
      int dataPointIndex,
      String label,
      String xValue,
      String yValue,
      double screenX,
      double screenY,
      Shape shape) {

    this.seriesName = seriesName;
    this.dataPointIndex = dataPointIndex;
    this.label = label;
    this.xValue = xValue;
    this.yValue = yValue;
    this.screenX = screenX;
    this.screenY = screenY;
    this.shape = shape;
  }

  /** The name of the series this data point belongs to, or {@code null} if not reported. */
  public String getSeriesName() {
    return seriesName;
  }

  /** The index of this data point within its series, or {@code -1} if not reported. */
  public int getDataPointIndex() {
    return dataPointIndex;
  }

  /** The single-label tooltip text, or {@code null} for x/y-pair tooltips. */
  public String getLabel() {
    return label;
  }

  /** The formatted x value, or {@code null} for single-label tooltips. */
  public String getXValue() {
    return xValue;
  }

  /** The formatted y value, or {@code null} for single-label tooltips. */
  public String getYValue() {
    return yValue;
  }

  /** The x pixel coordinate of the data point within the chart panel. */
  public double getScreenX() {
    return screenX;
  }

  /** The y pixel coordinate of the data point within the chart panel. */
  public double getScreenY() {
    return screenY;
  }

  /** The rendered hit shape used for collision detection (bar rectangle, marker ellipse, etc.). */
  public Shape getShape() {
    return shape;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChartDataPoint that = (ChartDataPoint) o;
    // Intentionally excludes shape: a shape is a fresh object each repaint and several
    // java.awt.geom shapes don't override equals(). Identity is defined by series + index +
    // labels + screen position, all of which are stable across repaints of an unchanged chart.
    return dataPointIndex == that.dataPointIndex
        && Double.compare(screenX, that.screenX) == 0
        && Double.compare(screenY, that.screenY) == 0
        && Objects.equals(seriesName, that.seriesName)
        && Objects.equals(label, that.label)
        && Objects.equals(xValue, that.xValue)
        && Objects.equals(yValue, that.yValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seriesName, dataPointIndex, label, xValue, yValue, screenX, screenY);
  }

  @Override
  public String toString() {
    return "ChartDataPoint{"
        + "seriesName='"
        + seriesName
        + '\''
        + ", dataPointIndex="
        + dataPointIndex
        + ", label='"
        + label
        + '\''
        + ", xValue='"
        + xValue
        + '\''
        + ", yValue='"
        + yValue
        + '\''
        + ", screenX="
        + screenX
        + ", screenY="
        + screenY
        + '}';
  }
}
