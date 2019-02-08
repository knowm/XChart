package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.lines.SeriesLines;

/** @author timmolter */
public class PlotContent_Category_Line_Area_Scatter<
        ST extends CategoryStyler, S extends CategorySeries>
    extends PlotContent_<ST, S> {

  private final ST categoryStyler;

  /**
   * Constructor
   *
   * @param chart
   */
  PlotContent_Category_Line_Area_Scatter(Chart<ST, S> chart) {

    super(chart);
    this.categoryStyler = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    // X-Axis
    double xTickSpace = categoryStyler.getPlotContentSize() * getBounds().getWidth();
    double xLeftMargin = Utils.getTickStartOffset((int) getBounds().getWidth(), xTickSpace);

    // Y-Axis
    double yTickSpace = categoryStyler.getPlotContentSize() * getBounds().getHeight();
    double yTopMargin = Utils.getTickStartOffset((int) getBounds().getHeight(), yTickSpace);

    boolean toolTipsEnabled = chart.getStyler().isToolTipsEnabled();
    Map<String, S> seriesMap = chart.getSeriesMap();

    int numCategories = seriesMap.values().iterator().next().getXData().size();
    double gridStep = xTickSpace / numCategories;

    for (S series : seriesMap.values()) {

      if (!series.isEnabled()) {
        continue;
      }
      String[] toolTips = series.getToolTips();
      boolean hasCustomToolTips = toolTips != null;

      Axis yAxis = chart.getYAxis(series.getYAxisGroup());
      double yMin = yAxis.getMin();
      double yMax = yAxis.getMax();
      if (categoryStyler.isYAxisLogarithmic()) {
        yMin = Math.log10(yMin);
        yMax = Math.log10(yMax);
      }
      // System.out.println("yMin = " + yMin);
      // System.out.println("yMax = " + yMax);

      // data points
      Collection<? extends Number> yData = series.getYData();

      double previousX = -Double.MAX_VALUE;
      double previousY = -Double.MAX_VALUE;

      Iterator<?> xItr = series.getXData().iterator();
      Iterator<? extends Number> yItr = yData.iterator();
      Iterator<? extends Number> ebItr = null;
      Collection<? extends Number> errorBars = series.getExtraValues();
      if (errorBars != null) {
        ebItr = errorBars.iterator();
      }
      Path2D.Double path = null;

      int categoryCounter = -1;
      while (yItr.hasNext()) {

        categoryCounter++;

        Number next = yItr.next();
        if (next == null) {

          // for area charts
          closePath(g, path, previousX, getBounds(), yTopMargin);
          path = null;

          previousX = -Double.MAX_VALUE;
          previousY = -Double.MAX_VALUE;
          continue;
        }
        Object nextCat = xItr.next();

        double yOrig = next.doubleValue();

        double y;

        // System.out.println(y);
        if (categoryStyler.isYAxisLogarithmic()) {
          y = Math.log10(yOrig);
        } else {
          y = yOrig;
        }
        // System.out.println(y);

        double yTransform =
            getBounds().getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);

        // a check if all y data are the exact same values
        if (Math.abs(yMax - yMin) / 5 == 0.0) {
          yTransform = getBounds().getHeight() / 2.0;
        }

        double xOffset =
            getBounds().getX() + xLeftMargin + categoryCounter * gridStep + gridStep / 2;
        double yOffset = getBounds().getY() + yTransform;
        // System.out.println(xOffset);
        // System.out.println(yTransform);
        // System.out.println(yOffset);
        // System.out.println("---");

        // paint line
        // System.out.println(series.getChartCategorySeriesRenderStyle());
        if (CategorySeriesRenderStyle.Line.equals(series.getChartCategorySeriesRenderStyle())
            || CategorySeriesRenderStyle.Area.equals(series.getChartCategorySeriesRenderStyle())) {

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
          series.getMarker().paint(g, xOffset, yOffset, categoryStyler.getMarkerSize());
        }

        // paint error bars
        if (errorBars != null) {

          double eb = ebItr.next().doubleValue();

          // set error bar style
          if (categoryStyler.isErrorBarsColorSeriesColor()) {
            g.setColor(series.getLineColor());
          } else {
            g.setColor(categoryStyler.getErrorBarsColor());
          }
          g.setStroke(errorBarStroke);

          // Top value
          double topValue;
          if (categoryStyler.isYAxisLogarithmic()) {
            topValue = yOrig + eb;
            topValue = Math.log10(topValue);
          } else {
            topValue = y + eb;
          }
          double topEBTransform =
              getBounds().getHeight()
                  - (yTopMargin + (topValue - yMin) / (yMax - yMin) * yTickSpace);
          double topEBOffset = getBounds().getY() + topEBTransform;

          // Bottom value
          double bottomValue;
          if (categoryStyler.isYAxisLogarithmic()) {
            bottomValue = yOrig - eb;
            // System.out.println(bottomValue);
            bottomValue = Math.log10(bottomValue);
          } else {
            bottomValue = y - eb;
          }
          double bottomEBTransform =
              getBounds().getHeight()
                  - (yTopMargin + (bottomValue - yMin) / (yMax - yMin) * yTickSpace);
          double bottomEBOffset = getBounds().getY() + bottomEBTransform;

          // Draw it
          Shape line = new Line2D.Double(xOffset, topEBOffset, xOffset, bottomEBOffset);
          g.draw(line);
          line = new Line2D.Double(xOffset - 3, bottomEBOffset, xOffset + 3, bottomEBOffset);
          g.draw(line);
          line = new Line2D.Double(xOffset - 3, topEBOffset, xOffset + 3, topEBOffset);
          g.draw(line);
        }

        if (toolTipsEnabled) {
          if (hasCustomToolTips) {
            String tt = toolTips[categoryCounter];
            if (tt != null) {
              chart.toolTips.addData(xOffset, yOffset, tt);
            }
          } else {
            chart.toolTips.addData(
                xOffset,
                yOffset,
                chart.getXAxisFormat().format(nextCat),
                chart.getYAxisFormat().format(y));
          }
        }
      }

      // close any open path for area charts
      g.setColor(series.getFillColor());
      closePath(g, path, previousX, getBounds(), yTopMargin);
    }
  }
}
