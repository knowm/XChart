package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.HorizontalBarSeries;
import org.knowm.xchart.style.HorizontalBarStyler;

public class Plot_HorizontalBar<ST extends HorizontalBarStyler, S extends HorizontalBarSeries>
    extends Plot_AxesChart<ST, S> {

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_HorizontalBar(Chart<ST, S> chart) {

    super(chart);
    this.plotContent = new PlotContent_HorizontalBar<ST, S>(chart);
  }
}
