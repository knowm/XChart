package org.knowm.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.knowm.xchart.BoxSeries;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.BoxStyler;

public class PlotContent_Box<ST extends BoxStyler, S extends BoxSeries>
    extends PlotContent_<ST, S> {

  private final ST boxPlotStyler;
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

    // X-Axis
    double xTickSpace = boxPlotStyler.getPlotContentSize() * getBounds().getWidth();
    xLeftMargin = Utils.getTickStartOffset((int) getBounds().getWidth(), xTickSpace);
    // Y-Axis
    yTickSpace = boxPlotStyler.getPlotContentSize() * getBounds().getHeight();
    yTopMargin = Utils.getTickStartOffset((int) getBounds().getHeight(), yTickSpace);
    boolean toolTipsEnabled = chart.getStyler().isToolTipsEnabled();
    double gridStep = xTickSpace / chart.getSeriesMap().size();

    BoxPlotDataCalculator<ST, S> boxPlotDataCalculator = new BoxPlotDataCalculator<>();
    // Calculate box plot data for all series
    List<BoxPlotData> boxPlotDataList =
        boxPlotDataCalculator.calculate(chart.getSeriesMap(), boxPlotStyler);
    BoxPlotData boxPlotData = null;
    int boxPlotCounter = -1;
    for (S series : chart.getSeriesMap().values()) {

      if (!series.isEnabled()) {
        continue;
      }
      boxPlotCounter++;
      boxPlotData = boxPlotDataList.get(boxPlotCounter);
      yMin = chart.getYAxis(series.getYAxisGroup()).getMin();
      yMax = chart.getYAxis(series.getYAxisGroup()).getMax();

      if (boxPlotStyler.isYAxisLogarithmic()) {
        yMin = Math.log10(yMin);
        yMax = Math.log10(yMax);
      }
      // data points
      Collection<? extends Number> yData = series.getYData();
      Iterator<? extends Number> yItr = yData.iterator();
      while (yItr.hasNext()) {

        Number next = yItr.next();

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

        // Points drawn outside box plot area, not within the lower limit to the upper limit
        if (yOrig > boxPlotData.upper || yOrig < boxPlotData.lower) {

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

          if (toolTipsEnabled) {
            chart.toolTips.addData(
                xOffset,
                yOffset,
                series.getName()
                    + ":"
                    + System.lineSeparator()
                    + chart.getYAxisFormat().format(yOrig));
          }
        } else if (chart.getStyler().getShowWithinAreaPoint()) {

          // Points drawn in box plot area, between lower limit and upper limit
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, xOffset, yOffset, boxPlotStyler.getMarkerSize());

          if (toolTipsEnabled) {
            chart.toolTips.addData(
                xOffset,
                yOffset,
                series.getName()
                    + ":"
                    + System.lineSeparator()
                    + chart.getYAxisFormat().format(yOrig));
          }
        }
      }

      drawBoxPlot(g, series.getName(), boxPlotData);
    }
  }

  private void drawBoxPlot(Graphics2D g, String seriesName, BoxPlotData boxPlotData) {

    double q1YOffset =
        getBounds().getY()
            + getBounds().getHeight()
            - (yTopMargin
                + ((boxPlotStyler.isYAxisLogarithmic()
                            ? Math.log10(boxPlotData.q1)
                            : boxPlotData.q1)
                        - yMin)
                    / (yMax - yMin)
                    * yTickSpace);
    double medianYOffset =
        getBounds().getY()
            + getBounds().getHeight()
            - (yTopMargin
                + ((boxPlotStyler.isYAxisLogarithmic()
                            ? Math.log10(boxPlotData.median)
                            : boxPlotData.median)
                        - yMin)
                    / (yMax - yMin)
                    * yTickSpace);
    double q3YOffset =
        getBounds().getY()
            + getBounds().getHeight()
            - (yTopMargin
                + ((boxPlotStyler.isYAxisLogarithmic()
                            ? Math.log10(boxPlotData.q3)
                            : boxPlotData.q3)
                        - yMin)
                    / (yMax - yMin)
                    * yTickSpace);
    double upperYOffset =
        getBounds().getY()
            + getBounds().getHeight()
            - (yTopMargin
                + ((boxPlotStyler.isYAxisLogarithmic()
                            ? Math.log10(boxPlotData.upper)
                            : boxPlotData.upper)
                        - yMin)
                    / (yMax - yMin)
                    * yTickSpace);
    double lowerYOffset =
        getBounds().getY()
            + getBounds().getHeight()
            - (yTopMargin
                + ((boxPlotStyler.isYAxisLogarithmic()
                            ? Math.log10(boxPlotData.lower)
                            : boxPlotData.lower)
                        - yMin)
                    / (yMax - yMin)
                    * yTickSpace);
    Shape middleline =
        new Line2D.Double(
            xOffset - xLeftMargin, medianYOffset, xOffset + xLeftMargin, medianYOffset);
    Shape maxLine =
        new Line2D.Double(
            xOffset - (xLeftMargin / 2.0),
            upperYOffset,
            xOffset + (xLeftMargin / 2.0),
            upperYOffset);
    Shape minLine =
        new Line2D.Double(
            xOffset - (xLeftMargin / 2.0),
            lowerYOffset,
            xOffset + (xLeftMargin / 2.0),
            lowerYOffset);
    Shape upLine = new Line2D.Double(xOffset, upperYOffset, xOffset, q3YOffset);
    Shape lowLine = new Line2D.Double(xOffset, lowerYOffset, xOffset, q1YOffset);
    Rectangle2D rect =
        new Rectangle2D.Double(
            xOffset - xLeftMargin, q3YOffset, 2.0 * xLeftMargin, q1YOffset - q3YOffset);
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

    Area area = new Area();
    area.add(new Area(maxLine.getBounds()));
    area.add(new Area(maxLine.getBounds()));
    area.add(new Area(minLine.getBounds()));
    area.add(new Area(upLine.getBounds()));
    area.add(new Area(lowLine.getBounds()));
    area.add(new Area(rect.getBounds()));

    if (boxPlotStyler.isToolTipsEnabled()) {
      chart.toolTips.addData(
          area,
          xOffset,
          yOffset,
          10,
          seriesName
              + ":"
              + System.lineSeparator()
              + "upper: "
              + chart.getYAxisFormat().format(boxPlotData.upper)
              + System.lineSeparator()
              + "q3: "
              + chart.getYAxisFormat().format(boxPlotData.q3)
              + System.lineSeparator()
              + "median: "
              + chart.getYAxisFormat().format(boxPlotData.median)
              + System.lineSeparator()
              + "q1: "
              + chart.getYAxisFormat().format(boxPlotData.q1)
              + System.lineSeparator()
              + "lower: "
              + chart.getYAxisFormat().format(boxPlotData.lower));
    }
  }
}
