/**
 * Copyright 2015-2017 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2011-2015 Xeiam LLC (http://xeiam.com) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.xchart.internal.chartpart;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;

import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for rendering the axis ticks for given values&labels
 *
 */
class AxisTickCalculator_Custom extends AxisTickCalculator_ {

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styler
   * @param markMap 
   */
  public AxisTickCalculator_Custom(Direction axisDirection, double workingSpace, double minValue, double maxValue, AxesChartStyler styler, Map<Double, Object> markMap) {

    super(axisDirection, workingSpace, minValue, maxValue, styler);
    axisFormat = new NumberFormatter(styler, axisDirection, minValue, maxValue);
    calculate(markMap);
  }
  
  public AxisTickCalculator_Custom(Direction axisDirection, double workingSpace, AxesChartStyler styler, Map<Double, Object> markMap, Series.DataType axisType, int categoryCount) {
    
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
      SimpleDateFormat simpleDateformat = new SimpleDateFormat(styler.getDatePattern(), styler.getLocale());
      simpleDateformat.setTimeZone(styler.getTimezone());
      axisFormat = simpleDateformat;
    }

    calculateForCategory(markMap, categoryCount);
  }

  private void calculate(Map<Double, Object> locationLabelMap) {

    // a check if all axis data are the exact same values
    if (minValue == maxValue) {
      String label = locationLabelMap.isEmpty() ? " " : locationLabelMap.values().iterator().next().toString();
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
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace); // in plot space double gridStep = getGridStepForDecimal(tickSpace);

    // generate all tickLabels and tickLocations from the first to last position
    for (Entry<Double, Object> entry : locationLabelMap.entrySet()) {
      Object value = entry.getValue();
      String tickLabel = value == null ? " " : value.toString();
      tickLabels.add(tickLabel);

      double tickLabelPosition = margin + ((entry.getKey().doubleValue() - minValue) / (maxValue - minValue) * tickSpace);
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
