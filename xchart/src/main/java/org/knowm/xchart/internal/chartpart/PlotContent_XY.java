package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Map;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.lines.SeriesLines;

/** @author timmolter */
public class PlotContent_XY<ST extends AxesChartStyler, S extends XYSeries>
    extends PlotContent_<ST, S> {

  private final ST xyStyler;

  /**
   * Constructor
   *
   * @param chart
   */
  PlotContent_XY(Chart<ST, S> chart) {

    super(chart);
    xyStyler = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    // X-Axis
    double xTickSpace = xyStyler.getPlotContentSize() * getBounds().getWidth();
    double xLeftMargin = Utils.getTickStartOffset((int) getBounds().getWidth(), xTickSpace);

    // Y-Axis
    double yTickSpace = xyStyler.getPlotContentSize() * getBounds().getHeight();
    double yTopMargin = Utils.getTickStartOffset((int) getBounds().getHeight(), yTickSpace);

    double xMin = chart.getXAxis().getMin();
    double xMax = chart.getXAxis().getMax();

    Line2D.Double line = new Line2D.Double();

    // logarithmic
    if (xyStyler.isXAxisLogarithmic()) {
      xMin = Math.log10(xMin);
      xMax = Math.log10(xMax);
    }

    Map<String, S> map = chart.getSeriesMap();

    for (S series : map.values()) {

      if (!series.isEnabled()) {
        continue;
      }
      Axis yAxis = chart.getYAxis(series.getYAxisGroup());
      double yMin = yAxis.getMin();
      double yMax = yAxis.getMax();
      if (xyStyler.isYAxisLogarithmic()) {
        yMin = Math.log10(yMin);
        yMax = Math.log10(yMax);
      }

      // data points
      double[] xData = series.getXData();
      double[] yData = series.getYData();

      double previousX = -Double.MAX_VALUE;
      double previousY = -Double.MAX_VALUE;

      double[] errorBars = series.getExtraValues();
      Path2D.Double path = null;

      boolean toolTipsEnabled = chart.getStyler().isToolTipsEnabled();
      String[] toolTips = series.getToolTips();
      boolean hasCustomToolTips = toolTips != null;

      for (int i = 0; i < xData.length; i++) {

        double x = xData[i];
        // System.out.println(x);
        if (xyStyler.isXAxisLogarithmic()) {
          x = Math.log10(x);
        }
        // System.out.println(x);

        double next = yData[i];
        if (Double.isNaN(next)) {

          // for area charts
          closePath(g, path, previousX, getBounds(), yTopMargin);
          path = null;

          previousX = -Double.MAX_VALUE;
          previousY = -Double.MAX_VALUE;
          continue;
        }

        double yOrig = yData[i];

        double y;

        // System.out.println(y);
        if (xyStyler.isYAxisLogarithmic()) {
          y = Math.log10(yOrig);
        } else {
          y = yOrig;
        }
        // System.out.println(y);

        double xTransform = xLeftMargin + ((x - xMin) / (xMax - xMin) * xTickSpace);
        double yTransform =
            getBounds().getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);

        // a check if all x data are the exact same values
        if (Math.abs(xMax - xMin) / 5 == 0.0) {
          xTransform = getBounds().getWidth() / 2.0;
        }

        // a check if all y data are the exact same values
        if (Math.abs(yMax - yMin) / 5 == 0.0) {
          yTransform = getBounds().getHeight() / 2.0;
        }

        double xOffset = getBounds().getX() + xTransform;
        double yOffset = getBounds().getY() + yTransform;
        // System.out.println(xTransform);
        // System.out.println(xOffset);
        // System.out.println(yTransform);
        // System.out.println(yOffset);
        // System.out.println("---");

        // paint line

        boolean isSeriesLineOrArea =
            XYSeriesRenderStyle.Line == series.getXYSeriesRenderStyle()
                || XYSeriesRenderStyle.Area == series.getXYSeriesRenderStyle();
        boolean isSeriesStepLineOrStepArea =
            XYSeriesRenderStyle.Step == series.getXYSeriesRenderStyle()
                || XYSeriesRenderStyle.StepArea == series.getXYSeriesRenderStyle();

        if (isSeriesLineOrArea || isSeriesStepLineOrStepArea) {
          if (series.getLineStyle() != SeriesLines.NONE) {

            if (previousX != -Double.MAX_VALUE && previousY != -Double.MAX_VALUE) {
              g.setColor(series.getLineColor());
              g.setStroke(series.getLineStyle());
              if (isSeriesLineOrArea) {
                line.setLine(previousX, previousY, xOffset, yOffset);
                g.draw(line);
              } else {
                if (previousX != xOffset) {
                  line.setLine(previousX, previousY, xOffset, previousY);
                  g.draw(line);
                }
                if (previousY != yOffset) {
                  line.setLine(xOffset, previousY, xOffset, yOffset);
                  g.draw(line);
                }
              }
            }
          }
        }

        // paint area
        if (XYSeriesRenderStyle.Area == series.getXYSeriesRenderStyle()
            || XYSeriesRenderStyle.StepArea == series.getXYSeriesRenderStyle()) {

          if (previousX != -Double.MAX_VALUE && previousY != -Double.MAX_VALUE) {

            double yBottomOfArea = getBounds().getY() + getBounds().getHeight() - yTopMargin;

            if (path == null) {
              path = new Path2D.Double();
              path.moveTo(previousX, yBottomOfArea);
              path.lineTo(previousX, previousY);
            }
            if (XYSeriesRenderStyle.Area == series.getXYSeriesRenderStyle()) {
              path.lineTo(xOffset, yOffset);
            } else {
              if (previousX != xOffset) {
                path.lineTo(xOffset, previousY);
              }
              if (previousY != yOffset) {
                path.lineTo(xOffset, yOffset);
              }
            }
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
          series.getMarker().paint(g, xOffset, yOffset, xyStyler.getMarkerSize());
        }

        // paint error bars
        if (errorBars != null) {

          double eb = errorBars[i];

          // set error bar style
          if (xyStyler.isErrorBarsColorSeriesColor()) {
            g.setColor(series.getLineColor());
          } else {
            g.setColor(xyStyler.getErrorBarsColor());
          }
          g.setStroke(errorBarStroke);

          // Top value
          double topValue;
          if (xyStyler.isYAxisLogarithmic()) {
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
          if (xyStyler.isYAxisLogarithmic()) {
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
          line.setLine(xOffset, topEBOffset, xOffset, bottomEBOffset);
          g.draw(line);
          line.setLine(xOffset - 3, bottomEBOffset, xOffset + 3, bottomEBOffset);
          g.draw(line);
          line.setLine(xOffset - 3, topEBOffset, xOffset + 3, topEBOffset);
          g.draw(line);
        }

        // add data labels
        if (toolTipsEnabled) {
          if (hasCustomToolTips) {
            String tt = toolTips[i];
            if (tt != null) {
              chart.toolTips.addData(xOffset, yOffset, tt);
            }
          } else {
            chart.toolTips.addData(
                xOffset,
                yOffset,
                chart.getXAxisFormat().format(x),
                chart.getYAxisFormat().format(yOrig));
          }
        }
      }

      // close any open path for area charts
      g.setColor(series.getFillColor());
      closePath(g, path, previousX, getBounds(), yTopMargin);
    }
  }
}
