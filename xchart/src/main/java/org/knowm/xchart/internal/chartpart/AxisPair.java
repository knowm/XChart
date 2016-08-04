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

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.CategoryStyler;

/**
 * @author timmolter
 */
public class AxisPair<ST extends AxesChartStyler, S extends Series> implements ChartPart {

  private final Chart<AxesChartStyler, Series_AxesChart> chart;

  private final Axis<AxesChartStyler, Series_AxesChart> xAxis;
  private final Axis<AxesChartStyler, Series_AxesChart> yAxis;

  /**
   * Constructor
   *
   * @param chart
   */
  public AxisPair(Chart<AxesChartStyler, Series_AxesChart> chart) {

    this.chart = chart;

    // add axes
    xAxis = new Axis<AxesChartStyler, Series_AxesChart>(chart, Axis.Direction.X);
    yAxis = new Axis<AxesChartStyler, Series_AxesChart>(chart, Axis.Direction.Y);
  }

  @Override
  public void paint(Graphics2D g) {

    prepareForPaint();

    yAxis.paint(g);
    xAxis.paint(g);
  }

  private void prepareForPaint() {

    // set the axis data types, making sure all are compatible
    xAxis.setAxisDataType(null);
    yAxis.setAxisDataType(null);
    for (Series_AxesChart series : chart.getSeriesMap().values()) {
      xAxis.setAxisDataType(series.getxAxisDataType());
      yAxis.setAxisDataType(series.getyAxisDataType());
    }

    // calculate axis min and max
    xAxis.resetMinMax();
    yAxis.resetMinMax();

    // if no series, we still want to plot an empty plot with axes. Since there are no min and max with no series added, we just fake it arbitrarily.
    if (chart.getSeriesMap() == null || chart.getSeriesMap().size() < 1) {
      xAxis.addMinMax(-1, 1);
      yAxis.addMinMax(-1, 1);
    }
    else {
      for (Series_AxesChart series : chart.getSeriesMap().values()) {
        // add min/max to axes
        // System.out.println(series.getxMin());
        // System.out.println(series.getxMax());
        // System.out.println(series.getyMin());
        // System.out.println(series.getyMax());
        // System.out.println("****");
        xAxis.addMinMax(series.getXMin(), series.getXMax());
        yAxis.addMinMax(series.getYMin(), series.getYMax());
      }
    }

    overrideMinMax();

    // logarithmic sanity check
    if (chart.getStyler().isXAxisLogarithmic() && xAxis.getMin() <= 0.0) {
      throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic X-Axis!!!");
    }
    if (chart.getStyler().isYAxisLogarithmic() && yAxis.getMin() <= 0.0) {
      // System.out.println(getMin());
      throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic Y-Axis!!!");
    }
  }

  /**
   * Here we can add special case min max calculations and take care of manual min max settings.
   */
  public void overrideMinMax() {

    double overrideXAxisMinValue = xAxis.getMin();
    double overrideXAxisMaxValue = xAxis.getMax();
    double overrideYAxisMinValue = yAxis.getMin();
    double overrideYAxisMaxValue = yAxis.getMax();

    if (chart.getStyler() instanceof CategoryStyler) {

      CategoryStyler categoryStyler = (CategoryStyler) chart.getStyler();
      if (categoryStyler.getDefaultSeriesRenderStyle() == CategorySeriesRenderStyle.Bar) {

        // if stacked, we need to completely re-calculate min and max.
        if (categoryStyler.isStacked()) {
          int numCategories = chart.getSeriesMap().values().iterator().next().getXData().size();
          double[] accumulatedStackOffsetPos = new double[numCategories];
          double[] accumulatedStackOffsetNeg = new double[numCategories];
          for (Series_AxesChart series : chart.getSeriesMap().values()) {

            int categoryCounter = 0;
            Iterator<? extends Number> yItr = series.getYData().iterator();
            while (yItr.hasNext()) {

              Number next = yItr.next();
              // skip when a value is null
              if (next == null) {
                categoryCounter++;
                continue;
              }

              if (next.doubleValue() > 0) {
                accumulatedStackOffsetPos[categoryCounter] += next.doubleValue();
              }
              else if (next.doubleValue() < 0) {
                accumulatedStackOffsetNeg[categoryCounter] += next.doubleValue();
              }
              categoryCounter++;
            }

          }

          double max = accumulatedStackOffsetPos[0];
          for (int i = 1; i < accumulatedStackOffsetPos.length; i++) {
            if (accumulatedStackOffsetPos[i] > max) {
              max = accumulatedStackOffsetPos[i];
            }
          }

          double min = accumulatedStackOffsetNeg[0];
          for (int i = 1; i < accumulatedStackOffsetNeg.length; i++) {
            if (accumulatedStackOffsetNeg[i] < min) {
              min = accumulatedStackOffsetNeg[i];
            }
          }

          overrideYAxisMaxValue = max;
          overrideYAxisMinValue = min;
          System.out.println("overrideYAxisMaxValue: " + overrideYAxisMaxValue);
          System.out.println("overrideYAxisMinValue: " + overrideYAxisMinValue);
        }

        // override min/max value for bar charts' Y-Axis
        // There is a special case where it's desired to anchor the axis min or max to zero, like in the case of bar charts. This flag enables that feature.
        if (yAxis.getMin() > 0.0) {
          overrideYAxisMinValue = 0.0;
        }
        if (yAxis.getMax() < 0.0) {
          overrideYAxisMaxValue = 0.0;
        }
      }

    }

    // override min and maxValue if specified
    if (chart.getStyler().getXAxisMin() != null)

    {
      overrideXAxisMinValue = chart.getStyler().getXAxisMin();
    }
    if (chart.getStyler().getXAxisMax() != null)

    {
      overrideXAxisMaxValue = chart.getStyler().getXAxisMax();
    }
    if (chart.getStyler().getYAxisMin() != null)

    {
      overrideYAxisMinValue = chart.getStyler().getYAxisMin();
    }
    if (chart.getStyler().getYAxisMax() != null)

    {
      overrideYAxisMaxValue = chart.getStyler().getYAxisMax();
    }

    xAxis.setMin(overrideXAxisMinValue);
    xAxis.setMax(overrideXAxisMaxValue);
    yAxis.setMin(overrideYAxisMinValue);
    yAxis.setMax(overrideYAxisMaxValue);

  }

  // Getters & Setters /////////////////////////////////////////////////

  protected Axis<AxesChartStyler, Series_AxesChart> getXAxis() {

    return xAxis;
  }

  protected Axis<AxesChartStyler, Series_AxesChart> getYAxis() {

    return yAxis;
  }

  @Override
  public Rectangle2D getBounds() {

    return null; // should never be called
  }
}
