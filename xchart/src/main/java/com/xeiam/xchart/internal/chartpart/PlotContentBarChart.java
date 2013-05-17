/**
 * Copyright 2013 Xeiam LLC.
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

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.xeiam.xchart.Series;
import com.xeiam.xchart.internal.Utils;

/**
 * @author timmolter
 */
public class PlotContentBarChart extends PlotContent {

  /**
   * Constructor
   * 
   * @param plot
   */
  protected PlotContentBarChart(Plot plot) {

    super(plot);
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle2D bounds = plot.getBounds();

    // X-Axis
    int xTickSpace = Utils.getTickSpace((int) bounds.getWidth());
    int xLeftMargin = Utils.getTickStartOffset((int) bounds.getWidth(), xTickSpace);

    // Y-Axis
    int yTickSpace = Utils.getTickSpace((int) bounds.getHeight());
    int yTopMargin = Utils.getTickStartOffset((int) bounds.getHeight(), yTickSpace);

    // get all categories
    Set<Object> categories = new TreeSet<Object>();
    Map<Integer, Series> seriesMap = getChartPainter().getAxisPair().getSeriesMap();
    for (Integer seriesId : seriesMap.keySet()) {

      Series series = seriesMap.get(seriesId);
      Iterator<?> xItr = series.getxData().iterator();
      while (xItr.hasNext()) {
        categories.add(xItr.next());
      }
    }
    int numBars = categories.size();
    int gridStep = (int) (xTickSpace / (double) numBars);

    // plot series
    int seriesCounter = 0;
    for (Integer seriesId : seriesMap.keySet()) {

      Series series = seriesMap.get(seriesId);

      // data points
      Collection<?> xData = series.getxData();

      Collection<Number> yData = series.getyData();
      BigDecimal yMin = getChartPainter().getAxisPair().getyAxis().getMin();
      BigDecimal yMax = getChartPainter().getAxisPair().getyAxis().getMax();

      // if min and max positive, set min to zero
      if (yMin.compareTo(BigDecimal.ZERO) > 0 && yMax.compareTo(BigDecimal.ZERO) > 0) {
        yMin = BigDecimal.ZERO;
      }
      // if min and max negative, set max to zero
      if (yMin.compareTo(BigDecimal.ZERO) < 0 && yMax.compareTo(BigDecimal.ZERO) < 0) {
        yMax = BigDecimal.ZERO;
      }

      // override min and maxValue if specified
      if (getChartPainter().getStyleManager().getYAxisMin() != null) {
        yMin = new BigDecimal(getChartPainter().getStyleManager().getYAxisMin());
      }
      else if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
        // int logMin = (int) Math.floor(Math.log10(getChartPainter().getAxisPair().getyAxis().getMin().doubleValue()));
        int logMin = (int) Math.floor(Math.log10(getChartPainter().getAxisPair().getyAxis().getMin().doubleValue()));
        // System.out.println("logMin: " + logMin);
        // System.out.println("min : " + getChartPainter().getAxisPair().getyAxis().getMin().doubleValue());
        // yMin = new BigDecimal(Math.log10(Utils.pow(10, logMin).doubleValue()));
        // yMin = new BigDecimal(Utils.pow(10, logMin).doubleValue());
        yMin = new BigDecimal(logMin);
      }
      if (getChartPainter().getStyleManager().getYAxisMax() != null) {
        yMax = new BigDecimal(getChartPainter().getStyleManager().getYAxisMax());
      }
      else if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
        yMax = new BigDecimal(Math.log10(yMax.doubleValue()));
      }
      // figure out the general form of the chart
      int chartForm = 1; // 1=positive, -1=negative, 0=span
      if (yMin.compareTo(BigDecimal.ZERO) > 0 && yMax.compareTo(BigDecimal.ZERO) > 0) {
        chartForm = 1; // positive chart
      }
      else if (yMin.compareTo(BigDecimal.ZERO) < 0 && yMax.compareTo(BigDecimal.ZERO) < 0) {
        chartForm = -1; // negative chart
      }
      else {
        chartForm = 0;// span chart
      }
      // System.out.println(yMin);
      // System.out.println(yMax);

      Iterator<?> categoryItr = categories.iterator();
      Iterator<Number> yItr = yData.iterator();

      int barCounter = 0;
      while (categoryItr.hasNext()) {

        if (xData.contains(categoryItr.next())) {

          BigDecimal y = new BigDecimal(yItr.next().doubleValue());
          if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
            y = new BigDecimal(Math.log10(y.doubleValue()));
          }
          else {
            y = new BigDecimal(y.doubleValue());
          }
          BigDecimal yTop = null;
          BigDecimal yBottom = null;

          switch (chartForm) {
          case 1: // positive chart
            yTop = new BigDecimal(y.doubleValue());
            yBottom = yMin;
            break;
          case -1: // negative chart
            yTop = yMax;
            yBottom = new BigDecimal(y.doubleValue());
            break;
          case 0: // span chart
            if (y.compareTo(BigDecimal.ZERO) >= 0) { // positive
              yTop = y;
              yBottom = BigDecimal.ZERO;
            }
            else {
              yTop = BigDecimal.ZERO;
              yBottom = y;
            }
            break;
          default:
            break;
          }

          double yTransform = bounds.getHeight() - (yTopMargin + yTop.subtract(yMin).doubleValue() / yMax.subtract(yMin).doubleValue() * yTickSpace);

          double yOffset = bounds.getY() + yTransform + 1;

          double zeroTransform = bounds.getHeight() - (yTopMargin + (yBottom.subtract(yMin).doubleValue()) / (yMax.subtract(yMin).doubleValue()) * yTickSpace);
          double zeroOffset = bounds.getY() + zeroTransform + 1;

          // paint bar
          double barWidth = gridStep / seriesMap.size() / 1.1;
          double barMargin = gridStep * .05;
          double xOffset = bounds.getX() + xLeftMargin + gridStep * barCounter++ + seriesCounter * barWidth + barMargin;
          g.setColor(series.getStrokeColor());

          Path2D.Double path = new Path2D.Double();
          path.moveTo(xOffset, yOffset);
          path.lineTo(xOffset + barWidth, yOffset);
          path.lineTo(xOffset + barWidth, zeroOffset);
          path.lineTo(xOffset, zeroOffset);
          path.closePath();
          g.fill(path);

        }
        else {
          barCounter++;
        }
      }
      seriesCounter++;
    }

  }

  @Override
  public ChartPainter getChartPainter() {

    return plot.getChartPainter();
  }

}
