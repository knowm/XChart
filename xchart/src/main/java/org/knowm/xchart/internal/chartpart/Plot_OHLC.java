package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.OHLCSeries;
import org.knowm.xchart.style.AxesChartStyler;

/** @author arthurmcgibbon */
public class Plot_OHLC<ST extends AxesChartStyler, S extends OHLCSeries>
    extends Plot_AxesChart<ST, S> {

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_OHLC(Chart<ST, S> chart) {

    super(chart);
    this.plotContent = new PlotContent_OHLC<ST, S>(chart);
  }
}
