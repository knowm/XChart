package org.knowm.xchart;

import org.knowm.xchart.internal.chartpart.RenderableSeries;
import org.knowm.xchart.internal.chartpart.RenderableSeries.LegendRenderType;
import org.knowm.xchart.internal.series.AxesChartSeriesNumericalNoErrorBars;

/**
 * A Series containing X and Y data to be plotted on a Chart
 *
 * @author timmolter
 */
public class XYSeries extends AxesChartSeriesNumericalNoErrorBars {

  private XYSeriesRenderStyle xySeriesRenderStyle = null;

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

  public XYSeriesRenderStyle getXYSeriesRenderStyle() {

    return xySeriesRenderStyle;
  }

  public XYSeries setXYSeriesRenderStyle(XYSeriesRenderStyle chartXYSeriesRenderStyle) {

    this.xySeriesRenderStyle = chartXYSeriesRenderStyle;
    return this;
  }

  @Override
  public LegendRenderType getLegendRenderType() {

    return xySeriesRenderStyle.getLegendRenderType();
  }

  public enum XYSeriesRenderStyle implements RenderableSeries {
    Line(LegendRenderType.Line),

    Area(LegendRenderType.Line),

    Step(LegendRenderType.Line),

    StepArea(LegendRenderType.Line),

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
