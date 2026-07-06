package org.knowm.xchart;

import java.util.List;
import java.util.Optional;
import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeriesCategory;
import org.knowm.xchart.internal.series.Series;

/** A Series containing category data to be plotted on a Chart */
public class CategorySeries extends AxesChartSeriesCategory {

  private boolean isOverlapped = false;

  private Optional<CategorySeriesRenderStyle> chartCategorySeriesRenderStyle = Optional.empty();

  // smooth curve
  private boolean smooth;

  /**
   * Constructor — accepts Lists (yData/errorBars are converted to double[] internally).
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

  /**
   * Constructor — direct primitive-array path; avoids boxing entirely.
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
      double[] yData,
      double[] errorBars,
      Series.DataType axisType) {

    super(name, xData, yData, errorBars, axisType);
  }

  public Optional<CategorySeriesRenderStyle> getChartCategorySeriesRenderStyle() {

    return chartCategorySeriesRenderStyle;
  }

  public CategorySeries setChartCategorySeriesRenderStyle(
      CategorySeriesRenderStyle categorySeriesRenderStyle) {

    this.chartCategorySeriesRenderStyle = Optional.ofNullable(categorySeriesRenderStyle);
    return this;
  }

  public boolean isOverlapped() {
    return isOverlapped;
  }

  public CategorySeries setOverlapped(boolean overlapped) {
    isOverlapped = overlapped;
    return this;
  }

  public boolean isSmooth() {

    return smooth;
  }

  /**
   * Sets whether the line/area series should be rendered with smooth cubic Bezier curves.
   *
   * @param smooth true for smooth curves, false for straight lines
   */
  public CategorySeries setSmooth(boolean smooth) {

    this.smooth = smooth;
    return this;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return chartCategorySeriesRenderStyle
        .orElseThrow(() -> new IllegalStateException("Category render style not set"))
        .getLegendRenderType();
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

    /**
     * Whether this render style participates in stacking when {@link
     * org.knowm.xchart.style.CategoryStyler#isStacked()} is enabled. Only the bar/area family ({@link
     * #Bar}, {@link #Area}, {@link #Stick}, {@link #SteppedBar}) stacks; point- and line-based styles
     * ({@link #Line}, {@link #Scatter}) are drawn at their own values and are never stacked on top of
     * the bar/area stack. Enumerated as an explicit allow-list so any future render style defaults to
     * non-stackable until intentionally opted in.
     *
     * @return true if series of this style are stacked
     */
    public boolean isStackable() {

      switch (this) {
        case Bar:
        case Area:
        case Stick:
        case SteppedBar:
          return true;
        default:
          return false;
      }
    }
  }
}
