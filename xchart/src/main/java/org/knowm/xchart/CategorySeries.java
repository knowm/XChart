package org.knowm.xchart;

import java.util.List;
import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;
import org.knowm.xchart.internal.series.Series;

/** A Series containing category data to be plotted on a Chart */
public class CategorySeries extends AxesChartSeriesCategory {

  private CategorySeriesRenderStyle chartCategorySeriesRenderStyle = null;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param errorBars
   * @param axisType
   */
  public CategorySeries(
      String name,
      List<?> xData,
      List<? extends Number> yData,
      List<? extends Number> errorBars,
      Series.DataType axisType) {

    super(name, xData, yData, errorBars, axisType);
  }

  public CategorySeriesRenderStyle getChartCategorySeriesRenderStyle() {

    return chartCategorySeriesRenderStyle;
  }

  public CategorySeries setChartCategorySeriesRenderStyle(
      CategorySeriesRenderStyle categorySeriesRenderStyle) {

    this.chartCategorySeriesRenderStyle = categorySeriesRenderStyle;
    return this;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return chartCategorySeriesRenderStyle.getLegendRenderType();
  }

  public enum CategorySeriesRenderStyle implements RenderableSeries {
    Line(LegendRenderType.Line),

    Area(LegendRenderType.Line),

    Scatter(LegendRenderType.Scatter),

    SteppedBar(LegendRenderType.Box),

    Bar(LegendRenderType.BoxNoOutline),

    Stick(LegendRenderType.Line);

    private final LegendRenderType legendRenderType;

    CategorySeriesRenderStyle(LegendRenderType legendRenderType) {

      this.legendRenderType = legendRenderType;
    }

    @Override
    public LegendRenderType getLegendRenderType() {

      return legendRenderType;
    }
  }
}
