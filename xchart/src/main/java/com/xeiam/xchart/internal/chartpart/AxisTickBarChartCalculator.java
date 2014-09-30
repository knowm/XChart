/**
 * Copyright 2011 - 2014 Xeiam LLC.
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
    double margin = Utils.getTickStartOffset(workingSpace, tickSpace); // in plot space double gridStep = getGridStepForDecimal(tickSpace);

    // get all categories
    List<Object> categories = new ArrayList<Object>();

    Series firstSeries = chartPainter.getAxisPair().getSeriesMap().values().iterator().next(); // we use this to check all series have the exact same length and values
    for (Series series : chartPainter.getAxisPair().getSeriesMap().values()) {

      Iterator<?> firstSeriesItr = firstSeries.getXData().iterator();
      Iterator<?> xItr = series.getXData().iterator();
      while (xItr.hasNext()) {

        // check matching
        Object next = xItr.next();
        Object firstSeriesNext = firstSeriesItr.next();
        if (!firstSeriesNext.equals(next)) {
          throw new IllegalArgumentException("X-Axis data must exactly match all other Series X-Axis data for Bar Charts!!");
        }

        Object x = null;
        if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.Number) {
          x = next;
        }
        else if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.Date) {
          x = (double) (((Date) next).getTime());
        }
        else if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.String) {
          x = next;
        }
        if (!categories.contains(x)) {
          categories.add(x);
        }
      }
    }

    if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.String) {

      double gridStep = (tickSpace / (double) categories.size());
      double firstPosition = gridStep / 2.0;
      int counter = 0;
      for (Object category : categories) {
        tickLabels.add(category.toString());
        double tickLabelPosition = margin + firstPosition + gridStep * counter++;
        tickLocations.add(tickLabelPosition);
      }
    }
    else if (categories.size() < 13) { // Number or Date and 12 or less categories. give each category a tick label

      double gridStep = (tickSpace / (double) categories.size());
      double firstPosition = gridStep / 2.0;

      // generate all tickLabels and tickLocations from the first to last position
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
        if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.Number) {
          tickLabels.add(numberFormatter.formatNumber(new BigDecimal(category.toString()), minValue, maxValue, axisDirection));
        }
        else if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.Date) {
          long span = (long) Math.abs(maxValue - minValue); // in data space
          long gridStepHint = (long) (span / (double) tickSpace * styleManager.getXAxisTickMarkSpacingHint());
          long timeUnit = dateFormatter.getTimeUnit(gridStepHint);
          tickLabels.add(dateFormatter.formatDate(((Number) category).doubleValue(), timeUnit));
        }
        double tickLabelPosition = (int) (margin + firstPosition + gridStep * counter++);
        tickLocations.add(tickLabelPosition);
      }
    }
    else { // Number or Date and more than 12 categories. divide up the axis tick space according to normal date oor number axis layout

      // generate all tickLabels and tickLocations from the first to last position

      if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.Number) {

        AxisTickNumericalCalculator axisTickNumericalCalculator = new AxisTickNumericalCalculator(axisDirection, workingSpace, minValue, maxValue, styleManager);
        tickLabels = axisTickNumericalCalculator.getTickLabels();
        tickLocations = axisTickNumericalCalculator.getTickLocations();

      }
      else if (chartPainter.getAxisPair().getXAxis().getAxisType() == AxisType.Date) {

        AxisTickDateCalculator axisTickDateCalculator = new AxisTickDateCalculator(axisDirection, workingSpace, minValue, maxValue, styleManager);
        tickLabels = axisTickDateCalculator.getTickLabels();
        tickLocations = axisTickDateCalculator.getTickLocations();
      }

    }

  }
}
