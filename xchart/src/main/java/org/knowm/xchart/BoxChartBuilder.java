package org.knowm.xchart;

import org.knowm.xchart.internal.ChartBuilder;

public class BoxChartBuilder extends ChartBuilder<BoxChartBuilder, BoxChart> {

  String xAxisTitle = "";
  String yAxisTitle = "";

  public BoxChartBuilder() {}

  public BoxChartBuilder xAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
    return this;
  }

  public BoxChartBuilder yAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
    return this;
  }

  @Override
  public BoxChart build() {

    return new BoxChart(this);
  }
}
