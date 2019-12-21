package org.knowm.xchart;

import java.util.List;

import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;

public class BoxPlotSeries extends AxesChartSeriesCategory {

  public BoxPlotSeries(String name, List<?> xData, List<? extends Number> yData, List<? extends Number> extraValues,
      DataType xAxisDataType) {

    super(name, xData, yData, extraValues, xAxisDataType);
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return null;
  }
}
