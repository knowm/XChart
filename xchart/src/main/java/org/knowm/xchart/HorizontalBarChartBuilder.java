package org.knowm.xchart;

import org.knowm.xchart.internal.ChartBuilder;

public class HorizontalBarChartBuilder
    extends ChartBuilder<HorizontalBarChartBuilder, HorizontalBarChart> {

  String xAxisTitle = "";
  String yAxisTitle = "";

  public HorizontalBarChartBuilder() {}

  public HorizontalBarChartBuilder xAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
    return this;
  }

  public HorizontalBarChartBuilder yAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
    return this;
  }

  /**
   * return fully built Chart_Category
   *
   * @return a CategoryChart
   */
  @Override
  public HorizontalBarChart build() {

    return new HorizontalBarChart(this);
  }
}
