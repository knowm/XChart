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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;

import com.xeiam.xchart.Series;
import com.xeiam.xchart.StyleManager;
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
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.red);
    // g.draw(bounds);

    StyleManager styleManager = plot.getChartPainter().getStyleManager();

    // this is for preventing the series to be drawn outside the plot area if min and max is overridden to fall inside the data range
    Rectangle rectangle = new Rectangle(0, 0, getChartPainter().getWidth(), getChartPainter().getHeight());
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.green);
    // g.draw(rectangle);
    g.setClip(bounds.createIntersection(rectangle));

    // X-Axis
    double xTickSpace = styleManager.getAxisTickSpacePercentage() * bounds.getWidth();
    double xLeftMargin = Utils.getTickStartOffset(bounds.getWidth(), xTickSpace);

    // Y-Axis
    double yTickSpace = styleManager.getAxisTickSpacePercentage() * bounds.getHeight();
    double yTopMargin = Utils.getTickStartOffset(bounds.getHeight(), yTickSpace);

    int numBars = getChartPainter().getAxisPair().getSeriesMap().values().iterator().next().getXData().size();
    double gridStep = xTickSpace / numBars;

    // plot series
    int seriesCounter = 0;
    for (Series series : getChartPainter().getAxisPair().getSeriesMap().values()) {

      // data points
      // Collection<?> xData = series.getXData();

      Collection<? extends Number> yData = series.getYData();
      double yMin = getChartPainter().getAxisPair().getYAxis().getMin();
      double yMax = getChartPainter().getAxisPair().getYAxis().getMax();

      // if min and max positive, set min to zero
      if (yMin > 0.0 && yMax > 0.0) {
        yMin = 0.0;
      }
      // if min and max negative, set max to zero
      if (yMin < 0.0 && yMax < 0.0) {
        yMax = 0.0;
      }

      // override min and maxValue if specified
      if (getChartPainter().getStyleManager().getYAxisMin() != null) {
        yMin = getChartPainter().getStyleManager().getYAxisMin();
      }
      else if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
        // int logMin = (int) Math.floor(Math.log10(getChartPainter().getAxisPair().getyAxis().getMin().doubleValue()));
        double logMin = Math.floor(Math.log10(getChartPainter().getAxisPair().getYAxis().getMin()));
        // System.out.println("logMin: " + logMin);
        // System.out.println("min : " + getChartPainter().getAxisPair().getyAxis().getMin().doubleValue());
        yMin = logMin;
      }
      if (getChartPainter().getStyleManager().getYAxisMax() != null) {
        yMax = getChartPainter().getStyleManager().getYAxisMax();
      }
      else if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
        yMax = Math.log10(yMax);
      }
      // figure out the general form of the chart
      int chartForm = 1; // 1=positive, -1=negative, 0=span
      if (yMin > 0.0 && yMax > 0.0) {
        chartForm = 1; // positive chart
      }
      else if (yMin < 0.0 && yMax < 0.0) {
        chartForm = -1; // negative chart
      }
      else {
        chartForm = 0;// span chart
      }
      // System.out.println(yMin);
      // System.out.println(yMax);

      // all the x-axis data are guaranteed to be the same so we just use the first one
      Iterator<? extends Number> yItr = yData.iterator();

      int barCounter = 0;
      while (yItr.hasNext()) {

        double y = ((Number) yItr.next()).doubleValue();
        if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
          y = Math.log10(y);
        }

        double yTop = 0.0;
        double yBottom = 0.0;

        switch (chartForm) {
        case 1: // positive chart

          // check for points off the chart draw area due to a custom yMin
          if (y < yMin) {
            barCounter++;
            continue;
          }

          yTop = y;
          yBottom = yMin;
          break;
        case -1: // negative chart

          // check for points off the chart draw area due to a custom yMin
          if (y > yMax) {
            barCounter++;
            continue;
          }

          yTop = yMax;
          yBottom = y;
          break;
        case 0: // span chart
          if (y >= 0.0) { // positive
            yTop = y;
            yBottom = 0.0;
          }
          else {
            yTop = 0.0;
            yBottom = y;
          }
          break;
        default:
          break;
        }

        double yTransform = bounds.getHeight() - (yTopMargin + (yTop - yMin) / (yMax - yMin) * yTickSpace);

        double yOffset = bounds.getY() + yTransform;

        double zeroTransform = bounds.getHeight() - (yTopMargin + (yBottom - yMin) / (yMax - yMin) * yTickSpace);
        double zeroOffset = bounds.getY() + zeroTransform;

        // paint bar
        boolean isOverlap = true;
        double xOffset;
        double barWidth;
        if (getChartPainter().getStyleManager().isBarsOverlapped()) {
          double barWidthPercentage = getChartPainter().getStyleManager().getBarWidthPercentage();
          barWidth = gridStep * barWidthPercentage;
          double barMargin = gridStep * (1 - barWidthPercentage) / 2;
          xOffset = bounds.getX() + xLeftMargin + gridStep * barCounter++ + barMargin;
          g.setColor(series.getStrokeColor());
        }
        else {
          double barWidthPercentage = getChartPainter().getStyleManager().getBarWidthPercentage();
          barWidth = gridStep / getChartPainter().getAxisPair().getSeriesMap().size() * barWidthPercentage;
          double barMargin = gridStep * (1 - barWidthPercentage) / 2;
          xOffset = bounds.getX() + xLeftMargin + gridStep * barCounter++ + seriesCounter * barWidth + barMargin;
          g.setColor(series.getStrokeColor());
        }
        Path2D.Double path = new Path2D.Double();
        path.moveTo(xOffset, yOffset);
        path.lineTo(xOffset + barWidth, yOffset);
        path.lineTo(xOffset + barWidth, zeroOffset);
        path.lineTo(xOffset, zeroOffset);
        path.closePath();
        g.setStroke(series.getStroke());
        if (getChartPainter().getStyleManager().isBarFilled()) {
          g.fill(path);
        }
        else {
          g.draw(path);
        }

      }
      seriesCounter++;
    }
    g.setClip(null);
  }

  @Override
  public ChartPainter getChartPainter() {

    return plot.getChartPainter();
  }

}
