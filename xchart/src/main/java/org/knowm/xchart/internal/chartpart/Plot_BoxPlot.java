package org.knowm.xchart.internal.chartpart;

import java.awt.Graphics2D;
import org.knowm.xchart.BoxSeries;
import org.knowm.xchart.style.BoxStyler;

public class Plot_BoxPlot<ST extends BoxStyler, S extends BoxSeries>
    extends Plot_AxesChart<ST, S> {

  public Plot_BoxPlot(Chart<ST, S> chart) {

    super(chart);
  }

  @Override
  public void paint(Graphics2D g) {

    this.plotContent = new PlotContent_Box<ST, S>(chart);
    super.paint(g);
  }
}
