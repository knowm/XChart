package org.knowm.xchart.internal.chartpart;

/**
 * Box plot data information(Upper whisker, Upper quartile, Middle quartile, Lower quartile, Lower
 * whisker)
 *
 * @author Mr14huashao
 */
// TODO remove these BoxPlot DTOs
public class BoxPlotData {

  /** Upper whisker */
  protected double upper;

  /** Upper quartile */
  protected double q3;

  /** Middle quartile */
  protected double median;

  /** Lower quartile */
  protected double q1;

  /** Lower whisker */
  protected double lower;
}
