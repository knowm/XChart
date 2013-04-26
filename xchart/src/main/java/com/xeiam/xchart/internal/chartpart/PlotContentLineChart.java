/**
 * Copyright 2011-2013 Xeiam LLC.
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
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.xeiam.xchart.Series;
import com.xeiam.xchart.StyleManager.ChartType;
import com.xeiam.xchart.internal.Utils;
import com.xeiam.xchart.internal.chartpart.Axis.AxisType;

/**
 * @author timmolter
 */
public class PlotContentLineChart extends PlotContent {

  /**
   * Constructor
   * 
   * @param plot
   */
  protected PlotContentLineChart(Plot plot) {

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

    Map<Integer, Series> seriesMap = getChartPainter().getAxisPair().getSeriesMap();
    for (Integer seriesId : seriesMap.keySet()) {

      Series series = seriesMap.get(seriesId);

      // data points
      Collection<?> xData = series.getxData();
      BigDecimal xMin = getChartPainter().getAxisPair().getxAxis().getMin();
      BigDecimal xMax = getChartPainter().getAxisPair().getxAxis().getMax();

      Collection<Number> yData = series.getyData();
      BigDecimal yMin = getChartPainter().getAxisPair().getyAxis().getMin();
      BigDecimal yMax = getChartPainter().getAxisPair().getyAxis().getMax();

      // override min and maxValue if specified
      if (getChartPainter().getStyleManager().getXAxisMin() != null) {
        xMin = new BigDecimal(getChartPainter().getStyleManager().getXAxisMin());
      }
      if (getChartPainter().getStyleManager().getYAxisMin() != null) {
        yMin = new BigDecimal(getChartPainter().getStyleManager().getYAxisMin());
      }
      if (getChartPainter().getStyleManager().getXAxisMax() != null) {
        xMax = new BigDecimal(getChartPainter().getStyleManager().getXAxisMax());
      }
      if (getChartPainter().getStyleManager().getYAxisMax() != null) {
        yMax = new BigDecimal(getChartPainter().getStyleManager().getYAxisMax());
      }

      // logarithmic
      if (getChartPainter().getStyleManager().isXAxisLogarithmic()) {
        xMin = new BigDecimal(Math.log10(xMin.doubleValue()));
        xMax = new BigDecimal(Math.log10(xMax.doubleValue()));
      }
      if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
        yMin = new BigDecimal(Math.log10(yMin.doubleValue()));
        yMax = new BigDecimal(Math.log10(yMax.doubleValue()));
      }
      Collection<Number> errorBars = series.getErrorBars();

      double previousX = Integer.MIN_VALUE;
      double previousY = Integer.MIN_VALUE;

      Iterator<?> xItr = xData.iterator();
      Iterator<Number> yItr = yData.iterator();
      Iterator<Number> ebItr = null;
      if (errorBars != null) {
        ebItr = errorBars.iterator();
      }
      while (xItr.hasNext()) {

        BigDecimal x = null;
        if (getChartPainter().getAxisPair().getxAxis().getAxisType() == AxisType.Number) {
          x = new BigDecimal(((Number) xItr.next()).doubleValue());
        }
        if (getChartPainter().getAxisPair().getxAxis().getAxisType() == AxisType.Date) {
          x = new BigDecimal(((Date) xItr.next()).getTime());
          // System.out.println(x);
        }

        if (getChartPainter().getStyleManager().isXAxisLogarithmic()) {
          x = new BigDecimal(Math.log10(x.doubleValue()));
        }

        Number next = yItr.next();
        if (next == null) {
          previousX = Integer.MIN_VALUE;
          previousY = Integer.MIN_VALUE;
          continue;
        }
        BigDecimal yOrig = new BigDecimal(next.doubleValue());
        BigDecimal y = null;
        BigDecimal eb = BigDecimal.ZERO;

        if (errorBars != null) {
          eb = new BigDecimal(ebItr.next().doubleValue());
        }

        // System.out.println(y);
        if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
          y = new BigDecimal(Math.log10(yOrig.doubleValue()));
        } else {
          y = new BigDecimal(yOrig.doubleValue());
        }

        int xTransform = (int) (xLeftMargin + (x.subtract(xMin).doubleValue() / xMax.subtract(xMin).doubleValue() * xTickSpace));
        int yTransform = (int) (bounds.getHeight() - (yTopMargin + y.subtract(yMin).doubleValue() / yMax.subtract(yMin).doubleValue() * yTickSpace));

        // a check if all x data are the exact same values
        if (Math.abs(xMax.subtract(xMin).doubleValue()) / 5 == 0.0) {
          xTransform = (int) (bounds.getWidth() / 2.0);
        }

        // a check if all y data are the exact same values
        if (Math.abs(yMax.subtract(yMin).doubleValue()) / 5 == 0.0) {
          yTransform = (int) (bounds.getHeight() / 2.0);
        }

        double xOffset = bounds.getX() + xTransform - 1;
        double yOffset = bounds.getY() + yTransform;
        // System.out.println(yOffset);
        // System.out.println(yTransform);

        // paint line
        if (series.getStroke() != null && getChartPainter().getStyleManager().getChartType() != ChartType.Scatter) {

          if (previousX != Integer.MIN_VALUE && previousY != Integer.MIN_VALUE) {
            g.setColor(series.getStrokeColor());
            g.setStroke(series.getStroke());
            Shape line = new Line2D.Double(previousX, previousY, xOffset, yOffset);
            g.draw(line);
          }
        }

        // paint area
        if (getChartPainter().getStyleManager().getChartType() == ChartType.Area) {
          if (previousX != Integer.MIN_VALUE && previousY != Integer.MIN_VALUE) {
            g.setColor(series.getStrokeColor());
            int yBottomOfArea = (int) (bounds.getY() + bounds.getHeight() - yTopMargin + 1);

            Path2D.Double path = new Path2D.Double();
            path.moveTo(previousX, previousY);
            path.lineTo(xOffset, yOffset);
            path.lineTo(xOffset, yBottomOfArea);
            path.lineTo(previousX, yBottomOfArea);
            path.closePath();
            g.fill(path);
          }
        }

        previousX = xOffset;
        previousY = yOffset;

        // paint marker
        if (series.getMarker() != null) {
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, xOffset, yOffset);
        }

        // paint errorbar
        if (errorBars != null) {

          g.setColor(getChartPainter().getStyleManager().getErrorBarsColor());
          g.setStroke(errorBarStroke);

          BigDecimal topValue = null;
          if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
            topValue = yOrig.add(eb);
            topValue = new BigDecimal(Math.log10(topValue.doubleValue()));
          } else {
            topValue = y.add(eb);
          }
          int topEBTransform = (int) (bounds.getHeight() - (yTopMargin + topValue.subtract(yMin).doubleValue() / yMax.subtract(yMin).doubleValue() * yTickSpace));
          int topEBOffset = (int) (bounds.getY() + topEBTransform);

          BigDecimal bottomValue = null;
          if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
            bottomValue = yOrig.subtract(eb);
            // System.out.println(bottomValue);
            bottomValue = new BigDecimal(Math.log10(bottomValue.doubleValue()));
          } else {
            bottomValue = y.subtract(eb);
          }
          int bottomEBTransform = (int) (bounds.getHeight() - (yTopMargin + bottomValue.subtract(yMin).doubleValue() / yMax.subtract(yMin).doubleValue() * yTickSpace));
          int bottomEBOffset = (int) (bounds.getY() + bottomEBTransform);

          Shape line = new Line2D.Double(xOffset, topEBOffset, xOffset, bottomEBOffset);
          g.draw(line);
          line = new Line2D.Double(xOffset - 3, bottomEBOffset, xOffset + 3, bottomEBOffset);
          g.draw(line);
          line = new Line2D.Double(xOffset - 3, topEBOffset, xOffset + 3, topEBOffset);
          g.draw(line);
        }
      }
    }
  }
}
