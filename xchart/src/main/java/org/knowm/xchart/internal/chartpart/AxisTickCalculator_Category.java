package org.knowm.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for
 * rendering the axis ticks for String axes
 *
 * @author timmolter
 */
class AxisTickCalculator_Category extends AxisTickCalculator_ {

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param categories
   * @param axisType
   * @param styler
   */
  public AxisTickCalculator_Category(
      Direction axisDirection,
      double workingSpace,
      List<?> categories,
      Series.DataType axisType,
      AxesChartStyler styler) {

    super(axisDirection, workingSpace, Double.NaN, Double.NaN, styler);

    calculate(categories, axisType);
  }

  private void calculate(List<?> categories, Series.DataType axisType) {

    // tick space - a percentage of the working space available for ticks
    double tickSpace = styler.getPlotContentSize() * workingSpace; // in plot space
    // System.out.println("workingSpace: " + workingSpace);
    // System.out.println("tickSpace: " + tickSpace);

    // where the tick should begin in the working space in pixels
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace);
    // System.out.println("Margin: " + margin);

    // generate all tickLabels and tickLocations from the first to last position
    double gridStep = (tickSpace / categories.size());
    // System.out.println("GridStep: " + gridStep);
    double firstPosition = gridStep / 2.0;

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

    int counter = 0;

    for (Object category : categories) {
      if (axisType == Series.DataType.String) {
        tickLabels.add(category.toString());
      } else if (axisType == Series.DataType.Number) {
        tickLabels.add(axisFormat.format(new BigDecimal(category.toString()).doubleValue()));
      } else if (axisType == Series.DataType.Date) {
        tickLabels.add(axisFormat.format((((Date) category).getTime())));
      }

      double tickLabelPosition = margin + firstPosition + gridStep * counter++;
      tickLocations.add(tickLabelPosition);
    }
  }
}
