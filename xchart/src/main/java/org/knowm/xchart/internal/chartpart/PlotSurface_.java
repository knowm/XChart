package org.knowm.xchart.internal.chartpart;

import java.awt.geom.Rectangle2D;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler;

/** @author timmolter */
public abstract class PlotSurface_<ST extends Styler, S extends Series> implements ChartPart {

  final Chart<ST, S> chart;

  /**
   * Constructor
   *
   * @param chart
   */
  PlotSurface_(Chart<ST, S> chart) {

    this.chart = chart;
  }

  @Override
  public Rectangle2D getBounds() {

    return chart.getPlot().getBounds();
  }
}
