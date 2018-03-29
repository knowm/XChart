package org.knowm.xchart;

import org.knowm.xchart.internal.ChartBuilder;

/** @author timmolter */
public class CategoryChartBuilder extends ChartBuilder<CategoryChartBuilder, CategoryChart> {

  String xAxisTitle = "";
  String yAxisTitle = "";

  public CategoryChartBuilder() {}

  public CategoryChartBuilder xAxisTitle(String xAxisTitle) {

    this.xAxisTitle = xAxisTitle;
    return this;
  }

  public CategoryChartBuilder yAxisTitle(String yAxisTitle) {

    this.yAxisTitle = yAxisTitle;
    return this;
  }

  /**
   * return fully built Chart_Category
   *
   * @return a CategoryChart
   */
  @Override
  public CategoryChart build() {

    return new CategoryChart(this);
  }
}
