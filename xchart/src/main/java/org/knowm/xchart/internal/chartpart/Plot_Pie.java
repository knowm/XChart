package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.PieSeries;
import org.knowm.xchart.style.PieStyler;

/** @author timmolter */
public class Plot_Pie<ST extends PieStyler, S extends PieSeries> extends Plot_Circular<ST, S> {

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_Pie(Chart<ST, S> chart) {

    super(chart);
  }

  protected void initContentAndSurface(Chart<ST, S> chart) {

    this.plotContent = new PlotContent_Pie<ST, S>(chart);
    this.plotSurface = new PlotSurface_Pie<ST, S>(chart);
  }
}
