package org.knowm.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.internal.chartpart.Axis_.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for
 * rendering the axis ticks for decimal axes
 */
class AxisTickCalculator_Number extends AxisTickCalculator_ {

  private final Formatter_Number formatterNumber;

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
    formatterNumber = new Formatter_Number(styler, axisDirection, minValue, maxValue);
    axisFormat = formatterNumber;
    calculate();
  }

  AxisTickCalculator_Number(
      Direction axisDirection,
      double workingSpace,
      double minValue,
      double maxValue,
      List<Double> axisValues,
      AxesChartStyler styler) {
    this(axisDirection, workingSpace, minValue, maxValue, axisValues, styler, 0);
  }

  AxisTickCalculator_Number(
      Direction axisDirection,
      double workingSpace,
      double minValue,
      double maxValue,
      List<Double> axisValues,
      AxesChartStyler styler,
      int yIndex) {
    super(axisDirection, workingSpace, minValue, maxValue, axisValues, styler);
    this.yIndex = yIndex;
    formatterNumber = new Formatter_Number(styler, axisDirection, minValue, maxValue, yIndex);
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
  public AxisTickCalculator_Number(
      Direction axisDirection,
      double workingSpace,
      double minValue,
      double maxValue,
      AxesChartStyler styler,
      int yIndex) {

    super(axisDirection, workingSpace, minValue, maxValue, styler);
    this.yIndex = yIndex;
    formatterNumber = new Formatter_Number(styler, axisDirection, minValue, maxValue, yIndex);
    axisFormat = formatterNumber;
    calculate();
  }

  @Override
  List<String> labelsForGridSpacing(
      BigDecimal lower, BigDecimal upper, BigDecimal first, BigDecimal next, BigDecimal step) {
    // Include the neighbouring built-in number labels in the spacing estimate. A minus sign
    // or an extra digit can change which grid fits. These labels are never emitted, and
    // custom formatters only receive visible values.
    List<String> labels = new ArrayList<>();
    BigDecimal start = lower.divideToIntegralValue(step).subtract(BigDecimal.ONE).multiply(step);
    for (BigDecimal value = start; value.compareTo(first) < 0; value = value.add(step)) {
      addSpacingLabel(labels, value);
    }
    labels.addAll(getTickLabels());
    BigDecimal limit = upper.add(step).add(step);
    for (BigDecimal value = next; value.compareTo(limit) < 0; value = value.add(step)) {
      addSpacingLabel(labels, value);
    }
    return labels;
  }

  private void addSpacingLabel(List<String> labels, BigDecimal value) {
    double tick = value.doubleValue();
    if (Double.isFinite(tick)) {
      labels.add(formatterNumber.format(tick));
    }
  }
}
