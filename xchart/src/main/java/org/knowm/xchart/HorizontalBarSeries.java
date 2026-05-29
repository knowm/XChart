package org.knowm.xchart;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeries;

/** A Series containing horizontal bar data to be plotted on a Chart */
public class HorizontalBarSeries extends AxesChartSeries {

  List<? extends Number> xData;

  List<?> yData;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   */
  public HorizontalBarSeries(String name, List<? extends Number> xData, List<?> yData) {

    super(name, getDataType(xData), getDataType(yData));
    this.xData = xData;
    this.yData = yData;
    calculateMinMax();
  }

  public void replaceData(List<? extends Number> xData, List<?> yData) {

    this.xData = xData;
    this.yData = yData;
    calculateMinMax();
  }

  @Override
  protected void calculateMinMax() {

    // xData
    double[] xMinMax = findMinMax(xData, getxAxisDataType());
    xMin = xMinMax[0];
    xMax = xMinMax[1];

    // yData
    double[] yMinMax = findMinMax(yData, getyAxisDataType());
    yMin = yMinMax[0];
    yMax = yMinMax[1];
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

  public List<? extends Number> getXData() {

    return xData;
  }

  public List<?> getYData() {

    return yData;
  }
}
