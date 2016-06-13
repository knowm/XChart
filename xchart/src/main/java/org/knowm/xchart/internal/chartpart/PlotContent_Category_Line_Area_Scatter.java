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
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.internal.Series;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.lines.SeriesLines;

/**
 * @author timmolter
 */
public class PlotContent_Category_Line_Area_Scatter<ST extends Styler, S extends Series> extends PlotContent_ {

  CategoryStyler stylerCategory;

  /**
   * Constructor
   *
   * @param plot
   */
  protected PlotContent_Category_Line_Area_Scatter(Chart<CategoryStyler, CategorySeries> chart) {

    super(chart);
    this.stylerCategory = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    // logarithmic
    // if (stylerCategory.isYAxisLogarithmic()) {
    // throw new IllegalArgumentException("Category Charts cannot have logarithmic axes!!! (Not Yet Implemented)");
    // }

    // X-Axis
    double xTickSpace = stylerCategory.getPlotContentSize() * getBounds().getWidth();
    double xLeftMargin = Utils.getTickStartOffset((int) getBounds().getWidth(), xTickSpace);

    // Y-Axis
    double yTickSpace = stylerCategory.getPlotContentSize() * getBounds().getHeight();
    double yTopMargin = Utils.getTickStartOffset((int) getBounds().getHeight(), yTickSpace);

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

    Map<String, CategorySeries> seriesMap = chart.getSeriesMap();

    int numCategories = seriesMap.values().iterator().next().getXData().size();
    double gridStep = xTickSpace / numCategories;

    for (CategorySeries series : seriesMap.values()) {

      // data points
      Collection<? extends Number> yData = series.getYData();

      double previousX = -Double.MAX_VALUE;
      double previousY = -Double.MAX_VALUE;

      Iterator<? extends Number> yItr = yData.iterator();
      Iterator<? extends Number> ebItr = null;
      Collection<? extends Number> errorBars = series.getExtraValues();
      if (errorBars != null) {
        ebItr = errorBars.iterator();
      }
      Path2D.Double path = null;

      int categoryCounter = 0;
      while (yItr.hasNext()) {

        Number next = yItr.next();
        if (next == null) {

          // for area charts
          closePath(g, path, previousX, getBounds(), yTopMargin);
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

        double yTransform = getBounds().getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);

        // a check if all y data are the exact same values
        if (Math.abs(yMax - yMin) / 5 == 0.0) {
          yTransform = getBounds().getHeight() / 2.0;
        }

        double xOffset = getBounds().getX() + xLeftMargin + categoryCounter++ * gridStep + gridStep / 2;
        double yOffset = getBounds().getY() + yTransform;
        // System.out.println(xOffset);
        // System.out.println(yTransform);
        // System.out.println(yOffset);
        // System.out.println("---");

        // paint line
        if (CategorySeriesRenderStyle.Line.equals(series.getChartCategorySeriesRenderStyle()) || CategorySeriesRenderStyle.Area.equals(series.getChartCategorySeriesRenderStyle())) {

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
        if (CategorySeriesRenderStyle.Area.equals(series.getChartCategorySeriesRenderStyle())) {

          if (previousX != -Double.MAX_VALUE && previousY != -Double.MAX_VALUE) {

            g.setColor(series.getFillColor());
            double yBottomOfArea = getBounds().getY() + getBounds().getHeight() - yTopMargin;

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
        if (CategorySeriesRenderStyle.Stick.equals(series.getChartCategorySeriesRenderStyle())) {

          if (series.getLineStyle() != SeriesLines.NONE) {

            double yBottomOfArea = getBounds().getY() + getBounds().getHeight() - yTopMargin;

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
          double topEBTransform = getBounds().getHeight() - (yTopMargin + (topValue - yMin) / (yMax - yMin) * yTickSpace);
          double topEBOffset = getBounds().getY() + topEBTransform;

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
          double bottomEBTransform = getBounds().getHeight() - (yTopMargin + (bottomValue - yMin) / (yMax - yMin) * yTickSpace);
          double bottomEBOffset = getBounds().getY() + bottomEBTransform;

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
      closePath(g, path, previousX, getBounds(), yTopMargin);
    }

  }

}
