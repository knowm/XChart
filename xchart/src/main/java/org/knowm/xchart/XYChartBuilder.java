package org.knowm.xchart;

import org.knowm.xchart.internal.ChartBuilder;

/** @author timmolter */
public class XYChartBuilder extends ChartBuilder<XYChartBuilder, XYChart> {

  String xAxisTitle = "";
  String yAxisTitle = "";

  public XYChartBuilder() {}

  public XYChartBuilder xAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
    return this;
  }

  public XYChartBuilder yAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
    return this;
  }

  /**
   * return fully built XYChart
   *
   * @return a XYChart
   */
  @Override
  public XYChart build() {

    return new XYChart(this);
  }
}
