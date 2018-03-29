package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/** @author timmolter */
public abstract class PlotContent_<ST extends Styler, S extends Series> implements ChartPart {

  final Chart<ST, S> chart;

  final Stroke errorBarStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

  /**
   * Constructor
   *
   * @param chart - The Chart
   */
  PlotContent_(Chart<ST, S> chart) {

    this.chart = chart;
  }

  protected abstract void doPaint(Graphics2D g);

  @Override
  public void paint(Graphics2D g) {

    Rectangle2D bounds = getBounds();
    // g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
    // g.setColor(Color.red);
    // g.draw(bounds);

    // if the area to draw a chart on is so small, don't even bother
    if (bounds.getWidth() < 30) {
      return;
    }

    java.awt.Shape saveClip = g.getClip();
    // this is for preventing the series to be drawn outside the plot area if min and max is
    // overridden to fall inside the data range
    g.setClip(bounds.createIntersection(bounds));

    chart.toolTips.prepare(g);

    doPaint(g);

    chart.toolTips.paint(g);

    g.setClip(saveClip);
  }

  @Override
  public Rectangle2D getBounds() {

    return chart.getPlot().getBounds();
  }

  /** Closes a path for area charts if one is available. */
  void closePath(
      Graphics2D g, Path2D.Double path, double previousX, Rectangle2D bounds, double yTopMargin) {

    if (path != null) {
      double yBottomOfArea = getBounds().getY() + getBounds().getHeight() - yTopMargin;
      path.lineTo(previousX, yBottomOfArea);
      path.closePath();
      g.fill(path);
    }
  }
}
