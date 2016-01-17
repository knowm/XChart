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

import org.knowm.xchart.Series_Category.ChartCategorySeriesRenderStyle;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Series_AxesChart;
import org.knowm.xchart.internal.style.StyleManagerAxesChart;
import org.knowm.xchart.Styler_Category;

/**
 * @author timmolter
 */
public class AxisPair<SM extends StyleManagerAxesChart, S extends Series> implements ChartPart {

  private final Chart<StyleManagerAxesChart, Series_AxesChart> chart;

  private final Axis<StyleManagerAxesChart, Series_AxesChart> xAxis;
  private final Axis<StyleManagerAxesChart, Series_AxesChart> yAxis;

  /**
   * Constructor
   *
   * @param chart
   */
  public AxisPair(Chart<StyleManagerAxesChart, Series_AxesChart> chart) {

    this.chart = chart;

    // add axes
    xAxis = new Axis<StyleManagerAxesChart, Series_AxesChart>(chart, Axis.Direction.X);
    yAxis = new Axis<StyleManagerAxesChart, Series_AxesChart>(chart, Axis.Direction.Y);
  }

  @Override
  public void paint(Graphics2D g) {

    prepareForPaint();

    yAxis.paint(g);
    xAxis.paint(g);
  }

  private void prepareForPaint() {

    // calculate axis min and max
    xAxis.resetMinMax();
    yAxis.resetMinMax();
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

    overrideMinMax();

    // logarithmic sanity check
    if (chart.getStyleManager().isXAxisLogarithmic() && xAxis.getMin() <= 0.0) {
      throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic X-Axis!!!");
    }
    if (chart.getStyleManager().isYAxisLogarithmic() && yAxis.getMin() <= 0.0) {
      // System.out.println(getMin());
      throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic Y-Axis!!!");
    }
  }

  public void overrideMinMax() {

    double overrideXAxisMinValue = xAxis.getMin();
    double overrideXAxisMaxValue = xAxis.getMax();
    double overrideYAxisMinValue = yAxis.getMin();
    double overrideYAxisMaxValue = yAxis.getMax();

    if (chart.getStyleManager() instanceof Styler_Category) {

      Styler_Category styleManagerCategory = (Styler_Category) chart.getStyleManager();
      if (styleManagerCategory.getChartCategorySeriesRenderStyle() == ChartCategorySeriesRenderStyle.Bar) {
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
    if (chart.getStyleManager().getXAxisMin() != null)

    {
      overrideXAxisMinValue = chart.getStyleManager().getXAxisMin();
    }
    if (chart.getStyleManager().getXAxisMax() != null)

    {
      overrideXAxisMaxValue = chart.getStyleManager().getXAxisMax();
    }
    if (chart.getStyleManager().getYAxisMin() != null)

    {
      overrideYAxisMinValue = chart.getStyleManager().getYAxisMin();
    }
    if (chart.getStyleManager().getYAxisMax() != null)

    {
      overrideYAxisMaxValue = chart.getStyleManager().getYAxisMax();
    }

    xAxis.setMin(overrideXAxisMinValue);
    xAxis.setMax(overrideXAxisMaxValue);
    yAxis.setMin(overrideYAxisMinValue);
    yAxis.setMax(overrideYAxisMaxValue);

  }

  // Getters & Setters /////////////////////////////////////////////////

  protected Axis<StyleManagerAxesChart, Series_AxesChart> getXAxis() {

    return xAxis;
  }

  protected Axis<StyleManagerAxesChart, Series_AxesChart> getYAxis() {

    return yAxis;
  }

  @Override
  public Rectangle2D getBounds() {

    return null; // should never be called
  }
}
