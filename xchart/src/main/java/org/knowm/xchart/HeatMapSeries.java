package org.knowm.xchart;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeries;

/**
 * A Series containing X, Y and heatData data to be plotted on a Chart
 *
 * @author Mr14huashao
 */
public class HeatMapSeries extends AxesChartSeries {

  List<?> xData;

  List<?> yData;

  List<? extends Number[]> heatData;

  // heatData value min
  double min;

  // heatData value max
  double max;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param heatData
   */
  protected HeatMapSeries(String name, List<?> xData, List<?> yData, List<Number[]> heatData) {

    super(name, getDataType(xData), getDataType(yData));
    this.xData = xData;
    this.yData = yData;
    this.heatData = heatData;
    calculateMinMax();
  }

  public void replaceData(List<?> xData, List<?> yData, List<Number[]> heatData) {

    this.xData = xData;
    this.yData = yData;
    this.heatData = heatData;
    calculateMinMax();
  }

  @Override
  protected void calculateMinMax() {

    min = Double.MAX_VALUE;
    max = Double.MIN_VALUE;
    Number number = null;
    for (Number[] numbers : heatData) {
      if (numbers == null) {
        continue;
      }
      number = numbers[2];
      if (number != null) {
        if (min > number.doubleValue()) {
          min = number.doubleValue();
        }
        if (max < number.doubleValue()) {
          max = number.doubleValue();
        }
      }
    }
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return null;
  }

  private static DataType getDataType(List<?> data) {

    DataType axisType;

    Iterator<?> itr = data.iterator();
    Object dataPoint = itr.next();
    if (dataPoint instanceof Number) {
      axisType = DataType.Number;
    } else if (dataPoint instanceof Date) {
      axisType = DataType.Date;
    } else if (dataPoint instanceof String) {
      axisType = DataType.String;
    } else {
      throw new IllegalArgumentException(
          "Series data must be either Number, Date or String type!!!");
    }
    return axisType;
  }

  public Collection<?> getXData() {

    return xData;
  }

  public Collection<?> getYData() {

    return yData;
  }

  public Collection<? extends Number[]> getHeatData() {

    return heatData;
  }

  public double getMin() {

    return min;
  }

  public HeatMapSeries setMin(double min) {

    this.min = min;
    return this;
  }

  public double getMax() {

    return max;
  }

  public HeatMapSeries setMax(double max) {

    this.max = max;
    return this;
  }
}
