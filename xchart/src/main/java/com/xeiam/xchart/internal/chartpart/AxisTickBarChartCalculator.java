/**
 * Copyright 2011 - 2013 Xeiam LLC.
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
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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
  public AxisTickBarChartCalculator(Direction axisDirection, int workingSpace, BigDecimal minValue, BigDecimal maxValue, ChartPainter chart) {

    super(axisDirection, workingSpace, minValue, maxValue, chart.getStyleManager());
    calculate(chart);
  }

  private void calculate(ChartPainter chartPainter) {

    // tick space - a percentage of the working space available for ticks, i.e. 95%
    int tickSpace = Utils.getTickSpace(workingSpace); // in plot space

    // where the tick should begin in the working space in pixels
    int margin = Utils.getTickStartOffset(workingSpace, tickSpace); // in plot space BigDecimal gridStep = getGridStepForDecimal(tickSpace);

    // get all categories
    Set<Object> categories = new TreeSet<Object>();
    for (Series series : chartPainter.getAxisPair().getSeriesMap().values()) {

      Iterator<?> xItr = series.getXData().iterator();
      while (xItr.hasNext()) {
        Object x = null;
        if (chartPainter.getAxisPair().getxAxis().getAxisType() == AxisType.Number) {
          x = new BigDecimal(((Number) xItr.next()).doubleValue());
        }
        else if (chartPainter.getAxisPair().getxAxis().getAxisType() == AxisType.Date) {
          x = new BigDecimal(((Date) xItr.next()).getTime());
        }
        else if (chartPainter.getAxisPair().getxAxis().getAxisType() == AxisType.String) {
          x = xItr.next();
        }
        categories.add(x);
      }
    }

    int numCategories = categories.size();

    int gridStep = (int) (tickSpace / (double) numCategories);
    int firstPosition = (int) (gridStep / 2.0);

    // generate all tickLabels and tickLocations from the first to last position
    NumberFormatter numberFormatter = null;
    DateFormatter dateFormatter = null;

    if (chartPainter.getAxisPair().getxAxis().getAxisType() == AxisType.Number) {
      numberFormatter = new NumberFormatter(styleManager);
    }
    else if (chartPainter.getAxisPair().getxAxis().getAxisType() == AxisType.Date) {
      dateFormatter = new DateFormatter(chartPainter.getStyleManager());
    }
    int counter = 0;
    for (Object category : categories) {
      if (chartPainter.getAxisPair().getxAxis().getAxisType() == AxisType.Number) {
        tickLabels.add(numberFormatter.formatNumber((BigDecimal) category));
      }
      else if (chartPainter.getAxisPair().getxAxis().getAxisType() == AxisType.Date) {
        long span = Math.abs(maxValue.subtract(minValue).longValue()); // in data space
        long gridStepHint = (long) (span / (double) tickSpace * DEFAULT_TICK_MARK_STEP_HINT_X);
        long timeUnit = dateFormatter.getTimeUnit(gridStepHint);
        tickLabels.add(dateFormatter.formatDate((BigDecimal) category, timeUnit));
      }
      else if (chartPainter.getAxisPair().getxAxis().getAxisType() == AxisType.String) {
        tickLabels.add(category.toString());
      }
      int tickLabelPosition = margin + firstPosition + gridStep * counter++;
      tickLocations.add(tickLabelPosition);
    }
  }

}
