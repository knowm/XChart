package org.knowm.xchart.internal.chartpart;

import java.util.List;
import java.util.function.Function;

import org.knowm.xchart.internal.chartpart.Axis_.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for
 * rendering the axis ticks for custom axes
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
    axisFormat = new Formatter_Custom(formattingCallback);
    calculate();
  }

  AxisTickCalculator_Callback(
      Function<Double, String> formattingCallback,
      Direction axisDirection,
      double workingSpace,
      double minValue,
      double maxValue,
      List<Double> axisValues,
      AxesChartStyler styler) {
    super(axisDirection, workingSpace, minValue, maxValue, axisValues, styler);
    axisFormat = new Formatter_Custom(formattingCallback);
    calculate();
  }

  @Override
  boolean areAllTickLabelsUnique(List<?> tickLabels) {
    // When a custom formatting function is in use, the caller controls the output intentionally
    // (e.g. returning " " to hide a tick). Skip the uniqueness guard so the do-while loop in
    // calculate() does not widen the grid step to eliminate those duplicates.
    return true;
  }
}
