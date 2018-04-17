package org.knowm.xchart.internal.chartpart;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for
 * rendering the axis ticks for given values&labels
 */
class AxisTickCalculator_Override extends AxisTickCalculator_ {

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styler
   * @param labelOverrideMap
   */
  public AxisTickCalculator_Override(
      Direction axisDirection,
      double workingSpace,
      double minValue,
      double maxValue,
      AxesChartStyler styler,
      Map<Double, Object> labelOverrideMap) {

    super(axisDirection, workingSpace, minValue, maxValue, styler);
    axisFormat = new NumberFormatter(styler, axisDirection, minValue, maxValue);
    calculate(labelOverrideMap);
  }

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param styler
   * @param markMap
   * @param axisType
   * @param categoryCount
   */
  public AxisTickCalculator_Override(
      Direction axisDirection,
      double workingSpace,
      AxesChartStyler styler,
      Map<Double, Object> markMap,
      Series.DataType axisType,
      int categoryCount) {

    super(axisDirection, workingSpace, Double.NaN, Double.NaN, styler);
    // set up String formatters that may be encountered
    if (axisType == Series.DataType.String) {
      axisFormat = new StringFormatter();
    } else if (axisType == Series.DataType.Number) {
      axisFormat = new NumberFormatter(styler, axisDirection, minValue, maxValue);
    } else if (axisType == Series.DataType.Date) {
      if (styler.getDatePattern() == null) {
        throw new RuntimeException("You need to set the Date Formatting Pattern!!!");
      }
      SimpleDateFormat simpleDateformat =
          new SimpleDateFormat(styler.getDatePattern(), styler.getLocale());
      simpleDateformat.setTimeZone(styler.getTimezone());
      axisFormat = simpleDateformat;
    }

    calculateForCategory(markMap, categoryCount);
  }

  private void calculate(Map<Double, Object> labelOverrideMap) {

    // a check if all axis data are the exact same values
    if (minValue == maxValue) {
      String label =
          labelOverrideMap.isEmpty() ? " " : labelOverrideMap.values().iterator().next().toString();
      tickLabels.add(label);
      tickLocations.add(workingSpace / 2.0);
      return;
    }

    // tick space - a percentage of the working space available for ticks
    double tickSpace = styler.getPlotContentSize() * workingSpace; // in plot space

    // this prevents an infinite loop when the plot gets sized really small.
    if (tickSpace < styler.getXAxisTickMarkSpacingHint()) {
      return;
    }

    // where the tick should begin in the working space in pixels
    double margin =
        Utils.getTickStartOffset(
            workingSpace,
            tickSpace); // in plot space double gridStep = getGridStepForDecimal(tickSpace);

    // generate all tickLabels and tickLocations from the first to last position
    for (Entry<Double, Object> entry : labelOverrideMap.entrySet()) {

      Object value = entry.getValue();
      String tickLabel = value == null ? " " : value.toString();
      tickLabels.add(tickLabel);

      double tickLabelPosition =
          margin + ((entry.getKey().doubleValue() - minValue) / (maxValue - minValue) * tickSpace);
      tickLocations.add(tickLabelPosition);
    }
  }

  private void calculateForCategory(Map<Double, Object> locationLabelMap, int categoryCount) {

    // tick space - a percentage of the working space available for ticks
    double tickSpace = styler.getPlotContentSize() * workingSpace; // in plot space
    // System.out.println("workingSpace: " + workingSpace);
    // System.out.println("tickSpace: " + tickSpace);

    // where the tick should begin in the working space in pixels
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace);
    // System.out.println("Margin: " + margin);

    // generate all tickLabels and tickLocations from the first to last position
    double gridStep = (tickSpace / categoryCount);
    // System.out.println("GridStep: " + gridStep);
    double firstPosition = gridStep / 2.0;

    for (Entry<Double, Object> entry : locationLabelMap.entrySet()) {

      Object value = entry.getValue();
      String tickLabel = value == null ? " " : value.toString();
      tickLabels.add(tickLabel);

      double tickLabelPosition = margin + firstPosition + gridStep * entry.getKey().doubleValue();
      tickLocations.add(tickLabelPosition);
    }
  }
}
