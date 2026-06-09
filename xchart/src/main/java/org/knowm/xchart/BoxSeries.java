package org.knowm.xchart;

import java.util.List;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;

public class BoxSeries extends AxesChartSeriesCategory {

  public BoxSeries(
      String name,
      List<?> xData,
      List<? extends Number> yData,
      List<? extends Number> extraValues,
      DataType xAxisDataType) {

    super(name, xData, yData, extraValues, xAxisDataType);
  }

  /** Constructor — direct primitive-array path; avoids boxing entirely. */
  public BoxSeries(
      String name, List<?> xData, double[] yData, double[] extraValues, DataType xAxisDataType) {

    super(name, xData, yData, extraValues, xAxisDataType);
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return null;
  }

  /**
   * Replace the Y data for this box series via list, with validation.
   *
   * @param newYData the new Y data; must be non-null, non-empty, and contain no null values
   */
  @Override
  public void replaceData(List<? extends Number> newYData) {

    if (newYData == null) {
      throw new IllegalArgumentException("Y-Axis data cannot be null !!!");
    }
    if (newYData.isEmpty()) {
      throw new IllegalArgumentException("Y-Axis data cannot be empty !!!");
    }
    if (newYData.contains(null)) {
      throw new IllegalArgumentException("Y-Axis data cannot contain null !!!");
    }
    super.replaceData(newYData);
  }

  /**
   * Replace the Y data for this box series via primitive array — avoids boxing entirely.
   *
   * @param newYData the new Y data; must be non-null and non-empty
   */
  @Override
  public void replaceData(double[] newYData) {

    if (newYData == null) {
      throw new IllegalArgumentException("Y-Axis data cannot be null !!!");
    }
    if (newYData.length == 0) {
      throw new IllegalArgumentException("Y-Axis data cannot be empty !!!");
    }
    super.replaceData(newYData);
  }
}
