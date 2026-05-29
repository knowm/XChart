package org.knowm.xchart;

import java.util.Optional;
import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeriesNumerical;

/** A Series containing X and Y data to be plotted on a Chart */
public class XYSeries extends AxesChartSeriesNumerical {

  private Optional<XYSeriesRenderStyle> xySeriesRenderStyle = Optional.empty();
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

    return xySeriesRenderStyle;
  }

  public XYSeries setXYSeriesRenderStyle(XYSeriesRenderStyle chartXYSeriesRenderStyle) {

    this.xySeriesRenderStyle = Optional.ofNullable(chartXYSeriesRenderStyle);
    return this;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return xySeriesRenderStyle
        .orElseThrow(() -> new IllegalStateException("XY render style not set"))
        .getLegendRenderType();
  }

  public boolean isSmooth() {
    return smooth;
  }

  public void setSmooth(boolean smooth) {
    this.smooth = smooth;
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
