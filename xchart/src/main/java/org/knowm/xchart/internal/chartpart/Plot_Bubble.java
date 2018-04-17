package org.knowm.xchart.internal.chartpart;

import org.knowm.xchart.BubbleSeries;
import org.knowm.xchart.style.BubbleStyler;

/** @author timmolter */
public class Plot_Bubble<ST extends BubbleStyler, S extends BubbleSeries>
    extends Plot_AxesChart<ST, S> {

  /**
   * Constructor
   *
   * @param chart
   */
  public Plot_Bubble(Chart<ST, S> chart) {

    super(chart);
    this.plotContent = new PlotContent_Bubble<ST, S>(chart);
  }
}
