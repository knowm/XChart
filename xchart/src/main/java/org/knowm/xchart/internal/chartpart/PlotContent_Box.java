package org.knowm.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.knowm.xchart.BoxSeries;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.BoxPlotStyler;

public class PlotContent_Box<ST extends BoxPlotStyler, S extends BoxSeries>
    extends PlotContent_<ST, S> {

  private final ST boxPlotStyler;
  Map<String, S> seriesMap = chart.getSeriesMap();
  int boxPlotCounter = -1;
  private double yMax;
  private double yMin;
  private double xLeftMargin;
  private double yTopMargin;
  private double yTickSpace;
  private double xOffset;
  private double yOffset;

  PlotContent_Box(Chart<ST, S> chart) {
    super(chart);
    boxPlotStyler = chart.getStyler();
  }

  @Override
  protected void doPaint(Graphics2D g) {

    int numBox = seriesMap.size();
    // X-Axis
    double xTickSpace = boxPlotStyler.getPlotContentSize() * getBounds().getWidth();
    xLeftMargin = Utils.getTickStartOffset((int) getBounds().getWidth(), xTickSpace);
    // Y-Axis
    yTickSpace = boxPlotStyler.getPlotContentSize() * getBounds().getHeight();
    yTopMargin = Utils.getTickStartOffset((int) getBounds().getHeight(), yTickSpace);
    boolean toolTipsEnabled = chart.getStyler().isToolTipsEnabled();
    double gridStep = xTickSpace / numBox;
    Double[][] boxDatas = new Double[numBox][BoxChartData.BOX_DATAS_LENGTH];

    // To get all box plot datas
    BoxChartData<ST, S> boxChartData = new BoxChartData<>();
    boxDatas = boxChartData.getBoxPlotData(seriesMap, boxPlotStyler);
    for (S series : seriesMap.values()) {

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
      Iterator<? extends Number> yItr = yData.iterator();
      Object nextCat = series.getName();
      Path2D.Double path = null;
      boxPlotCounter++;
      while (yItr.hasNext()) {

        Number next = yItr.next();

        if (next == null) {
          closePath(g, path, previousX, getBounds(), yTopMargin);
          path = null;
          previousX = -Double.MAX_VALUE;
          continue;
        }
        double yOrig = next.doubleValue();
        double y;

        if (boxPlotStyler.isYAxisLogarithmic()) {
          y = Math.log10(yOrig);
        } else {
          y = yOrig;
        }
        double yTransfrom =
            getBounds().getHeight() - (yTopMargin + (y - yMin) / (yMax - yMin) * yTickSpace);

        // a check if all y data are the exact same values
        if (Math.abs(yMax - yMin) / 5 == 0.0) {
          yTransfrom = getBounds().getHeight() / 2.0;
        }
        xOffset = getBounds().getX() + xLeftMargin + boxPlotCounter * gridStep + gridStep / 2.0;
        yOffset = getBounds().getY() + yTransfrom;

        // paint marker
        if (boxDatas[boxPlotCounter][BoxChartData.MAX_BOX_VALUE_INDEX] != null
            && (y > boxDatas[boxPlotCounter][BoxChartData.MAX_BOX_VALUE_INDEX]
                || y < boxDatas[boxPlotCounter][BoxChartData.MIN_BOX_VALUE_INDEX])) {

          Shape outPointLine1 =
              new Line2D.Double(
                  xOffset - boxPlotStyler.getMarkerSize(),
                  yOffset,
                  xOffset + boxPlotStyler.getMarkerSize(),
                  yOffset);
          Shape outPointLine2 =
              new Line2D.Double(
                  xOffset,
                  yOffset - boxPlotStyler.getMarkerSize(),
                  xOffset,
                  yOffset + boxPlotStyler.getMarkerSize());
          g.setColor(Color.RED);
          g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
          g.draw(outPointLine1);
          g.draw(outPointLine2);
        } else if (chart.getStyler().getShowWithinAreaPoint()) {
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, xOffset, yOffset, boxPlotStyler.getMarkerSize());
        }
        previousX = xOffset;

        // support toolTipsEnabled
        if (toolTipsEnabled && chart.getStyler().getShowWithinAreaPoint()) {
          if (hasCustomToolTips) {

            String tt = toolTips[boxPlotCounter];
            if (tt != null) {
              chart.toolTips.addData(xOffset, yOffset, tt);
            }
          } else {
            chart.toolTips.addData(
                xOffset,
                yOffset,
                chart.getXAxisFormat().format(nextCat),
                chart.getYAxisFormat().format(yOrig));
          }
        }
      }

      if (boxDatas[boxPlotCounter][BoxChartData.FIRST_PLOT_QUARTILES_INDEX] != null) {
        drawBoxPlot(g, boxDatas, hasCustomToolTips, toolTips, nextCat, toolTipsEnabled);
      }
      g.setColor(series.getFillColor());
      closePath(g, path, previousX, getBounds(), yTopMargin);
    }
  }

  private void drawBoxPlot(
      Graphics2D g,
      Double boxDatas[][],
      boolean hasCustomToolTips,
      String[] toolTips,
      Object nexCat,
      boolean toolTipsEnabled) {

    double firstPlotQuartiles = 0.0;
    double secondPlotQuartiles = 0.0;
    double thirdPlotQuartiles = 0.0;
    double maxBoxData = 0.0;
    double minBoxData = 0.0;
    firstPlotQuartiles =
        getBounds().getY()
            + getBounds().getHeight()
            - (yTopMargin
                + (boxDatas[boxPlotCounter][BoxChartData.FIRST_PLOT_QUARTILES_INDEX] - yMin)
                    / (yMax - yMin)
                    * yTickSpace);
    secondPlotQuartiles =
        getBounds().getY()
            + getBounds().getHeight()
            - (yTopMargin
                + (boxDatas[boxPlotCounter][BoxChartData.SECOND_PLOT_QUARTILES_INDEX] - yMin)
                    / (yMax - yMin)
                    * yTickSpace);
    thirdPlotQuartiles =
        getBounds().getY()
            + getBounds().getHeight()
            - (yTopMargin
                + (boxDatas[boxPlotCounter][BoxChartData.THIRD_PLOT_QUARTILES_INDEX] - yMin)
                    / (yMax - yMin)
                    * yTickSpace);
    maxBoxData =
        getBounds().getY()
            + getBounds().getHeight()
            - (yTopMargin
                + (boxDatas[boxPlotCounter][BoxChartData.MAX_BOX_VALUE_INDEX] - yMin)
                    / (yMax - yMin)
                    * yTickSpace);
    minBoxData =
        getBounds().getY()
            + getBounds().getHeight()
            - (yTopMargin
                + (boxDatas[boxPlotCounter][BoxChartData.MIN_BOX_VALUE_INDEX] - yMin)
                    / (yMax - yMin)
                    * yTickSpace);
    Shape middleline =
        new Line2D.Double(
            xOffset - xLeftMargin, secondPlotQuartiles, xOffset + xLeftMargin, secondPlotQuartiles);
    Shape maxLine =
        new Line2D.Double(
            xOffset - (xLeftMargin / 2.0), maxBoxData, xOffset + (xLeftMargin / 2.0), maxBoxData);
    Shape minLine =
        new Line2D.Double(
            xOffset - (xLeftMargin / 2.0), minBoxData, xOffset + (xLeftMargin / 2.0), minBoxData);
    Shape upLine = new Line2D.Double(xOffset, maxBoxData, xOffset, thirdPlotQuartiles);
    Shape lowLine = new Line2D.Double(xOffset, minBoxData, xOffset, firstPlotQuartiles);
    Rectangle2D rect =
        new Rectangle2D.Double(
            xOffset - xLeftMargin,
            thirdPlotQuartiles,
            2.0 * xLeftMargin,
            firstPlotQuartiles - thirdPlotQuartiles);
    g.setColor(Color.BLUE);
    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    g.draw(rect);
    g.setColor(Color.RED);
    g.draw(middleline);
    g.setColor(Color.BLACK);
    g.draw(maxLine);
    g.draw(minLine);
    g.setStroke(
        new BasicStroke(
            1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 6.0f, new float[] {4f, 0f, 4f}, 6f));
    g.draw(upLine);
    g.draw(lowLine);

    if (toolTipsEnabled
        && boxDatas[boxPlotCounter][BoxChartData.FIRST_PLOT_QUARTILES_INDEX] != null) {
      if (boxPlotStyler.isYAxisLogarithmic()) {
        boxDatas[boxPlotCounter][BoxChartData.FIRST_PLOT_QUARTILES_INDEX] =
            Math.pow(10, boxDatas[boxPlotCounter][BoxChartData.FIRST_PLOT_QUARTILES_INDEX]);
        boxDatas[boxPlotCounter][BoxChartData.SECOND_PLOT_QUARTILES_INDEX] =
            Math.pow(10, boxDatas[boxPlotCounter][BoxChartData.SECOND_PLOT_QUARTILES_INDEX]);
        boxDatas[boxPlotCounter][BoxChartData.THIRD_PLOT_QUARTILES_INDEX] =
            Math.pow(10, boxDatas[boxPlotCounter][BoxChartData.THIRD_PLOT_QUARTILES_INDEX]);
        boxDatas[boxPlotCounter][BoxChartData.MAX_BOX_VALUE_INDEX] =
            Math.pow(10, boxDatas[boxPlotCounter][BoxChartData.MAX_BOX_VALUE_INDEX]);
        boxDatas[boxPlotCounter][BoxChartData.MIN_BOX_VALUE_INDEX] =
            Math.pow(10, boxDatas[boxPlotCounter][BoxChartData.MIN_BOX_VALUE_INDEX]);
      }
      if (hasCustomToolTips) {

        String tt = toolTips[boxPlotCounter];

        if (tt != null) {
          chart.toolTips.addData(xOffset, firstPlotQuartiles, tt);
          chart.toolTips.addData(xOffset, secondPlotQuartiles, tt);
          chart.toolTips.addData(xOffset, thirdPlotQuartiles, tt);
          chart.toolTips.addData(xOffset, maxBoxData, tt);
          chart.toolTips.addData(xOffset, minBoxData, tt);
        }
      } else {
        chart.toolTips.addData(
            xOffset,
            firstPlotQuartiles,
            chart.getXAxisFormat().format(nexCat),
            chart
                .getYAxisFormat()
                .format(boxDatas[boxPlotCounter][BoxChartData.FIRST_PLOT_QUARTILES_INDEX]));
        chart.toolTips.addData(
            xOffset,
            secondPlotQuartiles,
            chart.getXAxisFormat().format(nexCat),
            chart
                .getYAxisFormat()
                .format(boxDatas[boxPlotCounter][BoxChartData.SECOND_PLOT_QUARTILES_INDEX]));
        chart.toolTips.addData(
            xOffset,
            thirdPlotQuartiles,
            chart.getXAxisFormat().format(nexCat),
            chart
                .getYAxisFormat()
                .format(boxDatas[boxPlotCounter][BoxChartData.THIRD_PLOT_QUARTILES_INDEX]));
        chart.toolTips.addData(
            xOffset,
            maxBoxData,
            chart.getXAxisFormat().format(nexCat),
            chart
                .getYAxisFormat()
                .format(boxDatas[boxPlotCounter][BoxChartData.MAX_BOX_VALUE_INDEX]));
        chart.toolTips.addData(
            xOffset,
            minBoxData,
            chart.getXAxisFormat().format(nexCat),
            chart
                .getYAxisFormat()
                .format(boxDatas[boxPlotCounter][BoxChartData.MIN_BOX_VALUE_INDEX]));
      }
    }
  }
}
