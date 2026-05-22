package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;

/** Abstract base for X and Y axes. */
public abstract class Axis_<ST extends AxesChartStyler, S extends AxesChartSeries>
    implements ChartPart {

  final Chart<ST, S> chart;
  final Rectangle2D.Double bounds;
  final ST axesChartStyler;

  /** the axis title */
  AxisTitle<ST, S> axisTitle;

  /** the axis tick */
  AxisTick<ST, S> axisTick;

  /** the axis group index */
  final int index;

  /** the dataType */
  private Series.DataType dataType;

  /** the axis tick calculator */
  AxisTickCalculator axisTickCalculator;

  double min;
  double max;

  Axis_(Chart<ST, S> chart, int index) {

    this.chart = chart;
    this.axesChartStyler = chart.getStyler();
    this.index = index;
    this.bounds = new Rectangle2D.Double();
  }

  /** Reset the default min and max values in preparation for calculating the actual min and max */
  void resetMinMax() {

    min = Double.MAX_VALUE;
    max = -1 * Double.MAX_VALUE;
  }

  /**
   * @param min
   * @param max
   */
  void addMinMax(double min, double max) {

    // NaN indicates String axis data, so min and max play no role
    if (Double.isNaN(this.min) || min < this.min) {
      this.min = min;
    }
    if (Double.isNaN(this.max) || max > this.max) {
      this.max = max;
    }
  }

  public abstract void preparePaint();

  @Override
  public abstract void paint(Graphics2D g);

  /**
   * Converts a chart coordinate value to screen coordinate. Same as AxisTickCalculators
   * calculation.
   *
   * @param chartPoint value in chart coordinate system
   * @return Coordinate of screen. eg: MouseEvent.getX(), MouseEvent.getY()
   */
  // TODO check these method out and make non public??
  public abstract double getScreenValue(double chartPoint);

  /**
   * Converts a screen coordinate to chart coordinate value. Reverses the AxisTickCalculators
   * calculation.
   *
   * @param screenPoint Coordinate of screen. eg: MouseEvent.getX(), MouseEvent.getY()
   * @return value in chart coordinate system
   */
  public abstract double getChartValue(double screenPoint);

  // Shared coordinate helpers /////////////////////////////////////

  /**
   * Shared tick-space math used by both X and Y screen-value calculations.
   *
   * @param workingSpace the pixel length of the axis
   * @param minVal the (possibly log-transformed) minimum data value
   * @param maxVal the (possibly log-transformed) maximum data value
   * @param value the (possibly log-transformed) data value to place
   * @return pixel offset from the start of the tick space, or {@code -1} if degenerate
   */
  double computeTickPosition(double workingSpace, double minVal, double maxVal, double value) {

    double tickSpace = axesChartStyler.getPlotContentSize() * workingSpace;
    if (tickSpace < axesChartStyler.getXAxisTickMarkSpacingHint()) {
      return -1; // degenerate; caller should return workingSpace / 2
    }
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace);
    return margin + ((value - minVal) / (maxVal - minVal) * tickSpace);
  }

  // Getters /////////////////////////////////////////////////

  Series.DataType getDataType() {

    return dataType;
  }

  public void setDataType(Series.DataType dataType) {

    if (dataType != null && this.dataType != null && this.dataType != dataType) {
      throw new IllegalArgumentException(
          "Different Axes (e.g. Date, Number, String) cannot be mixed on the same chart!!");
    }
    this.dataType = dataType;
  }

  double getMin() {

    return min;
  }

  void setMin(double min) {

    this.min = min;
  }

  double getMax() {

    return max;
  }

  void setMax(double max) {

    this.max = max;
  }

  AxisTick<ST, S> getAxisTick() {

    return axisTick;
  }

  AxisTitle<ST, S> getAxisTitle() {

    return axisTitle;
  }

  public AxisTickCalculator getAxisTickCalculator() {

    return this.axisTickCalculator;
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  public int getYIndex() {

    return index;
  }

  /**
   * Returns whether this axis owns (draws) the vertical axis line. Always {@code true} for X and
   * non-merged Y axes; overridden to {@code false} for slave axes in a merged Y-axis group.
   */
  public boolean isAxisLineOwner() {

    return true;
  }

  public double getScreenValueForMin() {
    return getScreenValue(min);
  }

  public double getScreenValueForMax() {
    return getScreenValue(max);
  }

  /**
   * Returns the (possibly empty) list of colocated slave axes registered on this master.
   * Non-Y axes always return an empty list.
   */
  public List<? extends Axis_<?, ?>> getColocatedSlaves() {

    return java.util.Collections.emptyList();
  }

  /** An axis direction */
  public enum Direction {

    /** the constant to represent X axis */
    X,

    /** the constant to represent Y axis */
    Y
  }
}
