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
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.xeiam.xchart.Series;
import com.xeiam.xchart.StyleManager;
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
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.red);
    // g.draw(bounds);

    StyleManager styleManager = plot.getChartPainter().getStyleManager();

    // this is for preventing the series to be drawn outside the plot area if min and max is overridden to fall inside the data range

    // Rectangle rectangle = g.getClipBounds();
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.green);
    // g.draw(rectangle);

    Rectangle rectangle = new Rectangle(0, 0, getChartPainter().getWidth(), getChartPainter().getHeight());
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.green);
    // g.draw(rectangle);
    g.setClip(bounds.createIntersection(rectangle));

    // g.setClip(bounds.createIntersection(g.getClipBounds()));

    // X-Axis
    double xTickSpace = styleManager.getAxisTickSpacePercentage() * bounds.getWidth();
    double xLeftMargin = Utils.getTickStartOffset((int) bounds.getWidth(), xTickSpace);

    // Y-Axis
    double yTickSpace = styleManager.getAxisTickSpacePercentage() * bounds.getHeight();
    double yTopMargin = Utils.getTickStartOffset((int) bounds.getHeight(), yTickSpace);

    for (Series series : getChartPainter().getAxisPair().getSeriesMap().values()) {

      // data points
      Collection<?> xData = series.getXData();
      // System.out.println(xData);
      double xMin = getChartPainter().getAxisPair().getXAxis().getMin();
      double xMax = getChartPainter().getAxisPair().getXAxis().getMax();

      Collection<? extends Number> yData = series.getYData();
      double yMin = getChartPainter().getAxisPair().getYAxis().getMin();
      double yMax = getChartPainter().getAxisPair().getYAxis().getMax();

      // override min and maxValue if specified
      if (getChartPainter().getStyleManager().getXAxisMin() != null) {
        xMin = getChartPainter().getStyleManager().getXAxisMin();
      }
      if (getChartPainter().getStyleManager().getYAxisMin() != null) {
        yMin = getChartPainter().getStyleManager().getYAxisMin();
      }
      if (getChartPainter().getStyleManager().getXAxisMax() != null) {
        xMax = getChartPainter().getStyleManager().getXAxisMax();
      }
      if (getChartPainter().getStyleManager().getYAxisMax() != null) {
        yMax = getChartPainter().getStyleManager().getYAxisMax();
      }

      // logarithmic
      if (getChartPainter().getStyleManager().isXAxisLogarithmic()) {
        xMin = Math.log10(xMin);
        xMax = Math.log10(xMax);
      }
      if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
        yMin = Math.log10(yMin);
        yMax = Math.log10(yMax);
      }
      Collection<? extends Number> errorBars = series.getErrorBars();

      double previousX = Integer.MIN_VALUE;
      double previousY = Integer.MIN_VALUE;

      Iterator<?> xItr = xData.iterator();
      Iterator<? extends Number> yItr = yData.iterator();
      Iterator<? extends Number> ebItr = null;
      if (errorBars != null) {
        ebItr = errorBars.iterator();
      }
      Path2D.Double path = null;

      while (xItr.hasNext()) {

        double x = 0.0;
        if (getChartPainter().getAxisPair().getXAxis().getAxisType() == AxisType.Number) {
          x = ((Number) xItr.next()).doubleValue();
          // System.out.println(x);
        }
        else if (getChartPainter().getAxisPair().getXAxis().getAxisType() == AxisType.Date) {
          x = ((Date) xItr.next()).getTime();
          // System.out.println(x);
        }

        if (getChartPainter().getStyleManager().isXAxisLogarithmic()) {
          x = Math.log10(x);
        }

        Number next = yItr.next();
        if (next == null) {

          // for area charts
          closePath(g, path, previousX, bounds, yTopMargin);
          path = null;

          previousX = Integer.MIN_VALUE;
          previousY = Integer.MIN_VALUE;
          continue;
        }

        double yOrig = next.doubleValue();

        double y = 0.0;

        // System.out.println(y);
        if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
          y = Math.log10(yOrig);
        }
        else {
          y = yOrig;
        }
        // System.out.println(y);

        double xTransform = xLeftMargin + ((x - xMin) / (xMax - xMin) * xTickSpace);
        double yTransform = bounds.getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);

        // a check if all x data are the exact same values
        if (Math.abs(xMax - xMin) / 5 == 0.0) {
          xTransform = bounds.getWidth() / 2.0;
        }

        // a check if all y data are the exact same values
        if (Math.abs(yMax - yMin) / 5 == 0.0) {
          yTransform = bounds.getHeight() / 2.0;
        }

        double xOffset = bounds.getX() + xTransform;
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
        if (getChartPainter().getStyleManager().getChartType() == ChartType.Area || Series.SeriesType.Area.equals(series.getSeriesType())) {

          if (previousX != Integer.MIN_VALUE && previousY != Integer.MIN_VALUE) {

            g.setColor(series.getFillColor());
            double yBottomOfArea = bounds.getY() + bounds.getHeight() - yTopMargin;

            if (path == null) {
              path = new Path2D.Double();
              path.moveTo(previousX, yBottomOfArea);
              path.lineTo(previousX, previousY);
            }
            path.lineTo(xOffset, yOffset);
          }
          if (xOffset < previousX) {
            throw new RuntimeException("X-Data must be in ascending order for Area Charts!!!");
          }
        }

        previousX = xOffset;
        previousY = yOffset;

        // paint marker
        if (series.getMarker() != null) {
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, xOffset, yOffset, getChartPainter().getStyleManager().getMarkerSize());
        }

        // paint errorbars

        double eb = 0.0;

        if (errorBars != null) {
          eb = ebItr.next().doubleValue();
        }

        if (errorBars != null) {

          g.setColor(getChartPainter().getStyleManager().getErrorBarsColor());
          g.setStroke(errorBarStroke);

          double topValue = 0.0;
          if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
            topValue = yOrig + eb;
            topValue = Math.log10(topValue);
          }
          else {
            topValue = y + eb;
          }
          double topEBTransform = bounds.getHeight() - (yTopMargin + (topValue - yMin) / (yMax - yMin) * yTickSpace);
          double topEBOffset = bounds.getY() + topEBTransform;

          double bottomValue = 0.0;
          if (getChartPainter().getStyleManager().isYAxisLogarithmic()) {
            bottomValue = yOrig - eb;
            // System.out.println(bottomValue);
            bottomValue = Math.log10(bottomValue);
          }
          else {
            bottomValue = y - eb;
          }
          double bottomEBTransform = bounds.getHeight() - (yTopMargin + (bottomValue - yMin) / (yMax - yMin) * yTickSpace);
          double bottomEBOffset = bounds.getY() + bottomEBTransform;

          Shape line = new Line2D.Double(xOffset, topEBOffset, xOffset, bottomEBOffset);
          g.draw(line);
          line = new Line2D.Double(xOffset - 3, bottomEBOffset, xOffset + 3, bottomEBOffset);
          g.draw(line);
          line = new Line2D.Double(xOffset - 3, topEBOffset, xOffset + 3, topEBOffset);
          g.draw(line);
        }
      }

      // close any open path for area charts
      closePath(g, path, previousX, bounds, yTopMargin);
    }

    g.setClip(null);
  }

  /**
   * Closes a path for area charts if one is available.
   */
  private void closePath(Graphics2D g, Path2D.Double path, double previousX, Rectangle2D bounds, double yTopMargin) {

    if (path != null) {
      double yBottomOfArea = bounds.getY() + bounds.getHeight() - yTopMargin;
      path.lineTo(previousX, yBottomOfArea);
      path.closePath();
      g.fill(path);
    }
  }

}
