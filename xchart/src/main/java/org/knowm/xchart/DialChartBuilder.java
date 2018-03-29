package org.knowm.xchart;

import org.knowm.xchart.internal.ChartBuilder;

/** @author timmolter */
public class DialChartBuilder extends ChartBuilder<DialChartBuilder, DialChart> {

  public DialChartBuilder() {}

  @Override
  public DialChart build() {

    return new DialChart(this);
  }
}
