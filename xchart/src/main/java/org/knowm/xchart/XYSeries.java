package org.knowm.xchart;

import java.util.Optional;
import java.util.function.Supplier;
import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeriesNumerical;

/** A Series containing X and Y data to be plotted on a Chart */
public class XYSeries extends AxesChartSeriesNumerical {

  private Optional<XYSeriesRenderStyle> xySeriesRenderStyle = Optional.empty();
  private Supplier<XYSeriesRenderStyle> defaultRenderStyleSupplier = () -> null;
  // smooth curve
  private boolean smooth;

  /**
   * Constructor
   *
   * @param name
   * @param xData
   * @param yData
   * @param errorBars
   */
  public XYSeries(
      String name, double[] xData, double[] yData, double[] errorBars, DataType axisType) {

    super(name, xData, yData, errorBars, axisType);
  }

  public Optional<XYSeriesRenderStyle> getXYSeriesRenderStyle() {

    if (xySeriesRenderStyle.isPresent()) {
      return xySeriesRenderStyle;
    }
    return Optional.ofNullable(defaultRenderStyleSupplier.get());
  }

  Optional<XYSeriesRenderStyle> getExplicitXYSeriesRenderStyle() {

    return xySeriesRenderStyle;
  }

  public XYSeries setXYSeriesRenderStyle(XYSeriesRenderStyle chartXYSeriesRenderStyle) {

    this.xySeriesRenderStyle = Optional.ofNullable(chartXYSeriesRenderStyle);
    return this;
  }

  XYSeries setDefaultRenderStyleSupplier(
      Supplier<XYSeriesRenderStyle> defaultRenderStyleSupplier) {

    this.defaultRenderStyleSupplier =
        defaultRenderStyleSupplier == null ? () -> null : defaultRenderStyleSupplier;
    return this;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return getXYSeriesRenderStyle()
        .orElseThrow(() -> new IllegalStateException("XY render style not set"))
        .getLegendRenderType();
  }

  public boolean isSmooth() {
    return smooth;
  }

  public XYSeries setSmooth(boolean smooth) {
    this.smooth = smooth;
    return this;
  }

  public enum XYSeriesRenderStyle implements RenderableSeries {
    Line(LegendRenderType.Line),

    Area(LegendRenderType.Line),

    Step(LegendRenderType.Line),

    StepArea(LegendRenderType.Line),

    PolygonArea(LegendRenderType.Box),

    Scatter(LegendRenderType.Scatter);

    private final LegendRenderType legendRenderType;

    XYSeriesRenderStyle(LegendRenderType legendRenderType) {

      this.legendRenderType = legendRenderType;
    }

    @Override
    public LegendRenderType getLegendRenderType() {

      return legendRenderType;
    }
  }
}
