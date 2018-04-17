package org.knowm.xchart;

import org.knowm.xchart.internal.ChartBuilder;

/** @author timmolter */
public class BubbleChartBuilder extends ChartBuilder<BubbleChartBuilder, BubbleChart> {

  String xAxisTitle = "";
  String yAxisTitle = "";

  public BubbleChartBuilder() {}

  public BubbleChartBuilder xAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
    return this;
  }

  public BubbleChartBuilder yAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
    return this;
  }

  /**
   * return fully built BubbleChart
   *
   * @return a BubbleChart
   */
  @Override
  public BubbleChart build() {

    return new BubbleChart(this);
  }
}
