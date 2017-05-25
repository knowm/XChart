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

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler.AxisAlignment;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * @author timmolter
 */
public class AxisPair<ST extends AxesChartStyler, S extends Series> implements ChartPart {

  private final Chart<AxesChartStyler, AxesChartSeries> chart;

  private final Axis<AxesChartStyler, AxesChartSeries> xAxis;
  private final Axis<AxesChartStyler, AxesChartSeries> yAxis;
  private final Map<Integer, Axis<AxesChartStyler, AxesChartSeries>> yAxisMap;
  
  private final Rectangle2D.Double leftYAxisBounds;
  private final Rectangle2D.Double rightYAxisBounds;

  /**
   * Constructor
   *
   * @param chart
   */
  public AxisPair(Chart<AxesChartStyler, AxesChartSeries> chart) {

    this.chart = chart;

    // add axes
    xAxis = new Axis<AxesChartStyler, AxesChartSeries>(chart, Axis.Direction.X, 0);
    yAxis = new Axis<AxesChartStyler, AxesChartSeries>(chart, Axis.Direction.Y, 0);
    yAxisMap = new TreeMap<Integer, Axis<AxesChartStyler, AxesChartSeries>>();
    yAxisMap.put(0, yAxis);
    leftYAxisBounds = new Rectangle2D.Double();
    rightYAxisBounds = new Rectangle2D.Double();
  }

  @Override
  public void paint(Graphics2D g) {

    prepareForPaint();

    int leftCount = 0;
    int rightCount = 0;

    leftYAxisBounds.width = 0;
    rightYAxisBounds.width = 0;

    
    AxesChartStyler styler = chart.getStyler();
    for (Entry<Integer, Axis<AxesChartStyler, AxesChartSeries>> e : yAxisMap.entrySet()) {
      Axis<AxesChartStyler, AxesChartSeries> ya = e.getValue();
      ya.preparePaint();
      Rectangle2D bounds = ya.getBounds();
      double width = bounds.getWidth();
      if (styler.getYAxisAlignment(e.getKey()) == AxisAlignment.Right) {
        rightYAxisBounds.width += width + chart.getStyler().getChartPadding();
        if(rightCount == 0) {
          rightYAxisBounds.setRect(bounds);
        } 
        rightCount++;
      } else {
        if(leftCount == 0) {
          leftYAxisBounds.setRect(bounds);
          leftYAxisBounds.width = 0;
        }
        leftYAxisBounds.width += width + chart.getStyler().getChartPadding();
        leftCount++;
      }
    }
    
    double leftStart = 0;
    double rightStart;
    if (rightCount == 0) {
      rightStart = 0;
    } else {
      rightStart =
        chart.getWidth()
        
            - rightYAxisBounds.width 
        
            - (styler.getLegendPosition() == LegendPosition.OutsideE ? chart.getLegend().getBounds().getWidth() : 0) 
        
             - 1 * styler.getChartPadding()
        
             - (styler.isYAxisTicksVisible() ? (styler.getPlotMargin()) : 0)
        
             - (styler.getLegendPosition() == LegendPosition.OutsideE && styler.isLegendVisible() ? styler.getChartPadding() : 0);
    }

    //leftYAxisBounds.x = leftStart;
    rightYAxisBounds.x = rightStart;
    
    if(leftCount == 0) {
      Rectangle2D.Double bounds = (java.awt.geom.Rectangle2D.Double) yAxis.getBounds();
      leftYAxisBounds.x = bounds.x;
      leftYAxisBounds.y = bounds.y;
      leftYAxisBounds.height = bounds.height;
    }
    
    for (Entry<Integer, Axis<AxesChartStyler, AxesChartSeries>> e : yAxisMap.entrySet()) {
      Axis<AxesChartStyler, AxesChartSeries> ya = e.getValue();
      Rectangle2D.Double bounds = (java.awt.geom.Rectangle2D.Double) ya.getBounds();
      double width = bounds.getWidth();
      if (styler.getYAxisAlignment(e.getKey()) == AxisAlignment.Right) {
        bounds.x = rightStart;
        // add padding after axis 
        rightStart += chart.getStyler().getChartPadding() + width;
      } else {
        // add padding before axis
        leftStart += chart.getStyler().getChartPadding();
        bounds.x = leftStart;
        leftStart += width;
      }
      ya.paint(g);
      // System.out.println("Y axis " + e.getKey() + " bounds: " + bounds);
    }
    
//    System.out.println("left Y   bounds: " + leftYAxisBounds);
//    System.out.println("right Y  bounds: " + rightYAxisBounds);
    
    xAxis.preparePaint();
    xAxis.paint(g);
  }

  private void prepareForPaint() {
    boolean mainYAxisUsed = false;
    if (chart.getSeriesMap() != null) {
      for (AxesChartSeries series : chart.getSeriesMap().values()) {
        int yIndex = series.getYIndex();
        if(!mainYAxisUsed && yIndex == 0) {
          mainYAxisUsed = true;
        }
        if (yAxisMap.containsKey(yIndex)) {
          continue;
        }
        yAxisMap.put(yIndex, new Axis<AxesChartStyler, AxesChartSeries>(chart, Axis.Direction.Y, yIndex));
      }
    }

    // set the axis data types, making sure all are compatible
    xAxis.setAxisDataType(null);
    for (Axis<AxesChartStyler, AxesChartSeries> ya : yAxisMap.values()) {
      ya.setAxisDataType(null);
    }
    for (AxesChartSeries series : chart.getSeriesMap().values()) {
      xAxis.setAxisDataType(series.getxAxisDataType());
      getYAxis(series.getYIndex()).setAxisDataType(series.getyAxisDataType());
      if (!mainYAxisUsed) {
        yAxis.setAxisDataType(series.getyAxisDataType());
      }
    }

    // calculate axis min and max
    xAxis.resetMinMax();
    for (Axis<AxesChartStyler, AxesChartSeries> ya : yAxisMap.values()) {
      ya.resetMinMax();
    }

    // if no series, we still want to plot an empty plot with axes. Since there are no min and max with no series added, we just fake it arbitrarily.
    if (chart.getSeriesMap() == null || chart.getSeriesMap().size() < 1) {
      xAxis.addMinMax(-1, 1);
      for (Axis<AxesChartStyler, AxesChartSeries> ya : yAxisMap.values()) {
        ya.addMinMax(-1, 1);
      }
    } else {
      int disabledCount = 0; // maybe all are disabled, so we check this condition
      for (AxesChartSeries series : chart.getSeriesMap().values()) {
        // add min/max to axes
        // System.out.println(series.getxMin());
        // System.out.println(series.getxMax());
        // System.out.println(series.getyMin());
        // System.out.println(series.getyMax());
        // System.out.println("****");
        if (!series.isEnabled()) {
          disabledCount++;
          continue;
        }
        xAxis.addMinMax(series.getXMin(), series.getXMax());
        
        getYAxis(series.getYIndex()).addMinMax(series.getYMin(), series.getYMax());
        if (!mainYAxisUsed) {
          yAxis.addMinMax(series.getYMin(), series.getYMax());
        }
      }
      if (disabledCount == chart.getSeriesMap().values().size()) {
        xAxis.addMinMax(-1, 1);
        for (Axis<AxesChartStyler, AxesChartSeries> ya : yAxisMap.values()) {
          ya.addMinMax(-1, 1);
        }
      }
    }

    overrideMinMaxForXAxis();
    for (Axis<AxesChartStyler, AxesChartSeries> ya : yAxisMap.values()) {
      overrideMinMaxForYAxis(ya);
    }

    // logarithmic sanity check
    if (chart.getStyler().isXAxisLogarithmic() && xAxis.getMin() <= 0.0) {
      throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic X-Axis!!!");
    }
    if (chart.getStyler().isYAxisLogarithmic()) {
      for (Axis<AxesChartStyler, AxesChartSeries> ya : yAxisMap.values()) {
        if(ya.getMin() <= 0.0) {
      // System.out.println(getMin());
      throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be less or equal to zero for a logarithmic Y-Axis!!!");
    }
      }
    }
    // infinity checks
    if (xAxis.getMin() == Double.POSITIVE_INFINITY || xAxis.getMax() == Double.POSITIVE_INFINITY) {
      throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be equal to Double.POSITIVE_INFINITY!!!");
    }
    for (Axis<AxesChartStyler, AxesChartSeries> ya : yAxisMap.values()) {
      if (ya.getMin() == Double.POSITIVE_INFINITY || ya.getMax() == Double.POSITIVE_INFINITY) {
        throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be equal to Double.POSITIVE_INFINITY!!!");
      }
      if (ya.getMin() == Double.NEGATIVE_INFINITY || ya.getMax() == Double.NEGATIVE_INFINITY) {
        throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be equal to Double.NEGATIVE_INFINITY!!!");
      }
    }

    if (xAxis.getMin() == Double.NEGATIVE_INFINITY || xAxis.getMax() == Double.NEGATIVE_INFINITY) {
      throw new IllegalArgumentException("Series data (accounting for error bars too) cannot be equal to Double.NEGATIVE_INFINITY!!!");
    }
  }

  protected Axis<AxesChartStyler, AxesChartSeries> getYAxis(int yIndex) {

    return yAxisMap.get(yIndex);
  }

  /**
   * Here we can add special case min max calculations and take care of manual min max settings.
   */
  private void overrideMinMaxForXAxis() {
    double overrideXAxisMinValue = xAxis.getMin();
    double overrideXAxisMaxValue = xAxis.getMax();
    // override min and maxValue if specified
    if (chart.getStyler().getXAxisMin() != null)

    {
      overrideXAxisMinValue = chart.getStyler().getXAxisMin();
    }
    if (chart.getStyler().getXAxisMax() != null)

    {
      overrideXAxisMaxValue = chart.getStyler().getXAxisMax();
    }
    xAxis.setMin(overrideXAxisMinValue);
    xAxis.setMax(overrideXAxisMaxValue);
  }
  
  private void overrideMinMaxForYAxis(Axis yAxis) {

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
          for (AxesChartSeries series : chart.getSeriesMap().values()) {

            if (!series.isEnabled()) {
              continue;
            }

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
              } else if (next.doubleValue() < 0) {
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
          // System.out.println("overrideYAxisMaxValue: " + overrideYAxisMaxValue);
          // System.out.println("overrideYAxisMinValue: " + overrideYAxisMinValue);
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
    if (chart.getStyler().getYAxisMin() != null)

    {
      overrideYAxisMinValue = chart.getStyler().getYAxisMin();
    }
    if (chart.getStyler().getYAxisMax() != null)

    {
      overrideYAxisMaxValue = chart.getStyler().getYAxisMax();
    }

    yAxis.setMin(overrideYAxisMinValue);
    yAxis.setMax(overrideYAxisMaxValue);
  }

  // Getters & Setters /////////////////////////////////////////////////

  Axis<AxesChartStyler, AxesChartSeries> getXAxis() {

    return xAxis;
  }

  Axis<AxesChartStyler, AxesChartSeries> getYAxis() {

    return yAxis;
  }

  @Override
  public Rectangle2D getBounds() {

    return null; // should never be called
  }

  public Rectangle2D.Double getLeftYAxisBounds() {

    return leftYAxisBounds;
  }
  
  public Rectangle2D.Double getRightYAxisBounds() {

    return rightYAxisBounds;
  }
}
