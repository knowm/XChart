package org.knowm.xchart.internal.chartpart;

import java.util.Collections;
import java.util.List;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for
 * rendering the axis ticks for decimal axes
 *
 * @author timmolter
 */
class AxisTickCalculator_Number extends AxisTickCalculator_ {

  private final Formatter_Number formatterNumber;

  AxisTickCalculator_Number(
      Direction axisDirection,
      double workingSpace,
      double minValue,
      double maxValue,
      List<Double> axisValues,
      AxesChartStyler styler,
      int yIndex) {
    super(axisDirection, workingSpace, minValue, maxValue, axisValues, styler);
    formatterNumber = new Formatter_Number(styler, axisDirection, minValue, maxValue);
    axisFormat = formatterNumber;
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

}
