package org.knowm.xchart.internal.chartpart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.knowm.xchart.BoxSeries;
import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.style.BoxStyler;

public class PlotContent_Box<ST extends BoxStyler, S extends BoxSeries>
    extends PlotContent_<ST, S> {

  private final ST boxPlotStyler;
  private final AxesChart<ST, S> axesChart;
  private double yMax;
  private double yMin;
  private double xLeftMargin;
  private double yTopMargin;
  private double yTickSpace;
  private double xOffset;
  private double yOffset;
  private double halfBoxWidth;

  PlotContent_Box(AxesChart<ST, S> chart) {

    super(chart);
    this.axesChart = chart;
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
    boolean toolTipsEnabled = interactionData != null;
    double gridStep = xTickSpace / chart.getSeriesMap().size();

    // Box half-width: legacy default is tied to the plot margin (independent of the number of
    // series); when a box width fraction is set, size each box relative to its per-series slot.
    double boxWidthFraction = boxPlotStyler.getBoxWidthFraction();
    if (boxWidthFraction > 0) {
      halfBoxWidth = Math.min(boxWidthFraction, 1.0) * gridStep / 2.0;
    } else {
      halfBoxWidth = xLeftMargin;
    }

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

      // skip series with no usable data (e.g. all nulls or empty after filtering)
      if (boxPlotData == null) {
        continue;
      }

      yMin = axesChart.getYAxis(series.getYAxisGroup()).getMin();
      yMax = axesChart.getYAxis(series.getYAxisGroup()).getMax();

      if (boxPlotStyler.isYAxisLogarithmic()) {
        yMin = Math.log10(yMin);
        yMax = Math.log10(yMax);
      }
      // data points
      double[] yArr = series.getYData();
      for (int dataIndex = 0; dataIndex < yArr.length; dataIndex++) {
        double yOrig = yArr[dataIndex];
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
            interactionData
                .addToolTip(
                    xOffset,
                    yOffset,
                    series.getName()
                        + ":"
                        + System.lineSeparator()
                        + axesChart.getYAxisFormat().format(yOrig))
                .withSeries(series, dataIndex);
          }
        } else if (chart.getStyler().getShowWithinAreaPoint()) {

          // Points drawn in box plot area, between lower limit and upper limit
          g.setColor(series.getMarkerColor());
          series.getMarker().paint(g, xOffset, yOffset, boxPlotStyler.getMarkerSize());

          if (toolTipsEnabled) {
            interactionData
                .addToolTip(
                    xOffset,
                    yOffset,
                    series.getName()
                        + ":"
                        + System.lineSeparator()
                        + axesChart.getYAxisFormat().format(yOrig))
                .withSeries(series, dataIndex);
          }
        }
      }

      drawBoxPlot(g, series, boxPlotData);
    }
  }

  private void drawBoxPlot(Graphics2D g, S series, BoxPlotData boxPlotData) {

    // when all data values are the same yMin == yMax; offsets would be NaN, so skip rendering
    if (yMax == yMin) {
      return;
    }

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
            xOffset - halfBoxWidth, medianYOffset, xOffset + halfBoxWidth, medianYOffset);
    Shape maxLine =
        new Line2D.Double(
            xOffset - (halfBoxWidth / 2.0),
            upperYOffset,
            xOffset + (halfBoxWidth / 2.0),
            upperYOffset);
    Shape minLine =
        new Line2D.Double(
            xOffset - (halfBoxWidth / 2.0),
            lowerYOffset,
            xOffset + (halfBoxWidth / 2.0),
            lowerYOffset);
    Shape upLine = new Line2D.Double(xOffset, upperYOffset, xOffset, q3YOffset);
    Shape lowLine = new Line2D.Double(xOffset, lowerYOffset, xOffset, q1YOffset);
    Rectangle2D rect =
        new Rectangle2D.Double(
            xOffset - halfBoxWidth, q3YOffset, 2.0 * halfBoxWidth, q1YOffset - q3YOffset);
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

    if (interactionData != null) {
      interactionData
          .addToolTip(
              area,
              xOffset,
              yOffset,
              10,
              series.getName()
                  + ":"
                  + System.lineSeparator()
                  + "upper: "
                  + axesChart.getYAxisFormat().format(boxPlotData.upper)
                  + System.lineSeparator()
                  + "q3: "
                  + axesChart.getYAxisFormat().format(boxPlotData.q3)
                  + System.lineSeparator()
                  + "median: "
                  + axesChart.getYAxisFormat().format(boxPlotData.median)
                  + System.lineSeparator()
                  + "q1: "
                  + axesChart.getYAxisFormat().format(boxPlotData.q1)
                  + System.lineSeparator()
                  + "lower: "
                  + axesChart.getYAxisFormat().format(boxPlotData.lower))
          // the box aggregates the whole series, so there is no single data point index
          .withSeries(series, -1);
    }
  }
}
