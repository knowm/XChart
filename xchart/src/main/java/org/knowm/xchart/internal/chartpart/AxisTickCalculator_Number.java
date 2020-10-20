package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for
 * rendering the axis ticks for decimal axes
 *
 * @author timmolter
 */
class AxisTickCalculator_Number extends AxisTickCalculator_ {

  private final NumberFormatter numberFormatter;

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styler
   */
  public AxisTickCalculator_Number(
      Direction axisDirection,
      double workingSpace,
      double minValue,
      double maxValue,
      AxesChartStyler styler) {

    super(axisDirection, workingSpace, minValue, maxValue, styler);
    numberFormatter = new NumberFormatter(styler, axisDirection, minValue, maxValue);
    axisFormat = numberFormatter;
    calculate();
  }

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styler
   * @param yIndex
   */
  public AxisTickCalculator_Number(
      Direction axisDirection,
      double workingSpace,
      double minValue,
      double maxValue,
      AxesChartStyler styler,
      int yIndex) {

    super(axisDirection, workingSpace, minValue, maxValue, styler);
    numberFormatter = new NumberFormatter(styler, axisDirection, minValue, maxValue, yIndex);
    axisFormat = numberFormatter;
    calculate();
  }
}
