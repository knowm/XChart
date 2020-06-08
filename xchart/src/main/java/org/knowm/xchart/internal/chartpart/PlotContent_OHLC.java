package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import org.knowm.xchart.OHLCSeries;
import org.knowm.xchart.OHLCSeries.OHLCSeriesRenderStyle;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.lines.SeriesLines;

/** @author arthurmcgibbon */
public class PlotContent_OHLC<ST extends AxesChartStyler, S extends OHLCSeries>
    extends PlotContent_<ST, S> {

  private final ST ohlcStyler;

  /**
   * Constructor
   *
   * @param chart
   */
  PlotContent_OHLC(Chart<ST, S> chart) {

    super(chart);
    ohlcStyler = chart.getStyler();
  }

  @Override
  public void doPaint(Graphics2D g) {

    Map<Double, String> candleLabelMap = new HashMap<>();
    Map<Double, Area> candleAreaMap = new HashMap<>();
    Map<Double, String> lineLabelMap = new HashMap<>();

    // X-Axis
    double xTickSpace = ohlcStyler.getPlotContentSize() * getBounds().getWidth();
    double xLeftMargin = Utils.getTickStartOffset((int) getBounds().getWidth(), xTickSpace);

    // Y-Axis
    double yTickSpace = ohlcStyler.getPlotContentSize() * getBounds().getHeight();
    double yTopMargin = Utils.getTickStartOffset((int) getBounds().getHeight(), yTickSpace);

    double xMin = chart.getXAxis().getMin();
    double xMax = chart.getXAxis().getMax();

    Line2D.Double line = new Line2D.Double();
    Rectangle2D.Double rect = new Rectangle2D.Double();

    // logarithmic
    if (ohlcStyler.isXAxisLogarithmic()) {
      xMin = Math.log10(xMin);
      xMax = Math.log10(xMax);
    }

    Map<String, S> map = chart.getSeriesMap();
    boolean toolTipsEnabled = chart.getStyler().isToolTipsEnabled();

    for (S series : map.values()) {

      if (!series.isEnabled()) {
        continue;
      }

      String[] toolTips = series.getToolTips();

      if (series.getOhlcSeriesRenderStyle() == OHLCSeriesRenderStyle.Line) {
        paintLine(g, series, lineLabelMap);
        continue;
      }
      Axis yAxis = chart.getYAxis(series.getYAxisGroup());
      double yMin = yAxis.getMin();
      double yMax = yAxis.getMax();
      if (ohlcStyler.isYAxisLogarithmic()) {
        yMin = Math.log10(yMin);
        yMax = Math.log10(yMax);
      }

      // data points
      double[] xData = series.getXData();
      double[] openData = series.getOpenData();
      double[] highData = series.getHighData();
      double[] lowData = series.getLowData();
      double[] closeData = series.getCloseData();

      double candleHalfWidth =
          Math.max(3, xTickSpace / xData.length / 2 - ohlcStyler.getAxisTickPadding());
      float lineWidth = Math.max(2, series.getLineStyle().getLineWidth());

      for (int i = 0; i < xData.length; i++) {

        double x = xData[i];
        if (ohlcStyler.isXAxisLogarithmic()) {
          x = Math.log10(x);
        }

        if (Double.isNaN(closeData[i])) {
          continue;
        }

        double openOrig = openData[i];
        double highOrig = highData[i];
        double lowOrig = lowData[i];
        double closeOrig = closeData[i];

        double openY;
        double highY;
        double lowY;
        double closeY;

        // System.out.println(y);
        if (ohlcStyler.isYAxisLogarithmic()) {
          openY = Math.log10(openOrig);
          highY = Math.log10(highOrig);
          lowY = Math.log10(lowOrig);
          closeY = Math.log10(closeOrig);
        } else {
          openY = openOrig;
          highY = highOrig;
          lowY = lowOrig;
          closeY = closeOrig;
        }

        double xTransform = xLeftMargin + ((x - xMin) / (xMax - xMin) * xTickSpace);
        double openTransform =
            getBounds().getHeight() - (yTopMargin + (openY - yMin) / (yMax - yMin) * yTickSpace);
        double highTransform =
            getBounds().getHeight() - (yTopMargin + (highY - yMin) / (yMax - yMin) * yTickSpace);
        double lowTransform =
            getBounds().getHeight() - (yTopMargin + (lowY - yMin) / (yMax - yMin) * yTickSpace);
        double closeTransform =
            getBounds().getHeight() - (yTopMargin + (closeY - yMin) / (yMax - yMin) * yTickSpace);

        // a check if all x data are the exact same values
        if (Math.abs(xMax - xMin) / 5 == 0.0) {
          xTransform = getBounds().getWidth() / 2.0;
        }

        // a check if all y data are the exact same values
        if (Math.abs(yMax - yMin) / 5 == 0.0) {
          openTransform = getBounds().getHeight() / 2.0;
          highTransform = getBounds().getHeight() / 2.0;
          lowTransform = getBounds().getHeight() / 2.0;
          closeTransform = getBounds().getHeight() / 2.0;
        }

        double xOffset = getBounds().getX() + xTransform;
        double openOffset = getBounds().getY() + openTransform;
        double highOffset = getBounds().getY() + highTransform;
        double lowOffset = getBounds().getY() + lowTransform;
        double closeOffset = getBounds().getY() + closeTransform;

        Area toolTipArea = null;
        // paint line
        if (series.getLineStyle() != SeriesLines.NONE) {

          if (xOffset != -Double.MAX_VALUE
              && openOffset != -Double.MAX_VALUE
              && highOffset != -Double.MAX_VALUE
              && lowOffset != -Double.MAX_VALUE
              && closeOffset != -Double.MAX_VALUE) {

            if (series.getLineColor() != null) {
              g.setColor(series.getLineColor());
            } else {

              if (closeOrig > openOrig) {
                g.setColor(series.getUpColor());
              } else {
                g.setColor(series.getDownColor());
              }
            }
            g.setStroke(series.getLineStyle());
            // high to low line
            line.setLine(xOffset, highOffset, xOffset, lowOffset);
            g.draw(line);
            final double xStart = xOffset - candleHalfWidth;
            final double xEnd = xOffset + candleHalfWidth;
            if (toolTipsEnabled) {
              rect.setRect(xOffset - lineWidth / 2, highOffset, lineWidth, lowOffset - highOffset);
              toolTipArea = new Area(rect);
            }
            if (series.getOhlcSeriesRenderStyle() == OHLCSeries.OHLCSeriesRenderStyle.Candle) {
              // candle style
              if (closeOrig > openOrig) {
                g.setPaint(series.getUpColor());
              } else {
                g.setPaint(series.getDownColor());
              }
              rect.setRect(
                  xStart,
                  Math.min(openOffset, closeOffset),
                  xEnd - xStart,
                  Math.abs(closeOffset - openOffset));
              g.fill(rect);
              // add data labels
              if (toolTipsEnabled) {
                toolTipArea.add(new Area(rect));
              }

            } else {
              // lines only
              line.setLine(xStart, openOffset, xOffset, openOffset);
              g.draw(line);
              line.setLine(xOffset, closeOffset, xEnd, closeOffset);
              g.draw(line);
              if (toolTipsEnabled) {
                rect.setRect(xStart, openOffset - lineWidth / 2, xOffset - xStart, lineWidth);
                toolTipArea.add(new Area(rect));
                rect.setRect(xOffset, closeOffset - lineWidth / 2, xEnd - xOffset, lineWidth);
                toolTipArea.add(new Area(rect));
              }
            }
          }
        }

        // add data labels
        if (toolTipsEnabled) {
          if (series.isCustomToolTips()) {
            if (toolTips != null) {
              String tt = toolTips[i];
              if (tt != null && !"".equals(tt)) {
                chart.toolTips.addData(toolTipArea, xOffset, highOffset, candleHalfWidth * 2, tt);
              }
            }
          } else {

            StringBuilder sb = new StringBuilder();
            if (series.getVolumeData() != null) {
              sb.append(chart.getXAxisFormat().format(x));
              sb.append(System.lineSeparator()).append("Volume: " + series.getVolumeData()[i]);
              sb.append(System.lineSeparator()).append(" ").append(System.lineSeparator());
            }
            sb.append(chart.getXAxisFormat().format(x));
            sb.append(System.lineSeparator()).append(series.getName()).append(":");
            sb.append(System.lineSeparator())
                .append("open: ")
                .append(chart.getYAxisFormat().format(openOrig));
            sb.append(System.lineSeparator())
                .append("close: ")
                .append(chart.getYAxisFormat().format(closeOrig));
            sb.append(System.lineSeparator())
                .append("low: ")
                .append(chart.getYAxisFormat().format(lowOrig));
            sb.append(System.lineSeparator())
                .append("high: ")
                .append(chart.getYAxisFormat().format(highOrig));
            candleLabelMap.put(x, sb.toString());
            candleAreaMap.put(x, toolTipArea);
          }
        }
      }

      // close any open path for area charts
      g.setColor(series.getFillColor());
    }

    if (!candleLabelMap.isEmpty()) {
      for (double key : candleLabelMap.keySet()) {
        if (lineLabelMap.get(key) != null && !"".equals(lineLabelMap.get(key))) {
          chart.toolTips.addData(
              candleAreaMap.get(key),
              0,
              0,
              0,
              candleLabelMap.get(key) + System.lineSeparator() + lineLabelMap.get(key));
        } else {
          chart.toolTips.addData(candleAreaMap.get(key), 0, 0, 0, candleLabelMap.get(key));
        }
      }
    }
  }

  private void paintLine(Graphics2D g, S series, Map<Double, String> lineLabelMap) {

    Line2D.Double line = new Line2D.Double();

    double xMin = chart.getXAxis().getMin();
    double xMax = chart.getXAxis().getMax();
    double yMin = chart.getYAxis(series.getYAxisGroup()).getMin();
    double yMax = chart.getYAxis(series.getYAxisGroup()).getMax();

    double xTickSpace = ohlcStyler.getPlotContentSize() * getBounds().getWidth();
    double xLeftMargin = Utils.getTickStartOffset((int) getBounds().getWidth(), xTickSpace);
    double yTickSpace = ohlcStyler.getPlotContentSize() * getBounds().getHeight();
    double yTopMargin = Utils.getTickStartOffset((int) getBounds().getHeight(), yTickSpace);

    double x = 0.0;
    double y = 0.0;
    double[] xData = series.getXData();
    double[] yData = series.getYData();
    double xOffset = 0.0;
    double yOffset = 0.0;
    double previousX = -Double.MAX_VALUE;
    double previousY = -Double.MAX_VALUE;
    for (int i = 0; i < xData.length; i++) {
      x = xData[i];
      y = yData[i];
      if (Double.isNaN(y)) {
        previousX = -Double.MAX_VALUE;
        previousY = -Double.MAX_VALUE;
        continue;
      }
      xOffset = getBounds().getX() + xLeftMargin + ((x - xMin) / (xMax - xMin) * xTickSpace);

      yOffset =
          getBounds().getY()
              + getBounds().getHeight()
              - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);

      if (previousX != -Double.MAX_VALUE && previousY != -Double.MAX_VALUE) {
        g.setColor(series.getLineColor());
        g.setStroke(series.getLineStyle());
        line.setLine(previousX, previousY, xOffset, yOffset);
        g.draw(line);
      }

      if (series.getMarker() != null) {
        g.setColor(series.getMarkerColor());
        series.getMarker().paint(g, xOffset, yOffset, chart.getStyler().getMarkerSize());
      }

      if (chart.getStyler().isToolTipsEnabled()) {

        if (lineLabelMap.get(x) == null) {
          lineLabelMap.put(x, series.getName() + ": " + chart.getYAxisFormat().format(y));
        } else {
          lineLabelMap.put(
              x,
              lineLabelMap.get(x)
                  + System.lineSeparator()
                  + series.getName()
                  + ": "
                  + chart.getYAxisFormat().format(y));
        }
      }
      previousX = xOffset;
      previousY = yOffset;
    }
  }
}
