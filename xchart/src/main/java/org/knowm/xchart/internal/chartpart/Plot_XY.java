package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.AxesChartStyler;

/** @author timmolter */
public class Plot_XY<ST extends AxesChartStyler, S extends XYSeries> extends Plot_AxesChart<ST, S> {

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_XY(Chart<ST, S> chart) {

    super(chart);
    this.plotContent = new PlotContent_XY<ST, S>(chart);
  }
}
