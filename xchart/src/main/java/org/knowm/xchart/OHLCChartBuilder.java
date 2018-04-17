package org.knowm.xchart;

import org.knowm.xchart.internal.ChartBuilder;

/** @author timmolter */
public class OHLCChartBuilder extends ChartBuilder<OHLCChartBuilder, OHLCChart> {

  String xAxisTitle = "";
  String yAxisTitle = "";

  public OHLCChartBuilder() {}

  public OHLCChartBuilder xAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
    return this;
  }

  public OHLCChartBuilder yAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
    return this;
  }

  /**
   * return fully built XYChart
   *
   * @return a XYChart
   */
  @Override
  public OHLCChart build() {

    return new OHLCChart(this);
  }
}
