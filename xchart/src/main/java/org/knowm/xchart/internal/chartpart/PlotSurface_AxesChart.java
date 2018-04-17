package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.AxesChartStyler;

/**
 * Draws the plot background, the plot border and the horizontal and vertical grid lines
 *
 * @author timmolter
 */
public class PlotSurface_AxesChart<ST extends AxesChartStyler, S extends Series>
    extends PlotSurface_<ST, S> {

  private final ST stylerAxesChart;

  /**
   * Constructor
   *
   * @param chart
   */
  PlotSurface_AxesChart(Chart<ST, S> chart) {

    super(chart);
    this.stylerAxesChart = chart.getStyler();
  }

  @Override
  public void paint(Graphics2D g) {

    Rectangle2D bounds = getBounds();

    // paint plot background
    Shape rect =
        new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    g.setColor(stylerAxesChart.getPlotBackgroundColor());
    g.fill(rect);
    // paint grid lines and/or inner plot ticks

    // horizontal

    if (stylerAxesChart.isPlotGridHorizontalLinesVisible()) {

      List<Double> yAxisTickLocations = chart.getYAxis().getAxisTickCalculator().getTickLocations();
      for (Double yAxisTickLocation : yAxisTickLocations) {
        double yOffset = bounds.getY() + bounds.getHeight() - yAxisTickLocation;

        if (yOffset > bounds.getY() && yOffset < bounds.getY() + bounds.getHeight()) {

          // draw lines
          g.setColor(stylerAxesChart.getPlotGridLinesColor());
          g.setStroke(stylerAxesChart.getPlotGridLinesStroke());
          Shape line =
              stylerAxesChart
                  .getPlotGridLinesStroke()
                  .createStrokedShape(
                      new Line2D.Double(
                          bounds.getX(), yOffset, bounds.getX() + bounds.getWidth(), yOffset));
          // g.setStroke(axesChartStyler.getPlotGridLinesStroke());
          // Shape line = new Line2D.Double(bounds.getX(), yOffset, bounds.getX() +
          // bounds.getWidth(), yOffset);
          g.draw(line);
          // g.drawLine((int) bounds.getX(), (int) yOffset, (int) (bounds.getX() +
          // bounds.getWidth()), (int) yOffset);
        }
      }
    }

    // horizontal tick marks
    if (stylerAxesChart.isPlotTicksMarksVisible()) {

      // draw left side
      List<Double> yAxisTickLocations =
          chart.getAxisPair().getLeftMainYAxis().getAxisTickCalculator().getTickLocations();
      for (Double yAxisTickLocation : yAxisTickLocations) {
        double yOffset = bounds.getY() + bounds.getHeight() - yAxisTickLocation;

        if (yOffset > bounds.getY() && yOffset < bounds.getY() + bounds.getHeight()) {

          // tick marks
          g.setColor(stylerAxesChart.getAxisTickMarksColor());
          g.setStroke(stylerAxesChart.getAxisTickMarksStroke());
          Shape line =
              new Line2D.Double(
                  bounds.getX(),
                  yOffset,
                  bounds.getX() + stylerAxesChart.getAxisTickMarkLength(),
                  yOffset);
          g.draw(line);
        }
      }

      // draw right side
      yAxisTickLocations =
          chart.getAxisPair().getRightMainYAxis().getAxisTickCalculator().getTickLocations();
      for (Double yAxisTickLocation : yAxisTickLocations) {
        double yOffset = bounds.getY() + bounds.getHeight() - yAxisTickLocation;

        if (yOffset > bounds.getY() && yOffset < bounds.getY() + bounds.getHeight()) {

          // tick marks
          g.setColor(stylerAxesChart.getAxisTickMarksColor());
          g.setStroke(stylerAxesChart.getAxisTickMarksStroke());
          Shape line =
              new Line2D.Double(
                  bounds.getX() + bounds.getWidth(),
                  yOffset,
                  bounds.getX() + bounds.getWidth() - stylerAxesChart.getAxisTickMarkLength(),
                  yOffset);
          g.draw(line);
        }
      }
    }

    // vertical

    if (stylerAxesChart.isPlotGridVerticalLinesVisible()
        || stylerAxesChart.isPlotTicksMarksVisible()) {

      List<Double> xAxisTickLocations = chart.getXAxis().getAxisTickCalculator().getTickLocations();
      for (Double xAxisTickLocation : xAxisTickLocations) {

        double tickLocation = xAxisTickLocation;
        double xOffset = bounds.getX() + tickLocation;

        if (xOffset > bounds.getX() && xOffset < bounds.getX() + bounds.getWidth()) {

          // draw lines
          if (stylerAxesChart.isPlotGridVerticalLinesVisible()) {

            g.setColor(stylerAxesChart.getPlotGridLinesColor());
            g.setStroke(stylerAxesChart.getPlotGridLinesStroke());
            // g.setStroke(axesChartStyler.getPlotGridLinesStroke());
            // System.out.println();
            Shape line =
                stylerAxesChart
                    .getPlotGridLinesStroke()
                    .createStrokedShape(
                        new Line2D.Double(
                            xOffset, bounds.getY(), xOffset, bounds.getY() + bounds.getHeight()));
            // Shape line = new Line2D.Double(xOffset, bounds.getY(), xOffset, bounds.getY() +
            // bounds.getHeight());
            g.draw(line);
          }
          // tick marks
          if (stylerAxesChart.isPlotTicksMarksVisible()) {

            g.setColor(stylerAxesChart.getAxisTickMarksColor());
            g.setStroke(stylerAxesChart.getAxisTickMarksStroke());

            Shape line =
                new Line2D.Double(
                    xOffset,
                    bounds.getY(),
                    xOffset,
                    bounds.getY() + stylerAxesChart.getAxisTickMarkLength());
            g.draw(line);
            line =
                new Line2D.Double(
                    xOffset,
                    bounds.getY() + bounds.getHeight(),
                    xOffset,
                    bounds.getY() + bounds.getHeight() - stylerAxesChart.getAxisTickMarkLength());
            g.draw(line);
          }
        }
      }
    }

    // paint plot border
    if (stylerAxesChart.isPlotBorderVisible()) {
      g.setColor(stylerAxesChart.getPlotBorderColor());
      g.setStroke(SOLID_STROKE);
      g.draw(rect);
    }
  }
}
