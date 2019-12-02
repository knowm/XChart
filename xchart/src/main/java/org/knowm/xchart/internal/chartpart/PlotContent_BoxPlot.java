package org.knowm.xchart.internal.chartpart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.knowm.xchart.BoxPlotSeries;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.BoxPlotStyler;

public class PlotContent_BoxPlot<ST extends BoxPlotStyler, S extends BoxPlotSeries> extends PlotContent_<ST, S> {

  private final ST boxPlotStyler;
  Map<String, S> seriesMap = chart.getSeriesMap();
  private double yMax;
  private double yMin;
  private double yOrig;
  private double xLeftMargin;
  private double yTopMargin;
  private double yTickSpace;
  private double xOffset;
  private double yOffset;
  private double firstPlotQuartiles;
  private double secondPlotQuartiles;
  private double thirdPlotQuartiles;
  private double maxBoxData;
  private double minBoxData;

  PlotContent_BoxPlot(Chart<ST, S> chart) {
    super(chart);
    boxPlotStyler = chart.getStyler();
  }

  @SuppressWarnings("rawtypes")
  @Override
  protected void doPaint(Graphics2D g) {
    int numBox = seriesMap.values().iterator().next().getXData().size();
    int xDataSize = Utils.getAxesChartSeriesXDataSize(seriesMap);
    // X-Axis
    double xTickSpace = boxPlotStyler.getPlotContentSize() * getBounds().getWidth();
    xLeftMargin = Utils.getTickStartOffset((int) getBounds().getWidth(), xTickSpace);
    // Y-Axis
    yTickSpace = boxPlotStyler.getPlotContentSize() * getBounds().getHeight();
    yTopMargin = Utils.getTickStartOffset((int) getBounds().getHeight(), yTickSpace);
    boolean toolTipsEnabled = chart.getStyler().isToolTipsEnabled();
    double gridStep = xTickSpace / numBox;
    Double[][] boxDatas = new Double[xDataSize][BoxPlotData.BoxDatas.ALL_BOX_DATAS.getIndex()];

    // To get all box plot datas
    if (seriesMap.size() >= 3) {
      BoxPlotData<ST, S> boxPlotData = new BoxPlotData<>();
      boxDatas = boxPlotData.getBoxPlotData(seriesMap, boxPlotStyler);
    }

    int boxCounter = -1;
    for (S series : seriesMap.values()) {
      boxCounter++;

      if (!series.isEnabled()) {
        continue;
      }
      String[] toolTips = series.getToolTips();
      boolean hasCustomToolTips = toolTips != null;
      Axis yAxis = chart.getYAxis(series.getYAxisGroup());
      yMin = yAxis.getMin();
      yMax = yAxis.getMax();

      if (boxPlotStyler.isYAxisLogarithmic()) {
        yMin = Math.log10(yMin);
        yMax = Math.log10(yMax);
      }
      // data points
      Collection<? extends Number> yData = series.getYData();
      double previousX = Double.MAX_VALUE;
      Iterator<?> xItr = series.getXData().iterator();
      Iterator<? extends Number> yItr = yData.iterator();
      Iterator<? extends Number> ebItr = null;
      Collection<? extends Number> errorBars = series.getExtraValues();

      if (errorBars != null) {
        ebItr = errorBars.iterator();
      }
      Path2D.Double path = null;
      int boxPlotCounter = -1;

      while (yItr.hasNext()) {
        boxPlotCounter++;
        Number next = yItr.next();

        if (next == null) {
          closePath(g, path, previousX, getBounds(), yTopMargin);
          path = null;
          previousX = -Double.MAX_VALUE;
          continue;
        }
        Object nextCat = xItr.next();
        yOrig = next.doubleValue();
        double y;

        if (boxPlotStyler.isYAxisLogarithmic()) {
          y = Math.log10(yOrig);
        } else {
          y = yOrig;
        }
        double yTransfrom = getBounds().getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);

        // a check if all y data are the exact same values
        if (Math.abs(yMax - yMin) / 5 == 0.0) {
          yTransfrom = getBounds().getHeight() / 2.0;
        }
        xOffset = getBounds().getX() + xLeftMargin + boxPlotCounter * gridStep + gridStep / 2.0;
        yOffset = getBounds().getY() + yTransfrom;

        // Draw the box chart only once
        // Box plot seriesMap.size() masts greater than 3
        if (boxCounter == 0 && seriesMap.size() >= 3) {

          if (boxDatas[boxPlotCounter][BoxPlotData.BoxDatas.FIRST_PLOT_QUARTILES.getIndex()] != null) {
            drawBoxPlot(g, boxDatas, boxPlotCounter);
          }
        }

        // paint marker
        if (series.getMarker() != null) {
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, xOffset, yOffset, boxPlotStyler.getMarkerSize());
        }
        previousX = xOffset;

        // paint error bars
        if (errorBars != null) {
          drawErrorBars(g, series, ebItr, y);
        }

        // support toolTipsEnabled
        if (toolTipsEnabled) {
          showToolTips(hasCustomToolTips, toolTips, boxPlotCounter, boxDatas, nextCat);
        }
      }
      g.setColor(series.getFillColor());
      closePath(g, path, previousX, getBounds(), yTopMargin);
    }
  }

  private void drawBoxPlot(Graphics2D g, Double getBoxDatas[][], int boxPlotCounter) {

    firstPlotQuartiles = getBounds().getY() + getBounds().getHeight()
        - (yTopMargin + (getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.FIRST_PLOT_QUARTILES.getIndex()] - yMin)
            / (yMax - yMin) * yTickSpace);
    secondPlotQuartiles = getBounds().getY() + getBounds().getHeight()
        - (yTopMargin + (getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.SECOND_PLOT_QUARTILES.getIndex()] - yMin)
            / (yMax - yMin) * yTickSpace);
    thirdPlotQuartiles = getBounds().getY() + getBounds().getHeight()
        - (yTopMargin + (getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.THIRD_PLOT_QUARTILES.getIndex()] - yMin)
            / (yMax - yMin) * yTickSpace);
    maxBoxData = getBounds().getY() + getBounds().getHeight()
        - (yTopMargin + (getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.MAX_BOX_VALUE.getIndex()] - yMin)
            / (yMax - yMin) * yTickSpace);
    minBoxData = getBounds().getY() + getBounds().getHeight()
        - (yTopMargin + (getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.MIN_BOX_VALUE.getIndex()] - yMin)
            / (yMax - yMin) * yTickSpace);
    Shape middleline = new Line2D.Double(xOffset - xLeftMargin, secondPlotQuartiles, xOffset + xLeftMargin,
        secondPlotQuartiles);
    Shape maxLine = new Line2D.Double(xOffset - (xLeftMargin / 2.0), maxBoxData, xOffset + (xLeftMargin / 2.0),
        maxBoxData);
    Shape minLine = new Line2D.Double(xOffset - (xLeftMargin / 2.0), minBoxData, xOffset + (xLeftMargin / 2.0),
        minBoxData);
    Shape line = new Line2D.Double(xOffset, maxBoxData, xOffset, minBoxData);
    g.setColor(Color.black);
    g.draw(middleline);
    g.draw(maxLine);
    g.draw(minLine);
    g.draw(line);
    Rectangle2D rect = new Rectangle2D.Double(xOffset - xLeftMargin, thirdPlotQuartiles, 2.0 * xLeftMargin,
        firstPlotQuartiles - thirdPlotQuartiles);
    g.draw(rect);
  }

  private void drawErrorBars(Graphics2D g, S series, Iterator<? extends Number> ebItr, double y) {

    double eb = ebItr.next().doubleValue();

    // set error style
    if (boxPlotStyler.isErrorBarsColorSeriesColor()) {
      g.setColor(series.getLineColor());
    } else {
      g.setColor(boxPlotStyler.getErrorBarsColor());
    }
    g.setStroke(errorBarStroke);
    // top value
    double topValue;

    if (boxPlotStyler.isYAxisLogarithmic()) {
      topValue = yOrig + eb;
      topValue = Math.log10(topValue);
    } else {
      topValue = y + eb;
    }
    double topEBTransfrom = getBounds().getHeight() - (yTopMargin + (topValue - yMin) / (yMax - yMin) * yTickSpace);
    double topEBOffset = getBounds().getY() + topEBTransfrom;
    // bottom value
    double bottomValue;

    if (boxPlotStyler.isYAxisLogarithmic()) {
      bottomValue = yOrig - eb;
      bottomValue = Math.log10(bottomValue);
    } else {
      bottomValue = y - eb;
    }
    double bottomEBTransfrom = getBounds().getHeight()
        - (yTopMargin + (bottomValue - yMin) / (yMax - yMin) * yTickSpace);
    double bottomEBOffset = getBounds().getY() + bottomEBTransfrom;
    // draw it
    Shape line = new Line2D.Double(xOffset, topEBOffset, xOffset, bottomEBOffset);
    g.draw(line);
    line = new Line2D.Double(xOffset - 3, bottomEBOffset, xOffset + 3, bottomEBOffset);
    g.draw(line);
    line = new Line2D.Double(xOffset - 3, topEBOffset, xOffset + 3, topEBOffset);
    g.draw(line);
  }

  private void showToolTips(boolean hasCustomToolTips, String[] toolTips, int boxPlotCounter, Double getBoxDatas[][],
      Object nextCat) {

    if (hasCustomToolTips) {
      String tt = toolTips[boxPlotCounter];

      if (tt != null) {

        if (seriesMap.size() >= 3
            && getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.FIRST_PLOT_QUARTILES.getIndex()] != null) {
          chart.toolTips.addData(xOffset, firstPlotQuartiles, tt);
          chart.toolTips.addData(xOffset, secondPlotQuartiles, tt);
          chart.toolTips.addData(xOffset, thirdPlotQuartiles, tt);
          chart.toolTips.addData(xOffset, maxBoxData, tt);
          chart.toolTips.addData(xOffset, minBoxData, tt);
        }
        chart.toolTips.addData(xOffset, yOffset, tt);
      }
    } else {

      if (seriesMap.size() >= 3
          && getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.FIRST_PLOT_QUARTILES.getIndex()] != null) {

        if (boxPlotStyler.isYAxisLogarithmic()) {
          getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.FIRST_PLOT_QUARTILES.getIndex()] = Math.pow(10,
              getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.FIRST_PLOT_QUARTILES.getIndex()]);
          getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.SECOND_PLOT_QUARTILES.getIndex()] = Math.pow(10,
              getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.SECOND_PLOT_QUARTILES.getIndex()]);
          getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.THIRD_PLOT_QUARTILES.getIndex()] = Math.pow(10,
              getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.THIRD_PLOT_QUARTILES.getIndex()]);
          getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.MAX_BOX_VALUE.getIndex()] = Math.pow(10,
              getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.MAX_BOX_VALUE.getIndex()]);
          getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.MIN_BOX_VALUE.getIndex()] = Math.pow(10,
              getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.MIN_BOX_VALUE.getIndex()]);
        }
        chart.toolTips.addData(xOffset, firstPlotQuartiles, chart.getXAxisFormat().format(nextCat),
            chart.getYAxisFormat()
                .format(getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.FIRST_PLOT_QUARTILES.getIndex()]));
        chart.toolTips.addData(xOffset, secondPlotQuartiles, chart.getXAxisFormat().format(nextCat),
            chart.getYAxisFormat()
                .format(getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.SECOND_PLOT_QUARTILES.getIndex()]));
        chart.toolTips.addData(xOffset, thirdPlotQuartiles, chart.getXAxisFormat().format(nextCat),
            chart.getYAxisFormat()
                .format(getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.THIRD_PLOT_QUARTILES.getIndex()]));
        chart.toolTips.addData(xOffset, maxBoxData, chart.getXAxisFormat().format(nextCat),
            chart.getYAxisFormat().format(getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.MAX_BOX_VALUE.getIndex()]));
        chart.toolTips.addData(xOffset, minBoxData, chart.getXAxisFormat().format(nextCat),
            chart.getYAxisFormat().format(getBoxDatas[boxPlotCounter][BoxPlotData.BoxDatas.MIN_BOX_VALUE.getIndex()]));
      }
      chart.toolTips.addData(xOffset, yOffset, chart.getXAxisFormat().format(nextCat),
          chart.getYAxisFormat().format(yOrig));
    }
  }
}
