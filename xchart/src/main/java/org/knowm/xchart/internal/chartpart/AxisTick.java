package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.chartpart.Axis.Direction;
import org.knowm.xchart.internal.series.AxesChartSeries;
import org.knowm.xchart.style.AxesChartStyler;

/** An axis tick */
public class AxisTick<ST extends AxesChartStyler, S extends AxesChartSeries> implements ChartPart {

  private final Chart<ST, S> chart;
  private final Direction direction;
  /** the axisticklabels */
  private final AxisTickLabels<ST, S> axisTickLabels;
  /** the axistickmarks */
  private final AxisTickMarks<ST, S> axisTickMarks;

  private Rectangle2D bounds;

  /**
   * Constructor
   *
   * @param chart
   * @param direction
   * @param yAxis
   */
  AxisTick(Chart<ST, S> chart, Direction direction, Axis yAxis) {

    this.chart = chart;
    this.direction = direction;
    axisTickLabels = new AxisTickLabels<ST, S>(chart, direction, yAxis);
    axisTickMarks = new AxisTickMarks<ST, S>(chart, direction, yAxis);
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }

  @Override
  public void paint(Graphics2D g) {

    if (direction == Axis.Direction.Y && chart.getStyler().isYAxisTicksVisible()) {

      axisTickLabels.paint(g);
      axisTickMarks.paint(g);

      bounds =
          new Rectangle2D.Double(
              axisTickLabels.getBounds().getX(),
              axisTickLabels.getBounds().getY(),
              axisTickLabels.getBounds().getWidth()
                  + chart.getStyler().getAxisTickPadding()
                  + axisTickMarks.getBounds().getWidth(),
              axisTickMarks.getBounds().getHeight());

      // g.setColor(Color.red);
      // g.draw(bounds);

    } else if (direction == Axis.Direction.X && chart.getStyler().isXAxisTicksVisible()) {

      axisTickLabels.paint(g);
      axisTickMarks.paint(g);

      bounds =
          new Rectangle2D.Double(
              axisTickMarks.getBounds().getX(),
              axisTickMarks.getBounds().getY(),
              axisTickLabels.getBounds().getWidth(),
              axisTickMarks.getBounds().getHeight()
                  + chart.getStyler().getAxisTickPadding()
                  + axisTickLabels.getBounds().getHeight());

      // g.setColor(Color.red);
      // g.draw(bounds);

    } else {
      bounds = new Rectangle2D.Double();
    }
  }

  // Getters /////////////////////////////////////////////////

  AxisTickLabels<ST, S> getAxisTickLabels() {

    return axisTickLabels;
  }
}
