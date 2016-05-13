/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis.AxisDataType;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for rendering the axis ticks for String axes
 *
 * @author timmolter
 */
public class AxisTickCalculator_Category extends AxisTickCalculator_ {

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param categories
   * @param axisType
   * @param styler
   */
  public AxisTickCalculator_Category(Direction axisDirection, double workingSpace, List<?> categories, AxisDataType axisType, AxesChartStyler styler) {

    super(axisDirection, workingSpace, Double.NaN, Double.NaN, styler);

    calculate(categories, axisType);
  }

  private void calculate(List<?> categories, AxisDataType axisType) {

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
    NumberFormatter numberFormatter = null;
    SimpleDateFormat simpleDateformat = null;
    if (axisType == AxisDataType.Number) {
      numberFormatter = new NumberFormatter(styler);
    }
    else if (axisType == AxisDataType.Date) {
      if (styler.getDatePattern() == null) {
        throw new RuntimeException("You need to set the Date Formatting Pattern!!!");
      }
      simpleDateformat = new SimpleDateFormat(styler.getDatePattern(), styler.getLocale());
      simpleDateformat.setTimeZone(styler.getTimezone());
    }

    int counter = 0;

    for (Object category : categories) {
      if (axisType == AxisDataType.String) {
        tickLabels.add(category.toString());
      }
      else if (axisType == AxisDataType.Number) {
        tickLabels.add(numberFormatter.formatNumber(new BigDecimal(category.toString()), minValue, maxValue, axisDirection));
      }
      else if (axisType == AxisDataType.Date) {
        tickLabels.add(simpleDateformat.format((((Date) category).getTime())));
      }

      double tickLabelPosition = margin + firstPosition + gridStep * counter++;
      tickLocations.add(tickLabelPosition);
    }

  }
}
