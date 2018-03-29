package org.knowm.xchart.demo;

import org.knowm.xchart.internal.chartpart.Chart;

/** @author timmolter */
public final class ChartInfo {

  private final String exampleChartName;
  private final Chart<?, ?> exampleChart;

  /**
   * Constructor
   *
   * @param exampleChartName
   * @param exampleChart
   */
  public ChartInfo(String exampleChartName, Chart<?, ?> exampleChart) {

    this.exampleChartName = exampleChartName;
    this.exampleChart = exampleChart;
  }

  public String getExampleChartName() {

    return exampleChartName;
  }

  public Chart<?, ?> getExampleChart() {

    return exampleChart;
  }

  @Override
  public String toString() {

    return this.exampleChartName;
  }
}
