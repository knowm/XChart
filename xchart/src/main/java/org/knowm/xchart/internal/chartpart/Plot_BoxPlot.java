package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;

import org.knowm.xchart.BoxPlotSeries;
import org.knowm.xchart.style.BoxPlotStyler;

public class Plot_BoxPlot<ST extends BoxPlotStyler, S extends BoxPlotSeries> extends Plot_AxesChart<ST, S> {

  public Plot_BoxPlot(Chart<ST, S> chart) {

    super(chart);
  }

  @Override
  public void paint(Graphics2D g) {

    this.plotContent = new PlotContent_BoxPlot<ST, S>(chart);
    super.paint(g);
  }
}
