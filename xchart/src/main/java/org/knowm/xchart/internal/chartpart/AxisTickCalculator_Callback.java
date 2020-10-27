package org.knowm.xchart.internal.chartpart;

import java.util.List;
import java.util.function.Function;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for
 * rendering the axis ticks for custom axes
 *
 * @author Marc Jakobi
 */
class AxisTickCalculator_Callback extends AxisTickCalculator_ {

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styler
   */
  public AxisTickCalculator_Callback(
      Function<Double, String> formattingCallback,
      Direction axisDirection,
      double workingSpace,
      double minValue,
      double maxValue,
      AxesChartStyler styler) {

    super(axisDirection, workingSpace, minValue, maxValue, styler);
    axisFormat = new CustomFormatter(formattingCallback);
    calculate();
  }

  AxisTickCalculator_Callback(
      Function<Double, String> formattingCallback,
      Direction axisDirection,
      double workingSpace,
      List<Double> axisValues,
      AxesChartStyler styler) {
    super(axisDirection, workingSpace, axisValues, styler);
    axisFormat = new CustomFormatter(formattingCallback);
    calculate();
  }
}
