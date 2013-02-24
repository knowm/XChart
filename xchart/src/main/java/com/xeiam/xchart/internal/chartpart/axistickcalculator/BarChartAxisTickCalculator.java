/**
 * Copyright (C) 2013 Xeiam LLC http://xeiam.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchart.internal.chartpart.axistickcalculator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.internal.chartpart.Axis.AxisType;
import com.xeiam.xchart.internal.chartpart.Axis.Direction;
import com.xeiam.xchart.internal.chartpart.AxisPair;

/**
 * This class encapsulates the logic to generate the axis tick mark and axis tick label data for rendering the axis ticks for decimal axes
 * 
 * @author timmolter
 */
public class BarChartAxisTickCalculator extends AxisTickCalculator {

  /**
   * Constructor
   * 
   * @param axisDirection
   * @param workingSpace
   * @param minValue
   * @param maxValue
   * @param styleManager
   */
  public BarChartAxisTickCalculator(Direction axisDirection, int workingSpace, BigDecimal minValue, BigDecimal maxValue, Chart chart) {

    super(axisDirection, workingSpace, minValue, maxValue, chart.getStyleManager());
    calculate(chart);
  }

  private void calculate(Chart chart) {

    // tick space - a percentage of the working space available for ticks, i.e. 95%
    int tickSpace = AxisPair.getTickSpace(workingSpace); // in plot space

    // where the tick should begin in the working space in pixels
    int margin = AxisPair.getTickStartOffset(workingSpace, tickSpace); // in plot space BigDecimal gridStep = getGridStepForDecimal(tickSpace);

    // get all categories
    Set<Object> categories = new TreeSet<Object>();
    Map<Integer, Series> seriesMap = chart.getAxisPair().getSeriesMap();
    for (Integer seriesId : seriesMap.keySet()) {

      Series series = seriesMap.get(seriesId);
      Iterator<?> xItr = series.getxData().iterator();
      while (xItr.hasNext()) {
        Object x = null;
        if (chart.getAxisPair().getxAxis().getAxisType() == AxisType.Number) {
          x = new BigDecimal(((Number) xItr.next()).doubleValue());
        } else if (chart.getAxisPair().getxAxis().getAxisType() == AxisType.Date) {
          x = new BigDecimal(((Date) xItr.next()).getTime());
        } else if (chart.getAxisPair().getxAxis().getAxisType() == AxisType.String) {
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

    if (chart.getAxisPair().getxAxis().getAxisType() == AxisType.Number) {
      numberFormatter = new NumberFormatter(styleManager);
    } else if (chart.getAxisPair().getxAxis().getAxisType() == AxisType.Date) {
      dateFormatter = new DateFormatter(chart.getStyleManager());
    }
    int counter = 0;
    for (Object category : categories) {
      if (chart.getAxisPair().getxAxis().getAxisType() == AxisType.Number) {
        tickLabels.add(numberFormatter.formatNumber((BigDecimal) category));
      } else if (chart.getAxisPair().getxAxis().getAxisType() == AxisType.Date) {
        long span = Math.abs(maxValue.subtract(minValue).longValue()); // in data space
        long gridStepHint = (long) (span / (double) tickSpace * DEFAULT_TICK_MARK_STEP_HINT_X);
        long timeUnit = dateFormatter.getTimeUnit(gridStepHint);
        tickLabels.add(dateFormatter.formatDate((BigDecimal) category, timeUnit));
      } else if (chart.getAxisPair().getxAxis().getAxisType() == AxisType.String) {
        tickLabels.add(category.toString());
      }
      int tickLabelPosition = margin + firstPosition + gridStep * counter++;
      tickLocations.add(tickLabelPosition);
    }
  }

}
