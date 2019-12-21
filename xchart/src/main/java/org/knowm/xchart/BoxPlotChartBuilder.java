package org.knowm.xchart;

import org.knowm.xchart.internal.ChartBuilder;

public class BoxPlotChartBuilder extends ChartBuilder<BoxPlotChartBuilder, BoxPlotChart> {

  String xAxisTitle = "";
  String yAxisTitle = "";

  public BoxPlotChartBuilder() {}

  public BoxPlotChartBuilder xAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
    return this;
  }

  public BoxPlotChartBuilder yAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
    return this;
  }

  @Override
  public BoxPlotChart build() {

    return new BoxPlotChart(this);
  }
}
