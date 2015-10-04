/**
 * Copyright 2011 - 2015 Xeiam LLC.
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
package com.xeiam.xchart.internal.chartpart;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.xeiam.xchart.Series;
import com.xeiam.xchart.internal.Utils;
import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.chartpart.Axis.Direction;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for rendering the axis ticks for decimal axes
 *
 * @author timmolter
 */
public class AxisTickBarChartCalculator extends AxisTickCalculator {

  /**
   * Constructor
   *
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styleManager
   */
  public AxisTickBarChartCalculator(Direction axisDirection, double workingSpace, double minValue, double maxValue, ChartPainter chart) {

    super(axisDirection, workingSpace, minValue, maxValue, chart.getStyleManager());
    calculate(chart);
  }

  private void calculate(ChartPainter chartPainter) {

    // tick space - a percentage of the working space available for ticks
    int tickSpace = (int) (styleManager.getAxisTickSpacePercentage() * workingSpace); // in plot space

    // where the tick should begin in the working space in pixels
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace);

    List<?> categories = (List<?>) chartPainter.getAxisPair().getSeriesMap().values().iterator().next().getXData();

    // verify all series have exactly the same xAxis
    if (chartPainter.getAxisPair().getSeriesMap().size() > 1) {

      for (Series series : chartPainter.getAxisPair().getSeriesMap().values()) {
        if (!series.getXData().equals(categories)) {
          throw new IllegalArgumentException("X-Axis data must exactly match all other Series X-Axis data for Bar Charts!!");
        }
      }
    }

    // generate all tickLabels and tickLocations from the first to last position
    double gridStep = (tickSpace / (double) categories.size());
    double firstPosition = gridStep / 2.0;

    // set up String formatters that may be encountered
    NumberFormatter numberFormatter = null;
    DateFormatter dateFormatter = null;
    if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.Number) {
      numberFormatter = new NumberFormatter(styleManager);
    }
    else if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.Date) {
      dateFormatter = new DateFormatter(chartPainter.getStyleManager());
    }

    int counter = 0;

    for (Object category : categories) {
      if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.String) {
        tickLabels.add(category.toString());
        double tickLabelPosition = margin + firstPosition + gridStep * counter++;
        tickLocations.add(tickLabelPosition);
      }
      else if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.Number) {
        tickLabels.add(numberFormatter.formatNumber(new BigDecimal(category.toString()), minValue, maxValue, axisDirection));
      }
      else if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.Date) {
        long span = (long) Math.abs(maxValue - minValue); // in data space
        long gridStepHint = (long) (span / (double) tickSpace * styleManager.getXAxisTickMarkSpacingHint());
        long timeUnit = dateFormatter.getTimeSpan(gridStepHint);
        tickLabels.add(dateFormatter.formatDate(((Number) ((Date) category).getTime()).doubleValue(), timeUnit));
      }
      double tickLabelPosition = (int) (margin + firstPosition + gridStep * counter++);
      tickLocations.add(tickLabelPosition);
    }

  }
}
