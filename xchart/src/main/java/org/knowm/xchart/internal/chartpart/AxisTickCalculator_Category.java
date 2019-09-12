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

    // Compute the spacing between categories when there are more than wanted
    
    int xAxisMaxLabelCount = styler.getXAxisMaxLabelCount();
    
    double divisor = 1.0;
    int categoriesCount = categories.size();
    if (0 < xAxisMaxLabelCount && xAxisMaxLabelCount < categoriesCount) {
    	divisor = categoriesCount / new Double(xAxisMaxLabelCount);
    	categoriesCount = xAxisMaxLabelCount;
    }
    
    // generate all tickLabels and tickLocations from the first to last position
    double gridStep = (tickSpace / categoriesCount);
    // System.out.println("GridStep: " + gridStep);
    double firstPosition = divisor > 1.0 ? 0.0 : gridStep / 2.0;

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

    Integer labelIndex = null;

    for (int i = 0; i < categories.size(); i++) {
      Object category = categories.get(i);
      Integer auxIndex = (int) Math.floor(i / divisor);
      if (labelIndex == null || labelIndex < auxIndex) {
        if (axisType == Series.DataType.String) {
          tickLabels.add(category.toString());
        } else if (axisType == Series.DataType.Number) {
          tickLabels.add(axisFormat.format(new BigDecimal(category.toString()).doubleValue()));
        } else if (axisType == Series.DataType.Date) {
          tickLabels.add(axisFormat.format((((Date) category).getTime())));
        }
        double tickLabelPosition = margin + firstPosition + gridStep * auxIndex;
        tickLocations.add(tickLabelPosition);
        labelIndex = auxIndex;
      }
    }
  }
}
