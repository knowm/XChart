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
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.knowm.xchart.Series_Category;
import org.knowm.xchart.Series_Category.ChartCategorySeriesRenderStyle;
import org.knowm.xchart.Styler_Category;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;

/**
 * @author timmolter
 */
public class PlotContent_Category_Line_Area_Scatter<ST extends Styler, S extends Series> extends PlotContent_ {

  Styler_Category stylerCategory;

  /**
   * Constructor
   *
   * @param plot
   */
  protected PlotContent_Category_Line_Area_Scatter(Chart<Styler_Category, Series_Category> chart) {

    super(chart);
    this.stylerCategory = chart.getStyler();
  }

  @Override
  public void paint(Graphics2D g) {

    // logarithmic
    // if (stylerCategory.isYAxisLogarithmic()) {
    // throw new IllegalArgumentException("Category Charts cannot have logarithmic axes!!! (Not Yet Implemented)");
    // }

    Rectangle2D bounds = getBounds();
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.red);
    // g.draw(bounds);

    // if the area to draw a chart on is so small, don't even bother
    if (bounds.getWidth() < 30) {
      return;
    }

    // this is for preventing the series to be drawn outside the plot area if min and max is overridden to fall inside the data range

    // Rectangle rectangle = g.getClipBounds();
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.green);
    // g.draw(rectangle);

    Rectangle2D rectangle = new Rectangle2D.Double(0, 0, chart.getWidth(), chart.getHeight());
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.green);
    // g.draw(rectangle);
    g.setClip(bounds.createIntersection(rectangle));

    // g.setClip(bounds.createIntersection(g.getClipBounds()));

    // X-Axis
    double xTickSpace = stylerCategory.getPlotContentSize() * bounds.getWidth();
    double xLeftMargin = Utils.getTickStartOffset((int) bounds.getWidth(), xTickSpace);

    // Y-Axis
    double yTickSpace = stylerCategory.getPlotContentSize() * bounds.getHeight();
    double yTopMargin = Utils.getTickStartOffset((int) bounds.getHeight(), yTickSpace);

    double xMin = chart.getAxisPair().getXAxis().getMin();
    double xMax = chart.getAxisPair().getXAxis().getMax();
    double yMin = chart.getAxisPair().getYAxis().getMin();
    double yMax = chart.getAxisPair().getYAxis().getMax();

    // logarithmic
    if (stylerCategory.isXAxisLogarithmic()) {
      xMin = Math.log10(xMin);
      xMax = Math.log10(xMax);
    }
    if (stylerCategory.isYAxisLogarithmic()) {
      yMin = Math.log10(yMin);
      yMax = Math.log10(yMax);
    }
    // System.out.println("yMin = " + yMin);
    // System.out.println("yMax = " + yMax);

    Map<String, Series_Category> seriesMap = chart.getSeriesMap();

    int numCategories = seriesMap.values().iterator().next().getXData().size();
    double gridStep = xTickSpace / numCategories;

    for (Series_Category series : seriesMap.values()) {

      // data points
      Collection<? extends Number> yData = series.getYData();

      double previousX = -Double.MAX_VALUE;
      double previousY = -Double.MAX_VALUE;

      Iterator<? extends Number> yItr = yData.iterator();
      Iterator<? extends Number> ebItr = null;
      Collection<? extends Number> errorBars = series.getErrorBars();
      if (errorBars != null) {
        ebItr = errorBars.iterator();
      }
      Path2D.Double path = null;

      int categoryCounter = 0;
      while (yItr.hasNext()) {

        Number next = yItr.next();
        if (next == null) {

          // for area charts
          closePath(g, path, previousX, bounds, yTopMargin);
          path = null;

          previousX = -Double.MAX_VALUE;
          previousY = -Double.MAX_VALUE;
          continue;
        }

        double yOrig = next.doubleValue();

        double y = 0.0;

        // System.out.println(y);
        if (stylerCategory.isYAxisLogarithmic()) {
          y = Math.log10(yOrig);
        }
        else {
          y = yOrig;
        }
        // System.out.println(y);

        double yTransform = bounds.getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);

        // a check if all y data are the exact same values
        if (Math.abs(yMax - yMin) / 5 == 0.0) {
          yTransform = bounds.getHeight() / 2.0;
        }

        double xOffset = bounds.getX() + xLeftMargin + categoryCounter++ * gridStep + gridStep / 2;
        double yOffset = bounds.getY() + yTransform;
        // System.out.println(xOffset);
        // System.out.println(yTransform);
        // System.out.println(yOffset);
        // System.out.println("---");

        // paint line
        if (ChartCategorySeriesRenderStyle.Line.equals(series.getChartCategorySeriesRenderStyle()) || ChartCategorySeriesRenderStyle.Area.equals(series.getChartCategorySeriesRenderStyle())) {

          if (series.getLineStyle() != SeriesLines.NONE) {

            if (previousX != -Double.MAX_VALUE && previousY != -Double.MAX_VALUE) {
              g.setColor(series.getLineColor());
              g.setStroke(series.getLineStyle());
              Shape line = new Line2D.Double(previousX, previousY, xOffset, yOffset);
              g.draw(line);
            }
          }
        }

        // paint area
        if (ChartCategorySeriesRenderStyle.Area.equals(series.getChartCategorySeriesRenderStyle())) {

          if (previousX != -Double.MAX_VALUE && previousY != -Double.MAX_VALUE) {

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

        // paint stick
        if (ChartCategorySeriesRenderStyle.Stick.equals(series.getChartCategorySeriesRenderStyle())) {

          if (series.getLineStyle() != SeriesLines.NONE) {

            double yBottomOfArea = bounds.getY() + bounds.getHeight() - yTopMargin;

            g.setColor(series.getLineColor());
            g.setStroke(series.getLineStyle());
            Shape line = new Line2D.Double(xOffset, yBottomOfArea, xOffset, yOffset);
            g.draw(line);
          }

        }

        previousX = xOffset;
        previousY = yOffset;

        // paint marker
        if (series.getMarker() != null) {
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, xOffset, yOffset, stylerCategory.getMarkerSize());
        }

        // paint error bars
        if (errorBars != null) {

          double eb = ebItr.next().doubleValue();

          // set error bar style
          if (stylerCategory.isErrorBarsColorSeriesColor()) {
            g.setColor(series.getLineColor());
          }
          else {
            g.setColor(stylerCategory.getErrorBarsColor());
          }
          g.setStroke(errorBarStroke);

          // Top value
          double topValue = 0.0;
          if (stylerCategory.isYAxisLogarithmic()) {
            topValue = yOrig + eb;
            topValue = Math.log10(topValue);
          }
          else {
            topValue = y + eb;
          }
          double topEBTransform = bounds.getHeight() - (yTopMargin + (topValue - yMin) / (yMax - yMin) * yTickSpace);
          double topEBOffset = bounds.getY() + topEBTransform;

          // Bottom value
          double bottomValue = 0.0;
          if (stylerCategory.isYAxisLogarithmic()) {
            bottomValue = yOrig - eb;
            // System.out.println(bottomValue);
            bottomValue = Math.log10(bottomValue);
          }
          else {
            bottomValue = y - eb;
          }
          double bottomEBTransform = bounds.getHeight() - (yTopMargin + (bottomValue - yMin) / (yMax - yMin) * yTickSpace);
          double bottomEBOffset = bounds.getY() + bottomEBTransform;

          // Draw it
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
