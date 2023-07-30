package org.knowm.xchart;

import org.knowm.xchart.internal.ChartBuilder;

public class PieChartBuilder extends ChartBuilder<PieChartBuilder, PieChart> {

  public PieChartBuilder() {}

  /**
   * return fully built ChartPie
   *
   * @return a ChartPie
   */
  @Override
  public PieChart build() {

    return new PieChart(this);
  }
}
