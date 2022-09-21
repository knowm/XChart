package org.knowm.xchart.demo;

import org.knowm.xchart.demo.charts.ExampleChart;

/** @author timmolter */
public final class ChartInfo {

  private final String exampleChartName;
  private final ExampleChart exampleChart;

  /**
   * Constructor
   *
   * @param exampleChartName
   * @param exampleChart
   */
  public ChartInfo(String exampleChartName, ExampleChart exampleChart) {

    this.exampleChartName = exampleChartName;
    this.exampleChart = exampleChart;
  }

  public String getExampleChartName() {

    return exampleChartName;
  }

  public ExampleChart getExampleChart() {

    return exampleChart;
  }

  @Override
  public String toString() {

    return this.exampleChartName;
  }
}
