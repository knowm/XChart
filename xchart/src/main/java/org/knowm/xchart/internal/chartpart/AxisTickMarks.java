package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.style.AxesChartStyler;
import org.knowm.xchart.style.Styler.YAxisPosition;

/** Axis tick marks. This includes the little tick marks and the line that hugs the plot area. */
public class AxisTickMarks<ST extends AxesChartStyler, S extends AxesChartSeries>
    implements ChartPart {

  private final Chart<ST, S> chart;
  private final Direction direction;
  private final Axis yAxis;
  private Rectangle2D bounds;

  /**
   * Constructor
   *
   * @param chart
   * @param direction
   */
  AxisTickMarks(Chart<ST, S> chart, Direction direction, Axis yAxis) {

    this.chart = chart;
    this.direction = direction;
    this.yAxis = yAxis;
  }

  @Override
  public void paint(Graphics2D g) {

    ST styler = chart.getStyler();
    g.setColor(styler.getAxisTickMarksColor());
    g.setStroke(styler.getAxisTickMarksStroke());

    if (direction == Axis.Direction.Y && styler.isYAxisTicksVisible()) { // Y-Axis

      int axisTickMarkLength = styler.getAxisTickMarkLength();

      boolean onRight = styler.getYAxisGroupPosistion(yAxis.getYIndex()) == YAxisPosition.Right;

      Rectangle2D yAxisBounds = yAxis.getBounds();
      Rectangle2D axisTickLabelBounds = yAxis.getAxisTick().getAxisTickLabels().getBounds();
      double xOffset;
      double lineXOffset;
      if (onRight) {
        xOffset = axisTickLabelBounds.getX() - styler.getAxisTickPadding() - axisTickMarkLength;
        lineXOffset = xOffset;
      } else {
        xOffset =
            axisTickLabelBounds.getX()
                + axisTickLabelBounds.getWidth()
                + styler.getAxisTickPadding();
        lineXOffset = xOffset + axisTickMarkLength;
      }

      double yOffset = yAxisBounds.getY();

      // bounds
      bounds =
          new Rectangle2D.Double(
              xOffset,
              yOffset,
              chart.getStyler().getAxisTickMarkLength(),
              yAxis.getBounds().getHeight());
      // g.setColor(Color.yellow);
      // g.draw(bounds);

      // tick marks
      if (styler.isAxisTicksMarksVisible()) {

        for (int i = 0; i < yAxis.getAxisTickCalculator().getTickLabels().size(); i++) {

          double tickLocation = yAxis.getAxisTickCalculator().getTickLocations().get(i);
          double flippedTickLocation = yOffset + yAxisBounds.getHeight() - tickLocation;
          if (flippedTickLocation > bounds.getY()
              && flippedTickLocation < bounds.getY() + bounds.getHeight()) {

            Shape line =
                new Line2D.Double(
                    xOffset,
                    flippedTickLocation,
                    xOffset + axisTickMarkLength,
                    flippedTickLocation);
            g.draw(line);
          }
        }
      }

      // Line
      if (styler.isAxisTicksLineVisible()) {

        Shape line =
            new Line2D.Double(lineXOffset, yOffset, lineXOffset, yOffset + yAxisBounds.getHeight());
        g.draw(line);
      }
    }
    // X-Axis
    else if (direction == Axis.Direction.X && styler.isXAxisTicksVisible()) {

      int axisTickMarkLength = styler.getAxisTickMarkLength();
      double xOffset = chart.getXAxis().getBounds().getX();
      double yOffset =
          chart.getXAxis().getAxisTick().getAxisTickLabels().getBounds().getY()
              - styler.getAxisTickPadding();

      // bounds
      bounds =
          new Rectangle2D.Double(
              xOffset,
              yOffset - axisTickMarkLength,
              chart.getXAxis().getBounds().getWidth(),
              axisTickMarkLength);
      // g.setColor(Color.yellow);
      // g.draw(bounds);

      // tick marks
      if (styler.isAxisTicksMarksVisible()) {

        for (int i = 0; i < chart.getXAxis().getAxisTickCalculator().getTickLabels().size(); i++) {

          double tickLocation = chart.getXAxis().getAxisTickCalculator().getTickLocations().get(i);
          double shiftedTickLocation = xOffset + tickLocation;

          if (shiftedTickLocation > bounds.getX()
              && shiftedTickLocation < bounds.getX() + bounds.getWidth()) {

            Shape line =
                new Line2D.Double(
                    shiftedTickLocation,
                    yOffset,
                    xOffset + tickLocation,
                    yOffset - axisTickMarkLength);
            g.draw(line);
          }
        }
      }

      // Line
      if (styler.isAxisTicksLineVisible()) {

        g.setStroke(styler.getAxisTickMarksStroke());
        g.drawLine(
            (int) xOffset,
            (int) (yOffset - axisTickMarkLength),
            (int) (xOffset + chart.getXAxis().getBounds().getWidth()),
            (int) (yOffset - axisTickMarkLength));
      }
    } else {
      bounds = new Rectangle2D.Double();
    }
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }
}
