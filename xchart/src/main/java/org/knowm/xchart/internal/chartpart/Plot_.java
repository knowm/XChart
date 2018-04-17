package org.knowm.xchart.internal.chartpart;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/** @author timmolter */
public class Plot_<ST extends Styler, S extends Series> implements ChartPart {

  final Chart<ST, S> chart;
  Rectangle2D bounds;

  PlotSurface_<ST, S> plotSurface;
  PlotContent_<ST, S> plotContent;

  /**
   * Constructor
   *
   * @param chart
   */
  Plot_(Chart<ST, S> chart) {

    this.chart = chart;
  }

  @Override
  public void paint(Graphics2D g) {

    // g.setColor(Color.red);
    // g.draw(bounds);

    plotSurface.paint(g);
    if (chart.getSeriesMap().isEmpty()) {
      return;
    }
    plotContent.paint(g);
  }

  @Override
  public Rectangle2D getBounds() {

    return bounds;
  }
}
