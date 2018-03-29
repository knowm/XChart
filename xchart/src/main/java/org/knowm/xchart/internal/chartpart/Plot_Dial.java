package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.DialSeries;
import org.knowm.xchart.style.DialStyler;

public class Plot_Dial<ST extends DialStyler, S extends DialSeries> extends Plot_Circular<ST, S> {

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_Dial(Chart<ST, S> chart) {

    super(chart);
  }

  @Override
  protected void initContentAndSurface(Chart<ST, S> chart) {

    this.plotContent = new PlotContent_Dial<ST, S>(chart);
    this.plotSurface = new PlotSurface_Pie<ST, S>(chart);
  }
}
